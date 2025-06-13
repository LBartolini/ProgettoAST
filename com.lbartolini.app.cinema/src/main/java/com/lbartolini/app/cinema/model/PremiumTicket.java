package com.lbartolini.app.cinema.model;

public class PremiumTicket extends Ticket {

	public PremiumTicket(Film film, User user) {
		super(film, user);
	}

	@Override
	public String getTicketType() {
		return "Premium";
	}

}
