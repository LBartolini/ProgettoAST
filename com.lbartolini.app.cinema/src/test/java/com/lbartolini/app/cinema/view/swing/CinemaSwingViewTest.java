package com.lbartolini.app.cinema.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbartolini.app.cinema.controller.FilmController;
import com.lbartolini.app.cinema.controller.UserController;
import com.lbartolini.app.cinema.controller.helper.BuyBaseTicketHelper;
import com.lbartolini.app.cinema.controller.helper.BuyPremiumTicketHelper;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;

@RunWith(GUITestRunner.class)
public class CinemaSwingViewTest extends AssertJSwingJUnitTestCase {
	
	@Mock
	private FilmController filmController;
	
	@Mock
	private UserController userController;
	
	@Mock
	private BuyBaseTicketHelper buyBaseTicketHelper;
	
	@Mock
	private BuyPremiumTicketHelper buyPremiumTicketHelper;
	
	private AutoCloseable closeable;

	private CinemaSwingView cinemaSwingView;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			cinemaSwingView = new CinemaSwingView(buyBaseTicketHelper, buyPremiumTicketHelper);
			cinemaSwingView.setFilmController(filmController);
			cinemaSwingView.setUserController(userController);
			return cinemaSwingView;
		});
		
		window = new FrameFixture(robot(), cinemaSwingView);
		window.show();
	}
	
	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}
	
	@Test @GUITest
	public void testInitialConfiguration() {
		window.label(JLabelMatcher.withText("username"));
		window.textBox("usernameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Login")).requireDisabled();
		window.button(JButtonMatcher.withText("Register")).requireDisabled();
		window.label(JLabelMatcher.withText("Tickets Purchased"));
		window.list("ticketList").requireDisabled();
		window.label(JLabelMatcher.withText("Film"));
		window.list("filmList");
		window.button(JButtonMatcher.withText("Buy Base")).requireDisabled();
		window.button(JButtonMatcher.withText("Buy Premium")).requireDisabled();
		window.label("errorLabel").requireText(" ");
		verify(filmController).getAllFilms();
	}

	@Test @GUITest
	public void testLoginAndRegisterButtonsEnabledWhenUsernameTextBoxNotEmpty() {
		window.textBox("usernameTextBox").enterText("some_username");
		
		window.button(JButtonMatcher.withText("Login")).requireEnabled();
		window.button(JButtonMatcher.withText("Register")).requireEnabled();
	}
	
	@Test @GUITest
	public void testLoginAndRegisterButtonsDisabledWhenUsernameTextBoxContainsOnlyBlankCharacters() {
		window.textBox("usernameTextBox").enterText(" ");
		
		window.button(JButtonMatcher.withText("Login")).requireDisabled();
		window.button(JButtonMatcher.withText("Register")).requireDisabled();
	}
	
	@Test @GUITest
	public void testBuyButtonsEnabledWhenUsernameTextBoxNotEmptyAndFilmSelectedHasEnoughTickets() {
		GuiActionRunner.execute(() -> 
			cinemaSwingView.getListFilmModel()
				.addElement(new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.emptyList()))
		);
		
		window.list("filmList").selectItem(0);
		window.textBox("usernameTextBox").enterText("some_username");
		JButtonFixture buyBase = window.button(JButtonMatcher.withText("Buy Base"));
		JButtonFixture buyPremium = window.button(JButtonMatcher.withText("Buy Premium"));
		
		buyBase.requireEnabled();
		buyPremium.requireEnabled();
		
		window.list("filmList").clearSelection();
		
		buyBase.requireDisabled();
		buyPremium.requireDisabled();
		
		window.list("filmList").selectItem(0);
		window.textBox("usernameTextBox").setText("");
		window.textBox("usernameTextBox").enterText(" ");
		
		buyBase.requireDisabled();
		buyPremium.requireDisabled();
	}
	
	@Test @GUITest
	public void testBuyButtonsDisabledWhenFilmSelectedDoesNotHaveEnoughTickets() {
		GuiActionRunner.execute(() -> 
			{
				int baseTicketsTotal = 1;
				int premiumTicketsTotal = 4;
				cinemaSwingView.getListFilmModel()
					.addElement(new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", baseTicketsTotal, premiumTicketsTotal, Collections.nCopies(baseTicketsTotal, "SOME_USER"), Collections.nCopies(premiumTicketsTotal, "SOME_USER")));
			}
		);
		
		window.list("filmList").selectItem(0);
		window.textBox("usernameTextBox").enterText("username");
		
		window.button(JButtonMatcher.withText("Buy Base")).requireDisabled();
		window.button(JButtonMatcher.withText("Buy Premium")).requireDisabled();
	}
	
	@Test @GUITest
	public void testShowError() {
		String errorMessage = "some error message";
		GuiActionRunner.execute(() -> {
			cinemaSwingView.showError(errorMessage);
		});
		
		window.label("errorLabel").requireText(errorMessage);
	}
	
	@Test @GUITest
	public void testShowAllFilms() {
		GuiActionRunner.execute(() -> {
			cinemaSwingView.getLblError().setText("some error message");
		});
		Film film1 = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.emptyList());
		Film film2 = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 5, 5, Collections.emptyList(), Collections.emptyList());
		
		GuiActionRunner.execute(() -> {
			cinemaSwingView.showAllFilms(Arrays.asList(film1, film2));
		});
		
		assertThat(window.list("filmList").contents()).containsExactly(film1.toString(), film2.toString());
		window.label("errorLabel").requireText(" ");
	}
	
	@Test @GUITest
	public void testShowTickets() {
		GuiActionRunner.execute(() -> {
			cinemaSwingView.getLblError().setText("some error message");
		});
		User user = new User("USERNAME");
		Film film1 = new Film("ID_1", "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.nCopies(1, "USERNAME"), Collections.nCopies(1, "USERNAME"));
		Ticket ticket1 = new Ticket(film1, user, 1, 1);
		Film film2 = new Film("ID_2", "NAME_2", "ROOM_2", "DATETIME_2", 10, 10, Collections.nCopies(2, "USERNAME"), Collections.nCopies(2, "USERNAME"));
		Ticket ticket2 = new Ticket(film2, user, 2, 2);
		
		GuiActionRunner.execute(() -> {
			cinemaSwingView.showTickets(Arrays.asList(ticket1, ticket2));
		});

		assertThat(window.list("ticketList").contents()).containsExactly(ticket1.toString(), ticket2.toString());
		window.label("errorLabel").requireText(" ");
	}
	
	
	@Test @GUITest
	public void testLogicLoginButton() {
		String username = " username ";
		window.textBox("usernameTextBox").enterText(username);
		
		window.button(JButtonMatcher.withText("Login")).click();
		
		verify(userController).getTickets(username.trim());
	}
	
	@Test @GUITest
	public void testLogicRegisterButton() {
		String username = " username ";
		window.textBox("usernameTextBox").enterText(username);
		
		window.button(JButtonMatcher.withText("Register")).click();
		
		verify(userController).registerUser(username.trim());
	}
	
	@Test @GUITest
	public void testLogicBuyBaseButton() {
		String username = " username ";
		String filmId = "ID_1";
		window.textBox("usernameTextBox").enterText(username);
		GuiActionRunner.execute(() -> {
			cinemaSwingView.getListFilmModel()
				.addElement(new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.emptyList()));
		});
		window.list("filmList").selectItem(0);
		
		window.button(JButtonMatcher.withText("Buy Base")).click();
		
		verify(filmController).buyTicket(filmId, username.trim(), buyBaseTicketHelper);
	}
	
	@Test @GUITest
	public void testLogicBuyPremiumButton() {
		String username = " username ";
		String filmId = "ID_1";
		window.textBox("usernameTextBox").enterText(username);
		GuiActionRunner.execute(() -> {
			cinemaSwingView.getListFilmModel()
				.addElement(new Film(filmId, "NAME_1", "ROOM_1", "DATETIME_1", 10, 10, Collections.emptyList(), Collections.emptyList()));
		});
		window.list("filmList").selectItem(0);
		
		window.button(JButtonMatcher.withText("Buy Premium")).click();
		
		verify(filmController).buyTicket(filmId, username.trim(), buyPremiumTicketHelper);
	}
}
