package com.lbartolini.app.cinema.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.TicketType;
import com.lbartolini.app.cinema.model.User;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserRepositoryTest {

	private static final String USERNAME_1 = "USERNAME_ABC";
	private static final String USERNAME_2 = "USERNAME_XYZ";
	
	private static final String DB_NAME = "cinema";
	private static final String USER_COLLECTION_NAME = "user";
	private static final String FILM_COLLECTION_NAME = "film";
	
	
	@ClassRule
	public static MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient mongoClient;
	private UserMongoRepository userRepository;
	private MongoCollection<Document> userCollection;
	private MongoCollection<Document> filmCollection;

	@Before
	public void setUp() {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		userRepository = new UserMongoRepository(mongoClient, DB_NAME, USER_COLLECTION_NAME, FILM_COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		userCollection = database.getCollection(USER_COLLECTION_NAME);
		filmCollection = database.getCollection(FILM_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Test
	public void testGetUserWhenUserNotPresent() {
		assertThat(userRepository.getUser(USERNAME_1)).isNull();
	}
	
	@Test
	public void testGetUserWhenUserIsPresent() {
		insertUserInDB(new User(USERNAME_1));
		insertUserInDB(new User(USERNAME_2));
		
		assertThat(userRepository.getUser(USERNAME_2)).isEqualTo(new User(USERNAME_2));	
	}
	
	@Test
	public void testGetTicketsWhenUserNotPresent() {
		assertThat(userRepository.getTickets(USERNAME_1)).isEmpty();
	}
	
	@Test
	public void testGetTicketsWhenNoFilmIsPresent() {
		insertUserInDB(new User(USERNAME_1));
		
		assertThat(userRepository.getTickets(USERNAME_1)).isEmpty();
	}
	
	@Test
	public void testGetTicketsWhenOneBaseAndOnePremiumArePresentInOneFilm() {
		User user = new User(USERNAME_1);
		insertUserInDB(user);
		
		Film film = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10);
		List<Document> baseTickets = new ArrayList<Document>();
		baseTickets.add(new Document("username", USERNAME_1));
		baseTickets.add(new Document("username", USERNAME_2));
		List<Document> premiumTickets = new ArrayList<Document>();
		premiumTickets.add(new Document("username", USERNAME_1));
		insertFilmInDB(film, baseTickets, premiumTickets);
		
		assertThat(userRepository.getTickets(USERNAME_1)).containsExactly(
				new Ticket(film, user, TicketType.BASE, 1),
				new Ticket(film, user, TicketType.PREMIUM, 1)
				);
	}
	
//	@Test
//	public void testGetTickets() {
//		User user1 = new User(USERNAME_1);
//		insertUserInDB(user1);
//		User user2 = new User(USERNAME_2);
//		insertUserInDB(user2);
//		
//		Film film1 = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10);
//		List<String> baseTickets1 = new ArrayList<String>();
//		baseTickets1.add(USERNAME_1);
//		baseTickets1.add(USERNAME_2);
//		List<String> premiumTickets1 = new ArrayList<String>();
//		premiumTickets1.add(USERNAME_2);
//		insertFilmInDB(film1, baseTickets1, premiumTickets1);
//		
//		Film film2 = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 5, 5);
//		List<String> baseTickets2 = new ArrayList<String>();
//		baseTickets2.add(USERNAME_1);
//		baseTickets2.add(USERNAME_2);
//		baseTickets2.add(USERNAME_1);
//		List<String> premiumTickets2 = new ArrayList<String>();
//		premiumTickets2.add(USERNAME_1);
//		insertFilmInDB(film2, baseTickets2, premiumTickets2);
//		
//		assertThat(userRepository.getTickets(USERNAME_1))
//			.containsExactly(
//					new Ticket(film1, user1, TicketType.BASE, 1),
//					new Ticket(film2, user1, TicketType.BASE, 2),
//					new Ticket(film2, user1, TicketType.PREMIUM, 1));
//	}
	
	private void insertUserInDB(User user) {
		userCollection.insertOne(new Document("username", user.getUsername()));
	}
	
	private void insertFilmInDB(Film film, List<Document> baseTickets, List<Document> premiumTickets) {
		
		filmCollection.insertOne(new Document()
				.append("id", film.getId())
				.append("name", film.getName())
				.append("room", film.getRoom())
				.append("datetime", film.getDatetime())
				.append("baseTicketsTotal", film.getBaseTicketsTotal())
				.append("premiumTicketsTotal", film.getPremiumTicketsTotal())
				.append("baseTickets", baseTickets)
				.append("premiumTickets", premiumTickets)
				);
	}

}
