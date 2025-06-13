package com.lbartolini.app.cinema.view;

import java.util.List;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;

public interface CinemaView {

	void showAllFilms(List<Film> list);

	void showError(String errorMessage);

	void showTickets(List<Ticket> list);

}
