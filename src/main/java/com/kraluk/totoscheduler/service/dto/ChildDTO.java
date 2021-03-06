package com.kraluk.totoscheduler.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Child entity.
 */
public class ChildDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String registerNumber;

    @NotNull
    @Size(min = 3)
    private String firstName;

    @NotNull
    @Size(min = 2)
    private String lastName;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChildDTO childDTO = (ChildDTO) o;
        if (childDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), childDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChildDTO{" +
            "id=" + getId() +
            ", registerNumber='" + getRegisterNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
