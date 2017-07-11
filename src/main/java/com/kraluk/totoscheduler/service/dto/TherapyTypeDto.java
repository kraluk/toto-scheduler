package com.kraluk.totoscheduler.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the TherapyType entity.
 */
public class TherapyTypeDto implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String name;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        TherapyTypeDto therapyTypeDto = (TherapyTypeDto) o;
        if (therapyTypeDto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), therapyTypeDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TherapyTypeDto{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
