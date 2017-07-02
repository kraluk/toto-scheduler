package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Period;
import com.kraluk.totoscheduler.service.dto.PeriodDto;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Period and its DTO PeriodDto.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PeriodMapper extends EntityMapper<PeriodDto, Period> {


    default Period fromId(Long id) {
        if (id == null) {
            return null;
        }
        Period period = new Period();
        period.setId(id);
        return period;
    }
}
