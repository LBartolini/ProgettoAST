package com.lbartolini.app.cinema.view;

import java.util.List;

import com.lbartolini.app.cinema.model.FilmProjection;

public interface CinemaView {

	void showAllFilms(List<FilmProjection> list);

}
