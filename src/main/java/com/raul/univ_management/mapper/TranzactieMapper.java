package com.raul.univ_management.mapper;


import com.raul.univ_management.dto.TranzactieDto;
import com.raul.univ_management.model.Tranzactie;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Tranzactie
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class TranzactieMapper {
    public TranzactieDto toDto(Tranzactie tr){
        if(tr == null)
            return null;

        return new TranzactieDto(
                tr.getId(),
                tr.getFacultateId(),
                 tr.getTip(),
                 tr.getDescriere()
         );
     }
 
     public Tranzactie toEntity(TranzactieDto dto){
         if(dto == null)
             return null;
         return Tranzactie.builder()
                 .id(dto.id())
                 .facultateId(dto.facultateId())
                 .tip(dto.tip_tranzactie())
                 .descriere(dto.descriere())
                 .build();
    }
}
