package com.caodaxing.mybatis;

import com.caodaxing.mybatis.factory.xml.DefaultMapperDefinitionDocumentReader;
import com.caodaxing.mybatis.factory.xml.MapperDefinitionDocumentReader;
import com.caodaxing.mybatis.factory.xml.XmlDocumentReader;
import com.caodaxing.mybatis.mapper.MapperDefinitionResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>加载xml资源</p>
 * @author daxingcao
 */
public abstract class BaseXmlResourceLoaderHandler extends AbstractResourceLoaderHandler implements MapperDefinitionResolver {

    private ThreadLocal<MapperDefinitionDocumentReader> reader = new ThreadLocal<>();

    @Override
    protected void loadMapperDefinitions()throws IOException {
        //接口xml映射资源
        Resource[] resources = getResources(this.resolvePackagePath(XML_URL_SUFFIX));
        if(resources != null && resources.length > 0){
            for(Resource resource : resources){
                EncodedResource encodedResource = new EncodedResource(resource);
                InputStream inputStream = encodedResource.getResource().getInputStream();
                //转换成xml流
                InputSource inputSource = new InputSource(inputStream);
                //设置编码格式
                if(encodedResource.getEncoding() != null){
                    inputSource.setEncoding(encodedResource.getEncoding());
                }
                XmlDocumentReader xmlDocumentReader = new XmlDocumentReader();
                Document document = xmlDocumentReader.loadDocument(inputSource, resource);
                test(document,resource);
            }
        }
    }

    public void test(Document document,Resource resource){
        if(document != null){
            Element element = document.getDocumentElement();
            MapperDefinitionDocumentReader reader = createMapperDefinitionDocumentReader();
            reader.registerMapperDefinitions(element,resource);
        }
    }

    private MapperDefinitionDocumentReader createMapperDefinitionDocumentReader(){
        MapperDefinitionDocumentReader reader = this.reader.get();
        if(reader != null){
            return reader;
        }else {
            reader = new DefaultMapperDefinitionDocumentReader(this);
            this.reader.set(reader);
            return reader;
        }
    }

}
