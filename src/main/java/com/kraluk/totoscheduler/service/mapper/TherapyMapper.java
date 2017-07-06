package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.*;
import com.kraluk.totoscheduler.service.dto.TherapyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Therapy and its DTO TherapyDTO.
 */
@Mapper(componentModel = "spring", uses = {TherapyEntryMapper.class, UserMapper.class, PeriodMapper.class, ChildMapper.class, })
public interface TherapyMapper extends EntityMapper <TherapyDTO, Therapy> {

    @Mapping(source = "therapyEntry.id", target = "therapyEntryId")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "period.id", target = "periodId")

    @Mapping(source = "child.id", target = "childId")
    TherapyDTO toDto(Therapy therapy); 

    @Mapping(source = "therapyEntryId", target = "therapyEntry")

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "childId", target = "child")
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
