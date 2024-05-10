package com.user.goaltracker.users.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.user.goaltracker.configration.DBConfig;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserRegistration {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Document createNewUser(Document request) {
        Document result = new Document();
        String collectionName = "users";
        String countryCode = request.getString("countrycode");
        String mobile = request.getString("mobile");
        String firstName = request.getString("firstName");
        String lastName = request.getString("lastName");
        String email = request.getString("email");

        // MongoCollection<Document> mycollection =
        // mongoTemplate.getCollection(collectionName);
        MongoCollection<Document> mycollection = null;
        MongoClient mongoClient = DBConfig.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
        mycollection = database.getCollection(collectionName);

        // Check if user with the provided mobile number already existsgit init
        Bson mobCheck = eq("userMobile", mobile);
        MongoCursor<Document> iter = mycollection.find(mobCheck).iterator();

        if (iter.hasNext()) {
            result.append("message", "User with the provided number " + mobile + " is already registered");
            result.append("status", "false");
            return result;
        }

        // Create new user document
        String status = "Active";
        List<String> userRole = new ArrayList<>();
        userRole.add("retailer");

        Document newUser = new Document()
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("userEmail", email)
                .append("userMobile", mobile)
                .append("countrycode", countryCode)
                .append("status", status)
                .append("createdAt", LocalDateTime.now().toString());

        // Insert new user document into collection
        mycollection.insertOne(newUser);

        if (newUser.containsKey("_id")) {
            result.append("message", "Registration successful.");
            result.append("status", "true");
        } else {
            result.append("message", "Failed to do registration.");
            result.append("status", "false");
        }
        // mongoClient.close();
        return result;
    }
}
