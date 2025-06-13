package com.lbartolini.app.cinema.model;

import java.util.Objects;

public class FilmProjection {
	
	private String id;
	private String name;
	private String datetime;
	private int baseTickets;
	private int premiumTickets;
	private int deluxeTickets;
	
	public FilmProjection(String id, String name, String datetime, int baseTickets, int premiumTickets,
			int deluxeTickets) {
		this.id = id;
		this.name = name;
		this.datetime = datetime;
		this.baseTickets = baseTickets;
		this.premiumTickets = premiumTickets;
		this.deluxeTickets = deluxeTickets;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getBaseTickets() {
		return baseTickets;
	}

	public void setBaseTickets(int baseTickets) {
		this.baseTickets = baseTickets;
	}

	public int getPremiumTickets() {
		return premiumTickets;
	}

	public void setPremiumTickets(int premiumTickets) {
		this.premiumTickets = premiumTickets;
	}

	public int getDeluxeTickets() {
		return deluxeTickets;
	}

	public void setDeluxeTickets(int deluxeTickets) {
		this.deluxeTickets = deluxeTickets;
	}

	@Override
	public String toString() {
		return "FilmProjection [id=" + id + ", name=" + name + ", datetime=" + datetime + ", baseTickets=" + baseTickets
				+ ", premiumTickets=" + premiumTickets + ", deluxeTickets=" + deluxeTickets + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseTickets, datetime, deluxeTickets, id, name, premiumTickets);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilmProjection other = (FilmProjection) obj;
		return baseTickets == other.baseTickets && Objects.equals(datetime, other.datetime)
				&& deluxeTickets == other.deluxeTickets && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && premiumTickets == other.premiumTickets;
	}
	
}
