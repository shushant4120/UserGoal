package com.user.goaltracker.task.dao;

import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.user.goaltracker.configration.DBConfig;
import com.user.goaltracker.task.request.TaskPOJO;
import com.user.goaltracker.userlog.dao.UserLogService;

public class TaskService {

    @Autowired
    private UserLogService userLogService;

    public Document createTask(Document request) {
        Document result = new Document();

        String goalId = request.getString("goalId");
        String description = request.getString("description");

        // Validate input fields
        if (goalId == null || description == null) {
            throw new IllegalArgumentException("Invalid input data.");
        }

        // Create MongoDB client
        try {
            String collectionName = "tasks";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            // Create a new task document
            Document newTask = new Document()
                    .append("goalId", goalId)
                    .append("description", description)
                    .append("createdAt", LocalDateTime.now().toString());

            // Insert the task document into the collection
            mycollection.insertOne(newTask);

            if (newTask.containsKey("_id")) {
                userLogService.saveUserLog(request.getString("userId"),
                        "User goal created successfully.", newTask.getObjectId("_id").toHexString());
            }
            result.put("success", true);
            result.put("message", "Task created successfully.");

        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "An error occurred while processing the request.");
            e.printStackTrace(); // Log the exception
        }

        return result;
    }

    public Document getTaskById(Document request) {
        Document result = new Document();
        List<TaskPOJO> taskDetailList = new ArrayList<>();

        try {
            // Check if request contains required fields
            if (!request.containsKey("taskId")) {
                throw new IllegalArgumentException("Request is missing required fields.");
            }

            String collectionName = "tasks";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            // Find the task document by ID
            Document task = mycollection.find(Filters.eq("_id", new ObjectId(request.getString("taskId")))).first();

            if (task != null) {
                TaskPOJO taskDetails = new TaskPOJO();
                taskDetails.setId(task.getObjectId("_id").toHexString());
                taskDetails.setDescription(task.getString("description"));
                // Set other task properties if needed
                taskDetailList.add(taskDetails);

                result.put("taskDetails", taskDetailList);
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("message", "Task not found.");
            }

        } catch (

        IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "An error occurred while processing the request.");
            e.printStackTrace(); // Log the exception
        }

        return result;
    }

    public Document updateTask(Document request) {
        Document result = new Document();

        String taskId = request.getString("taskId");
        String description = request.getString("description");

        // Validate input fields
        if (taskId == null) {
            throw new IllegalArgumentException("Invalid input data.");
        }

        // Create MongoDB client
        try {
            String collectionName = "tasks";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            Bson filter = eq("_id", new ObjectId(taskId));

            Document toupdate = new Document();
            toupdate.append("description", description);

            Document update = new Document("$set", toupdate);
            mycollection.updateOne(filter, update);

            result.put("success", true);
            result.put("message", "Task updated successfully.");

        } catch (IllegalArgumentException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "An error occurred while processing the request.");
            e.printStackTrace(); // Log the exception
        }

        return result;
    }

}
