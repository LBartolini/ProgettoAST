package com.lbartolini.app.cinema.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.lbartolini.app.cinema.controller.helper.BuyBaseTicketHelper;
import com.lbartolini.app.cinema.controller.helper.BuyPremiumTicketHelper;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.mongo.FilmMongoRepository;
import com.lbartolini.app.cinema.repository.mongo.UserMongoRepository;
import com.lbartolini.app.cinema.view.CinemaView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class FilmControllerIT {

	@Mock
	private CinemaView cinemaView;
	
	private static final String DB_NAME = "cinema";

	private static final String FILM_COLLECTION_NAME = "film";

	private static final String USER_COLLECTION_NAME = "user";
	
	private static final String FILM_ID = "FILM_ID";
	private static final int FILM_BASE_TICKETS_TOTAL = 4;
	private static final int FILM_PREMIUM_TICKETS_TOTAL = 2;
	private static final int FILM_INITIAL_BASE_TICKETS = 3;
	private static final int FILM_INITIAL_PREMIUM_TICKETS = 1;
	private Film film;
	
	private static final String USERNAME = "USERNAME";
	
	private FilmController filmController;
	
	@ClassRule
	public static MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	
	private MongoClient mongoClient;
	
	private FilmMongoRepository filmRepository;
	private UserMongoRepository userRepository;
	
	private MongoCollection<Document> filmCollection;

	private AutoCloseable closeable;

	private BuyBaseTicketHelper buyBaseTicketHelper;

	private BuyPremiumTicketHelper buyPremiumTicketHelper;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		filmRepository = new FilmMongoRepository(mongoClient, DB_NAME, FILM_COLLECTION_NAME);
		userRepository = new UserMongoRepository(mongoClient, DB_NAME, USER_COLLECTION_NAME, FILM_COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		filmCollection = database.getCollection(FILM_COLLECTION_NAME);
		filmController = new FilmController(filmRepository, userRepository, cinemaView);
		
		buyBaseTicketHelper = new BuyBaseTicketHelper(filmRepository);
		buyPremiumTicketHelper = new BuyPremiumTicketHelper(filmRepository);
		
		userRepository.registerUser(USERNAME);
		film = new Film(FILM_ID, "NAME_1", "ROOM_1", "DATETIME_1", FILM_BASE_TICKETS_TOTAL, FILM_PREMIUM_TICKETS_TOTAL, 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_BASE_TICKETS, USERNAME)), 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_PREMIUM_TICKETS, USERNAME)));
		insertFilmInDB(film);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
		mongoClient.close();
	}

	@Test
	public void testGetAllFilms() {
		filmController.getAllFilms();
		
		verify(cinemaView).showAllFilms(Arrays.asList(film));
	}
	
	@Test
	public void testBuyBaseTicket() {
		filmController.buyTicket(FILM_ID, "another_user", buyBaseTicketHelper);
		verify(cinemaView).showError("User not registered");
		
		filmController.buyTicket(FILM_ID, USERNAME, buyBaseTicketHelper);
		
		film.getBaseTickets().add(USERNAME);
		verify(cinemaView).showTickets(Arrays.asList(new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS+1, FILM_INITIAL_PREMIUM_TICKETS)));
		verify(cinemaView).showAllFilms(Arrays.asList(film));
		
		filmController.buyTicket(FILM_ID, USERNAME, buyBaseTicketHelper);
		
		verify(cinemaView).showError("No Tickets available");
		verifyNoMoreInteractions(cinemaView);
	}
	
	@Test
	public void testBuyPremiumTicket() {
		filmController.buyTicket(FILM_ID, "another_user", buyPremiumTicketHelper);
		verify(cinemaView).showError("User not registered");
		
		filmController.buyTicket(FILM_ID, USERNAME, buyPremiumTicketHelper);
		
		film.getPremiumTickets().add(USERNAME);
		verify(cinemaView).showTickets(Arrays.asList(new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS, FILM_INITIAL_PREMIUM_TICKETS+1)));
		verify(cinemaView).showAllFilms(Arrays.asList(film));
		
		filmController.buyTicket(FILM_ID, USERNAME, buyPremiumTicketHelper);
		
		verify(cinemaView).showError("No Tickets available");
		verifyNoMoreInteractions(cinemaView);
	}
	
	private void insertFilmInDB(Film film) {
		filmCollection.insertOne(new Document()
				.append("id", film.getId())
				.append("name", film.getName())
				.append("room", film.getRoom())
				.append("datetime", film.getDatetime())
				.append("baseTicketsTotal", film.getBaseTicketsTotal())
				.append("premiumTicketsTotal", film.getPremiumTicketsTotal())
				.append("baseTickets", film.getBaseTickets())
				.append("premiumTickets", film.getPremiumTickets())
				);
	}
}
