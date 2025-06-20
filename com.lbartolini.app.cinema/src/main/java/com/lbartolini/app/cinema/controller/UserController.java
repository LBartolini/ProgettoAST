package com.lbartolini.app.cinema.controller;

import com.lbartolini.app.cinema.repository.UserRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class UserController {
	
	private CinemaView cinemaView;
	private UserRepository userRepository;

	public UserController(UserRepository userRepository, CinemaView cinemaView) {
		this.cinemaView = cinemaView;
		this.userRepository = userRepository;
		
	}

	public void registerUser(String username) {
		if (userRepository.getUser(username) != null) {
			cinemaView.showError("User already registered");
			return;
		}
		
		userRepository.registerUser(username);
	}

	public void getTickets(String username) {
		if (userRepository.getUser(username) == null) {
			cinemaView.showError("User not registered");
			return;
		}
		
		cinemaView.showTickets(userRepository.getTickets(username));
	}

}
