package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Child;
import com.kraluk.totoscheduler.service.dto.ChildDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Child and its DTO ChildDto.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChildMapper extends EntityMapper<ChildDto, Child> {

    @Mapping(target = "therapies", ignore = true)
    Child toEntity(ChildDto childDto);

    default Child fromId(Long id) {
        if (id == null) {
            return null;
        }
        Child child = new Child();
        child.setId(id);
        return child;
    }
}
