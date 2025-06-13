package com.lbartolini.app.cinema.repository;

import java.util.List;

import com.lbartolini.app.cinema.model.Film;

public interface FilmRepository {

	List<Film> getAllFilms();

	void buyBaseTicket(String filmId, String userId);

	Film getFilm(String filmId);
}
