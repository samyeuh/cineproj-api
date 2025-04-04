package com.cineproj.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.cineproj.model.Film;

@Path("/films")
@Produces("application/json")
@Consumes("application/json")
public class FilmService {
	
	private static List<Film> films = new ArrayList<>();
	private static int currentId = 1;
	
	@POST
	public Response addFilm(Film film) {
		film.setId(currentId++);
		films.add(film);
		return Response.status(Response.Status.CREATED).entity(film).build();
	}
	
	@GET
	public List<Film> getAllFilms(){
		return films;
	}

}
