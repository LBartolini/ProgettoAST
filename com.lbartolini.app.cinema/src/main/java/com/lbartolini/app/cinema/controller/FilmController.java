package com.lbartolini.app.cinema.controller;

import com.lbartolini.app.cinema.repository.FilmRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class FilmController {
	
	private FilmRepository filmRepository;
	private CinemaView cinemaView;

	public FilmController(FilmRepository filmRepository, CinemaView cinemaView) {
		super();
		this.filmRepository = filmRepository;
		this.cinemaView = cinemaView;
	}

	public void getAllFilms() {
		cinemaView.showAllFilms(filmRepository.getAllFilms());
	}

}
