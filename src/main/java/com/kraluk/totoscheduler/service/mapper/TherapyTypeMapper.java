package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.TherapyType;
import com.kraluk.totoscheduler.service.dto.TherapyTypeDTO;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity TherapyType and its DTO TherapyTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TherapyTypeMapper extends EntityMapper<TherapyTypeDTO, TherapyType> {


    default TherapyType fromId(Long id) {
        if (id == null) {
            return null;
        }
        TherapyType therapyType = new TherapyType();
        therapyType.setId(id);
        return therapyType;
    }
}
