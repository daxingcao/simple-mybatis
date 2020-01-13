package com.caodaxing.mybatis.mapper;

import com.caodaxing.mybatis.MapperDefinition;
import org.springframework.util.Assert;
import java.util.HashMap;
import java.util.Map;

public class RootMapperDefinition implements MapperDefinition {

    private String nameScope;

    private Map<String, MapperMethod> methods = new HashMap<>();

    @Override
    public String getNameScope(){
        return this.nameScope;
    }

    @Override
    public void setNameScope(String nameScope){
        this.nameScope = nameScope;
    }

    @Override
    public void setMethod(String key, MapperMethod method){
        Assert.notNull(key,"the key must not be empty!");
        this.methods.put(key,method);
    }

    @Override
    public MapperMethod getMethod(String name){
        return this.methods.get(name);
    }

    @Override
    public boolean checkContainMethod(String name){
        return this.methods.containsKey(name);
    }

}
