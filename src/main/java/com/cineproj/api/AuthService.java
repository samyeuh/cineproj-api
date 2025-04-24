package com.cineproj.api;

import com.cineproj.request.AuthRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthService {

    @POST
    @Path("/login")
    public Response login(AuthRequest request) {
    	if (request == null) {
    	    return Response.status(Response.Status.BAD_REQUEST)
    	                   .entity("{\"message\":\"Missing JSON body\"}")
    	                   .build();
    	}
        String username = request.getUsername();
        String password = request.getPassword();

        if ("admin".equals(username) && "1234".equals(password)) {
            return Response.ok("{\"message\":\"Login successful\"}").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("{\"message\":\"Invalid credentials\"}")
                           .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(AuthRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        return Response.status(Response.Status.CREATED)
                       .entity("{\"message\":\"User registered\"}")
                       .build();
    }
}
