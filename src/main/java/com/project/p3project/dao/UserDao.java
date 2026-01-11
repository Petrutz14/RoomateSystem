package com.project.p3project.dao;

import com.project.p3project.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Map SQL to User object
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password_hash"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return user;
        }
    }

    // Insert a new user
    public int save(User user) {
        return jdbcTemplate.update(
                "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)",
                user.getUsername(), user.getEmail(), user.getPassword()
        );
    }

    // Get user by ID
    public User findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE id = ?",
                    new UserRowMapper(),
                    id
            );
        // Convert wonky Spring error to null
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Get all users
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

    //Delete one user
    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                id
        );
    }

    //Update one user
    public int update(Long id, User user) {
        return jdbcTemplate.update(
                "UPDATE users SET username = ?, email = ?, password_hash = ? WHERE id = ?",
                user.getUsername(), user.getEmail(), user.getPassword(), id
        );
    }

}
