package com.lbartolini.app.cinema.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.launcher.ApplicationLauncher;

import com.lbartolini.app.cinema.CinemaSwingAppBDD;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CinemaSwingSteps {
	
	private FrameFixture window;
	
	@After
	public void tearDown() {
		if (window != null) window.cleanUp();
	}

	@Given("The Cinema View is shown")
	public void the_Cinema_View_is_shown() {
		ApplicationLauncher.application("com.lbartolini.app.cinema.app.swing.CinemaSwingApp")
			.withArgs(
					"--mongo-port="+CinemaSwingAppBDD.mongoPort,
					"--db-name="+DatabaseSteps.DB_NAME,
					"--film-collection-name="+DatabaseSteps.FILM_COLLECTION_NAME,
					"--user-collection-name="+DatabaseSteps.USER_COLLECTION_NAME
					).start();
		
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Cinema View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}

	@Given("The user provides a username in the text field")
	public void the_user_provides_a_username_in_the_text_field() {
		window.textBox("usernameTextBox").enterText(DatabaseSteps.USERNAME_1);
	}

	@Then("The ticket list shows all the tickets purchased by that user")
	public void the_ticket_list_shows_all_the_tickets_purchased_by_that_user() {
		Film film = new Film(DatabaseSteps.FILM_ID_1, DatabaseSteps.FILM_NAME_1, 
				DatabaseSteps.FILM_ROOM_1, DatabaseSteps.FILM_DATETIME_1, 
				DatabaseSteps.FILM_BASE_TICKETS_TOTAL_1, DatabaseSteps.FILM_PREMIUM_TICKETS_TOTAL_1, 
				new ArrayList<String>(Collections.nCopies(DatabaseSteps.FILM_INITIAL_BASE_TICKETS_1, DatabaseSteps.USERNAME_1)), 
				new ArrayList<String>(Collections.nCopies(DatabaseSteps.FILM_INITIAL_PREMIUM_TICKETS_1, DatabaseSteps.USERNAME_1)));
		
		assertThat(window.list("ticketList").contents())
			.containsExactly(new Ticket(film, new User(DatabaseSteps.USERNAME_1), 
					DatabaseSteps.FILM_INITIAL_BASE_TICKETS_1, DatabaseSteps.FILM_INITIAL_PREMIUM_TICKETS_1).toString());
	}

	@Given("The user provides a not registered username in the text field")
	public void the_user_provides_a_not_registered_username_in_the_text_field() {
		window.textBox("usernameTextBox").enterText("some_unregistered_username");
	}

	@Then("An error is shown")
	public void an_error_is_shown() {
		window.label("errorLabel").requireText("User not registered");
	}

	@Given("The user selects a film from the film list")
	public void the_user_selects_a_film_from_the_film_list() {
		window.list("filmList").selectItem(Pattern.compile(".*"+DatabaseSteps.FILM_ID_1+".*"));
	}

	@When("The user clicks the {string} button")
	public void the_user_clicks_the_button(String string) {
		window.button(JButtonMatcher.withText(string)).click();
	}
	
	@Then("The ticket list shows the new base ticket")
	public void the_ticket_list_shows_the_new_base_ticket() {
		Film film = new Film(DatabaseSteps.FILM_ID_1, DatabaseSteps.FILM_NAME_1, 
				DatabaseSteps.FILM_ROOM_1, DatabaseSteps.FILM_DATETIME_1, 
				DatabaseSteps.FILM_BASE_TICKETS_TOTAL_1, DatabaseSteps.FILM_PREMIUM_TICKETS_TOTAL_1, 
				new ArrayList<String>(Collections.nCopies(DatabaseSteps.FILM_INITIAL_BASE_TICKETS_1+1, DatabaseSteps.USERNAME_1)), 
				new ArrayList<String>(Collections.nCopies(DatabaseSteps.FILM_INITIAL_PREMIUM_TICKETS_1, DatabaseSteps.USERNAME_1)));
		
		assertThat(window.list("ticketList").contents())
			.containsExactly(new Ticket(film, new User(DatabaseSteps.USERNAME_1), 
					DatabaseSteps.FILM_INITIAL_BASE_TICKETS_1+1, DatabaseSteps.FILM_INITIAL_PREMIUM_TICKETS_1).toString());
	}

	@Then("The ticket list shows the new premium ticket")
	public void the_ticket_list_shows_the_new_premium_ticket() {
		Film film = new Film(DatabaseSteps.FILM_ID_1, DatabaseSteps.FILM_NAME_1, 
				DatabaseSteps.FILM_ROOM_1, DatabaseSteps.FILM_DATETIME_1, 
				DatabaseSteps.FILM_BASE_TICKETS_TOTAL_1, DatabaseSteps.FILM_PREMIUM_TICKETS_TOTAL_1, 
				new ArrayList<String>(Collections.nCopies(DatabaseSteps.FILM_INITIAL_BASE_TICKETS_1, DatabaseSteps.USERNAME_1)), 
				new ArrayList<String>(Collections.nCopies(DatabaseSteps.FILM_INITIAL_PREMIUM_TICKETS_1+1, DatabaseSteps.USERNAME_1)));
		
		assertThat(window.list("ticketList").contents())
			.containsExactly(new Ticket(film, new User(DatabaseSteps.USERNAME_1), 
					DatabaseSteps.FILM_INITIAL_BASE_TICKETS_1, DatabaseSteps.FILM_INITIAL_PREMIUM_TICKETS_1+1).toString());
	}
	
}
