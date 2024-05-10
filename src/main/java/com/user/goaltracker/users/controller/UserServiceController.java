package com.user.goaltracker.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.bson.Document;

import java.util.Map;

import com.user.goaltracker.users.dao.UserRegistration;

@RestController
@RequestMapping("user")
public class UserServiceController {

    @PostMapping("/usercreation")
    public ResponseEntity<Document> createNewUser(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            UserRegistration userManagement = new UserRegistration();
            Document result = userManagement.createNewUser(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/userdetails")
    public ResponseEntity<Document> getUserDetails(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            UserRegistration userManagement = new UserRegistration();
            Document result = userManagement.getUserDetails(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Document> loginUser(
            @RequestBody Document request,
            @RequestHeader Map<String, String> header) {
        try {
            UserRegistration userManagement = new UserRegistration();
            Document result = userManagement.loginUser(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}