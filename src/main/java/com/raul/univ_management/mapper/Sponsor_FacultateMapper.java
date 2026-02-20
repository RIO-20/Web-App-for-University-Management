package com.raul.univ_management.mapper;


import com.raul.univ_management.dto.Sponsor_FacultateDto;
import com.raul.univ_management.model.Sponsor_Facultate;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Sponsor_Facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Sponsor_FacultateMapper {
    public Sponsor_FacultateDto toDto(Sponsor_Facultate dt){
        if(dt == null)
            return null;

        return new Sponsor_FacultateDto(
                dt.getId(),
                dt.getFacultateId(),
                dt.getSponsorId(),
                dt.getTip(),
                dt.getValoare()
        );
    }

    public Sponsor_Facultate toEntity(Sponsor_FacultateDto dto){
        if(dto == null)
            return null;
        return Sponsor_Facultate.builder()
                .facultateId(dto.facultateId())
                .sponsorId(dto.sponsorId())
                .tip(dto.tip())
                .valoare(dto.valoare())
                .build();
    }
}
