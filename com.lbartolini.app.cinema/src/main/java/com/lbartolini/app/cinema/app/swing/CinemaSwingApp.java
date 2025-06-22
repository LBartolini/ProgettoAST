package com.lbartolini.app.cinema.app.swing;

import java.awt.EventQueue;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.lbartolini.app.cinema.controller.FilmController;
import com.lbartolini.app.cinema.controller.UserController;
import com.lbartolini.app.cinema.controller.helper.BuyBaseTicketHelper;
import com.lbartolini.app.cinema.controller.helper.BuyPremiumTicketHelper;
import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.repository.mongo.FilmMongoRepository;
import com.lbartolini.app.cinema.repository.mongo.UserMongoRepository;
import com.lbartolini.app.cinema.view.swing.CinemaSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class CinemaSwingApp implements Callable<Void>{
	
	@Option(names={"--mongo-host"}, description="MongoDB host address")
	private String mongoHost = "localhost";
	
	@Option(names= {"--mongo-port"}, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names={"--db-name"}, description="Database name")
	private String dbName = "cinema";
	
	@Option(names={"--film-collection-name"}, description="Film collection name")
	private String filmCollectionName = "film";
	
	@Option(names={"--user-collection-name"}, description="User collection name")
	private String userCollectionName = "user";
	
	@Option(names={"--populate-db"}, description="Populate DB")
	private boolean populateDB = false;
	
	public static void main(String[] args) {
		new CommandLine(new CinemaSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				if(populateDB) populateDB(mongoClient);
				
				FilmMongoRepository filmRepository = new FilmMongoRepository(mongoClient, dbName, filmCollectionName);
				UserMongoRepository userRepository = new UserMongoRepository(mongoClient, dbName, userCollectionName, filmCollectionName);
				
				CinemaSwingView cinemaSwingView = new CinemaSwingView(new BuyBaseTicketHelper(filmRepository), new BuyPremiumTicketHelper(filmRepository));
				FilmController filmController = new FilmController(filmRepository, userRepository, cinemaSwingView);
				UserController userController = new UserController(userRepository, cinemaSwingView);
				cinemaSwingView.setFilmController(filmController);
				cinemaSwingView.setUserController(userController);
				cinemaSwingView.setVisible(true);
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception in CinemaApplication", e);
			}
		});
		
		return null;
	}

	private void populateDB(MongoClient mongoClient) {
		insertFilmInDB(new Film("FILM_ABC", "Interstellar", "Room Nord", "25/06/2025 18:00", 5, 5, Collections.emptyList(), Collections.emptyList()), mongoClient);
		insertFilmInDB(new Film("FILM_KJI", "Mission Impossible", "Room Est", "28/06/2025 21:00", 10, 10, Collections.emptyList(), Collections.emptyList()), mongoClient);
		insertFilmInDB(new Film("FILM_XYZ", "Forrest Gump", "Room Sud", "29/06/2025 16:45", 20, 2, Collections.emptyList(), Collections.emptyList()), mongoClient);
	}
	
	private void insertFilmInDB(Film film, MongoClient mongoClient) {
		mongoClient.getDatabase(dbName).getCollection(filmCollectionName)
			.insertOne(new Document()
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
