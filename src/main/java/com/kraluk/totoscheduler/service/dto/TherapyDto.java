package com.kraluk.totoscheduler.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Therapy entity.
 */
public class TherapyDto implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime date;

    private String comment;

    private Long therapyTypeId;

    private Long therapistId;

    private Long timeTableId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getTherapyTypeId() {
        return therapyTypeId;
    }

    public void setTherapyTypeId(Long therapyTypeId) {
        this.therapyTypeId = therapyTypeId;
    }

    public Long getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(Long therapistId) {
        this.therapistId = therapistId;
    }

    public Long getTimeTableId() {
        return timeTableId;
    }

    public void setTimeTableId(Long timeTableId) {
        this.timeTableId = timeTableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TherapyDto therapyDto = (TherapyDto) o;
        if (therapyDto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), therapyDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TherapyDto{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
