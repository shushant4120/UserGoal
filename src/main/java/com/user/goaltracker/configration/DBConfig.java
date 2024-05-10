package com.user.goaltracker.configration;

import java.util.concurrent.TimeUnit;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.stereotype.Component;

@Component
public class DBConfig {
    public static String RUN_ENV = "DEV";
    private static MongoClient mongoClient;
    private static DBConfig dbConfig;

    public static String getDBURL() {
        String dbpass = PropertiesExtractor.getProperty("db.pass");
        if (RUN_ENV.equals("DEV")) {

            return "mongodb+srv://shushantsjb:" + dbpass + "@usergoal.ez6aez0.mongodb.net/";

        } else {
            return "mongodb+srv://shushantsjb:" + dbpass + "@usergoal.ez6aez0.mongodb.net/";
        }
    }

    private DBConfig() {
        String dbstring = DBConfig.getDBURL();
        // System.out.println("dbstring == " + dbstring);
        ConnectionString connectionString = new ConnectionString(dbstring);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder -> builder.maxConnectionLifeTime(10, TimeUnit.MINUTES)
                        .maxWaitTime(10, TimeUnit.SECONDS).maxSize(50).minSize(5))
                .applyConnectionString(connectionString).build();
        mongoClient = MongoClients.create(settings);
    }

    public static MongoClient getMongoClient() {
        if (dbConfig == null) {
            dbConfig = new DBConfig();
        }
        // System.out.println("Total connection called = "+counter++);
        return mongoClient;
    }

    public static String getDBNAME() {
        return "usergoal";

    }
}
