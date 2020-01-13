package com.caodaxing.mybatis.factory;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyClassFactory implements InvocationHandler {

    private AbstractMapperFactory factory;

    public ProxyClassFactory(FactoryBean bean){
        if(bean instanceof AbstractMapperFactory){
            this.factory = (AbstractMapperFactory) bean;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,args);
        }
        Class<?> returnType = method.getReturnType();
//        Object obj = factory.executeSqlResolver(method, args);
//        if(obj instanceof List){
//            List list = (List) obj;
//            if(!List.class.equals(returnType)){
//                throw new IllegalStateException("Expectation 1, activity "+ list.size());
//            }
//        }
        return factory.executeSqlResolver(method, args);
    }
}
