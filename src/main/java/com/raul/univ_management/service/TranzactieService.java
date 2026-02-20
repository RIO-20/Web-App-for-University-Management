package com.raul.univ_management.service;


import com.raul.univ_management.dao.TranzactieDao;
import com.raul.univ_management.dto.TranzactieDto;
import com.raul.univ_management.mapper.TranzactieMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
/** Clasa service pentru logica de business legata de tranzactii
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class TranzactieService {
    private final TranzactieDao tranzactieDao;
    private final TranzactieMapper tranzactieMapper;

    public TranzactieService(TranzactieMapper tranzactieMapper, TranzactieDao tranzactieDao) {
        this.tranzactieMapper = tranzactieMapper;
        this.tranzactieDao = tranzactieDao;
    }


    @Transactional(readOnly = true)
    public List<TranzactieDto> findPerFaculty(long id) {
        return tranzactieDao.findPerFaculty(id).stream()
                .map(tranzactieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TranzactieDto> findRecentByFaculty(Long facultyId, int limit) {
        return tranzactieDao.findRecentByFaculty(facultyId, limit).stream()
                .map(tranzactieMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<TranzactieDto> findAll() {
        return tranzactieDao.findAll().stream()
                .map(tranzactieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insert(TranzactieDto dto) {
        com.raul.univ_management.model.Tranzactie tranzactie = tranzactieMapper.toEntity(dto);
        tranzactieDao.insert(tranzactie);
    }

    @Transactional
    public void update(TranzactieDto dto) {
        com.raul.univ_management.model.Tranzactie tranzactie = tranzactieMapper.toEntity(dto);
        tranzactieDao.update(tranzactie);
    }

    @Transactional(readOnly = true)
    public List<com.raul.univ_management.dto.FinancialImpactDto> getFinancialImpact(Long facultyId, Double major, Double minor) {
        return tranzactieDao.getFinancialImpact(facultyId, major, minor);
    }

    @Transactional(readOnly = true)
    public List<TranzactieDto> findHighValueTransactions() {
        return tranzactieDao.findHighValueTransactions().stream()
                .map(tranzactieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        tranzactieDao.delete(id);
    }
}
