package com.lbartolini.app.cinema.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import com.lbartolini.app.cinema.controller.FilmController;
import com.lbartolini.app.cinema.controller.UserController;
import com.lbartolini.app.cinema.controller.helper.BuyBaseTicketHelper;
import com.lbartolini.app.cinema.controller.helper.BuyPremiumTicketHelper;
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
	
	public static void main(String[] args) {
		new CommandLine(new CinemaSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				FilmMongoRepository filmRepository = new FilmMongoRepository(mongoClient, dbName, filmCollectionName);
				UserMongoRepository userRepository = new UserMongoRepository(mongoClient, dbName, userCollectionName, filmCollectionName);
				
				CinemaSwingView cinemaSwingView = new CinemaSwingView(new BuyBaseTicketHelper(filmRepository), new BuyPremiumTicketHelper(filmRepository));
				FilmController filmController = new FilmController(filmRepository, userRepository, cinemaSwingView);
				UserController userController = new UserController(userRepository, cinemaSwingView);
				cinemaSwingView.setFilmController(filmController);
				cinemaSwingView.setUserController(userController);
				cinemaSwingView.setVisible(true);
			} catch (Exception e) {
				System.out.println("Some error occured");
			}
		});;
		
		return null;
	}
	
	
}
