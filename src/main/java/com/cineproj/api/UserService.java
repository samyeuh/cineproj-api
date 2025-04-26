package com.cineproj.api;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.cineproj.model.User;

@Path("/users")
public class UserService {
	// TODO
	public int userDAO = 0;
	
	@PUT
	@Path("/{id}")
	public Response editUser(@PathParam("id") String id, User user) {
		try {
			User existingUser = userDAO.getUserById(id);
			if (existingUser == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("{\"error\":\"User not found\"}")
						.build();
			}
			
			if (username.getUsername() != null) existingUser
		}
	}

}
