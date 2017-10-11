package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.TimeTable;
import com.kraluk.totoscheduler.service.dto.TimeTableDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity TimeTable and its DTO TimeTableDTO.
 */
@Mapper(componentModel = "spring", uses = {ChildMapper.class,})
public interface TimeTableMapper extends EntityMapper<TimeTableDTO, TimeTable> {

    @Mapping(source = "child.id", target = "childId")
    TimeTableDTO toDto(TimeTable timeTable);

    @Mapping(target = "therapies", ignore = true)

    @Mapping(source = "childId", target = "child")
    TimeTable toEntity(TimeTableDTO timeTableDTO);

    default TimeTable fromId(Long id) {
        if (id == null) {
            return null;
        }
        TimeTable timeTable = new TimeTable();
        timeTable.setId(id);
        return timeTable;
    }
}
