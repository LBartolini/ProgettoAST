package com.lbartolini.app.cinema.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.lbartolini.app.cinema.model.User;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserRepositoryTest {
	
	private static final String DB_NAME = "cinema";
	private static final String COLLECTION_NAME = "user";
	
	private static final String USERNAME_1 = "USERNAME_ABC";
	
	private static final String USERNAME_2 = "USERNAME_XYZ";
	
	@ClassRule
	public static MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	private MongoClient mongoClient;
	private UserMongoRepository userRepository;
	private MongoCollection<Document> userCollection;

	@Before
	public void setUp() {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		userRepository = new UserMongoRepository(mongoClient, DB_NAME, COLLECTION_NAME);
		MongoDatabase database = mongoClient.getDatabase(DB_NAME);
		database.drop();
		userCollection = database.getCollection(COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		mongoClient.close();
	}

	@Test
	public void testGetUserWhenUserNotPresent() {
		assertThat(userRepository.getUser(USERNAME_1)).isNull();
	}
	
	@Test
	public void testGetUserWhenUserIsPresent() {
		insertUserInDB(USERNAME_1);
		insertUserInDB(USERNAME_2);
		
		assertThat(userRepository.getUser(USERNAME_2)).isEqualTo(new User(USERNAME_2));	
	}
	
	private void insertUserInDB(String username) {
		userCollection.insertOne(new Document().append("username", username));
	}

}
