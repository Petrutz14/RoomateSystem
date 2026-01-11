package com.project.p3project.dao;

import com.project.p3project.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class PaymentDAO {
    private final JdbcTemplate jdbcTemplate;

    public PaymentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Payment payment) {
        String sql = "INSERT INTO payments (expense_id, user_id, amount, payment_date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, payment.getExpenseId(), payment.getUserId(), payment.getAmount(), payment.getPaymentDate());
    }

    public BigDecimal getTotalPaidForExpense(Long expenseId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE expense_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, expenseId);
    }

    public List<Payment> findByExpenseId(Long expenseId) {
        String sql = "SELECT * FROM payments WHERE expense_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setExpenseId(rs.getLong("expense_id"));
            p.setUserId(rs.getLong("user_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentDate(rs.getObject("payment_date", LocalDate.class));
            return p;
        }, expenseId);
    }
}