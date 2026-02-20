package com.raul.univ_management.dao;

import com.raul.univ_management.model.Dotare_Tranzactie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.List;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru tranzactiile dotarilor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Dotare_TranzactieDao {
    private final JdbcTemplate jdbcTemplate;

    public Dotare_TranzactieDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Dotare_Tranzactie> dotareTranzactieRowMapper = (rs,rowNum) ->{
        Dotare_Tranzactie dotare_tranzactie = new Dotare_Tranzactie();
        dotare_tranzactie.setId(rs.getLong("ID_Dot_Tr"));
        dotare_tranzactie.setTranzactieId(rs.getLong("ID_Tranzactie"));
        dotare_tranzactie.setDotareId(rs.getLong("ID_Dotare"));
        dotare_tranzactie.setValoare(rs.getBigDecimal("Valoare"));
        dotare_tranzactie.setDescriere(rs.getString("Descriere"));
        dotare_tranzactie.setCantitate(rs.getInt("Cantitate"));
        return dotare_tranzactie;
    };

    public List<Dotare_Tranzactie> findAll(){
        String sql = "Select * from Dotare_Tranzactie";
        return jdbcTemplate.query(sql,dotareTranzactieRowMapper);
    }

    public void insert(Dotare_Tranzactie dt) {
        String sql = "INSERT INTO Dotare_Tranzactie (ID_Tranzactie, ID_Dotare, Cantitate, Valoare, Descriere) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dt.getTranzactieId(), dt.getDotareId(), dt.getCantitate(), dt.getValoare(), dt.getDescriere());
    }

    public void delete(Long id){
        String sql = "delete from Dotare_Tranzactie where ID_Dot_Tr = ?";
        jdbcTemplate.update(sql, id);
    }

    public int getCurrentStock(Long dotareId) {
        String sql = """
            SELECT COALESCE(SUM(
                CASE 
                    WHEN t.Tip = 'Achizitie' THEN dt.Cantitate 
                    WHEN t.Tip = 'Vanzare' THEN -dt.Cantitate 
                    ELSE 0 
                END
            ), 0)
            FROM Dotare_Tranzactie dt
            JOIN Tranzactie t ON dt.ID_Tranzactie = t.ID_Tranzactie
            WHERE dt.ID_Dotare = ?
        """;
        Integer stock = jdbcTemplate.queryForObject(sql, Integer.class, dotareId);
        return stock != null ? stock : 0;
    }
}
