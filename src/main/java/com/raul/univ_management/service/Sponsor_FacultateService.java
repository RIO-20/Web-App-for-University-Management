package com.raul.univ_management.service;


import com.raul.univ_management.dao.Sponsor_FacultateDao;
import com.raul.univ_management.dto.Sponsor_FacultateDto;
import com.raul.univ_management.mapper.Sponsor_FacultateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
/** Clasa service pentru logica de business legata de relatia sponsor-facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Sponsor_FacultateService {
    private final Sponsor_FacultateDao sponsorFacultateDao;
    private final Sponsor_FacultateMapper sponsorFacultateMapper;

    public Sponsor_FacultateService(Sponsor_FacultateDao sponsorFacultateDao, Sponsor_FacultateMapper sponsorFacultateMapper) {
        this.sponsorFacultateDao = sponsorFacultateDao;
        this.sponsorFacultateMapper = sponsorFacultateMapper;
    }


    @Transactional(readOnly = true)
    public List<Sponsor_FacultateDto> getAll() {
        return sponsorFacultateDao.getAll().stream()
                .map(sponsorFacultateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insert(Sponsor_FacultateDto dto) {
        sponsorFacultateDao.insert(sponsorFacultateMapper.toEntity(dto));
    }
}
