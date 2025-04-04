package com.cineproj.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Film {
    private int id;
    private String titre;
    private int dureeEnMinute;
    private String lang;
    private String soustitres;
    private String realisateur;
    private List<String> acteurs;
    private int ageMin;
    
    public Film() {}

	public Film(int id, String titre, int dureeEnMinute, String lang, String soustitres, String realisateur,
			List<String> acteurs, int ageMin) {
		this.id = id;
		this.titre = titre;
		this.dureeEnMinute = dureeEnMinute;
		this.lang = lang;
		this.soustitres = soustitres;
		this.realisateur = realisateur;
		this.acteurs = acteurs;
		this.ageMin = ageMin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int getDureeEnMinute() {
		return dureeEnMinute;
	}

	public void setDureeEnMinute(int dureeEnMinute) {
		this.dureeEnMinute = dureeEnMinute;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getSoustitres() {
		return soustitres;
	}

	public void setSoustitres(String soustitres) {
		this.soustitres = soustitres;
	}

	public String getRealisateur() {
		return realisateur;
	}

	public void setRealisateur(String realisateur) {
		this.realisateur = realisateur;
	}

	public List<String> getActeurs() {
		return acteurs;
	}

	public void setActeurs(List<String> acteurs) {
		this.acteurs = acteurs;
	}

	public int getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(int ageMin) {
		this.ageMin = ageMin;
	}	
    
}
