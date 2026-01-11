package com.project.p3project.service;

import com.project.p3project.dao.UserDAO;
import com.project.p3project.exception.BlankDataException;
import com.project.p3project.exception.InvalidEmailException;
import com.project.p3project.exception.UserNotFoundException;
import com.project.p3project.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserDAO userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserDAO userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    //Create user (Sign up)
    public void create(User user) {
        validateUser(user);
        //Hashing
        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);

        userDao.save(user);
    }

    //Login checker
    public User login(String email, String rawPassword) {
        User user = userDao.findByEmail(email);

        //Security Check
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return user;
    }

    //Get user by id
    public User getById(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        return user;
    }

    //Get all users
    public List<User> getAll() {
        return userDao.findAll();
    }

    //Update user by id
    public void update(Long id, User user) {
        validateUser(user);
        int rowsAffected = userDao.update(id, user);
        if (rowsAffected == 0) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
    }

    //Delete user by id
    public void delete(Long id) {
        int rowsAffected = userDao.deleteById(id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
    }



    //Validator function
    private void validateUser(User user) {
        if (user == null) {
            throw new BlankDataException("User data is missing.");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BlankDataException("Username");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BlankDataException("Email");
        }

        String emailRegex = "^(.+)@(.+)\\.(.+)$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new InvalidEmailException("Invalid email format.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BlankDataException("Password");
        }


    }
}