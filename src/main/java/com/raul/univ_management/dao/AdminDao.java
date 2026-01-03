package com.raul.univ_management.dao;
import com.raul.univ_management.model.Administrator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdminDao {
    private final JdbcTemplate jdbcTemplate;

    public AdminDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Administrator> adminRowMapper = (rs, rowNum) -> {
        Administrator admin = new Administrator();

        admin.setId(rs.getLong("ID_Administrator"));
        admin.setUsername(rs.getString("Username"));
        admin.setParola(rs.getString("Parola"));
        return admin;
    };

    public Optional<Administrator> findByUsername(String username) {
        String sql = "SELECT * FROM Administrator WHERE username = ?";

        // We use stream().findFirst() to safely handle cases where the user is not found
        return jdbcTemplate.query(sql, adminRowMapper, username)
                .stream()
                .findFirst();
    }
}
