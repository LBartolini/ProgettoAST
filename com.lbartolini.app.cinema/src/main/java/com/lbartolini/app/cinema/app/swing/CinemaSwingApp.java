package com.lbartolini.app.cinema.app.swing;

import java.awt.EventQueue;

import com.lbartolini.app.cinema.view.swing.CinemaSwingView;

public class CinemaSwingApp {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CinemaSwingView frame = new CinemaSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
