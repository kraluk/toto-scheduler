package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.*;
import com.kraluk.totoscheduler.service.dto.ChildDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Child and its DTO ChildDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ChildMapper extends EntityMapper <ChildDTO, Child> {
    
    @Mapping(target = "timeTables", ignore = true)
    Child toEntity(ChildDTO childDTO); 
    default Child fromId(Long id) {
        if (id == null) {
            return null;
        }
        Child child = new Child();
        child.setId(id);
        return child;
    }
}
