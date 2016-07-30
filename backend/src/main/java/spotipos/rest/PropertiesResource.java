package spotipos.rest;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import exceptions.InvalidPropertieException;
import spotipos.model.CreatePropertieResponse;
import spotipos.model.Propertie;
import spotipos.model.SearchPropertiesResponse;
import spotipos.services.PropertiesService;

@Path("properties")
public class PropertiesResource {

	@Inject PropertiesService propertiesService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@NotNull String body) {
		
		JSONObject jsonBody = new JSONObject(body);
		
		CreatePropertieResponse response = new CreatePropertieResponse();
		try {
			
			Propertie newPropertie = new Propertie();
			newPropertie.loadJsonObject(jsonBody);
			propertiesService.create(newPropertie);
			response.success = true;
			response.newPropertieId = newPropertie.getId();
			return Response.ok().entity(response).build();
			
		} catch (InvalidPropertieException e) {
			
			// bad request
			response.errors = e.getValidationResult();
			response.success = false;
			return Response.status(400).entity(response).build();
			
		}
	}
		
	
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
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@NotNull @PathParam("id") String id) {
		
		Propertie propertie = propertiesService.getPropertie(id);
		
		if (propertie == null) {
			return Response.status(404).build();
		}
		
		return Response.ok().entity(propertie).build();
	}

}
