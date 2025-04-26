package com.cineproj.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cineproj.model.Cinema;
import com.cineproj.request.CinemaSearchRequest;
import com.cineproj.utils.CinemaDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/cinemas")
@Produces("application/json")
@Consumes("application/json")
public class CinemaService {

	public CinemaDAO cinemaDAO = new CinemaDAO();
	
	@POST
	@Path("/add")
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
	
	@PUT
	@Path("/{id}")
	public Response editCinema(@PathParam("id") String id, Cinema cinema) {
	    try {
	        Cinema existingCinema = cinemaDAO.getCinemaById(id);
	        if (existingCinema == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("{\"error\":\"Cinema not found\"}")
	                           .build();
	        }

	        // Mise à jour seulement des champs envoyés
	        if (cinema.getName() != null) existingCinema.setName(cinema.getName());
	        if (cinema.getAddress() != null) existingCinema.setAddress(cinema.getAddress());
	        if (cinema.getCity() != null) existingCinema.setCity(cinema.getCity());
	        if (cinema.getOwner_id() != null) existingCinema.setOwner_id(cinema.getOwner_id());

	        cinemaDAO.updateCinema(existingCinema);

	        return Response.ok()
	                       .entity(existingCinema)
	                       .build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("{\"error\":\"" + e.getMessage() + "\"}")
	                       .build();
	    }
	}

	@DELETE
	@Path("/{id}")
	public Response deleteCinema(@PathParam("id") String id) {
	    try {
	        Cinema existingCinema = cinemaDAO.getCinemaById(id);
	        if (existingCinema == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("{\"error\":\"Cinema not found\"}")
	                           .build();
	        }

	        cinemaDAO.deleteCinema(id);

	        return Response.ok()
	                       .entity("{\"message\":\"Cinema deleted successfully\"}")
	                       .build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("{\"error\":\"" + e.getMessage() + "\"}")
	                       .build();
	    }
	}

	
	@GET
	@Path("/{id}")
	public Response getCinemaById(@PathParam("id") String id) {
		try {
			Cinema cinema = cinemaDAO.getCinemaById(id);
			if (cinema == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("{\"error\":\"No cinemas found\"}")
						.build();
			}
			return Response.ok().entity(cinema).build();
		} catch (Exception e) {
		    return Response.status(Response.Status.BAD_REQUEST)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
		
		
	}
	
	@POST
	public Response searchCinemas(CinemaSearchRequest request) {
		
		try {
			List<Cinema> cinemas = cinemaDAO.searchCinemas(
					request.id,
					request.name,
					request.address,
					request.city,
					request.owner_id
					);
			if (cinemas.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("{\"error\":\"No cinemas found\"}")
						.build();
			}
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> response = new HashMap<>();
			
			response.put("count", cinemas.size());
			response.put("results", cinemas);
			String json = mapper.writeValueAsString(response);

			return Response.ok()
			    .entity(json)
			    .build();
			
		} catch (Exception e) {
			e.printStackTrace();
		    return Response.status(Response.Status.BAD_REQUEST)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
	}
}
