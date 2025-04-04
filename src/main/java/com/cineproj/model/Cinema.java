package com.cineproj.model;

public class Cinema {
	
	private int id;
	private String name;
	private String adresse;
	private String ville;
	
	public Cinema() {}
	
	public Cinema(int id, String name, String adresse, String ville) {
		super();
		this.id = id;
		this.name = name;
		this.adresse = adresse;
		this.ville = ville;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
	
	

}
