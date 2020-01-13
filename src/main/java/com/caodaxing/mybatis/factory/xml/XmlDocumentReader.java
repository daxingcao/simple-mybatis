package com.caodaxing.mybatis.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author daxingcao
 */
public class XmlDocumentReader {

    private EntityResolver entityResolver;
    private ErrorHandler errorHandler;

    public XmlDocumentReader(){
        Log logger = LogFactory.getLog(getClass());
        this.errorHandler = new SimpleSaxErrorHandler(logger);
    }

    private EntityResolver getEntityResolver() {
        if (this.entityResolver == null) {
            this.entityResolver = new MapperEntityResolver();
        }
        return this.entityResolver;
    }

    private Document doLoadDocument(InputSource inputSource)throws Exception{
        DocumentLoader documentLoader = new DefaultDocumentLoader();
        return documentLoader.loadDocument(inputSource, getEntityResolver(), errorHandler,
                XmlValidationModeDetector.VALIDATION_DTD, false);
    }

    public Document loadDocument(InputSource inputSource,Resource resource){
        try {
            return doLoadDocument(inputSource);
        }
        catch (BeanDefinitionStoreException ex) {
            throw ex;
        }
        catch (SAXParseException ex) {
            throw new XmlBeanDefinitionStoreException(resource.getDescription(),
                    "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
        }
        catch (SAXException ex) {
            throw new XmlBeanDefinitionStoreException(resource.getDescription(),
                    "XML document from " + resource + " is invalid", ex);
        }
        catch (ParserConfigurationException ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                    "Parser configuration exception parsing XML from " + resource, ex);
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                    "IOException parsing XML document from " + resource, ex);
        }
        catch (Throwable ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                    "Unexpected exception parsing XML document from " + resource, ex);
        }
    }

}
