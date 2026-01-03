package com.raul.univ_management.dao;

import com.raul.univ_management.model.Facultate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class FacultateDao {
    private final JdbcTemplate jdbcTemplate;

    public FacultateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Facultate> facultateRowMapper = (rs, rowNum) -> Facultate.builder()
            .id(rs.getLong("ID_Facultate"))
            .adminId(rs.getLong("ID_Administrator"))
            .nume(rs.getString("Nume_Facultate"))
            .descriere(rs.getString("Descriere"))
            .buget(rs.getBigDecimal("Buget"))
            .build();

    public List<Facultate> findAll() {
        String sql = "SELECT * FROM Facultate";
        return jdbcTemplate.query(sql, facultateRowMapper);
    }
}
