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

@Path("/projections")
@Produces("application/json")
@Consumes("application/json")
public class ProjectionService {

	private static List<Projection> projections = new ArrayList<>();
	private static int currentId = 1;
	
	@POST
	public Response addProjection(Projection projection) {
		projection.setId(currentId++);
		projections.add(projection);
		return Response.status(Response.Status.CREATED).entity(projection).build();
	}
	
	@GET
	public List<Projection> getAllProjections() {
		return projections;
	}
}
