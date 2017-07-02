package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.TherapyEntry;
import com.kraluk.totoscheduler.repository.TherapyEntryRepository;
import com.kraluk.totoscheduler.service.dto.TherapyEntryDto;
import com.kraluk.totoscheduler.service.mapper.TherapyEntryMapper;
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
 * REST controller for managing TherapyEntry.
 */
@RestController
@RequestMapping("/api")
public class TherapyEntryResource {

    private final Logger log = LoggerFactory.getLogger(TherapyEntryResource.class);

    private static final String ENTITY_NAME = "therapyEntry";

    private final TherapyEntryRepository therapyEntryRepository;

    private final TherapyEntryMapper therapyEntryMapper;

    public TherapyEntryResource(TherapyEntryRepository therapyEntryRepository,
                                TherapyEntryMapper therapyEntryMapper) {
        this.therapyEntryRepository = therapyEntryRepository;
        this.therapyEntryMapper = therapyEntryMapper;
    }

    /**
     * POST  /therapy-entries : Create a new therapyEntry.
     *
     * @param therapyEntryDto the therapyEntryDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapyEntryDto, or with status 400 (Bad Request) if the therapyEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapy-entries")
    @Timed
    public ResponseEntity<TherapyEntryDto> createTherapyEntry(
        @Valid @RequestBody TherapyEntryDto therapyEntryDto) throws URISyntaxException {
        log.debug("REST request to save TherapyEntry : {}", therapyEntryDto);
        if (therapyEntryDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new therapyEntry cannot already have an ID")).body(null);
        }
        TherapyEntry therapyEntry = therapyEntryMapper.toEntity(therapyEntryDto);
        therapyEntry = therapyEntryRepository.save(therapyEntry);
        TherapyEntryDto result = therapyEntryMapper.toDto(therapyEntry);
        return ResponseEntity.created(new URI("/api/therapy-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapy-entries : Updates an existing therapyEntry.
     *
     * @param therapyEntryDto the therapyEntryDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapyEntryDto,
     * or with status 400 (Bad Request) if the therapyEntryDto is not valid,
     * or with status 500 (Internal Server Error) if the therapyEntryDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapy-entries")
    @Timed
    public ResponseEntity<TherapyEntryDto> updateTherapyEntry(
        @Valid @RequestBody TherapyEntryDto therapyEntryDto) throws URISyntaxException {
        log.debug("REST request to update TherapyEntry : {}", therapyEntryDto);
        if (therapyEntryDto.getId() == null) {
            return createTherapyEntry(therapyEntryDto);
        }
        TherapyEntry therapyEntry = therapyEntryMapper.toEntity(therapyEntryDto);
        therapyEntry = therapyEntryRepository.save(therapyEntry);
        TherapyEntryDto result = therapyEntryMapper.toDto(therapyEntry);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapyEntryDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapy-entries : get all the therapyEntries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of therapyEntries in body
     */
    @GetMapping("/therapy-entries")
    @Timed
    public List<TherapyEntryDto> getAllTherapyEntries() {
        log.debug("REST request to get all TherapyEntries");
        List<TherapyEntry> therapyEntries = therapyEntryRepository.findAll();
        return therapyEntryMapper.toDto(therapyEntries);
    }

    /**
     * GET  /therapy-entries/:id : get the "id" therapyEntry.
     *
     * @param id the id of the therapyEntryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the therapyEntryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/therapy-entries/{id}")
    @Timed
    public ResponseEntity<TherapyEntryDto> getTherapyEntry(@PathVariable Long id) {
        log.debug("REST request to get TherapyEntry : {}", id);
        TherapyEntry therapyEntry = therapyEntryRepository.findOne(id);
        TherapyEntryDto therapyEntryDto = therapyEntryMapper.toDto(therapyEntry);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapyEntryDto));
    }

    /**
     * DELETE  /therapy-entries/:id : delete the "id" therapyEntry.
     *
     * @param id the id of the therapyEntryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/therapy-entries/{id}")
    @Timed
    public ResponseEntity<Void> deleteTherapyEntry(@PathVariable Long id) {
        log.debug("REST request to delete TherapyEntry : {}", id);
        therapyEntryRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
