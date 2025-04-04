package com.cineproj.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cineproj.model.Cinema;

@Path("/cinemas")
@Produces("application/json")
@Consumes("application/json")
public class CinemaService {

	private static List<Cinema> cinemas = new ArrayList<>();
	private static int currentId = 1;
	
	@POST
	public Response addCinema(Cinema cinema) {
		cinema.setId(currentId++);
		cinemas.add(cinema);
		return Response.status(Response.Status.CREATED).entity(cinema).build();
	}
	
	@GET
	public List<Cinema> getAllCinemas() {
		return cinemas;
	}
}
