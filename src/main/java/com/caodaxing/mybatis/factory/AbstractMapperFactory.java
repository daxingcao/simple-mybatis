package com.caodaxing.mybatis.factory;

import com.caodaxing.mybatis.AbstractResourceLoaderHandler;
import com.caodaxing.mybatis.MapperDefinition;
import com.caodaxing.mybatis.ResourceLoaderHandler;
import com.caodaxing.mybatis.mapper.MapperMethod;
import com.caodaxing.mybatis.utils.ProxyUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author daxingcao
 * @param <T>
 */
public class AbstractMapperFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;
    private JdbcTemplate template;
    private MapperDefinition definition;

    public AbstractMapperFactory(Class<T> clazz, ResourceLoaderHandler resourceLoader){
        this.interfaceClass = clazz;
        this.template = new JdbcTemplate(resourceLoader.getDataSource());
        if(resourceLoader instanceof AbstractResourceLoaderHandler){
            this.definition = ((AbstractResourceLoaderHandler) resourceLoader)
                    .getMapperDefinition(interfaceClass.getSimpleName());
        }
    }

    public Object executeSqlResolver(Method method, Object[] args)throws Exception{
        Assert.notNull(method,"method must not be empty");
        if(definition == null){
            throw new IllegalStateException("the '"+interfaceClass.getName()+"' does not find matching xml mapping!");
        }
        if(!definition.checkContainMethod(method.getName())){
            throw new IllegalStateException("the");
        }
        MapperMethod mapperMethod = definition.getMethod(method.getName());
        Object obj = null;
        switch (mapperMethod.getType()){
            case "select":
                obj = executeSelect(method,mapperMethod);break;
            case "update":
                obj = executeUpdate(method,mapperMethod);break;
            default:
                throw new IllegalArgumentException("");
        }
        return obj;
    }

    private Object executeSelect(Method method,MapperMethod mapperMethod)throws ClassNotFoundException{
        String sql = mapperMethod.getExecuteSql();
        String resultType = mapperMethod.getResultType();
        Class<?> resCls = Class.forName(resultType);
        Class<?> returnType = method.getReturnType();
        List list = template.query(sql,new BeanPropertyRowMapper<>(resCls));
        if(List.class.equals(returnType)){
            return list;
        }else {
            if(!returnType.equals(resCls)){
                throw new ClassCastException("the method expects to return "+returnType.getName()+",but the mapping resultType attribute is "+resCls.getName());
            }
            if(list.size() > 1){
                throw new IllegalStateException("Expectation 1, activity "+list.size());
            }
            if(list.isEmpty()){
                return null;
            }
            return list.get(0);
        }
    }

    private Object executeUpdate(Method method,MapperMethod mapperMethod){
        String sql = mapperMethod.getExecuteSql();
        int update = template.update(sql);
        if(method.getReturnType() == null){
            return null;
        }else{
            return update;
        }
    }

    @Override
    public T getObject() throws Exception {
        return (T) ProxyUtil.getProxy(this);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
