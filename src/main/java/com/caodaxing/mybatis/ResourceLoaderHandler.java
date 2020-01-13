package com.caodaxing.mybatis;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;

public interface ResourceLoaderHandler {

    void initMapper() throws IOException;

    DataSource getDataSource();

    Set<Class> getClassMap();

    void addMapperDefinition(String name, MapperDefinition definition);

}
