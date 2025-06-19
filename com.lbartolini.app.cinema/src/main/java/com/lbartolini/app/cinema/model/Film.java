package com.lbartolini.app.cinema.model;

import java.util.List;
import java.util.Objects;

public class Film {
	
	private String id;
	private String name;
	private String room;
	private String datetime;
	private int baseTicketsTotal;
	private int premiumTicketsTotal;
	private List<String> baseTickets;
	private List<String> premiumTickets;
	
	public Film(String id, 
			String name, 
			String room, 
			String datetime, 
			int baseTicketsTotal, 
			int premiumTicketsTotal, 
			List<String> baseTickets,
			List<String> premiumTickets) {
		super();
		this.id = id;
		this.name = name;
		this.room = room;
		this.datetime = datetime;
		this.baseTicketsTotal = baseTicketsTotal;
		this.premiumTicketsTotal = premiumTicketsTotal;
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

	public int getBaseTicketsTotal() {
		return baseTicketsTotal;
	}

	public int getPremiumTicketsTotal() {
		return premiumTicketsTotal;
	}
	
	public List<String> getBaseTickets() {
		return baseTickets;
	}

	public List<String> getPremiumTickets() {
		return premiumTickets;
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseTickets, baseTicketsTotal, datetime, id, name, premiumTickets, premiumTicketsTotal,
				room);
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
		return Objects.equals(baseTickets, other.baseTickets) && baseTicketsTotal == other.baseTicketsTotal
				&& Objects.equals(datetime, other.datetime) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(premiumTickets, other.premiumTickets)
				&& premiumTicketsTotal == other.premiumTicketsTotal && Objects.equals(room, other.room);
	}

	@Override
	public String toString() {
		return "Film [id=" + id + ", name=" + name + ", room=" + room + ", datetime=" + datetime + ", baseTicketsTotal="
				+ baseTicketsTotal + ", premiumTicketsTotal=" + premiumTicketsTotal + ", baseTickets=" + baseTickets
				+ ", premiumTickets=" + premiumTickets + "]";
	}

}
