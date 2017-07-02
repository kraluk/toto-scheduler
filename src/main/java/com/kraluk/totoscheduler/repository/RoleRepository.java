package com.kraluk.totoscheduler.repository;

import com.kraluk.totoscheduler.domain.Role;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("select role from Role role where role.user.login = ?#{principal.username}")
    List<Role> findByUserIsCurrentUser();
    
}
