package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.*;
import com.kraluk.totoscheduler.service.dto.TherapistDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Therapist and its DTO TherapistDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface TherapistMapper extends EntityMapper <TherapistDTO, Therapist> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    TherapistDTO toDto(Therapist therapist); 

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "therapies", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Therapist toEntity(TherapistDTO therapistDTO); 
    default Therapist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Therapist therapist = new Therapist();
        therapist.setId(id);
        return therapist;
    }
}
