package com.project.p3project.controller;

import com.project.p3project.model.User;
import com.project.p3project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.p3project.service.JWTService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    //POST user
    @PostMapping("/register")
    public void createUser(@RequestBody User user) {
        userService.create(user);
    }


    //POST Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        User user = userService.login(loginData.getEmail(), loginData.getPassword());

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(java.util.Map.of("token", token));
    }

    //GET user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    //DELETE user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    //PUT user
    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.update(id, user);
    }
}


