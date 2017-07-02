package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.TherapyEntry;
import com.kraluk.totoscheduler.service.dto.TherapyEntryDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity TherapyEntry and its DTO TherapyEntryDto.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TherapyEntryMapper extends EntityMapper<TherapyEntryDto, TherapyEntry> {


    default TherapyEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        TherapyEntry therapyEntry = new TherapyEntry();
        therapyEntry.setId(id);
        return therapyEntry;
    }
}
