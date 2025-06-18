package com.lbartolini.app.cinema.controller;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbartolini.app.cinema.controller.exceptions.UserAlreadyRegisteredException;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.lbartolini.app.cinema.view.CinemaView;

public class UserControllerTest {
	
	private static final String USERNAME = "USERNAME";


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

}
