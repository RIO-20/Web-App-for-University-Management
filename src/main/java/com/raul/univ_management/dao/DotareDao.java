package com.raul.univ_management.dao;

import com.raul.univ_management.model.Dotare;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
/** Clasa DAO pentru interactiunea cu baza de date pentru dotari
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class DotareDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Dotare> dotareRowMapper = (rs, rowNum) -> {
        Dotare dot = new Dotare();
        dot.setId(rs.getLong("ID_Dotare"));
        dot.setIdSala(rs.getLong("ID_Sala"));
        dot.setNume(rs.getString("Nume_Dotare"));
        dot.setValoare(rs.getBigDecimal("Valoare_Dotare"));
        dot.setStare(rs.getString("Stare_Dotare"));
      
        try {
            dot.setCantitate(rs.getInt("Cantitate"));
        } catch (java.sql.SQLException e) {
            dot.setCantitate(1); 
        }
        return dot;
    };

    public DotareDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Dotare> findByIdRoom(Long idSala) {
        String sql = "Select * from Dotare where ID_Sala = ?";
        return this.jdbcTemplate.query(sql, this.dotareRowMapper, new Object[]{idSala});
    }

    public List<Dotare> findAll() {
        String sql = "Select * from Dotare";
        return this.jdbcTemplate.query(sql, this.dotareRowMapper);
    }

    public void insert(Dotare dotare) {
        String sql = "INSERT INTO Dotare (ID_Sala, Nume_Dotare, Valoare_Dotare, Stare_Dotare, Cantitate) VALUES (?, ?, ?, ?, ?)";
        org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, dotare.getIdSala());
            ps.setString(2, dotare.getNume());
            ps.setBigDecimal(3, dotare.getValoare());
            ps.setString(4, dotare.getStare());
            ps.setInt(5, dotare.getCantitate() > 0 ? dotare.getCantitate() : 1); // Default to 1
            return ps;
        }, keyHolder);
        
        if (keyHolder.getKey() != null) {
            dotare.setId(keyHolder.getKey().longValue());
        }
    }

    public void update(Dotare dotare) {
        String sql = "UPDATE Dotare SET ID_Sala = ?, Nume_Dotare = ?, Valoare_Dotare = ?, Stare_Dotare = ?, Cantitate = ? WHERE ID_Dotare = ?";
        this.jdbcTemplate.update(sql, dotare.getIdSala(), dotare.getNume(), dotare.getValoare(), dotare.getStare(), dotare.getCantitate(), dotare.getId());
    }

    public void updateQuantity(Long dotareId, int newQuantity) {
        String sql = "UPDATE Dotare SET Cantitate = ? WHERE ID_Dotare = ?";
        jdbcTemplate.update(sql, newQuantity, dotareId);
    }

    public void delete(Long id){
        String sql = "delete from Dotare where ID_Dotare = ?";
        this.jdbcTemplate.update(sql, id);
    }

    public List<com.raul.univ_management.dto.MaintenanceCostDto> getMaintenanceAnalysis(Long facultyId) {
        String sql = """
            SELECT
                s.ID_Sala,
                s.Nume_Sala as RoomName,
                SUM(CASE WHEN d.Stare_Dotare IN ('Broken', 'Old') THEN d.Valoare_Dotare ELSE 0 END) as ReplacementCost,
                COALESCE(
                    (SELECT SUM(dt.Valoare)
                     FROM Dotare_Tranzactie dt
                     JOIN Tranzactie t ON dt.ID_Tranzactie = t.ID_Tranzactie
                     JOIN Dotare d2 ON dt.ID_Dotare = d2.ID_Dotare
                     WHERE d2.ID_Sala = s.ID_Sala AND t.Tip = 'Reparatie')
                , 0) as RepairCost,
                CASE 
                    WHEN COALESCE((SELECT SUM(dt.Valoare) FROM Dotare_Tranzactie dt JOIN Tranzactie t ON dt.ID_Tranzactie = t.ID_Tranzactie JOIN Dotare d2 ON dt.ID_Dotare = d2.ID_Dotare WHERE d2.ID_Sala = s.ID_Sala AND t.Tip = 'Reparatie'), 0) > SUM(CASE WHEN d.Stare_Dotare IN ('Broken', 'Old') THEN d.Valoare_Dotare ELSE 0 END) 
                    THEN 'Replace' 
                    ELSE 'Maintain' 
                END as Recommendation
            FROM Sala s
            JOIN Dotare d ON s.ID_Sala = d.ID_Sala
            JOIN Facultate f ON s.ID_Facultate = f.ID_Facultate
            WHERE f.ID_Facultate = ?
            GROUP BY s.ID_Sala, s.Nume_Sala
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new com.raul.univ_management.dto.MaintenanceCostDto(
            rs.getLong("ID_Sala"),
            rs.getString("RoomName"),
            rs.getBigDecimal("ReplacementCost"),
            rs.getBigDecimal("RepairCost"),
            rs.getString("Recommendation")
        ), facultyId);
    }

    public java.util.Optional<String> getRoomName(Long roomId) {
        String sql = "SELECT Nume_Sala FROM Sala WHERE ID_Sala = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("Nume_Sala"), roomId).stream().findFirst();
    }

    public List<com.raul.univ_management.dto.EquipmentWithHistoryDto> getEquipmentWithHistory(Long roomId) {
      
        String sqlEquip = "SELECT * FROM Dotare WHERE ID_Sala = ?";
        
        return jdbcTemplate.query(sqlEquip, (rs, rowNum) -> {
            Long dotareId = rs.getLong("ID_Dotare");
            
        
            String sqlTrans = """
                SELECT t.ID_Tranzactie, t.Tip, dt.Valoare, t.Descriere
                FROM Tranzactie t
                JOIN Dotare_Tranzactie dt ON t.ID_Tranzactie = dt.ID_Tranzactie
                WHERE dt.ID_Dotare = ?
                ORDER BY t.ID_Tranzactie DESC
            """;
            
            List<com.raul.univ_management.dto.EquipmentWithHistoryDto.TransactionHistoryDto> history = jdbcTemplate.query(
                sqlTrans, 
                (rsT, rowNumT) -> new com.raul.univ_management.dto.EquipmentWithHistoryDto.TransactionHistoryDto(
                    rsT.getLong("ID_Tranzactie"),
                    rsT.getString("Tip"),
                    rsT.getBigDecimal("Valoare"),
                    rsT.getString("Descriere")
                ), 
                dotareId
            );
            
            return new com.raul.univ_management.dto.EquipmentWithHistoryDto(
                dotareId,
                rs.getString("Nume_Dotare"),
                rs.getString("Stare_Dotare"),
                rs.getBigDecimal("Valoare_Dotare"),
                rs.getInt("Cantitate"), // Map Quantity
                history
            );
        }, roomId);
    }
    public java.util.Map<String, Long> getEquipmentCountByRoom(Long roomId) {
        String sql = "SELECT Nume_Dotare, SUM(Cantitate) as Count FROM Dotare WHERE ID_Sala = ? GROUP BY Nume_Dotare";
        
        return jdbcTemplate.query(sql, (rs) -> {
            java.util.Map<String, Long> map = new java.util.HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("Nume_Dotare"), rs.getLong("Count"));
            }
            return map;
        }, roomId);
    }

    public void updateState(Long dotareId, String newState) {
        String sql = "UPDATE Dotare SET Stare_Dotare = ? WHERE ID_Dotare = ?";
        jdbcTemplate.update(sql, newState, dotareId);
    }
    
    public java.util.Optional<Dotare> findById(Long id) {
        String sql = "SELECT * FROM Dotare WHERE ID_Dotare = ?";
        return jdbcTemplate.query(sql, dotareRowMapper, id).stream().findFirst();
    }

    public List<Dotare> findAllByFaculty(Long facultyId) {
        String sql = """
            SELECT d.* 
            FROM Dotare d
            JOIN Sala s ON d.ID_Sala = s.ID_Sala
            WHERE s.ID_Facultate = ?
        """;
        return jdbcTemplate.query(sql, dotareRowMapper, facultyId);
    }

    public List<Dotare> findRepairableByFaculty(Long facultyId) {
        
        String sql = """
            SELECT d.* 
            FROM Dotare d
            JOIN Sala s ON d.ID_Sala = s.ID_Sala
            WHERE s.ID_Facultate = ?
            AND (UPPER(d.Stare_Dotare) IN ('STRICAT', 'VECHI'))
        """;
        return jdbcTemplate.query(sql, dotareRowMapper, facultyId);
    }

    public java.util.Optional<Dotare> findByRoomAndNameAndState(Long roomId, String name, String state) {
        String sql = "SELECT * FROM Dotare WHERE ID_Sala = ? AND Nume_Dotare = ? AND Stare_Dotare = ?";
        return jdbcTemplate.query(sql, dotareRowMapper, roomId, name, state).stream().findFirst();
    }
}
