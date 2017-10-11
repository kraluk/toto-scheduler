package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.TherapyType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TherapyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TherapyTypeRepository extends JpaRepository<TherapyType, Long> {

}
