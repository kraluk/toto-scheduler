package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Role;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select distinct role from Role role left join fetch role.therapists")
    List<Role> findAllWithEagerRelationships();

    @Query("select role from Role role left join fetch role.therapists where role.id =:id")
    Role findOneWithEagerRelationships(@Param("id") Long id);

}
