package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.service.TherapyTypeService;
import com.kraluk.totoscheduler.service.dto.TherapyTypeDTO;
import com.kraluk.totoscheduler.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * REST controller for managing TherapyType.
 */
@RestController
@RequestMapping("/api")
public class TherapyTypeResource {

    private final Logger log = LoggerFactory.getLogger(TherapyTypeResource.class);

    private static final String ENTITY_NAME = "therapyType";

    private final TherapyTypeService therapyTypeService;

    public TherapyTypeResource(TherapyTypeService therapyTypeService) {
        this.therapyTypeService = therapyTypeService;
    }

    /**
     * POST  /therapy-types : Create a new therapyType.
     *
     * @param therapyTypeDTO the therapyTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapyTypeDTO, or with status 400 (Bad Request) if the therapyType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapy-types")
    @Timed
    public ResponseEntity<TherapyTypeDTO> createTherapyType(
        @Valid @RequestBody TherapyTypeDTO therapyTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TherapyType : {}", therapyTypeDTO);
        if (therapyTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new therapyType cannot already have an ID")).body(null);
        }
        TherapyTypeDTO result = therapyTypeService.save(therapyTypeDTO);
        return ResponseEntity.created(new URI("/api/therapy-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapy-types : Updates an existing therapyType.
     *
     * @param therapyTypeDTO the therapyTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapyTypeDTO,
     * or with status 400 (Bad Request) if the therapyTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the therapyTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapy-types")
    @Timed
    public ResponseEntity<TherapyTypeDTO> updateTherapyType(
        @Valid @RequestBody TherapyTypeDTO therapyTypeDTO) throws URISyntaxException {
        log.debug("REST request to update TherapyType : {}", therapyTypeDTO);
        if (therapyTypeDTO.getId() == null) {
            return createTherapyType(therapyTypeDTO);
        }
        TherapyTypeDTO result = therapyTypeService.save(therapyTypeDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapyTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapy-types : get all the therapyTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of therapyTypes in body
     */
    @GetMapping("/therapy-types")
    @Timed
    public List<TherapyTypeDTO> getAllTherapyTypes() {
        log.debug("REST request to get all TherapyTypes");
        return therapyTypeService.findAll();
    }

    /**
     * GET  /therapy-types/:id : get the "id" therapyType.
     *
     * @param id the id of the therapyTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the therapyTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/therapy-types/{id}")
    @Timed
    public ResponseEntity<TherapyTypeDTO> getTherapyType(@PathVariable Long id) {
        log.debug("REST request to get TherapyType : {}", id);
        TherapyTypeDTO therapyTypeDTO = therapyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapyTypeDTO));
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
        therapyTypeService.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/therapy-types?query=:query : search for the therapyType corresponding
     * to the query.
     *
     * @param query the query of the therapyType search
     * @return the result of the search
     */
    @GetMapping("/_search/therapy-types")
    @Timed
    public List<TherapyTypeDTO> searchTherapyTypes(@RequestParam String query) {
        log.debug("REST request to search TherapyTypes for query {}", query);
        return therapyTypeService.search(query);
    }

}
