package com.caodaxing.mybatis.context;

/**
 * <p>mapper工厂</p>
 * @author daxingcao
 */
public interface MapperFactory {

    /**
     * 根据名字获取dao层接口的代理类
     * @param name 接口名
     * @return
     */
    Object getMapper(String name);

    /**
     * 根据lei获取dao层接口的代理类
     * @param clazz 类
     * @return
     */
    Object getMapper(Class clazz);

}
