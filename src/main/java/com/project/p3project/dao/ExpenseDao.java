package com.project.p3project.dao;

import com.project.p3project.model.Expense;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
            if (rs.getTimestamp("created_at") != null) {
                expense.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return expense;
        }
    }

    public int save(Expense expense) {
        return jdbcTemplate.update(
                "INSERT INTO expenses (user_id, apartment_id, title, category, amount, expense_date) VALUES (?, ?, ?, ?, ?, ?)",
                expense.getUserId(), expense.getApartmentId(), expense.getTitle(), expense.getCategory(), expense.getAmount(), expense.getExpenseDate()
        );
    }

    public Expense findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM expenses WHERE id = ?", new ExpenseRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Expense> findByApartmentId(Long apartmentId) {
        return jdbcTemplate.query("SELECT * FROM expenses WHERE apartment_id = ?", new ExpenseRowMapper(), apartmentId);
    }

    public List<Expense> findAll() {
        return jdbcTemplate.query("SELECT * FROM expenses", new ExpenseRowMapper());
    }

    public int update(Long id, Expense expense) {
        return jdbcTemplate.update(
                "UPDATE expenses SET apartment_id = ?, title = ?, category = ?, amount = ?, expense_date = ? WHERE id = ?",
                expense.getApartmentId(), expense.getTitle(), expense.getCategory(), expense.getAmount(), expense.getExpenseDate(), id
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM expenses WHERE id = ?", id);
    }
}