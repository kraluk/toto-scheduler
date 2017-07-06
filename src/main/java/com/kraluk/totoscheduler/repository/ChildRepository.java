package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Child;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends JpaRepository<Child,Long> {
    
}
