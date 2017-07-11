package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Therapy;
import com.kraluk.totoscheduler.service.dto.TherapyDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Therapy and its DTO TherapyDto.
 */
@Mapper(componentModel = "spring",
    uses = {TherapyTypeMapper.class, TherapistMapper.class, TimeTableMapper.class,})
public interface TherapyMapper extends EntityMapper<TherapyDto, Therapy> {

    @Mapping(source = "therapyType.id", target = "therapyTypeId")

    @Mapping(source = "therapist.id", target = "therapistId")

    @Mapping(source = "timeTable.id", target = "timeTableId")
    TherapyDto toDto(Therapy therapy);

    @Mapping(source = "therapyTypeId", target = "therapyType")

    @Mapping(source = "therapistId", target = "therapist")

    @Mapping(source = "timeTableId", target = "timeTable")
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
