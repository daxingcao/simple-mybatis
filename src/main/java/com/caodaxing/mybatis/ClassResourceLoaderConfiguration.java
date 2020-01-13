package com.caodaxing.mybatis;

import org.springframework.core.io.Resource;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import java.io.IOException;

/**
 * <p>加载class资源</p>
 * @author daxingcao
 */
public class ClassResourceLoaderConfiguration extends BaseXmlResourceLoaderHandler {

    @Override
    protected void loadPackageClass()throws IOException {
        Resource[] resources = getResources(resolvePackagePath(CLASS_URL_SUFFIX));
        if(resources != null){
            for(Resource resource : resources){
                if(resource.isReadable()){
                    ClassMetadata classMetadata = this.getMetadataReader(resource).getClassMetadata();
                    String className = classMetadata.getClassName();
                    System.out.println("class name is ->"+className);
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        registerClass(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private MetadataReader getMetadataReader(Resource resource)throws IOException{
        return getMetadataReaderFactory().getMetadataReader(resource);
    }

}
