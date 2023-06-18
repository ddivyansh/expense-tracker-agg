package com.example.expensetracker.controllers;

import com.example.expensetracker.Constants.Constants;
import com.example.expensetracker.domain.User;
import com.example.expensetracker.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Object loginUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(email, password);
        if(user == null){
            return "redirect:/register";
        }
        /*
        create a cookie with user-id as key and value as jwt token, and store the cookie in the response entity for our browser/client.
         */
        ResponseCookie userCookie = createUserCookie(user);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userCookie.toString()).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.registerUser(firstName, lastName, email, password);
        ResponseCookie userCookie = createUserCookie(user);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userCookie.toString()).build();
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Base64.encodeBase64String(Constants.API_SECRET_KEY.getBytes()))
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    private ResponseCookie createUserCookie(User user) {
        return ResponseCookie.from("user-id", generateJWTToken(user).get("token"))
                .path("/")
                .maxAge(1800)
                .build();
    }
}