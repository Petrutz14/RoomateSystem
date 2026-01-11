package com.project.p3project.service;

import com.project.p3project.dao.UserDao;
import com.project.p3project.exception.BlankDataException;
import com.project.p3project.exception.InvalidEmailException;
import com.project.p3project.exception.UserNotFoundException;
import com.project.p3project.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    //Create user (Sign up)
    public void create(User user) {
        validateUser(user);
        //Hashing
        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);

        userDao.save(user);
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