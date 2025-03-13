package com.example.graphql.controller;

//import com.example.api.DefaultApi;
//import com.example.graphql.model.User;

import com.example.api.UserReportApi;
import com.example.model.User;
import com.example.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController extends BaseController implements UserReportApi {


    @Override
//    @PreAu
    public ResponseEntity<User> getUser(String document) {
        User user = new User();
        user.setDocument("123456789");
        user.setName("User 123456789");
        user.setAge("20");
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Users> getUsers(Boolean enabled) {
        User user1 = new User();
        user1.setDocument("111");
        user1.setName("User 111");
        user1.setAge("111");

        User user2 = new User();
        user2.setDocument("12");
        user2.setName("User 12");
        user2.setAge("12");

        Users users = new Users();
        users.setResults(List.of(user1, user2));
        return ResponseEntity.ok(users);
    }
}
