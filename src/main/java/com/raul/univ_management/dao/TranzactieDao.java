package com.raul.univ_management.dao;


import com.raul.univ_management.model.Tranzactie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru tranzactii
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class TranzactieDao {
    private final JdbcTemplate jdbcTemplate;

    public TranzactieDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Tranzactie> tranzactieRowMapper = (rs, rowNum) ->{
        Tranzactie tr = new Tranzactie();
        tr.setId(rs.getLong("ID_Tranzactie"));
        tr.setFacultateId(rs.getLong("ID_Facultate"));
        tr.setTip(rs.getString("Tip"));
        tr.setDescriere(rs.getString("Descriere"));
        return tr;
    };

    public List<Tranzactie> findPerFaculty(long id_fac){
        String sql = "Select * from Tranzactie Where ID_Facultate = ?";
        return jdbcTemplate.query(sql,tranzactieRowMapper,id_fac);
    }

    public List<Tranzactie> findAll() {
        String sql = "Select * from Tranzactie";
        return jdbcTemplate.query(sql, tranzactieRowMapper);
    }

    public void insert(Tranzactie tranzactie) {
        String sql = "INSERT INTO Tranzactie (ID_Facultate, Tip, Descriere) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, tranzactie.getFacultateId());
            ps.setString(2, tranzactie.getTip());
            ps.setString(3, tranzactie.getDescriere());
            return ps;
        }, keyHolder);
        
        if (keyHolder.getKey() != null) {
            tranzactie.setId(keyHolder.getKey().longValue());
        }
    }

    public void update(Tranzactie tranzactie) {
        String sql = "UPDATE Tranzactie SET ID_Facultate = ?, Tip = ?, Descriere = ? WHERE ID_Tranzactie = ?";
        jdbcTemplate.update(sql, tranzactie.getFacultateId(), tranzactie.getTip(), tranzactie.getDescriere(), tranzactie.getId());
    }

    public void delete(Long id){
        String sql = "delete from Tranzactie where ID_Tranzactie = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Tranzactie> findRecentByFaculty(Long facultyId, int limit) {
        String sql = "SELECT TOP (?) * FROM Tranzactie WHERE ID_Facultate = ? ORDER BY ID_Tranzactie DESC";
        return jdbcTemplate.query(sql, tranzactieRowMapper, limit, facultyId);
    }

    public List<com.raul.univ_management.dto.FinancialImpactDto> getFinancialImpact(Long facultyId, Double majorThreshold, Double minorThreshold) {
        String sql = """
            SELECT 
                t.Tip as Tip_Tranzactie,
                SUM(dt.Valoare) as Valoare_Totala,
                CASE 
                    WHEN SUM(dt.Valoare) > ? THEN 'Major'
                    WHEN SUM(dt.Valoare) BETWEEN ? AND ? THEN 'Standard'
                    ELSE 'Minor'
                END as Impact_Financiar
            FROM Tranzactie t
            JOIN Facultate f ON t.ID_Facultate = f.ID_Facultate
            JOIN Dotare_Tranzactie dt ON t.ID_Tranzactie = dt.ID_Tranzactie
            WHERE f.ID_Facultate = ?
            GROUP BY t.Tip
        """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> new com.raul.univ_management.dto.FinancialImpactDto(
            rs.getString("Tip_Tranzactie"),
            rs.getBigDecimal("Valoare_Totala"),
            rs.getString("Impact_Financiar")
        ), majorThreshold, minorThreshold, majorThreshold, facultyId);
    }

    
    public List<Tranzactie> findHighValueTransactions() {
        String sql = """
            SELECT t.* 
            FROM Tranzactie t
            JOIN Dotare_Tranzactie dt ON t.ID_Tranzactie = dt.ID_Tranzactie
            WHERE dt.Valoare > 1.2 * (
                SELECT AVG(dt2.Valoare) 
                FROM Dotare_Tranzactie dt2 
                JOIN Tranzactie t2 ON dt2.ID_Tranzactie = t2.ID_Tranzactie 
                WHERE t2.Tip = t.Tip
            )
        """;
        return jdbcTemplate.query(sql, tranzactieRowMapper);
    }
}
