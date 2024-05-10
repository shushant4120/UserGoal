package com.user.goaltracker.goal.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.user.goaltracker.goal.dao.GoalService;

import java.util.Map;

@RestController
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping("/createUserGoal")
    public ResponseEntity<Document> createUserGoal(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            request.append("userId", header.get("userid"));
            Document result = goalService.createUserGoal(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/getGoalById")
    public ResponseEntity<Document> getGoalById(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            request.append("userId", header.get("userid"));
            Document result = goalService.getGoalById(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/updateUserGoal")
    public ResponseEntity<Document> updateUserGoal(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            request.append("userId", header.get("userid"));
            Document result = goalService.updateUserGoal(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
