package com.project.p3project.dao;

import com.project.p3project.model.Apartment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApartmentDAO {

    private final JdbcTemplate jdbcTemplate;

    public ApartmentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class ApartmentRowMapper implements RowMapper<Apartment> {
        @Override
        public Apartment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Apartment apt = new Apartment();
            apt.setId(rs.getLong("id"));
            apt.setAddress(rs.getString("address"));
            apt.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return apt;
        }
    }

    public Long save(Apartment apartment) {
        String sql = "INSERT INTO apartments (address) VALUES (?) RETURNING id";
        return jdbcTemplate.queryForObject(sql, Long.class, apartment.getAddress());
    }

    public boolean isMember(Long apartmentId, String email) {
        String sql = "SELECT COUNT(*) FROM apartment_members am " +
                "JOIN users u ON am.user_id = u.id " +
                "WHERE am.apartment_id = ? AND u.email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, apartmentId, email);
        return count != null && count > 0;
    }

    public List<Apartment> findAllForUser(String email) {
        String sql = "SELECT a.* FROM apartments a " +
                "JOIN apartment_members am ON a.id = am.apartment_id " +
                "JOIN users u ON am.user_id = u.id " +
                "WHERE u.email = ?";
        return jdbcTemplate.query(sql, new ApartmentRowMapper(), email);
    }

    public Apartment findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM apartments WHERE id = ?", new ApartmentRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int update(Long id, Apartment apartment) {
        return jdbcTemplate.update("UPDATE apartments SET address = ? WHERE id = ?", apartment.getAddress(), id);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM apartments WHERE id = ?", id);
    }

    public int addMember(Long apartmentId, Long userId) {
        return jdbcTemplate.update("INSERT INTO apartment_members (apartment_id, user_id) VALUES (?, ?)", apartmentId, userId);
    }

    public int removeMember(Long apartmentId, Long userId) {
        return jdbcTemplate.update("DELETE FROM apartment_members WHERE apartment_id = ? AND user_id = ?", apartmentId, userId);
    }
}