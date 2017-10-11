package com.kraluk.totoscheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@Document(indexName = "child")
public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 5)
    @Column(name = "register_number", nullable = false)
    private String registerNumber;

    @NotNull
    @Size(min = 3)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "jhi_comment")
    private String comment;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    private Set<TimeTable> timeTables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public Child registerNumber(String registerNumber) {
        this.registerNumber = registerNumber;
        return this;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public Child firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Child lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getComment() {
        return comment;
    }

    public Child comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public Child timeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
        return this;
    }

    public Child addTimeTable(TimeTable timeTable) {
        this.timeTables.add(timeTable);
        timeTable.setChild(this);
        return this;
    }

    public Child removeTimeTable(TimeTable timeTable) {
        this.timeTables.remove(timeTable);
        timeTable.setChild(null);
        return this;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Child child = (Child) o;
        if (child.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), child.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Child{" +
            "id=" + getId() +
            ", registerNumber='" + getRegisterNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
