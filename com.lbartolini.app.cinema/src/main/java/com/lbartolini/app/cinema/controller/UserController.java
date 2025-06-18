package com.lbartolini.app.cinema.controller;

import com.lbartolini.app.cinema.controller.exceptions.UserAlreadyRegisteredException;
import com.lbartolini.app.cinema.controller.exceptions.UserNotRegisteredException;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class UserController {
	
	private CinemaView cinemaView;
	private UserRepository userRepository;

	public UserController(CinemaView cinemaView, UserRepository userRepository) {
		this.cinemaView = cinemaView;
		this.userRepository = userRepository;
		
	}

	public void registerUser(String username) throws UserAlreadyRegisteredException {
		if (userRepository.getUser(username) != null) {
			cinemaView.showError("User already registered");
			throw new UserAlreadyRegisteredException();		
		}
		
		userRepository.registerUser(username);
	}

	public void getTickets(String username) throws UserNotRegisteredException {
		if (userRepository.getUser(username) == null) {
			cinemaView.showError("User not registered");
			throw new UserNotRegisteredException();
		}
		
		cinemaView.showTickets(userRepository.getTickets(username));
	}

}
