package com.lbartolini.app.cinema.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.lbartolini.app.cinema.controller.FilmController;
import com.lbartolini.app.cinema.controller.UserController;
import com.lbartolini.app.cinema.controller.helper.BuyBaseTicketHelper;
import com.lbartolini.app.cinema.controller.helper.BuyPremiumTicketHelper;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.repository.mongo.FilmMongoRepository;
import com.lbartolini.app.cinema.repository.mongo.UserMongoRepository;
import com.lbartolini.app.cinema.view.swing.CinemaSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RunWith(GUITestRunner.class)
public class SwingViewIT extends AssertJSwingJUnitTestCase {
	
	private static final String DB_NAME = "cinema";
	private static final String FILM_COLLECTION_NAME = "film";
	private static final String USER_COLLECTION_NAME = "user";
	
	private static final String FILM_ID = "FILM_ID";
	private static final int FILM_INITIAL_BASE_TICKETS = 3;
	private static final int FILM_INITIAL_PREMIUM_TICKETS = 5;
	private static final String USERNAME = "USERNAME";
	
	@ClassRule
	public static MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient mongoClient;
	private FilmMongoRepository filmRepository;
	private UserMongoRepository userRepository;
	private FilmController filmController;
	private UserController userController;
	private BuyBaseTicketHelper buyBaseTicketHelper;
	private BuyPremiumTicketHelper buyPremiumTicketHelper;
	private CinemaSwingView cinemaSwingView;
	private MongoCollection<Document> filmCollection;
	private Film film;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		filmRepository = new FilmMongoRepository(mongoClient, DB_NAME, FILM_COLLECTION_NAME);
		userRepository = new UserMongoRepository(mongoClient, DB_NAME, USER_COLLECTION_NAME, FILM_COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		filmCollection = database.getCollection(FILM_COLLECTION_NAME);
		
		film = new Film(FILM_ID, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_BASE_TICKETS, USERNAME)), 
				new ArrayList<String>(Collections.nCopies(FILM_INITIAL_PREMIUM_TICKETS, USERNAME)));
		insertFilmInDB(film);
		userRepository.registerUser(USERNAME);
		
		buyBaseTicketHelper = new BuyBaseTicketHelper(filmRepository);
		buyPremiumTicketHelper = new BuyPremiumTicketHelper(filmRepository);
		
		GuiActionRunner.execute(() -> {
			cinemaSwingView = new CinemaSwingView(buyBaseTicketHelper, buyPremiumTicketHelper);
			filmController = new FilmController(filmRepository, userRepository, cinemaSwingView);
			userController = new UserController(userRepository, cinemaSwingView);
			cinemaSwingView.setFilmController(filmController);
			cinemaSwingView.setUserController(userController);
			return cinemaSwingView;
		});
		
		window = new FrameFixture(robot(), cinemaSwingView);
		window.show();
	}
	
	@Override
	public void onTearDown() throws Exception {
		mongoClient.close();
	}
	
	@Test @GUITest
	public void testInitialConfiguration() {
		assertThat(window.list("filmList").contents()).containsExactly(film.toString());
		assertThat(window.list("ticketList").contents()).isEmpty();
	}
	
	@Test @GUITest
	public void testRegisterUser() {
		String userAlreadyRegistered = USERNAME;
		window.textBox("usernameTextBox").enterText(userAlreadyRegistered);
		window.button(JButtonMatcher.withText("Register")).click();
		
		window.label("errorLabel").requireText("User already registered");
		
		String userToRegister = "some_other_username";
		window.textBox("usernameTextBox").setText("").enterText(userToRegister);
		window.button(JButtonMatcher.withText("Register")).click();
		
		window.label("errorLabel").requireText(" ");
		assertThat(userRepository.getUser(userToRegister)).isEqualTo(new User(userToRegister));
	}
	
	@Test @GUITest
	public void testLoginShowingTicketsPurchased() {
		window.textBox("usernameTextBox").enterText("some_unregistered_user");
		window.button(JButtonMatcher.withText("Login")).click();
		
		window.label("errorLabel").requireText("User not registered");
		
		window.textBox("usernameTextBox").setText("").enterText(USERNAME);
		window.button(JButtonMatcher.withText("Login")).click();
		
		window.label("errorLabel").requireText(" ");
		assertThat(window.list("ticketList").contents()).containsExactly(new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS, FILM_INITIAL_PREMIUM_TICKETS).toString());
	}
	
	@Test @GUITest
	public void testBuyBaseTicket() {
		Film filmToBuy = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 7, 7, 
				new ArrayList<String>(Collections.emptyList()), 
				new ArrayList<String>(Collections.emptyList()));
		insertFilmInDB(filmToBuy);
		GuiActionRunner.execute(() -> filmController.getAllFilms());
		
		window.textBox("usernameTextBox").setText("").enterText(USERNAME);
		window.button(JButtonMatcher.withText("Login")).click();
		
		assertThat(window.list("ticketList").contents()).containsExactly(
				new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS, FILM_INITIAL_PREMIUM_TICKETS).toString());
		
		window.list("filmList").selectItem(1);
		window.button(JButtonMatcher.withText("Buy Base")).click();
		
		filmToBuy.getBaseTickets().add(USERNAME);
		
		assertThat(window.list("ticketList").contents()).containsExactly(
				new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS, FILM_INITIAL_PREMIUM_TICKETS).toString(),
				new Ticket(filmToBuy, new User(USERNAME), 1, 0).toString());
		assertThat(window.list("filmList").contents()).containsExactly(film.toString(), filmToBuy.toString());
	}
	
	@Test @GUITest
	public void testBuyPremiumTicket() {
		Film filmToBuy = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 7, 7, 
				new ArrayList<String>(Collections.emptyList()), 
				new ArrayList<String>(Collections.emptyList()));
		insertFilmInDB(filmToBuy);
		GuiActionRunner.execute(() -> filmController.getAllFilms());
		
		window.textBox("usernameTextBox").setText("").enterText(USERNAME);
		window.button(JButtonMatcher.withText("Login")).click();
		
		assertThat(window.list("ticketList").contents()).containsExactly(
				new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS, FILM_INITIAL_PREMIUM_TICKETS).toString());
		
		window.list("filmList").selectItem(1);
		window.button(JButtonMatcher.withText("Buy Premium")).click();
		
		filmToBuy.getPremiumTickets().add(USERNAME);
		
		assertThat(window.list("ticketList").contents()).containsExactly(
				new Ticket(film, new User(USERNAME), FILM_INITIAL_BASE_TICKETS, FILM_INITIAL_PREMIUM_TICKETS).toString(),
				new Ticket(filmToBuy, new User(USERNAME), 0, 1).toString());
		assertThat(window.list("filmList").contents()).containsExactly(film.toString(), filmToBuy.toString());
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
