package com.lbartolini.app.cinema.steps;

import java.util.ArrayList;
import java.util.Collections;

import org.bson.Document;

import com.lbartolini.app.cinema.CinemaSwingAppBDD;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.User;
import com.mongodb.MongoClient;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

public class DatabaseSteps {
	
	public static final String DB_NAME = "cinema";
	public static final String USER_COLLECTION_NAME = "user";
	public static final String FILM_COLLECTION_NAME = "film";
	
	public static final String USERNAME_1 = "username_1";
	public static final String USERNAME_2 = "username_2";
	
	public static final String FILM_ID_1 = "ID_1";
	public static final String FILM_NAME_1 = "NAME_1";
	public static final String FILM_ROOM_1 = "ROOM_1";
	public static final String FILM_DATETIME_1 = "DATETIME_1";
	public static final int FILM_BASE_TICKETS_TOTAL_1 = 5;
	public static final int FILM_PREMIUM_TICKETS_TOTAL_1 = 5;
	public static final int FILM_INITIAL_BASE_TICKETS_1 = 2;
	public static final int FILM_INITIAL_PREMIUM_TICKETS_1 = 3;
	
	public static final String FILM_ID_2 = "ID_2";
	public static final String FILM_NAME_2 = "NAME_2";
	public static final String FILM_ROOM_2 = "ROOM_2";
	public static final String FILM_DATETIME_2 = "DATETIME_2";
	public static final int FILM_BASE_TICKETS_TOTAL_2 = 3;
	public static final int FILM_PREMIUM_TICKETS_TOTAL_2 = 10;
	public static final int FILM_INITIAL_BASE_TICKETS_2 = 1;
	public static final int FILM_INITIAL_PREMIUM_TICKETS_2 = 0;
	
	private MongoClient mongoClient;

	@Before
	public void setUp() {
		mongoClient = new MongoClient("localhost", CinemaSwingAppBDD.mongoPort);
		mongoClient.getDatabase(DB_NAME).drop();
	}
	
	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Given("The Database contains some films and users")
	public void the_Database_contains_some_films_and_users() {
		insertUserInDB(new User(USERNAME_1));
		insertUserInDB(new User(USERNAME_2));
		
		insertFilmInDB(new Film(FILM_ID_1, FILM_NAME_1, FILM_ROOM_1, FILM_DATETIME_1, FILM_BASE_TICKETS_TOTAL_1, FILM_PREMIUM_TICKETS_TOTAL_1, 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_BASE_TICKETS_1, USERNAME_1)), 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_PREMIUM_TICKETS_1, USERNAME_1))));
		insertFilmInDB(new Film(FILM_ID_2, FILM_NAME_2, FILM_ROOM_2, FILM_DATETIME_2, FILM_BASE_TICKETS_TOTAL_2, FILM_PREMIUM_TICKETS_TOTAL_2, 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_BASE_TICKETS_2, USERNAME_2)), 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_PREMIUM_TICKETS_2, USERNAME_2))));
	}

	private void insertUserInDB(User user) {
		mongoClient.getDatabase(DB_NAME).getCollection(USER_COLLECTION_NAME)
			.insertOne(new Document("username", user.getUsername()));
	}
	
	private void insertFilmInDB(Film film) {
		mongoClient.getDatabase(DB_NAME).getCollection(FILM_COLLECTION_NAME)
			.insertOne(new Document()
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
