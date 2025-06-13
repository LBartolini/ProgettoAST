package com.lbartolini.app.cinema.model;

import java.util.Objects;

public class Film {
	
	private String id;
	private String name;
	private String room;
	private String datetime;
	private int baseTicketsRemaining;
	private int premiumTicketsRemaining;
	
	public Film(String id, String name, String room, String datetime, int baseTicketsRemaining, int premiumTicketsRemaining) {
		super();
		this.id = id;
		this.name = name;
		this.room = room;
		this.datetime = datetime;
		this.baseTicketsRemaining = baseTicketsRemaining;
		this.premiumTicketsRemaining = premiumTicketsRemaining;
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

	public int getBaseTicketsRemaining() {
		return baseTicketsRemaining;
	}

	public int getPremiumTicketsRemaining() {
		return premiumTicketsRemaining;
	}

	@Override
	public int hashCode() {
		return Objects.hash(datetime, id, name, room);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		return Objects.equals(datetime, other.datetime) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(room, other.room);
	}

	@Override
	public String toString() {
		return "FilmProjection [id=" + id + ", name=" + name + ", room=" + room + ", datetime=" + datetime
				+ ", baseTicketsRemaining=" + baseTicketsRemaining + ", premiumTicketsRemaining="
				+ premiumTicketsRemaining + "]";
	}

}
