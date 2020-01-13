package com.caodaxing.mybatis.context;

import com.caodaxing.mybatis.ClassResourceLoaderConfiguration;
import com.caodaxing.mybatis.factory.AbstractMapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;

/**
 * @author daxingcao
 */
public class CustomBeanDefinitionRegister implements BeanFactoryPostProcessor {

    private ClassResourceLoaderConfiguration loaderHandler;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            if(beanFactory instanceof BeanDefinitionRegistry){
                //初始化加载mybatis相关数据
                loaderHandler.initMapper();
                Set<Class> classMap = loaderHandler.getClassMap();
                if(classMap != null){
                    BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                    DataSource dataSource = loaderHandler.getDataSource();
                    for(Class<?> clazz : classMap){
                        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
                        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionBuilder.getBeanDefinition();
                        beanDefinition.setBeanClass(AbstractMapperFactory.class);
                        ConstructorArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
                        argumentValues.addGenericArgumentValue(clazz);
                        argumentValues.addGenericArgumentValue(this.loaderHandler);
                        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                        //将BeanDefinition注入容器
                        registry.registerBeanDefinition(clazz.getSimpleName(),beanDefinition);
                    }
                }
            }
        } catch (IOException var2){
            var2.printStackTrace();
        }
    }

    public void setLoaderHandler(ClassResourceLoaderConfiguration loaderHandler){
        this.loaderHandler = loaderHandler;
    }

}
