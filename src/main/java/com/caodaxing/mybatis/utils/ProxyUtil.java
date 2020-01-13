package com.caodaxing.mybatis.utils;


import com.caodaxing.mybatis.factory.AbstractMapperFactory;
import com.caodaxing.mybatis.factory.ProxyClassFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;

/**
 * <p>生成接口的代理类</p>
 * @author daxingcao
 */
public class ProxyUtil {

    public static Object getProxy(AbstractMapperFactory factory){
        Class interfaceClass = factory.getObjectType();
        Assert.notNull(interfaceClass,"the interface class is empty!");
        ProxyClassFactory handler = new ProxyClassFactory(factory);
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},handler);
    }

}
