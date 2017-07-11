package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.Child;
import com.kraluk.totoscheduler.repository.ChildRepository;
import com.kraluk.totoscheduler.service.dto.ChildDto;
import com.kraluk.totoscheduler.service.mapper.ChildMapper;
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
 * REST controller for managing Child.
 */
@RestController
@RequestMapping("/api")
public class ChildResource {
    private static final Logger log = LoggerFactory.getLogger(ChildResource.class);

    private static final String ENTITY_NAME = "child";

    private final ChildRepository childRepository;

    private final ChildMapper childMapper;

    public ChildResource(ChildRepository childRepository, ChildMapper childMapper) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
    }

    /**
     * POST  /children : Create a new child.
     *
     * @param childDto the childDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new childDto, or with status 400 (Bad Request) if the child has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/children")
    @Timed
    public ResponseEntity<ChildDto> createChild(@Valid @RequestBody ChildDto childDto)
        throws URISyntaxException {
        log.debug("REST request to save Child : {}", childDto);
        if (childDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new child cannot already have an ID")).body(null);
        }
        Child child = childMapper.toEntity(childDto);
        child = childRepository.save(child);
        ChildDto result = childMapper.toDto(child);
        return ResponseEntity.created(new URI("/api/children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /children : Updates an existing child.
     *
     * @param childDto the childDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated childDto,
     * or with status 400 (Bad Request) if the childDto is not valid,
     * or with status 500 (Internal Server Error) if the childDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/children")
    @Timed
    public ResponseEntity<ChildDto> updateChild(@Valid @RequestBody ChildDto childDto)
        throws URISyntaxException {
        log.debug("REST request to update Child : {}", childDto);
        if (childDto.getId() == null) {
            return createChild(childDto);
        }
        Child child = childMapper.toEntity(childDto);
        child = childRepository.save(child);
        ChildDto result = childMapper.toDto(child);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, childDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /children : get all the children.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of children in body
     */
    @GetMapping("/children")
    @Timed
    public List<ChildDto> getAllChildren() {
        log.debug("REST request to get all Children");
        List<Child> children = childRepository.findAll();
        return childMapper.toDto(children);
    }

    /**
     * GET  /children/:id : get the "id" child.
     *
     * @param id the id of the childDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the childDTO, or with status 404 (Not Found)
     */
    @GetMapping("/children/{id}")
    @Timed
    public ResponseEntity<ChildDto> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        Child child = childRepository.findOne(id);
        ChildDto childDto = childMapper.toDto(child);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(childDto));
    }

    /**
     * DELETE  /children/:id : delete the "id" child.
     *
     * @param id the id of the childDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/children/{id}")
    @Timed
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        log.debug("REST request to delete Child : {}", id);
        childRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
