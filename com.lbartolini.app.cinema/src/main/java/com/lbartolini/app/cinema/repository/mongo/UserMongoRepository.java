package com.lbartolini.app.cinema.repository.mongo;

import java.util.List;

import org.bson.Document;

import com.lbartolini.app.cinema.model.Ticket;
import com.lbartolini.app.cinema.model.User;
import com.lbartolini.app.cinema.repository.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class UserMongoRepository implements UserRepository {
	
	private MongoCollection<Document> userCollection;

	public UserMongoRepository(MongoClient mongoClient, String dbName, String collectionName) {
		userCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	@Override
	public User getUser(String username) {
		Document d = userCollection.find(Filters.eq("username", username)).first();
		if (d == null) return null;
		return new User(d.getString("username"));
	}

	@Override
	public List<Ticket> getTickets(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
