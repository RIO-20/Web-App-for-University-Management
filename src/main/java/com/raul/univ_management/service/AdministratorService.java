/** Clasa service pentru logica de business legata de administratori
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.AdminDao;
import com.raul.univ_management.dto.AdministratorDto;
import com.raul.univ_management.model.Administrator;
import com.raul.univ_management.mapper.AdministratorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdministratorService {

    private final AdminDao adminDao;
    private final AdministratorMapper administratorMapper;

    public AdministratorService(AdminDao adminDao, AdministratorMapper administratorMapper) {
        this.adminDao = adminDao;
        this.administratorMapper = administratorMapper;
    }

   
    @Transactional(readOnly = true)
    public AdministratorDto findByUsernameOrEmail(String identifier) {
        return adminDao.findByUsernameOrEmail(identifier)
                .map(administratorMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public void insert(AdministratorDto dto) {
        Administrator admin = administratorMapper.toEntity(dto);
        adminDao.insert(admin);
    }

    @Transactional
    public void update(AdministratorDto dto) {
        Administrator admin = administratorMapper.toEntity(dto);
        adminDao.update(admin);
    }

    @Transactional
    public void delete(Long id) {
        adminDao.delete(id);
    }
}
