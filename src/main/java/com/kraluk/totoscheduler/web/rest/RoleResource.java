package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.Role;
import com.kraluk.totoscheduler.repository.RoleRepository;
import com.kraluk.totoscheduler.service.dto.RoleDto;
import com.kraluk.totoscheduler.service.mapper.RoleMapper;
import com.kraluk.totoscheduler.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {
    private static final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleResource(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * POST  /roles : Create a new role.
     *
     * @param roleDto the roleDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roleDto, or with status 400 (Bad Request) if the role has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roles")
    @Timed
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto)
        throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDto);
        if (roleDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new role cannot already have an ID")).body(null);
        }
        Role role = roleMapper.toEntity(roleDto);
        role = roleRepository.save(role);
        RoleDto result = roleMapper.toDto(role);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roles : Updates an existing role.
     *
     * @param roleDto the roleDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roleDto,
     * or with status 400 (Bad Request) if the roleDto is not valid,
     * or with status 500 (Internal Server Error) if the roleDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roles")
    @Timed
    public ResponseEntity<RoleDto> updateRole(@Valid @RequestBody RoleDto roleDto)
        throws URISyntaxException {
        log.debug("REST request to update Role : {}", roleDto);
        if (roleDto.getId() == null) {
            return createRole(roleDto);
        }
        Role role = roleMapper.toEntity(roleDto);
        role = roleRepository.save(role);
        RoleDto result = roleMapper.toDto(role);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, roleDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     */
    @GetMapping("/roles")
    @Timed
    public List<RoleDto> getAllRoles() {
        log.debug("REST request to get all Roles");
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toDto(roles);
    }

    /**
     * GET  /roles/:id : get the "id" role.
     *
     * @param id the id of the roleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/roles/{id}")
    @Timed
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Role role = roleRepository.findOne(id);
        RoleDto roleDto = roleMapper.toDto(role);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(roleDto));
    }

    /**
     * DELETE  /roles/:id : delete the "id" role.
     *
     * @param id the id of the roleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roles/{id}")
    @Timed
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
