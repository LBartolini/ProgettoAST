package com.lbartolini.app.cinema.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.lbartolini.app.cinema.model.Film;
import com.lbartolini.app.cinema.repository.FilmRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class FilmMongoRepository implements FilmRepository {
	
	private MongoCollection<Document> filmCollection;
	
	public FilmMongoRepository(MongoClient mongoClient, String dbName, String filmCollectionName) {
		filmCollection = mongoClient.getDatabase(dbName).getCollection(filmCollectionName);
	}

	@Override
	public List<Film> getAllFilms() {
		return StreamSupport.stream(filmCollection.find().spliterator(), false)
				.map(FilmMongoRepository::convertDocumentToFilm)
				.collect(Collectors.toList());
	}

	@Override
	public void buyBaseTicket(String filmId, String username) {
		filmCollection.findOneAndUpdate(Filters.eq("id", filmId), Updates.push("baseTickets", username));
	}

	@Override
	public void buyPremiumTicket(String filmId, String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Film getFilm(String filmId) {
		Document found = filmCollection.find(Filters.eq("id", filmId)).first();
		if (found == null) return null;
		return convertDocumentToFilm(found);
	}

	static Film convertDocumentToFilm(Document d) {
		return new Film(
				d.getString("id"), 
				d.getString("name"), 
				d.getString("room"),
				d.getString("datetime"),
				d.getInteger("baseTicketsTotal", 0),
				d.getInteger("premiumTicketsTotal", 0),
				d.getList("baseTickets", String.class),
				d.getList("premiumTickets", String.class));
	}
	
}
