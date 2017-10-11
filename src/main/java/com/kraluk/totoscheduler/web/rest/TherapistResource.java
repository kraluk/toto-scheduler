package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.service.TherapistService;
import com.kraluk.totoscheduler.web.rest.util.HeaderUtil;
import com.kraluk.totoscheduler.service.dto.TherapistDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Therapist.
 */
@RestController
@RequestMapping("/api")
public class TherapistResource {

    private final Logger log = LoggerFactory.getLogger(TherapistResource.class);

    private static final String ENTITY_NAME = "therapist";

    private final TherapistService therapistService;

    public TherapistResource(TherapistService therapistService) {
        this.therapistService = therapistService;
    }

    /**
     * POST  /therapists : Create a new therapist.
     *
     * @param therapistDTO the therapistDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new therapistDTO, or with status 400 (Bad Request) if the therapist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/therapists")
    @Timed
    public ResponseEntity<TherapistDTO> createTherapist(@RequestBody TherapistDTO therapistDTO) throws URISyntaxException {
        log.debug("REST request to save Therapist : {}", therapistDTO);
        if (therapistDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new therapist cannot already have an ID")).body(null);
        }
        TherapistDTO result = therapistService.save(therapistDTO);
        return ResponseEntity.created(new URI("/api/therapists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /therapists : Updates an existing therapist.
     *
     * @param therapistDTO the therapistDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated therapistDTO,
     * or with status 400 (Bad Request) if the therapistDTO is not valid,
     * or with status 500 (Internal Server Error) if the therapistDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/therapists")
    @Timed
    public ResponseEntity<TherapistDTO> updateTherapist(@RequestBody TherapistDTO therapistDTO) throws URISyntaxException {
        log.debug("REST request to update Therapist : {}", therapistDTO);
        if (therapistDTO.getId() == null) {
            return createTherapist(therapistDTO);
        }
        TherapistDTO result = therapistService.save(therapistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, therapistDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /therapists : get all the therapists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of therapists in body
     */
    @GetMapping("/therapists")
    @Timed
    public List<TherapistDTO> getAllTherapists() {
        log.debug("REST request to get all Therapists");
        return therapistService.findAll();
        }

    /**
     * GET  /therapists/:id : get the "id" therapist.
     *
     * @param id the id of the therapistDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the therapistDTO, or with status 404 (Not Found)
     */
    @GetMapping("/therapists/{id}")
    @Timed
    public ResponseEntity<TherapistDTO> getTherapist(@PathVariable Long id) {
        log.debug("REST request to get Therapist : {}", id);
        TherapistDTO therapistDTO = therapistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(therapistDTO));
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
        therapistService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/therapists?query=:query : search for the therapist corresponding
     * to the query.
     *
     * @param query the query of the therapist search
     * @return the result of the search
     */
    @GetMapping("/_search/therapists")
    @Timed
    public List<TherapistDTO> searchTherapists(@RequestParam String query) {
        log.debug("REST request to search Therapists for query {}", query);
        return therapistService.search(query);
    }

}
