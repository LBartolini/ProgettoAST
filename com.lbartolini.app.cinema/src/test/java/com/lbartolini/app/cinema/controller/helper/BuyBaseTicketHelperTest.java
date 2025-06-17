package com.lbartolini.app.cinema.controller.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.repository.FilmRepository;

public class BuyBaseTicketHelperTest {

	private static final String USERNAME = "USERNAME";
	
	private static final String FILM_DATETIME = "25/06/2025 18:45";
	private static final String FILM_ROOM = "Room X";
	private static final String FILM_ID = "ABC123";
	private static final String FILM_NAME = "Film A";
	private static final int FILM_TOTAL_PREMIUM_TICKETS = 10;

	@Mock
	public FilmRepository filmRepository;
	
	@InjectMocks
	public BuyBaseTicketHelper buyPremiumTicketHelper;
	
	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		buyPremiumTicketHelper = new BuyBaseTicketHelper(filmRepository);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testBuyBaseTicket() {
		buyPremiumTicketHelper.buyTicket(FILM_ID, USERNAME);
		
		verify(filmRepository).buyBaseTicket(FILM_ID, USERNAME);
	}
	
	@Test
	public void testGetRemainingTicketsWhenFilmDoesNotExist() {
		when(filmRepository.getFilm(FILM_ID)).thenReturn(null);
		
		assertThat(buyPremiumTicketHelper.getRemainingTickets(FILM_ID)).isZero();
		
		verify(filmRepository).getFilm(FILM_ID);
	}
	
	@Test
	public void testGetRemainingTicketsWhenOneIsLeft() {
		int baseTicketsTotal = 1;
		int expectedRemainingTickets = 1;
		Film film = new Film(FILM_ID, FILM_NAME, FILM_ROOM, FILM_DATETIME, baseTicketsTotal, FILM_TOTAL_PREMIUM_TICKETS, Collections.emptyList(), Collections.emptyList());
		when(filmRepository.getFilm(FILM_ID)).thenReturn(film);
		
		assertThat(buyPremiumTicketHelper.getRemainingTickets(FILM_ID)).isEqualTo(expectedRemainingTickets);
		
		verify(filmRepository).getFilm(FILM_ID);
	}
	
	@Test
	public void testGetRemainingTicketsWhenSomeAreLeft() {
		int baseTicketsTotal = 5;
		int expectedRemainingTickets = 2;
		Film film = new Film(FILM_ID, FILM_NAME, FILM_ROOM, FILM_DATETIME, baseTicketsTotal, FILM_TOTAL_PREMIUM_TICKETS, Collections.nCopies(baseTicketsTotal-expectedRemainingTickets, "SOME_USER"), Collections.emptyList());
		when(filmRepository.getFilm(FILM_ID)).thenReturn(film);
		
		assertThat(buyPremiumTicketHelper.getRemainingTickets(FILM_ID)).isEqualTo(expectedRemainingTickets);
		
		verify(filmRepository).getFilm(FILM_ID);
	}

}
