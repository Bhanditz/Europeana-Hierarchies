package eu.europeana.hierarchies.service.impl;


import eu.europeana.hierarchies.service.HierarchyService;
import eu.europeana.hierarchies.service.utils.InputNodeCreator;
import eu.europeana.hierarchy.InputNode;
import eu.europeana.hierarchy.InputNodeList;
import eu.europeana.hierarchy.ParentNode;
import eu.europeana.hierarchy.ParentNodeList;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service Accessing the Neo4j unmanaged extensions
 * Created by ymamakis on 1/22/16.
 */
public class Neo4jHierarchyService implements HierarchyService{

    private ObjectMapper objectMapper = new ObjectMapper();
    private Client client =  ClientBuilder.newBuilder().build();
    @Override
    public ParentNode createNode(Map<String, Object> params) throws IOException {
        InputNode node = InputNodeCreator.createInputNodeFromMap(params);
        if(node!=null){
            WebTarget target = client.target("/node/generate");
            Form form =new Form();
            form.param("recordValues", objectMapper.writeValueAsString(node));
            return target.path("").request().post(Entity.form(form)).readEntity(ParentNode.class);
        }
        return null;
    }

    @Override
    public void createRelationsForNode(String id) {
        WebTarget target = client.target("/relations/generate/"+ StringUtils.replace(id,"/","%2F"));
        target.path("").request().post(null);
    }

    @Override
    public Set<ParentNode> createNodes(List<Map<String, Object>> records) throws IOException {
        InputNodeList list = new InputNodeList();
        List<InputNode> inList = new ArrayList<>();
        for(Map<String,Object> map:records){
            InputNode node = InputNodeCreator.createInputNodeFromMap(map);
            if(node!=null){
                inList.add(node);
            }
        }
        WebTarget target = client.target("/nodes/generate");
        Form form =new Form();
        list.setInputNodeList(inList);
        form.param("recordValues",objectMapper.writeValueAsString(list));
        return target.path("").request().post(Entity.form(form)).readEntity(ParentNodeList.class).getParentNodeList();
    }

    @Override
    public void createRelationsForNodes(List<String> ids) {
        for(String id:ids){
            createRelationsForNode(id);
        }
    }
    @Override
    public void createOrderedAndUnorderedRelationsForNode(String id) {
        String finalId = StringUtils.replace(id,"/","%2F");
        WebTarget target = client.target("/relations/generate/order/"+ finalId);
        target.request().post(null);
        WebTarget unordered = client.target("/order/fakeorder/nodeId/"+ finalId);
        unordered.request().get();
    }

    @Override
    public void createOrderedAndUnorderedRelationsForNodes(List<String> ids) {
        for(String id:ids){
            createOrderedAndUnorderedRelationsForNode(id);
        }
    }

    @Override
    public void deleteNode(String id) {
        WebTarget target = client.target("/relations/delete/"+ StringUtils.replace(id,"/","%2F"));
        target.request().post(null);
    }

    @Override
    public void deleteNodes(List<String> ids) {
        for(String id:ids){
            deleteNode(id);
        }
    }

    @Override
    public void deleteByCollection(String collection){
        WebTarget target = client.target("/collection/"+collection);
        target.request().get();
    }
}
