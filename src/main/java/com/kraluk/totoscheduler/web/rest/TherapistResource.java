package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.Therapist;
import com.kraluk.totoscheduler.repository.TherapistRepository;
import com.kraluk.totoscheduler.service.dto.TherapistDto;
import com.kraluk.totoscheduler.service.mapper.TherapistMapper;
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

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Therapist.
 */
@RestController
@RequestMapping("/api")
public class TherapistResource {
    private static final Logger log = LoggerFactory.getLogger(TherapistResource.class);

    private static final String ENTITY_NAME = "therapist";

    private final TherapistRepository therapistRepository;

    private final TherapistMapper therapistMapper;

    public TherapistResource(TherapistRepository therapistRepository,
                             TherapistMapper therapistMapper) {
        this.therapistRepository = therapistRepository;
        this.therapistMapper = therapistMapper;
    }

    /**
     * POST  /therapists : Create a new therapist.
     *
     * @param therapistDto the therapistDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapistDto, or with status 400 (Bad Request) if the therapist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapists")
    @Timed
    public ResponseEntity<TherapistDto> createTherapist(@RequestBody TherapistDto therapistDto)
        throws URISyntaxException {
        log.debug("REST request to save Therapist : {}", therapistDto);
        if (therapistDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new therapist cannot already have an ID")).body(null);
        }
        Therapist therapist = therapistMapper.toEntity(therapistDto);
        therapist = therapistRepository.save(therapist);
        TherapistDto result = therapistMapper.toDto(therapist);
        return ResponseEntity.created(new URI("/api/therapists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapists : Updates an existing therapist.
     *
     * @param therapistDto the therapistDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapistDto,
     * or with status 400 (Bad Request) if the therapistDto is not valid,
     * or with status 500 (Internal Server Error) if the therapistDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapists")
    @Timed
    public ResponseEntity<TherapistDto> updateTherapist(@RequestBody TherapistDto therapistDto)
        throws URISyntaxException {
        log.debug("REST request to update Therapist : {}", therapistDto);
        if (therapistDto.getId() == null) {
            return createTherapist(therapistDto);
        }
        Therapist therapist = therapistMapper.toEntity(therapistDto);
        therapist = therapistRepository.save(therapist);
        TherapistDto result = therapistMapper.toDto(therapist);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapistDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapists : get all the therapists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of therapists in body
     */
    @GetMapping("/therapists")
    @Timed
    public List<TherapistDto> getAllTherapists() {
        log.debug("REST request to get all Therapists");
        List<Therapist> therapists = therapistRepository.findAll();
        return therapistMapper.toDto(therapists);
    }

    /**
     * GET  /therapists/:id : get the "id" therapist.
     *
     * @param id the id of the therapistDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the therapistDTO, or with status 404 (Not Found)
     */
    @GetMapping("/therapists/{id}")
    @Timed
    public ResponseEntity<TherapistDto> getTherapist(@PathVariable Long id) {
        log.debug("REST request to get Therapist : {}", id);
        Therapist therapist = therapistRepository.findOne(id);
        TherapistDto therapistDto = therapistMapper.toDto(therapist);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapistDto));
    }

    /**
     * DELETE  /therapists/:id : delete the "id" therapist.
     *
     * @param id the id of the therapistDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/therapists/{id}")
    @Timed
    public ResponseEntity<Void> deleteTherapist(@PathVariable Long id) {
        log.debug("REST request to delete Therapist : {}", id);
        therapistRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
