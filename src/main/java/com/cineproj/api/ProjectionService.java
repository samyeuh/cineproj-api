package com.cineproj.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cineproj.model.Projection;
import com.cineproj.utils.ProjectionDAO;

@Path("/projections")
@Produces("application/json")
@Consumes("application/json")
public class ProjectionService {
	// TODO
	private ProjectionDAO projectionDAO;
	
	@POST
	@Path("/add")
	public Response addProjection(Projection projection) {
		try {
			projectionDAO.insertProjection(projection);
			return Response.status(Response.Status.CREATED).entity(projection).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\":\"" + e.getMessage() + "\"}").build();
		}
	}
}
