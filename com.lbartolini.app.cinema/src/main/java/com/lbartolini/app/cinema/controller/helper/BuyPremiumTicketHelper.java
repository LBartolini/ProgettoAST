package com.lbartolini.app.cinema.controller.helper;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.repository.FilmRepository;

public class BuyPremiumTicketHelper implements BuyTicketHelper {

	private FilmRepository filmRepository;

	public BuyPremiumTicketHelper(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	@Override
	public int getRemainingTickets(String filmId) {
		Film film = filmRepository.getFilm(filmId);
		if (film == null) return 0;
		return film.getPremiumTicketsTotal()-film.getPremiumTickets().size();
	}

	@Override
	public void buyTicket(String filmId, String username) {
		filmRepository.buyPremiumTicket(filmId, username);
	}

}
