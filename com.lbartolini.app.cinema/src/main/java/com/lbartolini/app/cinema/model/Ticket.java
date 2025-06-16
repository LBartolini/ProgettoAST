package com.lbartolini.app.cinema.model;

import java.util.Objects;

public class Ticket {
	
	private Film film;
	private User user;
	private TicketType ticketType;
	private int numberOfTickets;
	
	public Ticket(Film film, User user, TicketType ticketType, int numberOfTickets) {
		super();
		this.film = film;
		this.user = user;
		this.ticketType = ticketType;
		this.numberOfTickets = numberOfTickets;
	}

	public Film getFilm() {
		return film;
	}

	public User getUser() {
		return user;
	}
	

	public TicketType getTicketType() {
		return ticketType;
	}

	public int getNumberOfTickets() {
		return numberOfTickets;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(film, numberOfTickets, ticketType, user);
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
		return Objects.equals(film, other.film) && numberOfTickets == other.numberOfTickets
				&& ticketType == other.ticketType && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Ticket [film=" + film + ", user=" + user + ", ticketType=" + ticketType + ", numberOfTickets="
				+ numberOfTickets + "]";
	}

}
