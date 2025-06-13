package com.lbartolini.app.cinema.repository;

import java.util.List;

import com.lbartolini.app.cinema.model.FilmProjection;

public interface FilmRepository {

	List<FilmProjection> getAllFilms();
}
