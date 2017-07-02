package com.kraluk.totoscheduler.service.dto;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the TherapyEntry entity.
 */
public class TherapyEntryDto implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TherapyEntryDto therapyEntryDto = (TherapyEntryDto) o;
        if (therapyEntryDto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), therapyEntryDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("name", name)
            .toString();
    }
}
