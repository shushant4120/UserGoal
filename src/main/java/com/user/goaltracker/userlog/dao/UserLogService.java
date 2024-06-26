package com.user.goaltracker.userlog.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.user.goaltracker.configration.DBConfig;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;

import java.time.LocalDateTime;

@Service
public class UserLogService {

    private final String collectionName = "userlogs";

    public void saveUserLog(String userId, String action, String actionId) {

        try {
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            Document logDocument = new Document()
                    .append("userId", userId)
                    .append("action", action)
                    .append("actionId", actionId)
                    .append("timestamp", LocalDateTime.now().toString());

            // Insert the log document into the collection
            mycollection.insertOne(logDocument);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
