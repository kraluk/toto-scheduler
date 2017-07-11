package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.TherapyType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the TherapyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TherapyTypeRepository extends JpaRepository<TherapyType, Long> {

}
