package com.lbartolini.app.cinema.model;

import java.util.Objects;

public abstract class Ticket {
	
	private Film film;
	private User user;
	
	public Ticket(Film film, User user) {
		super();
		this.film = film;
		this.user = user;
	}

	public Film getFilm() {
		return film;
	}

	public User getUser() {
		return user;
	}
	
	abstract public String getTicketType();

	@Override
	public int hashCode() {
		return Objects.hash(film, user);
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
		return Objects.equals(film, other.film) && Objects.equals(user, other.user) && Objects.equals(getTicketType(), other.getTicketType());
	}

	@Override
	public String toString() {
		return "Ticket [film=" + film + ", user=" + user + ", getTicketType()=" + getTicketType() + "]";
	}
	
	

}
