package com.lbartolini.app.cinema.repository;

import java.util.List;

import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;

public interface UserRepository {

	User getUserByUsername(String username);
	
	User getUserById(String userId);

	List<Ticket> getTickets(String userId);

}
