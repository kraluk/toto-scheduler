package com.kraluk.totoscheduler.service.mapper;

import com.kraluk.totoscheduler.domain.Therapist;
import com.kraluk.totoscheduler.service.dto.TherapistDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Therapist and its DTO TherapistDto.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class,})
public interface TherapistMapper extends EntityMapper<TherapistDto, Therapist> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    TherapistDto toDto(Therapist therapist);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "therapies", ignore = true)
    Therapist toEntity(TherapistDto therapistDto);

    default Therapist fromId(Long id) {
        if (id == null) {
            return null;
        }
        Therapist therapist = new Therapist();
        therapist.setId(id);
        return therapist;
    }
}
