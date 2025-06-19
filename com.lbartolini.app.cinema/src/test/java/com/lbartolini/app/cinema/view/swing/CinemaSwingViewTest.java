package com.lbartolini.app.cinema.view.swing;

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

import com.lbartolini.app.cinema.model.Film;

@RunWith(GUITestRunner.class)
public class CinemaSwingViewTest extends AssertJSwingJUnitTestCase {

	private CinemaSwingView cinemaSwingView;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			cinemaSwingView = new CinemaSwingView();
			return cinemaSwingView;
		});
		
		window = new FrameFixture(robot(), cinemaSwingView);
		window.show();
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

}
