package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.*;
import com.kraluk.totoscheduler.service.dto.RoleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface RoleMapper extends EntityMapper <RoleDTO, Role> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    RoleDTO toDto(Role role); 

    @Mapping(source = "userId", target = "user")
    Role toEntity(RoleDTO roleDTO); 
    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
