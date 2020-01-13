package com.caodaxing.mybatis;

import com.caodaxing.mybatis.factory.BasicMapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daxingcao
 */
public abstract class AbstractResourceLoaderHandler extends
        BasicMapperFactory implements ResourceLoaderHandler, ResourceLoaderAware, ApplicationContextAware {

    protected final static String XML_URL_SUFFIX = "**/*.xml";
    protected final static String CLASS_URL_SUFFIX = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;
    private ResourceLoader resourceLoader;
    private ApplicationContext context;
    private Environment environment;
    private String classPackage;
    private String mapperPackage;
    private Set<Class> classMap = new HashSet<>();
    private Map<String, MapperDefinition> mapperDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public void initMapper()throws IOException {
        loadPackageClass();
        loadMapperDefinitions();
    }

    @Override
    public DataSource getDataSource(){
        return this.context.getBean(DataSource.class);
    }

    protected abstract void loadPackageClass()throws IOException;

    protected abstract void loadMapperDefinitions()throws IOException;

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    protected String resolvePackagePath(String suffix){
        String resolvePath;
        //最终调用PropertyPlaceholderHelper类replacePlaceholders方法，替换掉'{}','[]','()'
        if(CLASS_URL_SUFFIX.equals(suffix)){
            resolvePath = this.environment.resolveRequiredPlaceholders(getClassPackage());
        }else if (XML_URL_SUFFIX.equals(suffix)){
            resolvePath = this.environment.resolveRequiredPlaceholders(getMapperPackage());
        }else {
            throw new IllegalArgumentException("不能解析"+suffix+"文件类型!");
        }
        //将路径中的'.'替换成'/',并加上前缀和后缀
        return ResourcePatternResolver.CLASSPATH_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(resolvePath) + suffix;
    }

    protected void registerClass(Class clazz){
        this.classMap.add(clazz);
    }

    @Override
    public Set<Class> getClassMap(){
        return this.classMap;
    }

    @Override
    public void addMapperDefinition(String name,MapperDefinition definition){
        Assert.notNull(name,"the name must not be empty!");
        this.mapperDefinitionMap.put(name,definition);
    }

    public MapperDefinition getMapperDefinition(String name){
        return this.mapperDefinitionMap.get(name);
    }

    public MapperDefinition[] getMapperDefinitions(){
        Set<String> strings = this.mapperDefinitionMap.keySet();
        Set<MapperDefinition> set = new HashSet<>();
        for(String key : strings){
            MapperDefinition mapperDefinition = this.mapperDefinitionMap.get(key);
            set.add(mapperDefinition);
        }
        return set.toArray(new MapperDefinition[strings.size()]);
    }

    Resource[] getResources(String path)throws IOException {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
        return resolver.getResources(path);
    }

    public ApplicationContext getContext(){
        return context;
    }

    public Environment getEnvironment(){
        return this.environment;
    }

    public MetadataReaderFactory getMetadataReaderFactory(){
        return this.metadataReaderFactory;
    }

    public ResourceLoader getResourceLoader(){
        return this.resourceLoader;
    }

}
