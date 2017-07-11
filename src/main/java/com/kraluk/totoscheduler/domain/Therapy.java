package com.kraluk.totoscheduler.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * A Therapy.
 */
@Entity
@Table(name = "therapy")
public class Therapy extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @Column(name = "jhi_comment")
    private String comment;

    @OneToOne
    @JoinColumn(unique = true)
    private TherapyType therapyType;

    @ManyToOne
    private Therapist therapist;

    @ManyToOne
    private TimeTable timeTable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TherapyType getTherapyType() {
        return therapyType;
    }

    public Therapy therapyType(TherapyType therapyType) {
        this.therapyType = therapyType;
        return this;
    }

    public void setTherapyType(TherapyType therapyType) {
        this.therapyType = therapyType;
    }

    public Therapist getTherapist() {
        return therapist;
    }

    public Therapy therapist(Therapist therapist) {
        this.therapist = therapist;
        return this;
    }

    public void setTherapist(Therapist therapist) {
        this.therapist = therapist;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public Therapy timeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
        return this;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
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
            ", date='" + getDate() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
