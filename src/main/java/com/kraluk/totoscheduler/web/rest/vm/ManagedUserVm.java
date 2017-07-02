package com.kraluk.totoscheduler.web.rest.vm;

import com.google.common.base.MoreObjects;
import com.kraluk.totoscheduler.service.dto.UserDto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.Size;

/**
 * View Model extending the UserDto, which is meant to be used in the user management UI.
 */
public class ManagedUserVm extends UserDto {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedUserVm() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserVm(Long id, String login, String password, String firstName, String lastName,
                         String email, boolean activated, String imageUrl, String langKey,
                         String createdBy, Instant createdDate, String lastModifiedBy,
                         Instant lastModifiedDate,
                         Set<String> authorities) {

        super(id, login, firstName, lastName, email, activated, imageUrl, langKey,
            createdBy, createdDate, lastModifiedBy, lastModifiedDate, authorities);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("password", password)
            .toString();
    }
}
