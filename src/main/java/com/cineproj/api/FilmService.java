package com.cineproj.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cineproj.model.Film;
import com.cineproj.utils.FilmDAO;

@Path("/films")
@Produces("application/json")
@Consumes("application/json")
public class FilmService {
	
	private FilmDAO filmDAO = new FilmDAO();

	@POST
	public Response addFilm(Film film) {
		try {
			filmDAO.insertFilm(film);
		} catch (Exception e) {
		    e.printStackTrace();
		    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
	    
	    return Response.status(Response.Status.CREATED).entity(film).build();
	}
	
	@GET
	public Response getAllFilms() throws SQLException{
		List<Film> films = new ArrayList<>();
		try {
			films = filmDAO.getAllFilms();
		} catch (Exception e) {
		    e.printStackTrace();
		    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		                   .entity("{\"error\":\"" + e.getMessage() + "\"}")
		                   .build();
		}
		
		return Response.ok().entity(films).build();
	}

}
