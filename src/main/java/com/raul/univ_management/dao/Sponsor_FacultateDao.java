package com.raul.univ_management.dao;

import com.raul.univ_management.model.Sponsor_Facultate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.List;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru relatia sponsor-facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Sponsor_FacultateDao {
    private final JdbcTemplate jdbcTemplate;

    public Sponsor_FacultateDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sponsor_Facultate> sponsorFacultateRowMapper = (rs,rowNum)->{
        Sponsor_Facultate sponsor_facultate = new Sponsor_Facultate();
        sponsor_facultate.setId(rs.getLong("ID_Spon_Fac"));
        sponsor_facultate.setFacultateId(rs.getLong("ID_Facultate"));
        sponsor_facultate.setSponsorId(rs.getLong("ID_Sponsor"));
        sponsor_facultate.setTip(rs.getString("Tip_sponsorizare"));
        sponsor_facultate.setValoare(rs.getBigDecimal("Valoare"));

        return sponsor_facultate;
    };

    public List<Sponsor_Facultate> getAll(){
        String sql = "Select * from Sponsor_Facultate";
        return jdbcTemplate.query(sql,sponsorFacultateRowMapper);
    }

    public void insert(Sponsor_Facultate sf) {
        String sql = "INSERT INTO Sponsor_Facultate (ID_Facultate, ID_Sponsor, Tip_sponsorizare, Valoare) VALUES (?, ?, ?, ?)";
        System.out.println("DEBUG: DAO executing SQL: " + sql + " | Params: " + sf.getFacultateId() + ", " + sf.getSponsorId() + ", " + sf.getTip() + ", " + sf.getValoare());
        jdbcTemplate.update(sql, sf.getFacultateId(), sf.getSponsorId(), sf.getTip(), sf.getValoare());
    }

    public void delete(Long id){
        String sql = "delete from Sponsor_Facultate where ID_Spon_Fac = ?";
        jdbcTemplate.update(sql, id);
    }
}
