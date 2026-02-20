/** Clasa service pentru logica de business legata de sali
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.SalaDao;
import com.raul.univ_management.dto.SalaDto;
import com.raul.univ_management.mapper.SalaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaService {
    private final SalaDao salaDao;
    private final SalaMapper salaMapper;
    private final com.raul.univ_management.dao.DotareDao dotareDao;

    public SalaService(SalaDao salaDao, SalaMapper salaMapper, com.raul.univ_management.dao.DotareDao dotareDao){
        this.salaDao = salaDao;
        this.salaMapper  = salaMapper;
        this.dotareDao = dotareDao;
    }

    @Transactional(readOnly = true)
    public List<SalaDto> findByFaculty(Long id){
        return salaDao.findByFaculty(id).stream()
                .map(salaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<com.raul.univ_management.dto.RoomStatsDto> getRoomStatsForFaculty(Long facultyId) {
        List<com.raul.univ_management.model.Sala> rooms = salaDao.findByFaculty(facultyId);
        List<com.raul.univ_management.dto.RoomStatsDto> stats = new java.util.ArrayList<>();

        for (com.raul.univ_management.model.Sala room : rooms) {
            List<com.raul.univ_management.model.Dotare> equipmentList = dotareDao.findByIdRoom(room.getId());
            
            int total = equipmentList.size();
            int functional = 0;
            int broken = 0;
            int old = 0;

            for (com.raul.univ_management.model.Dotare d : equipmentList) {
                if ("functional".equalsIgnoreCase(d.getStare())) functional++;
                else if ("stricat".equalsIgnoreCase(d.getStare())) broken++;
                else if ("vechi".equalsIgnoreCase(d.getStare())) old++;
            }

            stats.add(new com.raul.univ_management.dto.RoomStatsDto(
                room.getNume(), total, functional, broken, old
            ));
        }
        return stats;
    }

    @Transactional(readOnly = true)
    public List<SalaDto> findAll() {
        return salaDao.findAll().stream()
                .map(salaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insert(SalaDto dto) {
        com.raul.univ_management.model.Sala sala = salaMapper.toEntity(dto);
        salaDao.insert(sala);
    }

    @Transactional
    public void update(SalaDto dto) {
        com.raul.univ_management.model.Sala sala = salaMapper.toEntity(dto);
        salaDao.update(sala);
    }

    @Transactional
    public void delete(Long id) {
        salaDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<com.raul.univ_management.dto.RoomEfficiencyDto> getRoomEfficiencyAnalysis(Long facultyId) {
        return salaDao.getRoomEfficiencyAnalysis(facultyId);
    }
}
