package com.user.goaltracker.goal.dao;

import static com.mongodb.client.model.Filters.eq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.user.goaltracker.configration.DBConfig;
import com.user.goaltracker.goal.request.GoalPOJO;
import com.user.goaltracker.task.request.TaskPOJO;
import com.user.goaltracker.userlog.dao.UserLogService;

@Service
public class GoalService {
    @Autowired
    private UserLogService userLogService;

    public Document createUserGoal(Document request) {
        Document result = new Document();

        String userId = request.getString("userId");
        String goalName = request.getString("goalName");
        String description = request.getString("description");
        int minTimeline = request.getInteger("minTimeline");
        int maxTimeline = request.getInteger("maxTimeline");

        // Validate input fields
        if (userId == null || goalName == null || description == null || minTimeline <= 0 || maxTimeline <= 0
                || minTimeline > maxTimeline) {
            throw new IllegalArgumentException("Invalid input data.");
        }

        // Create MongoDB client
        try {
            String collectionName = "goals";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            // Create a new user goal document
            Document newUserGoal = new Document()
                    .append("userId", userId)
                    .append("goalName", goalName)
                    .append("description", description)
                    .append("minTimeline", minTimeline)
                    .append("maxTimeline", maxTimeline)
                    .append("createdAt", LocalDateTime.now().toString());

            // Insert the user goal document into the collection
            mycollection.insertOne(newUserGoal);

            if (newUserGoal.containsKey("_id")) {
                userLogService.saveUserLog(request.getString("userId"),
                        "User goal created successfully.", newUserGoal.getObjectId("_id").toHexString());
            }
            result.put("success", true);
            result.put("message", "User goal created successfully.");

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

    public Document getGoalById(Document request) {
        Document result = new Document();
        List<GoalPOJO> goalDetailList = new ArrayList<>();

        try {
            // Check if request contains required fields
            if (!request.containsKey("type") || !request.containsKey("goalId") || !request.containsKey("userId")) {
                throw new IllegalArgumentException("Request is missing required fields.");
            }

            String collectionName = "goals";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            Bson filter;
            if (request.getInteger("type") == 0) {
                filter = Filters.and(Filters.eq("_id", new ObjectId(request.getString("goalId"))),
                        Filters.eq("status", "Active"));
            } else {
                filter = Filters.and(Filters.eq("userId", new ObjectId(request.getString("userId"))),
                        Filters.eq("status", "Active"));
            }

            MongoCursor<Document> goalItr = mycollection.find(filter).iterator();

            while (goalItr.hasNext()) {
                Document goal = goalItr.next();
                GoalPOJO goalDetails = new GoalPOJO();
                goalDetails.setId(goal.getObjectId("_id").toHexString());
                goalDetails.setUserId(goal.getString("userId"));
                goalDetails.setDescription(goal.getString("description"));
                goalDetails.setMinTimeline(goal.getInteger("minTimeline"));
                goalDetails.setMaxTimeline(goal.getInteger("maxTimeline"));

                List<TaskPOJO> tasksList = new ArrayList<>();
                List<Document> tasksDocuments = goal.getList("tasks", Document.class);
                for (Document taskDocument : tasksDocuments) {
                    TaskPOJO task = new TaskPOJO();
                    task.setId(taskDocument.getObjectId("_id").toHexString());
                    task.setDescription(taskDocument.getString("description"));
                    task.setQuantity(taskDocument.getInteger("quantity"));
                    task.setFrequency(taskDocument.getString("frequency"));
                    task.setRemindersEnabled(taskDocument.getBoolean("remindersEnabled"));
                    task.setReminders(taskDocument.getList("reminders", String.class));
                    tasksList.add(task);
                }
                goalDetails.setTasks(tasksList);
                goalDetailList.add(goalDetails);
            }
            result.put("goalDetails", goalDetailList);
            result.put("success", true);

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

    public Document updateUserGoal(Document request) {
        Document result = new Document();

        String goalId = request.getString("goalId");
        String description = request.getString("description");
        String goalName = request.getString("goalName");

        // Validate input fields
        if (goalId == null) {
            throw new IllegalArgumentException("Invalid input data.");
        }

        // Create MongoDB client
        try {
            String collectionName = "goals";
            MongoCollection<Document> mycollection = null;
            MongoClient mongoClient = DBConfig.getMongoClient();
            MongoDatabase database = mongoClient.getDatabase(DBConfig.getDBNAME());
            mycollection = database.getCollection(collectionName);

            Bson filter = eq("_id", new ObjectId(goalId));

            Document toupdate = new Document();
            toupdate.append("goalName", goalName);
            toupdate.append("description", description);

            Document update = new Document("$set", toupdate);
            mycollection.updateOne(filter, update);

            if (request.containsKey("userId")) {
                userLogService.saveUserLog(request.getString("userId"),
                        "User goal updated successfully.", goalId);
            }
            result.put("success", true);
            result.put("message", "User goal updated successfully.");

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
