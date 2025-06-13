package com.lbartolini.app.cinema.controller;

import java.util.List;

import com.lbartolini.app.cinema.model.FilmProjection;
import com.lbartolini.app.cinema.repository.FilmRepository;

public class FilmController {
	
	private FilmRepository filmRepository;

	public FilmController(FilmRepository filmRepository) {
		super();
		this.filmRepository = filmRepository;
	}

	public List<FilmProjection> getAllFilms() {
		return filmRepository.getAllFilms();
	}

}
