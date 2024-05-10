package com.user.goaltracker.task.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.user.goaltracker.task.dao.TaskService;

import java.util.Map;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/createTask")
    public ResponseEntity<Document> createTask(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            request.append("userId", header.get("userid"));
            Document result = taskService.createTask(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/getTaskById")
    public ResponseEntity<Document> getTaskById(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            request.append("userId", header.get("userid"));
            Document result = taskService.getTaskById(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/updateTask")
    public ResponseEntity<Document> updateTask(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            request.append("userId", header.get("userid"));
            Document result = taskService.updateTask(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
