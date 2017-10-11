package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Role;
import com.kraluk.totoscheduler.service.dto.RoleDTO;

import org.mapstruct.Mapper;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {TherapistMapper.class,})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {


    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
