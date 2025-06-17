package com.lbartolini.app.cinema.controller.helper;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.repository.FilmRepository;

public class BuyBaseTicketHelper implements BuyTicketHelper {

	private FilmRepository filmRepository;

	public BuyBaseTicketHelper(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	@Override
	public int getRemainingTickets(String filmId) {
		Film film = filmRepository.getFilm(filmId);
		if (film == null) return 0;
		return film.getBaseTicketsTotal()-film.getBaseTickets().size();
	}

	@Override
	public void buyTicket(String filmId, String username) {
		filmRepository.buyBaseTicket(filmId, username);
	}

}
