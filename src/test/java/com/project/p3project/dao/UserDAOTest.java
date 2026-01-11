package com.project.p3project.dao;

import com.project.p3project.model.User;
import org.junit.jupiter.api.Test; // JUnit 5
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 1. Starts the Spring context (and connects to your DB)
@Transactional  // 2. VERY IMPORTANT: Rolls back changes after test finishes
class UserDAOTest {

    @Autowired
    private UserDAO userDao;

    @Test
    void testSaveAndFindUser() {
        // --- ARRANGE: Create a new user object ---
        User newUser = new User();
        newUser.setUsername("junit_user");
        newUser.setEmail("test@example.com");
        newUser.setPassword("hashed_secret");
        // Note: We don't set ID or CreatedAt, the DB handles that

        // --- ACT: Save the user to the database ---
        int rowsAffected = userDao.save(newUser);

        // --- ASSERT: Check if it worked ---
        assertThat(rowsAffected).isEqualTo(1); // 1 row should be inserted

        // Verify we can find it in the list of all users
        List<User> users = userDao.findAll();
        assertThat(users).extracting(User::getUsername)
                .contains("junit_user");
    }

    @Test
    void testDeleteUser() {
        // 1. Create and Save a user first
        User userToDelete = new User();
        userToDelete.setUsername("delete_me");
        userToDelete.setEmail("delete@example.com");
        userToDelete.setPassword("12345");
        userDao.save(userToDelete);

        // 2. We need to fetch the ID (since your save() doesn't return it)
        // We find the user we just created by filtering the list
        List<User> users = userDao.findAll();
        User savedUser = users.stream()
                .filter(u -> u.getUsername().equals("delete_me"))
                .findFirst()
                .orElseThrow();

        // 3. Delete the user
        int deleteCount = userDao.deleteById(savedUser.getId());

        // 4. Verify deletion
        assertThat(deleteCount).isEqualTo(1);

        // Ensure it is really gone
        List<User> remainingUsers = userDao.findAll();
        assertThat(remainingUsers).extracting(User::getUsername)
                .doesNotContain("delete_me");
    }
}