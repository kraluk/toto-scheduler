package com.kraluk.totoscheduler.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TherapyEntry entity.
 */
public class TherapyEntryDTO implements Serializable {

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

        TherapyEntryDTO therapyEntryDTO = (TherapyEntryDTO) o;
        if(therapyEntryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), therapyEntryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TherapyEntryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
