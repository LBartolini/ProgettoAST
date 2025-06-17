package com.lbartolini.app.cinema.repository.mongo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class UserMongoRepository implements UserRepository {
	
	private MongoCollection<Document> userCollection;
	private MongoCollection<Document> filmCollection;

	public UserMongoRepository(MongoClient mongoClient, String dbName, String userCollectionName, String filmCollectionName) {
		userCollection = mongoClient.getDatabase(dbName).getCollection(userCollectionName);
		filmCollection = mongoClient.getDatabase(dbName).getCollection(filmCollectionName);
	}

	@Override
	public User getUser(String username) {
		Document d = userCollection.find(Filters.eq("username", username)).first();
		if (d == null) return null;
		return new User(d.getString("username"));
	}

	@Override
	public List<Ticket> getTickets(String username) {
		return StreamSupport
			.stream(filmCollection.find(
					Filters.or(
							Filters.eq("baseTickets", username), 
							Filters.eq("premiumTickets", username))).spliterator(), false)
			.map((Document d) -> convertDocumentToTicketOfUser(username, d))
			.collect(Collectors.toList());
	}

	private Ticket convertDocumentToTicketOfUser(String username, Document d) {
		Film film = new Film(
				d.getString("id"), 
				d.getString("name"), 
				d.getString("room"),
				d.getString("datetime"),
				d.getInteger("baseTicketsTotal", 0),
				d.getInteger("premiumTicketsTotal", 0),
				d.getList("baseTickets", String.class),
				d.getList("premiumTickets", String.class));
		User user = new User(username);
		return new Ticket(film, user, 
				Collections.frequency(d.getList("baseTickets", String.class), username),
				Collections.frequency(d.getList("premiumTickets", String.class), username));
	}

}
