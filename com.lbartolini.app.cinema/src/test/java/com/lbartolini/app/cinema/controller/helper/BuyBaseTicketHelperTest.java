package com.lbartolini.app.cinema.controller.helper;

import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbartolini.app.cinema.repository.FilmRepository;

public class BuyBaseTicketHelperTest {
	
	private static final String FILM_ID = "FILM_ID";

	private static final String USERNAME = "USERNAME";

	@Mock
	public FilmRepository filmRepository;
	
	@InjectMocks
	public BuyBaseTicketHelper buyBaseTicketHelper;
	
	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		buyBaseTicketHelper = new BuyBaseTicketHelper(filmRepository);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testBuyBaseTicket() {
		buyBaseTicketHelper.buyTicket(FILM_ID, USERNAME);
		
		verify(filmRepository).buyBaseTicket(FILM_ID, USERNAME);
	}

}
