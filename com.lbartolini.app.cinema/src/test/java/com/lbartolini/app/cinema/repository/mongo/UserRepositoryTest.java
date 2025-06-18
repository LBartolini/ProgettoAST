package com.lbartolini.app.cinema.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
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
	public void testGetTicketsWhenBaseAndPremiumArePresentInOneFilm() {
		User user = new User(USERNAME_1);
		insertUserInDB(user);
		
		List<String> baseTickets = new ArrayList<String>();
		baseTickets.add(USERNAME_2);
		baseTickets.add(USERNAME_1);
		List<String> premiumTickets = new ArrayList<String>();
		premiumTickets.add(USERNAME_1);
		Film film = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, baseTickets, premiumTickets);
		insertFilmInDB(film);
		
		assertThat(userRepository.getTickets(USERNAME_1))
			.containsExactly(new Ticket(film, user, 1, 1));
	}
	
	@Test
	public void testGetTickets() {
		User user1 = new User(USERNAME_1);
		insertUserInDB(user1);
		User user2 = new User(USERNAME_2);
		insertUserInDB(user2);
		
		List<String> baseTickets1 = new ArrayList<String>();
		baseTickets1.add(USERNAME_1);
		baseTickets1.add(USERNAME_2);
		baseTickets1.add(USERNAME_2);
		List<String> premiumTickets1 = new ArrayList<String>();
		premiumTickets1.add(USERNAME_1);
		Film film1 = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, baseTickets1, premiumTickets1);
		insertFilmInDB(film1);
		
		List<String> baseTickets2 = new ArrayList<String>();
		baseTickets2.add(USERNAME_1);
		baseTickets2.add(USERNAME_2);
		baseTickets2.add(USERNAME_1);
		List<String> premiumTickets2 = new ArrayList<String>();
		premiumTickets2.add(USERNAME_2);
		premiumTickets2.add(USERNAME_2);
		premiumTickets2.add(USERNAME_1);
		Film film2 = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 5, 5, baseTickets2, premiumTickets2);
		insertFilmInDB(film2);
		
		assertThat(userRepository.getTickets(USERNAME_2))
			.containsExactlyInAnyOrder(
					new Ticket(film1, user2, 2, 0),
					new Ticket(film2, user2, 1, 2));
	}
	
	@Test
	public void testRegisterUser() {
		userRepository.registerUser(USERNAME_1);
		
		assertThat(retrieveAllUsers()).containsExactly(new User(USERNAME_1));
	}
	
	private List<User> retrieveAllUsers() {
		return StreamSupport
				.stream(userCollection.find().spliterator(), false)
				.map((Document d) -> new User(d.getString("username")))
				.collect(Collectors.toList());
	}

	private void insertUserInDB(User user) {
		userCollection.insertOne(new Document("username", user.getUsername()));
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
