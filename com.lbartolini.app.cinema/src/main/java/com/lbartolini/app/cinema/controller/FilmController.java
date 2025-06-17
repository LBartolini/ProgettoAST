package com.lbartolini.app.cinema.controller;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.FilmRepository;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class FilmController {
	
	private FilmRepository filmRepository;
	private CinemaView cinemaView;
	private UserRepository userRepository;

	public FilmController(FilmRepository filmRepository, UserRepository userRepository, CinemaView cinemaView) {
		super();
		this.filmRepository = filmRepository;
		this.userRepository = userRepository;
		this.cinemaView = cinemaView;
	}

	public void getAllFilms() {
		cinemaView.showAllFilms(filmRepository.getAllFilms());
	}

	public void buyBaseTicket(String filmId, String username) throws NoTicketsAvailableException {
		Film film = filmRepository.getFilm(filmId);
		int ticketsRemaining = film.getBaseTicketsTotal()-film.getBaseTickets().size();
		
		if (ticketsRemaining <= 0) {
			cinemaView.showError("No Base Tickets available");
			throw new NoTicketsAvailableException();
		}
		
		User user = userRepository.getUser(username);
		
		filmRepository.buyBaseTicket(filmId, user.getUsername());
		
		cinemaView.showTickets(userRepository.getTickets(user.getUsername()));
	}

}
