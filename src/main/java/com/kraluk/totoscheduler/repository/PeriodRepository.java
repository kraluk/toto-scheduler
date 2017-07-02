package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Period;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Period entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodRepository extends JpaRepository<Period,Long> {
    
}
