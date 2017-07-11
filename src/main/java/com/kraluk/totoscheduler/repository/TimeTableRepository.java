package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.TimeTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the TimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

}
