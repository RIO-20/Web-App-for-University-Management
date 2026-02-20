package com.raul.univ_management.dao;
import com.raul.univ_management.model.Administrator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru administratori
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class AdminDao {
    private final JdbcTemplate jdbcTemplate;

    public AdminDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Administrator> adminRowMapper = (rs, rowNum) -> {
        Administrator admin = new Administrator();

        admin.setId(rs.getLong("ID_Administrator"));
        admin.setNume(rs.getString("Nume"));
        admin.setPrenume(rs.getString("Prenume"));
        admin.setEmail(rs.getString("Email"));
        admin.setUsername(rs.getString("Username"));
        admin.setParola(rs.getString("Parola"));
        admin.setTelefon(rs.getString("telefon"));
        return admin;
    };

    public Optional<Administrator> findByUsernameOrEmail(String identifier) {
        String sql = "SELECT * FROM Administrator WHERE Username = ? OR Email = ?";

        return jdbcTemplate.query(sql, adminRowMapper, identifier, identifier)
                .stream()
                .findFirst();
    }
    public java.util.List<Administrator> findAll() {
        String sql = "SELECT * FROM Administrator";
        return jdbcTemplate.query(sql, adminRowMapper);
    }

    public void insert(Administrator admin) {
        String sql = "INSERT INTO Administrator (Nume, Prenume, Email, Username, Parola, telefon) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, admin.getNume(), admin.getPrenume(), admin.getEmail(), admin.getUsername(), admin.getParola(), admin.getTelefon());
    }

    public void delete(Long id){
        String sql = "delete from Administrator where ID_Administrator = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(Administrator admin) {
        String sql = "UPDATE Administrator SET Nume = ?, Prenume = ?, Email = ?, Username = ?, Parola = ?, telefon = ? WHERE ID_Administrator = ?";
        jdbcTemplate.update(sql, admin.getNume(), admin.getPrenume(), admin.getEmail(), admin.getUsername(), admin.getParola(), admin.getTelefon(), admin.getId());
    }

    public java.util.List<String> findEmailsForContact(Long currentAdminId) {
        String sql = "select A.email from Administrator A join Facultate F on (F.ID_Administrator = A.ID_Administrator) where A.ID_Administrator <> ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("email"), currentAdminId);
    }
}
