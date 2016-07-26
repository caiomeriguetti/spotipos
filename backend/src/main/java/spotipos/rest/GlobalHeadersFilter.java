package spotipos.rest;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
 
public class GlobalHeadersFilter implements ContainerResponseFilter {
 
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {
    	responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
    	responseContext.getHeaders().add("Access-Control-Allow-Methods", "PUT, DELETE, POST, GET");
    }
}