package com.kraluk.totoscheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Child.
 */
@Entity
@Table(name = "child")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
    @Size(min = 5)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_comment")
    private String comment;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Therapy> therapies = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public Child name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<Therapy> getTherapies() {
        return therapies;
    }

    public Child therapies(Set<Therapy> therapies) {
        this.therapies = therapies;
        return this;
    }

    public Child addTherapy(Therapy therapy) {
        this.therapies.add(therapy);
        therapy.setChild(this);
        return this;
    }

    public Child removeTherapy(Therapy therapy) {
        this.therapies.remove(therapy);
        therapy.setChild(null);
        return this;
    }

    public void setTherapies(Set<Therapy> therapies) {
        this.therapies = therapies;
    }

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
            ", name='" + getName() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
