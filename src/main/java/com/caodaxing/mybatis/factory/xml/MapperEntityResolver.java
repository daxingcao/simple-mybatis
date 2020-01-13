package com.caodaxing.mybatis.factory.xml;

import org.springframework.core.io.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.IOException;

/**
 * @author daxingcao
 */
public class MapperEntityResolver implements EntityResolver {

    private final static String DTD_SUFFIX = ".dtd";

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
        if(systemId != null && systemId.endsWith(DTD_SUFFIX)){
            String fileName = systemId.substring(systemId.lastIndexOf("/") + 1);
            Resource resource = new ClassPathResource(fileName,getClass());
            InputSource inputSource = new InputSource(resource.getInputStream());
            inputSource.setPublicId(publicId);
            inputSource.setSystemId(systemId);
            return inputSource;
        }
        return null;
    }

}
