package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.TherapyEntry;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TherapyEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TherapyEntryRepository extends JpaRepository<TherapyEntry,Long> {
    
}
