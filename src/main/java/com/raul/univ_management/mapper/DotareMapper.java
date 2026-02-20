package com.raul.univ_management.mapper;
import com.raul.univ_management.dto.DotareDto;
import com.raul.univ_management.model.Dotare;
import org.springframework.stereotype.Component;

@Component
/** Clasa Mapper pentru convertirea datelor Dotare
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class DotareMapper {
  public DotareDto toDto(Dotare dot){
    if(dot == null)
        return null;
    
    return new DotareDto(
      dot.getId(),
      dot.getIdSala(),
      dot.getNume(),
      dot.getValoare(),
      dot.getStare(),
      dot.getCantitate()
    );
  }

  public Dotare toEntity(DotareDto dto){
    if(dto == null)
        return null;
    return Dotare.builder()
      .id(dto.id())
      .idSala(dto.idSala())
      .nume(dto.nume())
      .valoare(dto.valoare())
      .stare(dto.stare())
      .cantitate(dto.cantitate())
      .build();
  }
  
}
