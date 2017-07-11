package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.TherapyType;
import com.kraluk.totoscheduler.repository.TherapyTypeRepository;
import com.kraluk.totoscheduler.service.dto.TherapyTypeDto;
import com.kraluk.totoscheduler.service.mapper.TherapyTypeMapper;
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
 * REST controller for managing TherapyType.
 */
@RestController
@RequestMapping("/api")
public class TherapyTypeResource {
    private static final Logger log = LoggerFactory.getLogger(TherapyTypeResource.class);

    private static final String ENTITY_NAME = "therapyType";

    private final TherapyTypeRepository therapyTypeRepository;

    private final TherapyTypeMapper therapyTypeMapper;

    public TherapyTypeResource(TherapyTypeRepository therapyTypeRepository,
                               TherapyTypeMapper therapyTypeMapper) {
        this.therapyTypeRepository = therapyTypeRepository;
        this.therapyTypeMapper = therapyTypeMapper;
    }

    /**
     * POST  /therapy-types : Create a new therapyType.
     *
     * @param therapyTypeDto the therapyTypeDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapyTypeDto, or with status 400 (Bad Request) if the therapyType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapy-types")
    @Timed
    public ResponseEntity<TherapyTypeDto> createTherapyType(
        @Valid @RequestBody TherapyTypeDto therapyTypeDto) throws URISyntaxException {
        log.debug("REST request to save TherapyType : {}", therapyTypeDto);
        if (therapyTypeDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new therapyType cannot already have an ID")).body(null);
        }
        TherapyType therapyType = therapyTypeMapper.toEntity(therapyTypeDto);
        therapyType = therapyTypeRepository.save(therapyType);
        TherapyTypeDto result = therapyTypeMapper.toDto(therapyType);
        return ResponseEntity.created(new URI("/api/therapy-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapy-types : Updates an existing therapyType.
     *
     * @param therapyTypeDto the therapyTypeDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapyTypeDto,
     * or with status 400 (Bad Request) if the therapyTypeDto is not valid,
     * or with status 500 (Internal Server Error) if the therapyTypeDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapy-types")
    @Timed
    public ResponseEntity<TherapyTypeDto> updateTherapyType(
        @Valid @RequestBody TherapyTypeDto therapyTypeDto) throws URISyntaxException {
        log.debug("REST request to update TherapyType : {}", therapyTypeDto);
        if (therapyTypeDto.getId() == null) {
            return createTherapyType(therapyTypeDto);
        }
        TherapyType therapyType = therapyTypeMapper.toEntity(therapyTypeDto);
        therapyType = therapyTypeRepository.save(therapyType);
        TherapyTypeDto result = therapyTypeMapper.toDto(therapyType);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapyTypeDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapy-types : get all the therapyTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of therapyTypes in body
     */
    @GetMapping("/therapy-types")
    @Timed
    public List<TherapyTypeDto> getAllTherapyTypes() {
        log.debug("REST request to get all TherapyTypes");
        List<TherapyType> therapyTypes = therapyTypeRepository.findAll();
        return therapyTypeMapper.toDto(therapyTypes);
    }

    /**
     * GET  /therapy-types/:id : get the "id" therapyType.
     *
     * @param id the id of the therapyTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the therapyTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/therapy-types/{id}")
    @Timed
    public ResponseEntity<TherapyTypeDto> getTherapyType(@PathVariable Long id) {
        log.debug("REST request to get TherapyType : {}", id);
        TherapyType therapyType = therapyTypeRepository.findOne(id);
        TherapyTypeDto therapyTypeDto = therapyTypeMapper.toDto(therapyType);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapyTypeDto));
    }

    /**
     * DELETE  /therapy-types/:id : delete the "id" therapyType.
     *
     * @param id the id of the therapyTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/therapy-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteTherapyType(@PathVariable Long id) {
        log.debug("REST request to delete TherapyType : {}", id);
        therapyTypeRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
