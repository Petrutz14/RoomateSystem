package com.project.p3project.dao;

import com.project.p3project.model.Expense;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ExpenseDao {

    private final JdbcTemplate jdbcTemplate;

    public ExpenseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class ExpenseRowMapper implements RowMapper<Expense> {
        @Override
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense expense = new Expense();
            expense.setId(rs.getLong("id"));
            expense.setUserId(rs.getLong("user_id"));
            expense.setApartmentId(rs.getLong("apartment_id"));
            expense.setTitle(rs.getString("title"));
            expense.setCategory(rs.getString("category"));
            expense.setAmount(rs.getBigDecimal("amount"));

            // Modern way to fetch LocalDate from JDBC
            expense.setExpenseDate(rs.getObject("expense_date", LocalDate.class));

            // Handle created_at safely
            LocalDate createdAt = rs.getObject("created_at", LocalDate.class);
            if (createdAt != null) {
                expense.setCreatedAt(createdAt);
            }

            return expense;
        }
    }

    public int save(Expense expense) {
        return jdbcTemplate.update(
                "INSERT INTO expenses (user_id, apartment_id, title, category, amount, expense_date) VALUES (?, ?, ?, ?, ?, ?)",
                expense.getUserId(),
                expense.getApartmentId(),
                expense.getTitle(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getExpenseDate() // Pass the LocalDate directly
        );
    }

    public Expense findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM expenses WHERE id = ?", new ExpenseRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Expense> findByApartmentAndUser(Long apartmentId, String userEmail) {
        String sql = "SELECT e.* FROM expenses e " +
                "JOIN users u ON e.user_id = u.id " +
                "WHERE e.apartment_id = ? AND u.email = ?";
        return jdbcTemplate.query(sql, new ExpenseRowMapper(), apartmentId, userEmail);
    }

    public List<Expense> findAll() {
        return jdbcTemplate.query("SELECT * FROM expenses", new ExpenseRowMapper());
    }

    public int update(Long id, Expense expense) {
        return jdbcTemplate.update(
                "UPDATE expenses SET apartment_id = ?, title = ?, category = ?, amount = ?, expense_date = ? WHERE id = ?",
                expense.getApartmentId(),
                expense.getTitle(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getExpenseDate(),
                id
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM expenses WHERE id = ?", id);
    }
}