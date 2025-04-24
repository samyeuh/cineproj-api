package com.cineproj.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.cineproj.model.Cinema;
import com.cineproj.utils.CinemaDAO;

@Path("/cinemas")
@Produces("application/json")
@Consumes("application/json")
public class CinemaService {

	public CinemaDAO cinemaDAO = new CinemaDAO();
	
	@POST
	public Response addCinema(Cinema cinema) {
		try {
			cinemaDAO.insertCinema(cinema);
		} catch (Exception e) {
			e.printStackTrace();
		    return Response.status(Response.Status.BAD_REQUEST)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
		return Response.status(Response.Status.CREATED).entity(cinema).build();
	}
	
	@GET
	public Response getAllCinemas() {
		List<Cinema> cinemas = new ArrayList<>();
		try {
			cinemas = cinemaDAO.getAllCinemas();
		} catch (Exception e) {
			e.printStackTrace();
		    return Response.status(Response.Status.BAD_REQUEST)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
		
		return Response.ok().entity(cinemas).build();
	}
	
	@GET
	public Response getCinemaById(@QueryParam("id") String id) {
		Cinema cinema;
		try {
			cinema = cinemaDAO.getCinemaById(UUID.fromString(id));
		} catch (Exception e) {
			e.printStackTrace();
		    return Response.status(Response.Status.BAD_REQUEST)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
		
		return Response.ok().entity(cinema).build();
	}
}
