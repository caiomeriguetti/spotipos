package spotipos.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import spotipos.model.SearchPropertiesResponse;
import spotipos.services.PropertiesService;

@Path("properties")
public class PropertiesResource {

	@Inject PropertiesService propertiesService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("ax") Integer ax,
						   @QueryParam("ay") Integer ay,
						   @QueryParam("bx") Integer bx,
						   @QueryParam("by") Integer by) {
		
		
		SearchPropertiesResponse response = new SearchPropertiesResponse();
		response.properties = propertiesService.getProperties(ax, ay, bx, by);
		response.foundProperties = response.properties.length;
		
		return Response.ok().entity(response).build();
	}

}
