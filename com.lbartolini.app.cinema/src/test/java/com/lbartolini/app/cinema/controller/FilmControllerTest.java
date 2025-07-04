package com.lbartolini.app.cinema.controller;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyList;
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

import com.lbartolini.app.cinema.controller.helper.BuyTicketHelper;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.FilmRepository;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class FilmControllerTest {
	
	private static final String USERNAME = "USERNAME_XYZ";
	
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
	private FilmController filmController;
	
	private AutoCloseable closeable;
	
	@Mock
	private FilmRepository filmRepository;
	
	@Mock
	private BuyTicketHelper buyHelper;

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
	public void testGetAllFilmsWhenNoFilmExists() {
		when(filmRepository.getAllFilms()).thenReturn(Collections.emptyList());
		
		filmController.getAllFilms();
		
		verify(cinemaView).showAllFilms(Collections.emptyList());
	}
	
	@Test
	public void testGetAllFilmsWhenSomeArePresent() {
		List<Film> films = Arrays.asList(new Film(FILM_ID_1, FILM_NAME_1, FILM_ROOM_1, FILM_DATETIME_1, FILM_TOTAL_BASE_TICKETS_1, FILM_TOTAL_PREMIUM_TICKETS_1, Collections.emptyList(), Collections.emptyList()),
				new Film(FILM_ID_2, FILM_NAME_2, FILM_ROOM_2, FILM_DATETIME_2, FILM_TOTAL_BASE_TICKETS_2, FILM_TOTAL_PREMIUM_TICKETS_2, Collections.emptyList(), Collections.emptyList()));
		when(filmRepository.getAllFilms()).thenReturn(films);
		
		filmController.getAllFilms();
		
		verify(cinemaView).showAllFilms(films);
	}
	
	@Test
	public void testBuyTicketWhenNoTicketsAvailable() {
		when(buyHelper.getRemainingTickets(FILM_ID_1)).thenReturn(0);
		
		filmController.buyTicket(FILM_ID_1, USERNAME, buyHelper);
		
		verify(buyHelper).getRemainingTickets(FILM_ID_1);
		verify(cinemaView).showError("No Tickets available");
		verifyNoMoreInteractions(buyHelper);
		verifyNoMoreInteractions(cinemaView);
	}
	
	@Test
	public void testBuyTicketWhenUserNotPresent() {
		when(buyHelper.getRemainingTickets(FILM_ID_1)).thenReturn(1);
		when(userRepository.getUser(USERNAME)).thenReturn(null);
		
		filmController.buyTicket(FILM_ID_1, USERNAME, buyHelper);
		
		InOrder inOrder = inOrder(buyHelper, userRepository, cinemaView);
		inOrder.verify(buyHelper).getRemainingTickets(FILM_ID_1);
		inOrder.verify(userRepository).getUser(USERNAME);
		inOrder.verify(cinemaView).showError("User not registered");
		inOrder.verifyNoMoreInteractions();
	}
	
	@Test
	public void testBuyTicketOneAvailable() {
		when(buyHelper.getRemainingTickets(FILM_ID_1)).thenReturn(1);
		User user = new User(USERNAME); 
		when(userRepository.getUser(USERNAME)).thenReturn(user);
		
		assertThatNoException().isThrownBy(() -> filmController.buyTicket(FILM_ID_1, USERNAME, buyHelper));
		
		InOrder inOrder = inOrder(buyHelper, userRepository, cinemaView);
		inOrder.verify(buyHelper).getRemainingTickets(FILM_ID_1);
		inOrder.verify(userRepository).getUser(USERNAME);
		inOrder.verify(buyHelper).buyTicket(FILM_ID_1, USERNAME);
		inOrder.verify(userRepository).getTickets(USERNAME);
		inOrder.verify(cinemaView).showTickets(anyList());
		inOrder.verify(cinemaView).showAllFilms(anyList());
	}
	
	@Test
	public void testBuyTicketMultipleAvailable() {
		when(buyHelper.getRemainingTickets(FILM_ID_1)).thenReturn(4);
		User user = new User(USERNAME); 
		when(userRepository.getUser(USERNAME)).thenReturn(user);
		
		assertThatNoException().isThrownBy(() -> filmController.buyTicket(FILM_ID_1, USERNAME, buyHelper));
		
		InOrder inOrder = inOrder(buyHelper, userRepository, cinemaView);
		inOrder.verify(buyHelper).getRemainingTickets(FILM_ID_1);
		inOrder.verify(userRepository).getUser(USERNAME);
		inOrder.verify(buyHelper).buyTicket(FILM_ID_1, USERNAME);
		inOrder.verify(userRepository).getTickets(USERNAME);
		inOrder.verify(cinemaView).showTickets(anyList());
		inOrder.verify(cinemaView).showAllFilms(anyList());
	}
	
}
