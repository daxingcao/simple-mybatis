package com.caodaxing.mybatis.factory.xml;

import com.caodaxing.mybatis.AbstractResourceLoaderHandler;
import com.caodaxing.mybatis.MapperDefinition;
import com.caodaxing.mybatis.ResourceLoaderHandler;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

/**
 * @author daxingcao
 */
public class DefaultMapperDefinitionDocumentReader implements MapperDefinitionDocumentReader {

    private ResourceLoaderHandler resourceLoaderHandler;
    private MapperDefinitionParserDelegate delegate;

    public DefaultMapperDefinitionDocumentReader(AbstractResourceLoaderHandler resourceLoaderHandler){
        this.resourceLoaderHandler = resourceLoaderHandler;
        this.delegate = new MapperDefinitionParserDelegate();
    }

    @Override
    public void registerMapperDefinitions(Element root,Resource resource) {
        if(root != null){
            initDelegate(resource);
            parserBefore(root);
            parserMapperDefinitions(root);
        }
    }

    protected void parserBefore(Element root){}

    private void parserMapperDefinitions(Element root){
        MapperDefinition definition = delegate.parserElementToMapperDefinition(root);
        this.getRegister().addMapperDefinition(delegate.getName(),definition);
    }

    private ResourceLoaderHandler getRegister(){
        return this.resourceLoaderHandler;
    }

    private void initDelegate(Resource resource){
        if(this.delegate == null){
            this.delegate = new MapperDefinitionParserDelegate();
        }
        delegate.setDefaultName(resource);
        delegate.initExistIds();
    }

}
