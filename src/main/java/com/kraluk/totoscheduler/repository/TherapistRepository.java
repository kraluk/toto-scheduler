package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Therapist;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Therapist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {

}
