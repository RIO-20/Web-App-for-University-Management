/** Clasa service pentru logica de business legata de tranzactiile dotarilor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.Dotare_TranzactieDao;
import com.raul.univ_management.dto.Dotare_TranzactieDto;
import com.raul.univ_management.mapper.Dotare_TranzactieMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Dotare_TranzactieService {
    private final Dotare_TranzactieDao dotareTranzactieDao;
    private final Dotare_TranzactieMapper dotareTranzactieMapper;

    public Dotare_TranzactieService(Dotare_TranzactieDao dotareTranzactieDao, Dotare_TranzactieMapper dotareTranzactieMapper) {
        this.dotareTranzactieDao = dotareTranzactieDao;
        this.dotareTranzactieMapper = dotareTranzactieMapper;
    }


    @Transactional(readOnly = true)
    public List<Dotare_TranzactieDto> findAll() {
        return dotareTranzactieDao.findAll().stream()
                .map(dotareTranzactieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insert(Dotare_TranzactieDto dto) {
        dotareTranzactieDao.insert(dotareTranzactieMapper.toEntity(dto));
    }
}
