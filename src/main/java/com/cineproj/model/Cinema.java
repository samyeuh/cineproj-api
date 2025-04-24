package com.cineproj.model;

import java.util.UUID;

public class Cinema {
	
	private UUID id;
	private String name;
	private String adresse;
	private String city;
	private UUID owner_id;
	
	public Cinema() {}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public UUID getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(UUID owner_id) {
		this.owner_id = owner_id;
	}
	
	
	
	

}
