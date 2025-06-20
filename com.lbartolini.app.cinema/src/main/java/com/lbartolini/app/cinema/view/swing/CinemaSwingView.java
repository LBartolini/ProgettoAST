package com.lbartolini.app.cinema.view.swing;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.lbartolini.app.cinema.controller.FilmController;
import com.lbartolini.app.cinema.controller.UserController;
import com.lbartolini.app.cinema.controller.helper.BuyBaseTicketHelper;
import com.lbartolini.app.cinema.controller.helper.BuyPremiumTicketHelper;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.view.CinemaView;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CinemaSwingView extends JFrame implements CinemaView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JButton btnLogin;
	private JPanel panel;
	private JButton btnRegister;
	private JLabel lblTicket;
	private JLabel lblFilm;
	private JList<Ticket> listTicket;
	private DefaultListModel<Ticket> listTicketModel;
	private JList<Film> listFilm;
	private DefaultListModel<Film> listFilmModel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JLabel lblError;
	private JPanel panel_1;
	private JButton btnBuyBaseTicket;
	private JButton btnBuyPremiumTicket;
	
	private FilmController filmController;
	private UserController userController;
	
	/**
	 * Create the frame.
	 */
	public CinemaSwingView(BuyBaseTicketHelper buyBaseTicketHelper, BuyPremiumTicketHelper buyPremiumTicketHelper) {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{83, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblUsername = new JLabel("username");
		GridBagConstraints gbc_lblLabelusername = new GridBagConstraints();
		gbc_lblLabelusername.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLabelusername.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabelusername.gridx = 0;
		gbc_lblLabelusername.gridy = 0;
		contentPane.add(lblUsername, gbc_lblLabelusername);
		
		txtUsername = new JTextField();
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				btnLogin.setEnabled(!txtUsername.getText().trim().isEmpty());
				btnRegister.setEnabled(!txtUsername.getText().trim().isEmpty());
				
				checkBuyButtonsEnabled();
			}
		});
		txtUsername.setName("usernameTextBox");
		GridBagConstraints gbc_txtUsernametextbox = new GridBagConstraints();
		gbc_txtUsernametextbox.insets = new Insets(0, 0, 5, 0);
		gbc_txtUsernametextbox.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtUsernametextbox.gridx = 1;
		gbc_txtUsernametextbox.gridy = 0;
		contentPane.add(txtUsername, gbc_txtUsernametextbox);
		txtUsername.setColumns(10);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{187, 187, 0};
		gbl_panel.rowHeights = new int[]{27, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				userController.getTickets(txtUsername.getText().trim());
			}
		});
		btnLogin.setEnabled(false);
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.BOTH;
		gbc_btnLogin.insets = new Insets(0, 0, 0, 5);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 0;
		panel.add(btnLogin, gbc_btnLogin);
		
		btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				userController.registerUser(txtUsername.getText().trim());
			}
		});
		btnRegister.setEnabled(false);
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.fill = GridBagConstraints.BOTH;
		gbc_btnRegister.gridx = 1;
		gbc_btnRegister.gridy = 0;
		panel.add(btnRegister, gbc_btnRegister);
		
		lblTicket = new JLabel("Tickets Purchased");
		GridBagConstraints gbc_lblTicket = new GridBagConstraints();
		gbc_lblTicket.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTicket.insets = new Insets(0, 0, 5, 5);
		gbc_lblTicket.gridx = 0;
		gbc_lblTicket.gridy = 3;
		contentPane.add(lblTicket, gbc_lblTicket);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		listTicketModel = new DefaultListModel<>();
		listTicket = new JList<>(listTicketModel);
		listTicket.setEnabled(false);
		listTicket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTicket.setName("ticketList");
		scrollPane.setViewportView(listTicket);
		
		lblFilm = new JLabel("Film");
		GridBagConstraints gbc_lblFilm = new GridBagConstraints();
		gbc_lblFilm.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFilm.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilm.gridx = 0;
		gbc_lblFilm.gridy = 5;
		contentPane.add(lblFilm, gbc_lblFilm);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 5;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{187, 187, 0};
		gbl_panel_1.rowHeights = new int[]{27, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		btnBuyBaseTicket = new JButton("Buy Base");
		btnBuyBaseTicket.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filmController.buyTicket(
						listFilm.getSelectedValue().getId(), 
						txtUsername.getText().trim(), 
						buyBaseTicketHelper);
			}
		});
		btnBuyBaseTicket.setEnabled(false);
		GridBagConstraints gbc_btnBuyBaseTicket = new GridBagConstraints();
		gbc_btnBuyBaseTicket.fill = GridBagConstraints.BOTH;
		gbc_btnBuyBaseTicket.insets = new Insets(0, 0, 0, 5);
		gbc_btnBuyBaseTicket.gridx = 0;
		gbc_btnBuyBaseTicket.gridy = 0;
		panel_1.add(btnBuyBaseTicket, gbc_btnBuyBaseTicket);
		
		btnBuyPremiumTicket = new JButton("Buy Premium");
		btnBuyPremiumTicket.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filmController.buyTicket(
						listFilm.getSelectedValue().getId(), 
						txtUsername.getText().trim(), 
						buyPremiumTicketHelper);
			}
		});
		btnBuyPremiumTicket.setEnabled(false);
		GridBagConstraints gbc_btnBuyPremiumTicket = new GridBagConstraints();
		gbc_btnBuyPremiumTicket.fill = GridBagConstraints.BOTH;
		gbc_btnBuyPremiumTicket.gridx = 1;
		gbc_btnBuyPremiumTicket.gridy = 0;
		panel_1.add(btnBuyPremiumTicket, gbc_btnBuyPremiumTicket);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 6;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);
		
		listFilmModel = new DefaultListModel<>();
		listFilm = new JList<>(listFilmModel);
		listFilm.addListSelectionListener(arg0 -> checkBuyButtonsEnabled());
		listFilm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFilm.setName("filmList");
		scrollPane_1.setViewportView(listFilm);
		
		lblError = new JLabel(" ");
		getLblError().setForeground(new Color(224, 27, 36));
		getLblError().setName("errorLabel");
		GridBagConstraints gbc_lblError = new GridBagConstraints();
		gbc_lblError.gridwidth = 2;
		gbc_lblError.gridx = 0;
		gbc_lblError.gridy = 7;
		contentPane.add(getLblError(), gbc_lblError);
	}

	@Override
	public void showAllFilms(List<Film> list) {
		list.stream().forEach(listFilmModel::addElement);
		resetErrorMessage();
	}

	@Override
	public void showError(String errorMessage) {
		getLblError().setText(errorMessage);
	}

	@Override
	public void showTickets(List<Ticket> list) {
		list.stream().forEach(listTicketModel::addElement);
		resetErrorMessage();
	}

	DefaultListModel<Film> getListFilmModel() {
		return listFilmModel;
	}
	
	private void resetErrorMessage() {
		lblError.setText(" ");
	}
	
	JLabel getLblError() {
		return lblError;
	}

	private void checkBuyButtonsEnabled() {
		int baseTicketsLeft = 0;
		int premiumTicketsLeft = 0;
		if (listFilm.getSelectedIndex() != -1) {
			Film f = listFilm.getSelectedValue();
			baseTicketsLeft = f.getBaseTicketsTotal() - f.getBaseTickets().size();
			premiumTicketsLeft = f.getPremiumTicketsTotal() - f.getPremiumTickets().size();
		}
		
		btnBuyBaseTicket.setEnabled(!txtUsername.getText().trim().isEmpty() && baseTicketsLeft > 0);
		btnBuyPremiumTicket.setEnabled(!txtUsername.getText().trim().isEmpty() && premiumTicketsLeft > 0);
	}

	public void setFilmController(FilmController filmController) {
		this.filmController = filmController;
		this.filmController.getAllFilms();
	}

	public void setUserController(UserController userController) {
		this.userController = userController;
	}

}
