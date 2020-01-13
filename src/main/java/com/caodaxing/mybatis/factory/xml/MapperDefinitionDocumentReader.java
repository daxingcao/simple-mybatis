package com.caodaxing.mybatis.factory.xml;

import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

public interface MapperDefinitionDocumentReader {

    void registerMapperDefinitions(Element root, Resource resource);

}
