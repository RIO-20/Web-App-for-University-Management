package com.raul.univ_management.dao;

import com.raul.univ_management.model.Sponsor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru sponsori
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class SponsorDao {
    private final JdbcTemplate jdbcTemplate;

    public SponsorDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sponsor> sponsorRowMapper = (rs,rowNum) -> {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(rs.getLong("ID_Sponsor"));
        sponsor.setNume(rs.getString("Nume_Sponsor"));
        sponsor.setDescriere(rs.getString("Descriere"));
        sponsor.setEmail(rs.getString("email"));
        return sponsor;
    };

    public List<Sponsor> findAll(){
        String sql = "Select * from Sponsor";
        return jdbcTemplate.query(sql,sponsorRowMapper);
    }

    public void insert(Sponsor sponsor){
        String sql = "INSERT INTO Sponsor (Nume_Sponsor, Descriere, email) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, sponsor.getNume(), sponsor.getDescriere(), sponsor.getEmail());
    }


    public void update(Sponsor sponsor) {
        String sql = "UPDATE Sponsor SET Nume_Sponsor = ?, Descriere = ?, email = ? WHERE ID_Sponsor = ?";
        jdbcTemplate.update(sql, sponsor.getNume(), sponsor.getDescriere(), sponsor.getEmail(), sponsor.getId());
    }

    public void delete(Long id){
        String sql = "delete from Sponsor where ID_Sponsor = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<com.raul.univ_management.dto.SponsorStatsDto> getSponsorAnalysis(Long facultyId) {
        String sql = """
            SELECT 
                s.ID_Sponsor,
                s.Nume_Sponsor,
                s.email,
                SUM(sf.Valoare) as TotalDonated,
                COUNT(sf.ID_Spon_Fac) as DonationCount
            FROM Sponsor s
            JOIN Sponsor_Facultate sf ON s.ID_Sponsor = sf.ID_Sponsor
            WHERE sf.ID_Facultate = ?
            GROUP BY s.ID_Sponsor, s.Nume_Sponsor, s.email
            ORDER BY TotalDonated DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new com.raul.univ_management.dto.SponsorStatsDto(
            rs.getLong("ID_Sponsor"),
            rs.getString("Nume_Sponsor"),
            rs.getString("email"),
            rs.getBigDecimal("TotalDonated"),
            rs.getInt("DonationCount")
        ), facultyId);
    }
    
    public List<com.raul.univ_management.dto.SponsorStatsDto> getGlobalSponsorAnalysis() {
        String sql = """
            SELECT 
                s.ID_Sponsor,
                s.Nume_Sponsor,
                s.email,
                SUM(sf.Valoare) as TotalDonated,
                COUNT(sf.ID_Spon_Fac) as DonationCount
            FROM Sponsor s
            JOIN Sponsor_Facultate sf ON s.ID_Sponsor = sf.ID_Sponsor
            GROUP BY s.ID_Sponsor, s.Nume_Sponsor, s.email
            ORDER BY TotalDonated DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new com.raul.univ_management.dto.SponsorStatsDto(
            rs.getLong("ID_Sponsor"),
            rs.getString("Nume_Sponsor"),
            rs.getString("email"),
            rs.getBigDecimal("TotalDonated"),
            rs.getInt("DonationCount")
        ));
    }
    

}
