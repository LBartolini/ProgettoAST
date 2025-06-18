package com.lbartolini.app.cinema.controller;

import com.lbartolini.app.cinema.controller.exceptions.UserAlreadyRegisteredException;
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
		userRepository.getUser(username);
		cinemaView.showError("User already registered");
		throw new UserAlreadyRegisteredException();
	}

}
