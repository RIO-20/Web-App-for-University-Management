/** Clasa service pentru logica de business legata de sponsori
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.FacultateDao;
import com.raul.univ_management.dao.SponsorDao;
import com.raul.univ_management.dao.Sponsor_FacultateDao;
import com.raul.univ_management.dto.SponsorStatsDto;
import com.raul.univ_management.model.Sponsor;
import com.raul.univ_management.model.Sponsor_Facultate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SponsorService {

    private final SponsorDao sponsorDao;
    private final Sponsor_FacultateDao sponsorFacultateDao;
    private final FacultateDao facultateDao;
    private final com.raul.univ_management.dao.DotareDao dotareDao;

    public SponsorService(SponsorDao sponsorDao, Sponsor_FacultateDao sponsorFacultateDao, FacultateDao facultateDao, com.raul.univ_management.dao.DotareDao dotareDao) {
        this.sponsorDao = sponsorDao;
        this.sponsorFacultateDao = sponsorFacultateDao;
        this.facultateDao = facultateDao;
        this.dotareDao = dotareDao;
    }

    public List<SponsorStatsDto> getSponsorAnalysis(Long facultyId) {
        return sponsorDao.getSponsorAnalysis(facultyId);
    }
    
    public List<SponsorStatsDto> getGlobalSponsorAnalysis() {
        return sponsorDao.getGlobalSponsorAnalysis();
    }

    public List<Sponsor> findAll() {
        return sponsorDao.findAll();
    }

    @Transactional
    public void registerSponsor(Sponsor sponsor) {
        sponsorDao.insert(sponsor);
    }


    @Transactional
    public void insert(com.raul.univ_management.dto.SponsorDto dto) {
        Sponsor sponsor = new Sponsor();
        sponsor.setNume(dto.nume_sponsor());
        sponsor.setEmail(dto.email());
        sponsor.setDescriere(dto.descriere());
        sponsorDao.insert(sponsor);
    }

    @Transactional
    public void update(com.raul.univ_management.dto.SponsorDto dto) {
        Sponsor sponsor = new Sponsor();
        sponsor.setId(dto.id());
        sponsor.setNume(dto.nume_sponsor());
        sponsor.setEmail(dto.email());
        sponsor.setDescriere(dto.descriere());
        sponsorDao.update(sponsor);
    }

    @Transactional
    public void delete(Long id) {
        sponsorDao.delete(id);
    }

    @Transactional
    public void registerDonation(Long sponsorId, Long facultyId, String type, BigDecimal value, String description, Long roomId, Integer quantity) {

        String normalizedType = type.toLowerCase();
        if (!normalizedType.equals("bani") && !normalizedType.equals("obiecte")) {
            throw new IllegalArgumentException("Invalid donation type. Must be 'bani' or 'obiecte'.");
        }

 
        Sponsor_Facultate sf = new Sponsor_Facultate();
        sf.setSponsorId(sponsorId);
        sf.setFacultateId(facultyId);
        sf.setTip(normalizedType);
        sf.setValoare(value);
        
        System.out.println("DEBUG: Service calling sponsorFacultateDao.insert with val: " + value + " type: " + normalizedType);
        sponsorFacultateDao.insert(sf);
        System.out.println("DEBUG: Service sponsorFacultateDao.insert completed.");

        
        if (normalizedType.equals("bani")) {
            facultateDao.updateBudget(facultyId, value);
        } else if (normalizedType.equals("obiecte") && roomId != null && quantity != null && quantity > 0) {
            
            com.raul.univ_management.model.Dotare dotare = new com.raul.univ_management.model.Dotare();
            dotare.setIdSala(roomId);
            dotare.setNume(description != null ? description : "Donation from Sponsor " + sponsorId);
            
            
            if (value != null && value.compareTo(BigDecimal.ZERO) > 0) {
                dotare.setValoare(value.divide(new BigDecimal(quantity), 2, java.math.RoundingMode.HALF_UP));
            } else {
                dotare.setValoare(BigDecimal.ZERO);
            }
            
            dotare.setStare("functional"); 
            dotare.setCantitate(quantity);
            
            dotareDao.insert(dotare);
        }
    }



}
