package com.cineproj.api;

import com.cineproj.model.User;
import com.cineproj.request.AuthRequest;
import com.cineproj.utils.AuthDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthService {
	
	public AuthDAO userDAO = new AuthDAO();

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
    	try {
    		User user = new User();
    		user.setUsername(request.getUsername());
    		user.setPassword(request.getPassword());
    		
    		userDAO.login(user);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Response.Status.BAD_REQUEST)
    				.entity("{\"error\":\"" + e.getMessage() + "\"}")
    				.build();
    	}
    	
    	return Response.ok().entity("{\"message\":\"Login successful\"}").build();
    }

    @POST
    @Path("/register")
    public Response register(AuthRequest request) {
    	// TODO: � crypter !
        try {
        	User user = new User();
        	user.setUsername(request.getUsername());
        	user.setPassword(request.getPassword());
        	
        	userDAO.register(user);
        } catch (Exception e) {
        	e.printStackTrace();
        	return Response.status(Response.Status.BAD_REQUEST)
        			.entity("{\"error\":\"" + e.getMessage() + "\"}")
	                   .build();
        }
        
        return Response.status(Response.Status.CREATED)
                       .entity("{\"message\":\"User registered\"}")
                       .build();
    }
}
