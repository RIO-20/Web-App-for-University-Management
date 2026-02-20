package com.raul.univ_management.dao;

import com.raul.univ_management.model.Sala;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru sali
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class SalaDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Sala> salaRowMapper = (rs, rowNum) -> {
        Sala sala = new Sala();
        sala.setId(rs.getLong("ID_Sala"));
        sala.setFacultateId(rs.getLong("ID_Facultate"));
        sala.setNume(rs.getString("Nume_Sala"));
        sala.setTip(rs.getString("Tip_Sala"));
        sala.setCapacitate(rs.getInt("Capacitate"));
        return sala;
    };

    public SalaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Sala> findByFaculty(Long id) {
        String sql = "Select * from Sala where ID_Facultate = ?";
        return this.jdbcTemplate.query(sql, this.salaRowMapper, new Object[]{id});
    }

    public List<Sala> findAll() {
        String sql = "SELECT * FROM Sala";
        return this.jdbcTemplate.query(sql, this.salaRowMapper);
    }

    public void insert(Sala sala) {
        String sql = "INSERT INTO Sala (ID_Facultate, Nume_Sala, Tip_Sala, Capacitate) VALUES (?, ?, ?, ?)";
        org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        
        this.jdbcTemplate.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, sala.getFacultateId());
            ps.setString(2, sala.getNume());
            ps.setString(3, sala.getTip());
            ps.setInt(4, sala.getCapacitate());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            sala.setId(keyHolder.getKey().longValue());
        }
    }

    public void update(Sala sala) {
        String sql = "UPDATE Sala SET ID_Facultate = ?, Nume_Sala = ?, Tip_Sala = ?, Capacitate = ? WHERE ID_Sala = ?";
        this.jdbcTemplate.update(sql, sala.getFacultateId(), sala.getNume(), sala.getTip(), sala.getCapacitate(), sala.getId());
    }

    public void delete(Long id){
        String sql = "delete from Sala where ID_Sala = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<com.raul.univ_management.dto.RoomEfficiencyDto> getRoomEfficiencyAnalysis(Long facultyId) {
        String sql = """
            SELECT 
                s.Nume_Sala,
                s.Capacitate,
                COALESCE(SUM(d.Valoare_Dotare), 0) as RoomValue,
                CAST(COALESCE(SUM(d.Valoare_Dotare), 0) AS DECIMAL(10,2)) / NULLIF(s.Capacitate, 0) as ValuePerSeat,
                CASE 
                    WHEN (CAST(COALESCE(SUM(d.Valoare_Dotare), 0) AS DECIMAL(10,2)) / NULLIF(s.Capacitate, 0)) > 
                         (SELECT AVG(CAST(sub_d.Valoare_Dotare AS DECIMAL(10,2))/NULLIF(sub_s.Capacitate, 0)) 
                          FROM Dotare sub_d 
                          JOIN Sala sub_s ON sub_d.ID_Sala = sub_s.ID_Sala 
                          WHERE sub_s.ID_Facultate = ?) * 1.5 
                    THEN 'Elite'
                    WHEN (CAST(COALESCE(SUM(d.Valoare_Dotare), 0) AS DECIMAL(10,2)) / NULLIF(s.Capacitate, 0)) < 
                         (SELECT AVG(CAST(sub_d.Valoare_Dotare AS DECIMAL(10,2))/NULLIF(sub_s.Capacitate, 0)) 
                          FROM Dotare sub_d 
                          JOIN Sala sub_s ON sub_d.ID_Sala = sub_s.ID_Sala 
                          WHERE sub_s.ID_Facultate = ?) * 0.5 
                    THEN 'Needs Upgrade'
                    ELSE 'Standard'
                END as Status
            FROM Sala s
            LEFT JOIN Dotare d ON s.ID_Sala = d.ID_Sala
            WHERE s.ID_Facultate = ?
            GROUP BY s.ID_Sala, s.Nume_Sala, s.Capacitate
            HAVING s.Capacitate > 0
            ORDER BY ValuePerSeat DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new com.raul.univ_management.dto.RoomEfficiencyDto(
            rs.getString("Nume_Sala"),
            rs.getInt("Capacitate"),
            rs.getBigDecimal("RoomValue"),
            rs.getBigDecimal("ValuePerSeat"),
            rs.getString("Status")
        ), facultyId, facultyId, facultyId);
    }
}
