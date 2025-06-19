package com.lbartolini.app.cinema.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.lbartolini.app.cinema.model.Film;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class FilmRepositoryTest {
	
	private static final String DB_NAME = "cinema";
	private static final String FILM_COLLECTION_NAME = "film";
	
	private static final String FILM_ID = "FILM_ID";
	private static final String USERNAME = "USERNAME";
	
	@ClassRule
	public static MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient mongoClient;
	private FilmMongoRepository filmRepository;
	private MongoCollection<Document> filmCollection;

	@Before
	public void setUp() {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		filmRepository = new FilmMongoRepository(mongoClient, DB_NAME, FILM_COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		filmCollection = database.getCollection(FILM_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Test
	public void testGetAllFilmsWhenNoFilmsPresent() {
		assertThat(filmRepository.getAllFilms()).isEmpty();
	}
	
	@Test
	public void testGetAllFilmsWhenOnePresent() {
		Film film = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(2, "SOME_USER"), Collections.emptyList());
		insertFilmInDB(film);
		
		assertThat(filmRepository.getAllFilms()).containsExactly(film);
	}
	
	@Test
	public void testGetAllFilmsWhenSomePresent() {
		Film film1 = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(2, "SOME_USER"), Collections.emptyList());
		insertFilmInDB(film1);
		Film film2 = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 5, 5, Collections.nCopies(1, "SOME_USER"), Collections.nCopies(3, "SOME_USER"));
		insertFilmInDB(film2);
		
		assertThat(filmRepository.getAllFilms()).containsExactly(film1, film2);
	}
	
	@Test
	public void testGetFilmWhenNoFilmsPresent() {
		assertThat(filmRepository.getFilm(FILM_ID)).isNull();
	}
	
	@Test
	public void testGetFilmWhenFilmNotPresent() {
		Film anotherFilm = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(2, "SOME_USER"), Collections.emptyList());
		insertFilmInDB(anotherFilm);
		
		assertThat(filmRepository.getFilm(FILM_ID)).isNull();
	}
	
	@Test
	public void testGetFilmWhenOneIsPresent() {
		String filmId = "ID_1";
		Film filmToFind = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(2, "SOME_USER"), Collections.emptyList());
		insertFilmInDB(filmToFind);
		
		assertThat(filmRepository.getFilm(filmId)).isEqualTo(filmToFind);
	}
	
	@Test
	public void testGetFilmWhenSomeArePresent() {
		Film anotherFilm = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 5, 5, Collections.nCopies(1, "SOME_USER"), Collections.nCopies(3, "SOME_USER"));
		insertFilmInDB(anotherFilm);
		String filmId = "ID_1";
		Film filmToFind = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(2, "SOME_USER"), Collections.emptyList());
		insertFilmInDB(filmToFind);
		
		assertThat(filmRepository.getFilm(filmId)).isEqualTo(filmToFind);
	}
	
	@Test
	public void testBuyBaseTicketWhenFirstToBuy() {
		String filmId = "ID_1";
		Film film = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.emptyList());
		insertFilmInDB(film);
		
		filmRepository.buyBaseTicket(filmId, USERNAME);
		
		assertThat(getFilmFromDB(filmId).getBaseTickets()).containsExactly(USERNAME);
	}
	
	@Test
	public void testBuyBaseTicketWhenOtherAlreadyBoughtSome() {
		String filmId = "ID_1";
		String otherUser = "OTHER_USER_ID";
		Film film = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(1, otherUser), Collections.emptyList());
		insertFilmInDB(film);
		
		filmRepository.buyBaseTicket(filmId, USERNAME);
		
		assertThat(getFilmFromDB(filmId).getBaseTickets()).containsExactly(otherUser, USERNAME);
	}
	
	@Test
	public void testBuyBaseTicketWhenUserAlreadyBoughtOne() {
		String filmId = "ID_1";
		Film film = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(1, USERNAME), Collections.emptyList());
		insertFilmInDB(film);
		
		filmRepository.buyBaseTicket(filmId, USERNAME);
		
		assertThat(getFilmFromDB(filmId).getBaseTickets()).containsExactly(USERNAME, USERNAME);
	}
	
	@Test
	public void testBuyPremiumTicketWhenFirstToBuy() {
		String filmId = "ID_1";
		Film film = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.emptyList());
		insertFilmInDB(film);
		
		filmRepository.buyPremiumTicket(filmId, USERNAME);
		
		assertThat(getFilmFromDB(filmId).getPremiumTickets()).containsExactly(USERNAME);
	}
	
	@Test
	public void testBuyPremiumTicketWhenOtherAlreadyBoughtSome() {
		String filmId = "ID_1";
		String otherUser = "OTHER_USER_ID";
		Film film = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.nCopies(1, otherUser));
		insertFilmInDB(film);
		
		filmRepository.buyPremiumTicket(filmId, USERNAME);
		
		assertThat(getFilmFromDB(filmId).getPremiumTickets()).containsExactly(otherUser, USERNAME);
	}
	
	@Test
	public void testBuyPremiumTicketWhenUserAlreadyBoughtOne() {
		String filmId = "ID_1";
		Film film = new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.nCopies(1, USERNAME));
		insertFilmInDB(film);
		
		filmRepository.buyPremiumTicket(filmId, USERNAME);
		
		assertThat(getFilmFromDB(filmId).getPremiumTickets()).containsExactly(USERNAME, USERNAME);
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
	
	private Film getFilmFromDB(String filmId) {
		return FilmMongoRepository.convertDocumentToFilm(filmCollection.find(Filters.eq("id", filmId)).first());
	}

}
