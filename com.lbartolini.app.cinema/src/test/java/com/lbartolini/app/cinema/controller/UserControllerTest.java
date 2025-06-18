package com.lbartolini.app.cinema.controller;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbartolini.app.cinema.controller.exceptions.UserAlreadyRegisteredException;
import com.lbartolini.app.cinema.controller.exceptions.UserNotRegisteredException;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class UserControllerTest {
	
	private static final String USERNAME = "USERNAME";
	
	private static final String FILM_DATETIME_1 = "25/06/2025 18:45";
	private static final String FILM_ROOM_1 = "Room X";
	private static final String FILM_ID_1 = "ABC123";
	private static final String FILM_NAME_1 = "Film A";
	private static final int FILM_TOTAL_PREMIUM_TICKETS_1 = 10;
	private static final int FILM_TOTAL_BASE_TICKETS_1 = 10;

	private static final String FILM_ID_2 = "XYZ456";
	private static final String FILM_NAME_2 = "Film B";
	private static final String FILM_ROOM_2 = "Room Y";
	private static final String FILM_DATETIME_2 = "28/07/2025 21:00";
	private static final int FILM_TOTAL_BASE_TICKETS_2 = 10;
	private static final int FILM_TOTAL_PREMIUM_TICKETS_2 = 10;

	@InjectMocks
	UserController userController;
	
	
	private AutoCloseable closeable;

	@Mock
	private CinemaView cinemaView;

	@Mock
	private UserRepository userRepository;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testRegisterUserWhenUserAlreadyRegistered() {
		when(userRepository.getUser(USERNAME)).thenReturn(new User(USERNAME));
		
		assertThrows(UserAlreadyRegisteredException.class, () -> userController.registerUser(USERNAME));
		
		verify(userRepository).getUser(USERNAME);
		verify(cinemaView).showError("User already registered");
		verifyNoMoreInteractions(userRepository);
		verifyNoMoreInteractions(cinemaView);
	}
	
	@Test
	public void testRegisterUser() {
		when(userRepository.getUser(USERNAME)).thenReturn(null);
		
		assertThatNoException().isThrownBy(() -> userController.registerUser(USERNAME));
		
		InOrder inOrder = inOrder(userRepository, cinemaView);
		inOrder.verify(userRepository).getUser(USERNAME);
		inOrder.verify(userRepository).registerUser(USERNAME);
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void testGetTicketsWhenUserNotRegistered() {
		when(userRepository.getUser(USERNAME)).thenReturn(null);
		
		assertThrows(UserNotRegisteredException.class, () -> userController.getTickets(USERNAME));
		
		verify(userRepository).getUser(USERNAME);
		verify(cinemaView).showError("User not registered");
		verifyNoMoreInteractions(userRepository);
		verifyNoMoreInteractions(cinemaView);
	}
	
	@Test
	public void testGetTicketsWhenNoTicketsBought() {
		when(userRepository.getUser(USERNAME)).thenReturn(new User(USERNAME));
		when(userRepository.getTickets(USERNAME)).thenReturn(Collections.emptyList());
		
		assertThatNoException().isThrownBy(() -> userController.getTickets(USERNAME));
		
		InOrder inOrder = inOrder(userRepository, cinemaView);
		inOrder.verify(userRepository).getUser(USERNAME);
		inOrder.verify(userRepository).getTickets(USERNAME);
		inOrder.verify(cinemaView).showTickets(Collections.emptyList());
	}
	
	@Test
	public void testGetTicketsWhenSomeTicketsBought() {
		User user = new User(USERNAME);
		when(userRepository.getUser(USERNAME)).thenReturn(user);
		int baseTicketsFilm1 = 1;
		int premiumTicketsFilm1 = 2;
		Film film1 = new Film(FILM_ID_1, FILM_NAME_1, FILM_ROOM_1, FILM_DATETIME_1, FILM_TOTAL_BASE_TICKETS_1, FILM_TOTAL_PREMIUM_TICKETS_1, Collections.nCopies(baseTicketsFilm1, USERNAME), Collections.nCopies(premiumTicketsFilm1, USERNAME));
		int baseTicketsFilm2 = 3;
		int premiumTicketsFilm2 = 0;
		Film film2 = new Film(FILM_ID_2, FILM_NAME_2, FILM_ROOM_2, FILM_DATETIME_2, FILM_TOTAL_BASE_TICKETS_2, FILM_TOTAL_PREMIUM_TICKETS_2, Collections.nCopies(baseTicketsFilm2, USERNAME), Collections.nCopies(premiumTicketsFilm2, USERNAME));
		List<Ticket> tickets = Arrays.asList(
				new Ticket(film1, user, baseTicketsFilm1, premiumTicketsFilm1),
				new Ticket(film2, user, baseTicketsFilm2, premiumTicketsFilm2));
		when(userRepository.getTickets(USERNAME)).thenReturn(tickets);
		
		assertThatNoException().isThrownBy(() -> userController.getTickets(USERNAME));
		
		InOrder inOrder = inOrder(userRepository, cinemaView);
		inOrder.verify(userRepository).getUser(USERNAME);
		inOrder.verify(userRepository).getTickets(USERNAME);
		inOrder.verify(cinemaView).showTickets(tickets);
	}
	
}
