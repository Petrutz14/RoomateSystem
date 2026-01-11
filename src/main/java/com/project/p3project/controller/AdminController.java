package com.project.p3project.controller;

import com.project.p3project.dao.UserDAO;
import com.project.p3project.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserDAO userDAO;

    public AdminController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userDAO.deleteById(id);
        return "User deleted successfully";
    }
}
