package com.raul.univ_management.mapper;

import com.raul.univ_management.dto.AdministratorDto;
import com.raul.univ_management.model.Administrator;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Administrator
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class AdministratorMapper {

    public AdministratorDto toDto(Administrator admin) {
        if (admin == null) {
            return null;
        }
        return new AdministratorDto(
                admin.getId(),
                admin.getNume(),
                admin.getPrenume(),
                admin.getEmail(),
                admin.getTelefon(),
                admin.getUsername(),
                admin.getParola()
        );
    }

    public Administrator toEntity(AdministratorDto dto) {
        if (dto == null) {
            return null;
        }
        return Administrator.builder()
                .id(dto.id() != null ? dto.id() : 0)
                .nume(dto.nume())
                .prenume(dto.prenume())
                .email(dto.email())
                .telefon(dto.telefon())
                .username(dto.username())
                .parola(dto.parola())
                .build();
    }
}
