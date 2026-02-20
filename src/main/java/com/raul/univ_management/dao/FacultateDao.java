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
/** Clasa DAO pentru interactiunea cu baza de date pentru facultati
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
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

    public void insert(Facultate facultate){
        String sql = "INSERT INTO Facultate (ID_Administrator, Nume_Facultate,Descriere,Buget) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, facultate.getAdminId());
            ps.setString(2, facultate.getNume());
            ps.setString(3, facultate.getDescriere());
            ps.setBigDecimal(4, facultate.getBuget());
            return ps;
        }, keyHolder);
        facultate.setId(keyHolder.getKey().longValue());
    }

    public void update(Facultate facultate){
        String sql = "UPDATE Facultate SET ID_Administrator = ?, Nume_Facultate = ?, Descriere = ?, Buget = ? WHERE ID_Facultate = ?";
        jdbcTemplate.update(sql, facultate.getAdminId(), facultate.getNume(), facultate.getDescriere(), facultate.getBuget(), facultate.getId());
    }

    public void delete(Long id){
        String sqlTranzactii = "DELETE FROM Tranzactie WHERE ID_Facultate = ?";
        jdbcTemplate.update(sqlTranzactii, id);
        String sqlFacultate = "DELETE FROM Facultate WHERE ID_Facultate = ?";
        jdbcTemplate.update(sqlFacultate, id);
    }

    public java.util.Optional<Facultate> findByAdminId(Long adminId) {
        String sql = "SELECT * FROM Facultate WHERE ID_Administrator = ?";
        List<Facultate> results = jdbcTemplate.query(sql, facultateRowMapper, adminId);
        return results.stream().findFirst();
    }

    public java.util.Optional<com.raul.univ_management.dto.BudgetStatsDto> getBudgetStats(Long facultyId) {
        String sql = """
            SELECT 
                (f.Buget + COALESCE(SUM(dt.Valoare), 0)) as Initial_Budget,
                COALESCE(SUM(dt.Valoare), 0) as Total_Spent,
                f.Buget as Remaining_Budget
            FROM Facultate f
            LEFT JOIN Tranzactie t ON f.ID_Facultate = t.ID_Facultate
            LEFT JOIN Dotare_Tranzactie dt ON t.ID_Tranzactie = dt.ID_Tranzactie
            WHERE f.ID_Facultate = ?
            GROUP BY f.Buget
        """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> new com.raul.univ_management.dto.BudgetStatsDto(
            rs.getBigDecimal("Initial_Budget"),
            rs.getBigDecimal("Total_Spent"),
            rs.getBigDecimal("Remaining_Budget")
        ), facultyId).stream().findFirst();
    }

    public void updateBudget(Long facultyId, java.math.BigDecimal amountToAdd) {
        String sql = "UPDATE Facultate SET Buget = Buget + ? WHERE ID_Facultate = ?";
        jdbcTemplate.update(sql, amountToAdd, facultyId);
    }

    public List<Facultate> findWellEquippedFaculties() {
        String sql = """
            SELECT f.ID_Facultate, f.ID_Administrator, f.Nume_Facultate, CAST(f.Descriere AS VARCHAR(MAX)) as Descriere, f.Buget
            FROM Facultate f
            JOIN Sala s ON f.ID_Facultate = s.ID_Facultate
            JOIN Dotare d ON s.ID_Sala = d.ID_Sala
            GROUP BY f.ID_Facultate, f.ID_Administrator, f.Nume_Facultate, CAST(f.Descriere AS VARCHAR(MAX)), f.Buget
            HAVING SUM(d.Valoare_Dotare) > (
                SELECT AVG(TotalVal) FROM (
                    SELECT SUM(d2.Valoare_Dotare) as TotalVal
                    FROM Facultate f2
                    JOIN Sala s2 ON f2.ID_Facultate = s2.ID_Facultate
                    JOIN Dotare d2 ON s2.ID_Sala = d2.ID_Sala
                    GROUP BY f2.ID_Facultate
                ) as SubQuery
            )
        """;
        return jdbcTemplate.query(sql, facultateRowMapper);
    }
}
