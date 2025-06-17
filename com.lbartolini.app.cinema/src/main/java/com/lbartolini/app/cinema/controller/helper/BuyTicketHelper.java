package com.lbartolini.app.cinema.controller.helper;

public interface BuyTicketHelper {

	int getRemainingTickets(String filmId);

	void buyTicket(String filmId, String username);

}
