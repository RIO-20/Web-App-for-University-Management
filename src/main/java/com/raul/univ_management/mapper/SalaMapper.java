package com.raul.univ_management.mapper;


import com.raul.univ_management.dto.SalaDto;
import com.raul.univ_management.model.Sala;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Sala
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class SalaMapper {
    public SalaDto toDto(Sala sala){
        if(sala == null)
            return null;

        return new SalaDto(
                sala.getId(),
                sala.getFacultateId(),
                 sala.getNume(),
                 sala.getTip(),
                 sala.getCapacitate()
         );
     }
 
     public Sala toEntity(SalaDto dto){
         if(dto == null)
             return null;
         return Sala.builder()
                 .facultateId(dto.facultateId())
                 .tip(dto.tip())
                 .capacitate(dto.capacitate())
                 .nume(dto.nume())
                 .build();
    }
}
