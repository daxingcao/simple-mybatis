package com.caodaxing.mybatis.factory;

import com.caodaxing.mybatis.MapperDefinition;
import com.caodaxing.mybatis.context.MapperDefinitionRegistor;
import com.caodaxing.mybatis.context.MapperFactory;

/**
 * @author daxingcao
 */
public class BasicMapperFactory implements MapperFactory, MapperDefinitionRegistor {
    @Override
    public void registerMapperDefinition(MapperDefinition mapperDefinition) {

    }

    @Override
    public Object getMapper(String name) {
        return null;
    }

    @Override
    public Object getMapper(Class clazz) {
        return null;
    }
}
