package com.raul.univ_management.mapper;

import com.raul.univ_management.dto.FacultateDto;
import com.raul.univ_management.model.Facultate;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class FacultateMapper {

    public FacultateDto toDto(Facultate facultate) {
        if (facultate == null) {
            return null;
        }
        return new FacultateDto(
                facultate.getId(),
                facultate.getAdminId(),
                facultate.getNume(),
                facultate.getDescriere(),
                facultate.getBuget()
        );
    }

    public Facultate toEntity(FacultateDto dto) {
        if (dto == null) {
            return null;
        }
        return Facultate.builder()
                .id(dto.id() != null ? dto.id() : 0) 
                .adminId(dto.adminId() != null ? dto.adminId() : 0)
                .nume(dto.nume())
                .descriere(dto.descriere())
                .buget(dto.buget())
                .build();
    }
}
