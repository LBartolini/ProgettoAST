package com.lbartolini.app.cinema.model;

import java.util.Objects;

public class FilmProjection {
	
	private String id;
	private String name;
	private String room;
	private String datetime;
	private int baseTickets;
	private int premiumTickets;
	
	public FilmProjection(String id, String name, String room, String datetime, int baseTickets, int premiumTickets) {
		super();
		this.id = id;
		this.name = name;
		this.room = room;
		this.datetime = datetime;
		this.baseTickets = baseTickets;
		this.premiumTickets = premiumTickets;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRoom() {
		return room;
	}

	public String getDatetime() {
		return datetime;
	}

	public int getBaseTickets() {
		return baseTickets;
	}

	public int getPremiumTickets() {
		return premiumTickets;
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseTickets, datetime, id, name, premiumTickets, room);
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
				&& Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && premiumTickets == other.premiumTickets
				&& Objects.equals(room, other.room);
	}

	@Override
	public String toString() {
		return "FilmProjection [id=" + id + ", name=" + name + ", room=" + room + ", datetime=" + datetime
				+ ", baseTickets=" + baseTickets + ", premiumTickets=" + premiumTickets + "]";
	}
	

}
