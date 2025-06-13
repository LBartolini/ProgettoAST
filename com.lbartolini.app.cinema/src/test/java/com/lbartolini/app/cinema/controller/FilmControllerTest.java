package com.lbartolini.app.cinema.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbartolini.app.cinema.model.FilmProjection;
import com.lbartolini.app.cinema.repository.FilmRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class FilmControllerTest {
	
	@InjectMocks
	private FilmController filmController;
	
	private AutoCloseable closeable;
	
	@Mock
	private FilmRepository filmRepository;

	@Mock
	private CinemaView cinemaView;

	@Before
	public void setUp() throws Exception {
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
		List<FilmProjection> films = Arrays.asList(new FilmProjection("ABC123", "Film A", "Room X", "25/06/2025 18:45", 10, 10),
				new FilmProjection("XYZ456", "Film B", "Room Y", "25/06/2025 18:45", 10, 10));
		when(filmRepository.getAllFilms()).thenReturn(films);
		
		filmController.getAllFilms();
		
		verify(cinemaView).showAllFilms(films);
	}

}
