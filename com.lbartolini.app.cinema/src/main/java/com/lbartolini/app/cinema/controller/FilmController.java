package com.lbartolini.app.cinema.controller;

import com.lbartolini.app.cinema.controller.helper.BuyTicketHelper;
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

	public void buyTicket(String filmId, String username, BuyTicketHelper buyHelper) {
		int ticketsRemaining = buyHelper.getRemainingTickets(filmId);
		
		if (ticketsRemaining <= 0) {
			cinemaView.showError("No Base Tickets available");
			return;
		}
		
		User user = userRepository.getUser(username);
		
		if (user == null) {
			cinemaView.showError("User not registered");
			return;
		}
		
		buyHelper.buyTicket(filmId, user.getUsername());
		
		cinemaView.showTickets(userRepository.getTickets(user.getUsername()));
	}

}
