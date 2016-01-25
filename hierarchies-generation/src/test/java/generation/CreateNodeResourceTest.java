package generation;

import eu.europeana.hierarchies.generation.CreateNodeResource;
import eu.europeana.hierarchy.InputNode;
import eu.europeana.hierarchy.ParentNode;
import eu.europeana.hierarchy.StringValue;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import java.io.IOException;
import java.util.*;

/**
 * Unit test for the create resource
 * Created by ymamakis on 1/25/16.
 */
@RunWith(JUnit4.class)
public class CreateNodeResourceTest {
    ServerControls server;
    @Before
    public void prepare(){
        server = TestServerBuilders.newInProcessBuilder().withExtension("/",CreateNodeResource.class).newServer();
    }

    @Test
    public void testOneNodeCreation() throws IOException{
       WebTarget target = ClientBuilder.newClient().target(server.httpURI());
        //WebTarget target = null;
        InputNode node = new InputNode();
        Set<StringValue> stringValues = new HashSet<>();
        StringValue sv = new StringValue();
        sv.setKey("rdf:about");
        sv.setValue("/test/id");
        stringValues.add(sv);
        node.setStringValues(stringValues);
        Form form = new Form();
        form.param("recordValues",new ObjectMapper().writeValueAsString(node));
        ParentNode pNode = (ParentNode)target.path("/node/generate").request().post(Entity.form(form)).getEntity();
        Assert.assertNotNull(pNode);
    }


}
