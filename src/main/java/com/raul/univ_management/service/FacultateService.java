/** Clasa service pentru logica de business legata de facultati
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.FacultateDao;
import com.raul.univ_management.model.Facultate;
import com.raul.univ_management.dto.FacultateDto;
import com.raul.univ_management.mapper.FacultateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultateService {

    private final FacultateDao facultateDao;
    private final FacultateMapper facultateMapper;

    public FacultateService(FacultateDao facultateDao, FacultateMapper facultateMapper) {
        this.facultateDao = facultateDao;
        this.facultateMapper = facultateMapper;
    }

    
    @Transactional(readOnly = true)
    public List<FacultateDto> getAllFaculties() {
        return facultateDao.findAll().stream()
                .map(facultateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insertFacultate(FacultateDto dto) {
        Facultate facultate = facultateMapper.toEntity(dto);
        facultateDao.insert(facultate);
    }

    @Transactional
    public void updateFacultate(FacultateDto dto) {
        Facultate facultate = facultateMapper.toEntity(dto);
        facultateDao.update(facultate);
    }

    @Transactional
    public void deleteFacultate(Long id) {
        facultateDao.delete(id);
    }

    @Transactional(readOnly = true)
    public FacultateDto findByAdminId(Long adminId) {
        return facultateDao.findByAdminId(adminId)
                .map(facultateMapper::toDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public com.raul.univ_management.dto.BudgetStatsDto getBudgetStats(Long facultyId) {
        return facultateDao.getBudgetStats(facultyId).orElse(null);
    }


    @Transactional(readOnly = true)
    public List<FacultateDto> findWellEquippedFaculties() {
        return facultateDao.findWellEquippedFaculties().stream()
                .map(facultateMapper::toDto)
                .collect(Collectors.toList());
    }
}
