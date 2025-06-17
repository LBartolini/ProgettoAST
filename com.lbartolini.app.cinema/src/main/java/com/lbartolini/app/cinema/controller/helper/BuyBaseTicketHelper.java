package com.lbartolini.app.cinema.controller.helper;

import com.lbartolini.app.cinema.repository.FilmRepository;

public class BuyBaseTicketHelper implements BuyTicketHelper {

	private FilmRepository filmRepository;

	public BuyBaseTicketHelper(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	@Override
	public int getRemainingTickets(String filmId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void buyTicket(String filmId, String username) {
		filmRepository.buyBaseTicket(filmId, username);
	}

}
