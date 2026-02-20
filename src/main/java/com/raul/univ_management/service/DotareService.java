/** Clasa service pentru logica de business legata de dotari
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.DotareDao;
import com.raul.univ_management.dto.DotareDto;
import com.raul.univ_management.mapper.DotareMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class DotareService{
  private final DotareDao dotareDao;
  private final DotareMapper dotareMapper;

  public DotareService(DotareDao dotareDao, DotareMapper dotareMapper) {
    this.dotareDao = dotareDao;
    this.dotareMapper = dotareMapper;
  }
  @Transactional(readOnly = true)
    public List<DotareDto> getAllDotari(Long idRoom) {
      return dotareDao.findByIdRoom(idRoom).stream()
          .map(dotareMapper::toDto)
          .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DotareDto> findAll() {
        return dotareDao.findAll().stream()
            .map(dotareMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public void insert(DotareDto dto) {
        com.raul.univ_management.model.Dotare dotare = dotareMapper.toEntity(dto);
        dotareDao.insert(dotare);
    }

    @Transactional
    public void update(DotareDto dto) {
        com.raul.univ_management.model.Dotare dotare = dotareMapper.toEntity(dto);
        dotareDao.update(dotare);
    }

    @Transactional
    public void delete(Long id) {
        dotareDao.delete(id);
    }

    @Transactional(readOnly = true)
    public List<com.raul.univ_management.dto.MaintenanceCostDto> getMaintenanceAnalysis(Long facultyId) {
        return dotareDao.getMaintenanceAnalysis(facultyId);
    }

    @Transactional(readOnly = true)
    public List<com.raul.univ_management.dto.EquipmentWithHistoryDto> getEquipmentWithHistory(Long roomId) {
        var list = dotareDao.getEquipmentWithHistory(roomId);
        return list != null ? list : java.util.Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Long> getEquipmentCountByRoom(Long roomId) {
        return dotareDao.getEquipmentCountByRoom(roomId);
    }

    @Transactional(readOnly = true)
    public List<DotareDto> findAllByFaculty(Long facultyId) {
        return dotareDao.findAllByFaculty(facultyId).stream()
                .map(dotareMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DotareDto> findRepairableByFaculty(Long facultyId) {
        return dotareDao.findRepairableByFaculty(facultyId).stream()
                .map(dotareMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String getRoomName(Long roomId) {
        return dotareDao.getRoomName(roomId).orElse("Unknown Room");
    }
}