package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.*;
import com.kraluk.totoscheduler.service.dto.TherapyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Therapy and its DTO TherapyDTO.
 */
@Mapper(componentModel = "spring", uses = {TherapyTypeMapper.class, TherapistMapper.class, TimeTableMapper.class, })
public interface TherapyMapper extends EntityMapper <TherapyDTO, Therapy> {

    @Mapping(source = "therapyType.id", target = "therapyTypeId")

    @Mapping(source = "therapist.id", target = "therapistId")

    @Mapping(source = "timeTable.id", target = "timeTableId")
    TherapyDTO toDto(Therapy therapy); 

    @Mapping(source = "therapyTypeId", target = "therapyType")

    @Mapping(source = "therapistId", target = "therapist")

    @Mapping(source = "timeTableId", target = "timeTable")
    Therapy toEntity(TherapyDTO therapyDTO); 
    default Therapy fromId(Long id) {
        if (id == null) {
            return null;
        }
        Therapy therapy = new Therapy();
        therapy.setId(id);
        return therapy;
    }
}
