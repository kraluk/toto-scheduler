package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Therapy;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Therapy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TherapyRepository extends JpaRepository<Therapy,Long> {

    @Query("select therapy from Therapy therapy where therapy.user.login = ?#{principal.username}")
    List<Therapy> findByUserIsCurrentUser();
    
}
