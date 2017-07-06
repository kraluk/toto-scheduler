package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.*;
import com.kraluk.totoscheduler.service.dto.TherapyEntryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TherapyEntry and its DTO TherapyEntryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TherapyEntryMapper extends EntityMapper <TherapyEntryDTO, TherapyEntry> {
    
    
    default TherapyEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        TherapyEntry therapyEntry = new TherapyEntry();
        therapyEntry.setId(id);
        return therapyEntry;
    }
}
