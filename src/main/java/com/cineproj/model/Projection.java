package com.cineproj.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Projection {
	
	private int id;
	private Film film;
	private Cinema cinema;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	private Map<String, List<LocalTime>> calendrier;
	
	public Projection() {}
	
	public Projection(int id, Film film, Cinema cinema, LocalDate dateDebut, LocalDate dateFin,
			Map<String, List<LocalTime>> calendrier) {
		super();
		this.id = id;
		this.film = film;
		this.cinema = cinema;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.calendrier = calendrier;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public Map<String, List<LocalTime>> getCalendrier() {
		return calendrier;
	}

	public void setCalendrier(Map<String, List<LocalTime>> calendrier) {
		this.calendrier = calendrier;
	}
	
	
	

}
