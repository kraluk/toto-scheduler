package com.kraluk.totoscheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Therapist.
 */
@Entity
@Table(name = "therapist")
@Document(indexName = "therapist")
public class Therapist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "jhi_comment")
    private String comment;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "therapist")
    @JsonIgnore
    private Set<Therapy> therapies = new HashSet<>();

    @ManyToMany(mappedBy = "therapists")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Therapist title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public Therapist comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public Therapist user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Therapy> getTherapies() {
        return therapies;
    }

    public Therapist therapies(Set<Therapy> therapies) {
        this.therapies = therapies;
        return this;
    }

    public Therapist addTherapy(Therapy therapy) {
        this.therapies.add(therapy);
        therapy.setTherapist(this);
        return this;
    }

    public Therapist removeTherapy(Therapy therapy) {
        this.therapies.remove(therapy);
        therapy.setTherapist(null);
        return this;
    }

    public void setTherapies(Set<Therapy> therapies) {
        this.therapies = therapies;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Therapist roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Therapist addRole(Role role) {
        this.roles.add(role);
        role.getTherapists().add(this);
        return this;
    }

    public Therapist removeRole(Role role) {
        this.roles.remove(role);
        role.getTherapists().remove(this);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
        Therapist therapist = (Therapist) o;
        if (therapist.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), therapist.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Therapist{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
