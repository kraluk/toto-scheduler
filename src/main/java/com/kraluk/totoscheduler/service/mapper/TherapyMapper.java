package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Therapy;
import com.kraluk.totoscheduler.service.dto.TherapyDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Therapy and its DTO TherapyDto.
 */
@Mapper(componentModel = "spring",
    uses = {TherapyEntryMapper.class, UserMapper.class, PeriodMapper.class, ChildMapper.class,})
public interface TherapyMapper extends EntityMapper<TherapyDto, Therapy> {

    @Mapping(source = "therapyEntry.id", target = "therapyEntryId")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "period.id", target = "periodId")

    @Mapping(source = "child.id", target = "childId")
    TherapyDto toDto(Therapy therapy);

    @Mapping(source = "therapyEntryId", target = "therapyEntry")

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "periodId", target = "period")

    @Mapping(source = "childId", target = "child")
    Therapy toEntity(TherapyDto therapyDto);

    default Therapy fromId(Long id) {
        if (id == null) {
            return null;
        }
        Therapy therapy = new Therapy();
        therapy.setId(id);
        return therapy;
    }
}
