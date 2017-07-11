package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Role;
import com.kraluk.totoscheduler.service.dto.RoleDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Role and its DTO RoleDto.
 */
@Mapper(componentModel = "spring", uses = {TherapistMapper.class,})
public interface RoleMapper extends EntityMapper<RoleDto, Role> {

    @Mapping(source = "therapist.id", target = "therapistId")
    RoleDto toDto(Role role);

    @Mapping(source = "therapistId", target = "therapist")
    Role toEntity(RoleDto roleDto);

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
