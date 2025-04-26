package com.cineproj.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

import com.cineproj.model.Projection;
import com.cineproj.request.ProjectionSearchRequest;
import com.cineproj.utils.CinemaDAO;
import com.cineproj.utils.FilmDAO;
import com.cineproj.utils.ProjectionDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/projections")
@Produces("application/json")
@Consumes("application/json")
public class ProjectionService {
	// TODO
	private ProjectionDAO projectionDAO = new ProjectionDAO();
	private CinemaDAO cinemaDAO = new CinemaDAO();
	private FilmDAO filmDAO = new FilmDAO();
	
	@POST
	@Path("/add")
	public Response addProjection(Projection projection) {
	    try {
	        // Convertir le calendrier de String vers LocalTime
	        if (projection.getCalendrier() != null) {
	            Map<String, List<LocalTime>> calendrierConverted = new HashMap<>();
	            for (Map.Entry<String, List<String>> entry : projection.getCalendrier().entrySet()) {
	                List<LocalTime> horaires = new ArrayList<>();
	                for (String heureStr : entry.getValue()) {
	                    horaires.add(LocalTime.parse(heureStr));
	                }
	                calendrierConverted.put(entry.getKey(), horaires);
	            }
	            projection.setCalendrier(calendrierConverted);
	        }

	        projectionDAO.insertProjection(projection);
	        return Response.status(Response.Status.CREATED).entity(projection).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity("{\"error\":\"" + e.getMessage() + "\"}")
	                .build();
	    }
	}
	
	@PUT
	@Path("/{id}")
	public Response editProjection(@PathParam("id") String id, Projection projection) {
	    try {
	        Projection existingProjection = projectionDAO.getProjectionById(id);
	        if (existingProjection == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("{\"error\":\"Projection not found\"}")
	                           .build();
	        }

	        // Sécurise la mise à jour uniquement si les champs sont présents
	        if (projection.getFilm() != null && projection.getFilm().getId() != null) {
	            existingProjection.setFilm(filmDAO.getFilmById(projection.getFilm().getId().toString()));
	        }
	        if (projection.getCinema() != null && projection.getCinema().getId() != null) {
	            existingProjection.setCinema(cinemaDAO.getCinemaById(projection.getCinema().getId().toString()));
	        }
	        if (projection.getDateDebut() != null) {
	            existingProjection.setDateDebut(LocalDate.parse(projection.getDateDebut()));
	        }
	        if (projection.getDateFin() != null) {
	            existingProjection.setDateFin(LocalDate.parse(projection.getDateFin()));
	        }
	        if (projection.getCalendrier() != null && !projection.getCalendrier().isEmpty()) {
	            Map<String, List<LocalTime>> calendrierConverted = new HashMap<>();
	            for (Map.Entry<String, List<String>> entry : projection.getCalendrier().entrySet()) {
	                List<LocalTime> horaires = new ArrayList<>();
	                for (String heureStr : entry.getValue()) {
	                    if (heureStr != null && !heureStr.trim().isEmpty()) {
	                        horaires.add(LocalTime.parse(heureStr));
	                    }
	                }
	                calendrierConverted.put(entry.getKey(), horaires);
	            }
	            existingProjection.setCalendrier(calendrierConverted);
	        }

	        projectionDAO.updateProjection(existingProjection);

	        return Response.ok()
	                       .entity(existingProjection)
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
	public Response deleteProjection(@PathParam("id") String id) {
	    try {
	        Projection existingProjection = projectionDAO.getProjectionById(id);
	        if (existingProjection == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("{\"error\":\"Projection not found\"}")
	                           .build();
	        }

	        projectionDAO.deleteProjection(id);

	        return Response.ok()
	                       .entity("{\"message\":\"Projection deleted successfully\"}")
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
	public Response getProjectionById(@PathParam("id") String projectionId) {
		try {
			Projection projection = projectionDAO.getProjectionById(projectionId);
			
			if (projection == null) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity("{\"error\":\"No projections found\"}")
                        .build();
			}
			
			return Response.ok().entity(projection).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
		}
	}
	
	@POST
	public Response searchProjections(ProjectionSearchRequest request) {
		try {
			List<Projection> projections = projectionDAO.searchProjections(
				request.id,
				request.film,
				request.cinema,
				request.dateDebut,
				request.dateFin,
				request.daysOfWeek,
				request.hour
			);
			if (projections.isEmpty()) {
				 return Response.status(Response.Status.NOT_FOUND)
                         .entity("{\"error\":\"No projections found\"}")
                         .build();
			}
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> response = new HashMap<>();
			
			response.put("count", projections.size());
			response.put("results", projections);
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
