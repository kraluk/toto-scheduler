package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Therapy;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Therapy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Long> {

}
