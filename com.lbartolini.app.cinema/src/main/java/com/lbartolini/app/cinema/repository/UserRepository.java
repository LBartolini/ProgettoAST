package com.lbartolini.app.cinema.repository;

import java.util.List;

import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;

public interface UserRepository {

	User getUser(String username);

	List<Ticket> getTickets(String username);

	void registerUser(String username);

}
