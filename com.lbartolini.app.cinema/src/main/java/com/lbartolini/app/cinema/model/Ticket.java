package com.lbartolini.app.cinema.model;

import java.util.Objects;

public class Ticket {
	
	private Film film;
	private User user;
	private int baseTickets;
	private int premiumTickets;
	
	public Ticket(Film film, User user, int baseTickets, int premiumTickets) {
		super();
		this.film = film;
		this.user = user;
		this.baseTickets = baseTickets;
		this.premiumTickets = premiumTickets;
	}

	public Film getFilm() {
		return film;
	}

	public User getUser() {
		return user;
	}

	public int getBaseTickets() {
		return baseTickets;
	}

	public int getPremiumTickets() {
		return premiumTickets;
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseTickets, film, premiumTickets, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		return baseTickets == other.baseTickets && Objects.equals(film, other.film)
				&& premiumTickets == other.premiumTickets && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Ticket [film=" + film + ", user=" + user + ", baseTickets=" + baseTickets + ", premiumTickets="
				+ premiumTickets + "]";
	}
	
}
