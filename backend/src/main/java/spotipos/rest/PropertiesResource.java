package spotipos.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import redis.clients.jedis.Jedis;
import spotipos.services.PropertiesService;

@Path("properties")
public class PropertiesResource {

	@Inject PropertiesService propertiesService;
	
	@GET
	@Path("index")
	public String index() {
		Jedis jedis = new Jedis("localhost");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		
		return value;
	}

}
