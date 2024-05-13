package com.user.goaltracker.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/userdetails")
    public ResponseEntity<Document> getUserDetails(

            @RequestHeader Map<String, String> header) {
        try {
            UserRegistration userManagement = new UserRegistration();
            Document result = userManagement.getUserDetails(header.get("userid"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Document> loginUser(
            @RequestParam String mobile, @RequestParam String password,
            @RequestHeader Map<String, String> header) {
        try {
            UserRegistration userManagement = new UserRegistration();
            Document result = userManagement.loginUser(mobile, password);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}