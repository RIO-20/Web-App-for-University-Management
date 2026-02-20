package com.raul.univ_management.mapper;

import com.raul.univ_management.dto.SponsorDto;
import com.raul.univ_management.model.Sponsor;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Sponsor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class SponsorMapper {
    public SponsorDto toDto(Sponsor spon){
        if(spon == null)
            return null;

        return new SponsorDto(
                spon.getId(),
                spon.getNume(),
                spon.getDescriere(),
                spon.getEmail()
        );
    }

    public Sponsor toEntity(SponsorDto dto){
        if(dto == null)
            return null;

        return Sponsor.builder()
                .nume(dto.nume_sponsor())
                .descriere(dto.descriere())
                .email(dto.email())
                .build();
    }
}
