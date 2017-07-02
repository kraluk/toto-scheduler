package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.Therapy;
import com.kraluk.totoscheduler.repository.TherapyRepository;
import com.kraluk.totoscheduler.service.dto.TherapyDto;
import com.kraluk.totoscheduler.service.mapper.TherapyMapper;
import com.kraluk.totoscheduler.web.rest.util.HeaderUtil;
import com.kraluk.totoscheduler.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Therapy.
 */
@RestController
@RequestMapping("/api")
public class TherapyResource {

    private static final Logger log = LoggerFactory.getLogger(TherapyResource.class);

    private static final String ENTITY_NAME = "therapy";

    private final TherapyRepository therapyRepository;

    private final TherapyMapper therapyMapper;

    public TherapyResource(TherapyRepository therapyRepository, TherapyMapper therapyMapper) {
        this.therapyRepository = therapyRepository;
        this.therapyMapper = therapyMapper;
    }

    /**
     * POST  /therapies : Create a new therapy.
     *
     * @param therapyDto the therapyDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapyDto, or with status 400 (Bad Request) if the therapy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapies")
    @Timed
    public ResponseEntity<TherapyDto> createTherapy(@Valid @RequestBody TherapyDto therapyDto)
        throws URISyntaxException {
        log.debug("REST request to save Therapy : {}", therapyDto);
        if (therapyDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new therapy cannot already have an ID")).body(null);
        }
        Therapy therapy = therapyMapper.toEntity(therapyDto);
        therapy = therapyRepository.save(therapy);
        TherapyDto result = therapyMapper.toDto(therapy);
        return ResponseEntity.created(new URI("/api/therapies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapies : Updates an existing therapy.
     *
     * @param therapyDto the therapyDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapyDto,
     * or with status 400 (Bad Request) if the therapyDto is not valid,
     * or with status 500 (Internal Server Error) if the therapyDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapies")
    @Timed
    public ResponseEntity<TherapyDto> updateTherapy(@Valid @RequestBody TherapyDto therapyDto)
        throws URISyntaxException {
        log.debug("REST request to update Therapy : {}", therapyDto);
        if (therapyDto.getId() == null) {
            return createTherapy(therapyDto);
        }
        Therapy therapy = therapyMapper.toEntity(therapyDto);
        therapy = therapyRepository.save(therapy);
        TherapyDto result = therapyMapper.toDto(therapy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapyDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapies : get all the therapies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of therapies in body
     */
    @GetMapping("/therapies")
    @Timed
    public ResponseEntity<List<TherapyDto>> getAllTherapies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Therapies");
        Page<Therapy> page = therapyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/therapies");
        return new ResponseEntity<>(therapyMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /therapies/:id : get the "id" therapy.
     *
     * @param id the id of the therapyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the therapyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/therapies/{id}")
    @Timed
    public ResponseEntity<TherapyDto> getTherapy(@PathVariable Long id) {
        log.debug("REST request to get Therapy : {}", id);
        Therapy therapy = therapyRepository.findOne(id);
        TherapyDto therapyDto = therapyMapper.toDto(therapy);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapyDto));
    }

    /**
     * DELETE  /therapies/:id : delete the "id" therapy.
     *
     * @param id the id of the therapyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/therapies/{id}")
    @Timed
    public ResponseEntity<Void> deleteTherapy(@PathVariable Long id) {
        log.debug("REST request to delete Therapy : {}", id);
        therapyRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
