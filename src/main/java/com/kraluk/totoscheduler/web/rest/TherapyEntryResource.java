package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.TherapyEntry;

import com.kraluk.totoscheduler.repository.TherapyEntryRepository;
import com.kraluk.totoscheduler.web.rest.util.HeaderUtil;
import com.kraluk.totoscheduler.service.dto.TherapyEntryDTO;
import com.kraluk.totoscheduler.service.mapper.TherapyEntryMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

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

    public TherapyEntryResource(TherapyEntryRepository therapyEntryRepository, TherapyEntryMapper therapyEntryMapper) {
        this.therapyEntryRepository = therapyEntryRepository;
        this.therapyEntryMapper = therapyEntryMapper;
    }

    /**
     * POST  /therapy-entries : Create a new therapyEntry.
     *
     * @param therapyEntryDTO the therapyEntryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapyEntryDTO, or with status 400 (Bad Request) if the therapyEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapy-entries")
    @Timed
    public ResponseEntity<TherapyEntryDTO> createTherapyEntry(@Valid @RequestBody TherapyEntryDTO therapyEntryDTO) throws URISyntaxException {
        log.debug("REST request to save TherapyEntry : {}", therapyEntryDTO);
        if (therapyEntryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new therapyEntry cannot already have an ID")).body(null);
        }
        TherapyEntry therapyEntry = therapyEntryMapper.toEntity(therapyEntryDTO);
        therapyEntry = therapyEntryRepository.save(therapyEntry);
        TherapyEntryDTO result = therapyEntryMapper.toDto(therapyEntry);
        return ResponseEntity.created(new URI("/api/therapy-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapy-entries : Updates an existing therapyEntry.
     *
     * @param therapyEntryDTO the therapyEntryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapyEntryDTO,
     * or with status 400 (Bad Request) if the therapyEntryDTO is not valid,
     * or with status 500 (Internal Server Error) if the therapyEntryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapy-entries")
    @Timed
    public ResponseEntity<TherapyEntryDTO> updateTherapyEntry(@Valid @RequestBody TherapyEntryDTO therapyEntryDTO) throws URISyntaxException {
        log.debug("REST request to update TherapyEntry : {}", therapyEntryDTO);
        if (therapyEntryDTO.getId() == null) {
            return createTherapyEntry(therapyEntryDTO);
        }
        TherapyEntry therapyEntry = therapyEntryMapper.toEntity(therapyEntryDTO);
        therapyEntry = therapyEntryRepository.save(therapyEntry);
        TherapyEntryDTO result = therapyEntryMapper.toDto(therapyEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapyEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapy-entries : get all the therapyEntries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of therapyEntries in body
     */
    @GetMapping("/therapy-entries")
    @Timed
    public List<TherapyEntryDTO> getAllTherapyEntries() {
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
    public ResponseEntity<TherapyEntryDTO> getTherapyEntry(@PathVariable Long id) {
        log.debug("REST request to get TherapyEntry : {}", id);
        TherapyEntry therapyEntry = therapyEntryRepository.findOne(id);
        TherapyEntryDTO therapyEntryDTO = therapyEntryMapper.toDto(therapyEntry);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapyEntryDTO));
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
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
