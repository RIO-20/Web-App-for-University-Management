package com.raul.univ_management.mapper;


import com.raul.univ_management.dto.Dotare_TranzactieDto;
import com.raul.univ_management.model.Dotare_Tranzactie;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Dotare_Tranzactie
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Dotare_TranzactieMapper {
    public Dotare_TranzactieDto toDto(Dotare_Tranzactie dt){
        if(dt == null)
            return null;

        return new Dotare_TranzactieDto(
                dt.getId(),
                dt.getTranzactieId(),
                dt.getDotareId(),
                dt.getCantitate(),
                dt.getValoare(),
                dt.getDescriere()
        );
    }

    public Dotare_Tranzactie toEntity(Dotare_TranzactieDto dto){
        if(dto == null)
            return null;
        return Dotare_Tranzactie.builder()
                .tranzactieId(dto.tranzactieId())
                .dotareId(dto.dotareId())
                .cantitate(dto.cantitate())
                .valoare(dto.valoare())
                .descriere(dto.descriere())
                .build();
    }
}
