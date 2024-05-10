package com.user.goaltracker.reminder.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.user.goaltracker.configration.DBConfig;

public class UserReminder {
    public String insertReminder(Document request) throws Exception {

        String collectionname = "reminder";
        MongoCollection<Document> mycollection = null;
        MongoClient mongoClient = DBConfig.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
        mycollection = database.getCollection(collectionname);

        request.append("createdAt", "");

        mycollection.insertOne(request);

        if (request.containsKey("_id")) {
            return request.getObjectId("_id").toHexString();
        }
        return "Failed to insert";

    }

    public String updateReminder(Document request) throws Exception {

        String collectionname = "reminder";
        MongoCollection<Document> mycollection = null;
        MongoClient mongoClient = DBConfig.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
        mycollection = database.getCollection(collectionname);

        Bson filter = eq("_id", new ObjectId(request.getString("reminderId")));

        request.remove("reminderId");
        request.append("updatedAt", "");
        Document updateAt = new Document("$set", request);
        UpdateResult result = mycollection.updateOne(filter, updateAt);
        if (result.getModifiedCount() > 0) {
            return "Reminder Updated Successfully";
        }
        return "Failed to update";

    }

    public List<Document> getReminderList(Document request) {
        List<Document> reminderList = new ArrayList<Document>();
        String collectionname = "reminder";
        MongoCollection<Document> mycollection = null;
        MongoClient mongoClient = DBConfig.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
        mycollection = database.getCollection(collectionname);
        Bson userfilter;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mma");
        LocalDateTime now = LocalDateTime.now();

        String currentDateTime = now.format(formatter);

        if (request.containsKey("userReminder")) {
            userfilter = and(
                    eq("userid", request.getString("userid")));
        } else if (request.containsKey("reminderId")) {
            userfilter = and(
                    eq("reminderFor.id", request.getString("reminderId")));
        } else {
            userfilter = and(eq("date", currentDateTime.split(" ")[0]),
                    eq("time", currentDateTime.split(" ")[1]));
        }

        MongoCursor<Document> iter = mycollection.find(userfilter).cursor();
        while (iter.hasNext()) {
            Document doc = iter.next();
            doc.append("reminderId", doc.getObjectId("_id").toHexString());
            reminderList.add(doc);
        }

        return reminderList;
    }

    public String deleteReminder(Document request) {
        String response = "";
        try {

            Bson filter = eq("_id", new ObjectId(request.getString("reminderId")));
            String collectionname = "reminder";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionname);
            DeleteResult result = mycollection.deleteOne(filter);
            if (result.getDeletedCount() > 0) {
                response = "Reminder deleted successfully";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Failed to delete reminder";
        }

        return response;
    }
}
