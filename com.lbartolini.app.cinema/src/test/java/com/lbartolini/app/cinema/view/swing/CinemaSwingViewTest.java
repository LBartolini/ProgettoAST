package com.lbartolini.app.cinema.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

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



}
