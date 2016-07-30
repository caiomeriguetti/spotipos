package spotipos.tests;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertiesResourceTest extends JerseyTest {
	
	public PropertiesResourceTest() {
        super(new spotipos.config.Application());
    }
	
	@Test
	public void testCreate() throws Exception {
		Invocation.Builder request = invocation(target().path("properties"));
		JSONObject toBeCreated = new JSONObject();
		toBeCreated.put("x", 10);
		toBeCreated.put("y", 10);
		toBeCreated.put("title", "Test title");
		toBeCreated.put("description", "Test description");
		toBeCreated.put("price", 10.50);
		toBeCreated.put("beds", 1);
		toBeCreated.put("baths", 3);
		toBeCreated.put("squareMeters", 55);
		
		Response response = request.post(Entity.json(toBeCreated.toString()));
		
		String jsonResponse = response.readEntity(String.class);
		assertNotNull(jsonResponse);
		
		JSONObject responseData = new JSONObject(jsonResponse);
		assertTrue(responseData.getBoolean("success"));
		assertNotNull(responseData.getString("newPropertieId"));
		
		String id = responseData.getString("newPropertieId");
		
		request = invocation(target().path("properties/" + id));
		response = request.get();
		jsonResponse = response.readEntity(String.class);
		
		assertNotNull(jsonResponse);
		
		responseData = new JSONObject(jsonResponse);
		
		assertEquals(id, responseData.getString("id"));
		assertEquals(toBeCreated.getInt("x"), responseData.getInt("x"));
		assertEquals(toBeCreated.getInt("y"), responseData.getInt("y"));
		assertEquals(toBeCreated.getString("title"), responseData.getString("title"));
		assertEquals(toBeCreated.getString("description"), responseData.getString("description"));
		assertEquals(toBeCreated.getLong("price"), responseData.getLong("price"));
		assertEquals(toBeCreated.getInt("beds"), responseData.getInt("beds"));
		assertEquals(toBeCreated.getInt("baths"), responseData.getInt("baths"));
		assertEquals(toBeCreated.getInt("squareMeters"), responseData.getInt("squareMeters"));
	}
	
	@Test
	public void testSearch() throws Exception {
		Invocation.Builder request = invocation(target().path("properties"));
		
	}
	
	private Invocation.Builder invocation(WebTarget target) {
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
		invocationBuilder.header("Content-Type", "text/plain; charset=utf-8");
		invocationBuilder.header("Accept", "*/*");
		invocationBuilder.header("Accept-Encoding", "gzip, deflate, sdch");
		invocationBuilder.header("Accept-Language", "en-US,en;q=0.8");
		
		return invocationBuilder;
	}

}
