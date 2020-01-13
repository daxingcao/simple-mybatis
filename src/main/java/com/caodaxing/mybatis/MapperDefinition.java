package com.caodaxing.mybatis;


import com.caodaxing.mybatis.mapper.MapperMethod;

public interface MapperDefinition {

    String getNameScope();

    void setNameScope(String nameScope);

    MapperMethod getMethod(String name);

    boolean checkContainMethod(String name);

    void setMethod(String id, MapperMethod method);

}
