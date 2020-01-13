package com.caodaxing.mybatis.factory.xml;

import com.caodaxing.mybatis.MapperDefinition;
import com.caodaxing.mybatis.mapper.MapperMethod;
import com.caodaxing.mybatis.mapper.RootMapperDefinition;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.HashSet;
import java.util.Set;

public class MapperDefinitionParserDelegate {

    private static final String SELECT_ELEMENT = "select";

    private static final String UPDATE_ELEMENT = "update";

    private static final String INSERT_ELEMENT = "insert";

    private static final String DELETE_ELEMENT = "delete";

    private static final String PARAMETER_TYPE_ATTRIBUTE = "parameterType";

    private static final String RESULT_TYPE_ATTRIBUTE = "resultType";

    private static final String ID_ATTRIBUTE = "id";

    private static final String MAPPER_ELEMENT = "mapper";

    private static final String NAME_SCOPE_ATTRIBUTE = "nameScope";

    private String defaultName = null;
    private Set<String> existIdList = new HashSet<>();

    public boolean checkNameScope(Element element){
        return false;
    }

    public MapperDefinition parserElementToMapperDefinition(Element root){
        if(root != null && MAPPER_ELEMENT.equals(root.getNodeName())){
            MapperDefinition definition = new RootMapperDefinition();
            if(root.hasAttribute(NAME_SCOPE_ATTRIBUTE)){
                definition.setNameScope(root.getAttribute(NAME_SCOPE_ATTRIBUTE));
            }
            if(root.hasChildNodes()){
                NodeList childNodes = root.getChildNodes();
                parserChildren(childNodes,definition);
            }
            return definition;
        }
        return null;
    }

    private void parserChildren(NodeList nodeList,MapperDefinition definition){
        if(nodeList != null){
            for(int i = 0; i < nodeList.getLength();i++){
                Node item = nodeList.item(i);
                if(item instanceof Element){
                    Element element = (Element) item;
                    String nodeName = element.getNodeName();
                    switch (nodeName){
                        case SELECT_ELEMENT:
                            parserExecuteElement(element,definition);break;
                        case INSERT_ELEMENT:
                            parserExecuteElement(element,definition);break;
                        case UPDATE_ELEMENT:
                            parserExecuteElement(element,definition);break;
                        case DELETE_ELEMENT:
                            parserExecuteElement(element,definition);break;
                        default:
                    }
                }
            }
        }
    }

    private void parserExecuteElement(Element node,MapperDefinition definition){
        MapperMethod method = new MapperMethod();
        String id = node.getAttribute(ID_ATTRIBUTE);
        if(!StringUtils.hasLength(id)){
            throw new IllegalStateException("the id attribute must exist");
        }
        if(existIdList.contains(id)){
            throw new IllegalStateException("the id can not be repeated");
        }
        method.setType(node.getNodeName());
        synchronized (this){
            existIdList.add(id);
            method.setMethodName(id);
            if(node.hasAttribute(PARAMETER_TYPE_ATTRIBUTE)){
                method.setParameterType(node.getAttribute(PARAMETER_TYPE_ATTRIBUTE));
            }
            if(node.hasAttribute(RESULT_TYPE_ATTRIBUTE)){
                method.setResultType(node.getAttribute(RESULT_TYPE_ATTRIBUTE));
            }
            method.setExecuteSql(node.getTextContent());
            definition.setMethod(id,method);
        }
    }

    public void setDefaultName(Resource resource){
        String filename = resource.getFilename();
        if(!StringUtils.isEmpty(filename)){
            int index = filename.lastIndexOf(".");
            if(index > 0){
                filename = filename.substring(0,index);
                this.defaultName = filename;
            }
        }
    }

    public void initExistIds(){
        this.existIdList.clear();
    }

    public String getName(){
        return this.defaultName;
    }
}
