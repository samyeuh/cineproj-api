package com.cineproj.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.cineproj.model.Film;
import com.cineproj.request.FilmSearchRequest;
import com.cineproj.utils.FilmDAO;

@Path("/films")
@Produces("application/json")
@Consumes("application/json")
public class FilmService {
	
	private FilmDAO filmDAO = new FilmDAO();

	@POST
	@Path("/add")
	public Response addFilm(Film film) {
		try {
			filmDAO.insertFilm(film);
			return Response.status(Response.Status.CREATED).entity(film).build();
		} catch (Exception e) {
		    e.printStackTrace();
		    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
	    
	    
	}
	
	@GET
	@Path("/{id}")
	public Response getFilmById(@PathParam("id") String filmId) throws SQLException {
	    try {
	        List<Film> result = filmDAO.searchFilms(
	            filmId, null, null, null, null, null, null, null, null
	        );
	        
	        if (result.isEmpty()) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("{\"error\":\"Film not found\"}")
	                           .build();
	        }

	        return Response.ok().entity(result.get(0)).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("{\"error\":\"" + e.getMessage() + "\"}")
	                       .build();
	    }
	}
	
	@POST
	public Response searchFilms(FilmSearchRequest request) {
	    try {
	        List<Film> films = filmDAO.searchFilms(
	            request.id,
	            request.titre,
	            request.lang,
	            request.realisateur,
	            request.ageMin,
	            request.ageMax,
	            request.soustitres,
	            request.dureeMin,
	            request.dureeMax
	        );
	        return Response.ok(films).build();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("{\"error\":\"" + e.getMessage() + "\"}")
	                       .build();
	    }
	}


}
