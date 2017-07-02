package com.kraluk.totoscheduler.service.dto;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Child entity.
 */
public class ChildDto implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String registerNumber;

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

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
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

        ChildDto childDto = (ChildDto) o;
        if (childDto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), childDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", id)
            .add("registerNumber", registerNumber)
            .add("name", name)
            .add("comment", comment)
            .toString();
    }
}
