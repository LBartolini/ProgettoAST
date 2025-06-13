package com.lbartolini.app.cinema.model;

public class BaseTicket extends Ticket {

	public BaseTicket(Film film, User user) {
		super(film, user);
	}

	@Override
	public String getTicketType() {
		return "Base";
	}

}
