package spotipos.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import spotipos.services.PropertiesService;

@Path("properties")
public class PropertiesResource {

	@Inject PropertiesService propertiesService;
	
	@GET
	@Path("index")
	public String index() {
		return "Index";
	}

}
