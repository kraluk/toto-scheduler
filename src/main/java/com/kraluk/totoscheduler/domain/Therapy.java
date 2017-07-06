package com.kraluk.totoscheduler.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Therapy.
 */
@Entity
@Table(name = "therapy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Therapy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_comment", nullable = false)
    private String comment;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @OneToOne
    @JoinColumn(unique = true)
    private TherapyEntry therapyEntry;

    @ManyToOne
    private User user;

    @ManyToOne
    private Period period;

    @ManyToOne
    private Child child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public Therapy comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Therapy date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public TherapyEntry getTherapyEntry() {
        return therapyEntry;
    }

    public Therapy therapyEntry(TherapyEntry therapyEntry) {
        this.therapyEntry = therapyEntry;
        return this;
    }

    public void setTherapyEntry(TherapyEntry therapyEntry) {
        this.therapyEntry = therapyEntry;
    }

    public User getUser() {
        return user;
    }

    public Therapy user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Period getPeriod() {
        return period;
    }

    public Therapy period(Period period) {
        this.period = period;
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Child getChild() {
        return child;
    }

    public Therapy child(Child child) {
        this.child = child;
        return this;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Therapy therapy = (Therapy) o;
        if (therapy.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), therapy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Therapy{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
