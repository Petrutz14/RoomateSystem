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
public class ApartmentDao {

    private final JdbcTemplate jdbcTemplate;

    public ApartmentDao(JdbcTemplate jdbcTemplate) {
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

    public int save(Apartment apartment) {
        return jdbcTemplate.update("INSERT INTO apartments (address) VALUES (?)", apartment.getAddress());
    }

    public Apartment findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM apartments WHERE id = ?", new ApartmentRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Apartment> findAll() {
        return jdbcTemplate.query("SELECT * FROM apartments", new ApartmentRowMapper());
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