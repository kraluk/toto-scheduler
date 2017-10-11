package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.service.ChildService;
import com.kraluk.totoscheduler.web.rest.util.HeaderUtil;
import com.kraluk.totoscheduler.service.dto.ChildDTO;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Child.
 */
@RestController
@RequestMapping("/api")
public class ChildResource {

    private final Logger log = LoggerFactory.getLogger(ChildResource.class);

    private static final String ENTITY_NAME = "child";

    private final ChildService childService;

    public ChildResource(ChildService childService) {
        this.childService = childService;
    }

    /**
     * POST  /children : Create a new child.
     *
     * @param childDTO the childDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new childDTO, or with status 400 (Bad Request) if the child has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/children")
    @Timed
    public ResponseEntity<ChildDTO> createChild(@Valid @RequestBody ChildDTO childDTO) throws URISyntaxException {
        log.debug("REST request to save Child : {}", childDTO);
        if (childDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new child cannot already have an ID")).body(null);
        }
        ChildDTO result = childService.save(childDTO);
        return ResponseEntity.created(new URI("/api/children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /children : Updates an existing child.
     *
     * @param childDTO the childDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated childDTO,
     * or with status 400 (Bad Request) if the childDTO is not valid,
     * or with status 500 (Internal Server Error) if the childDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/children")
    @Timed
    public ResponseEntity<ChildDTO> updateChild(@Valid @RequestBody ChildDTO childDTO) throws URISyntaxException {
        log.debug("REST request to update Child : {}", childDTO);
        if (childDTO.getId() == null) {
            return createChild(childDTO);
        }
        ChildDTO result = childService.save(childDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, childDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /children : get all the children.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of children in body
     */
    @GetMapping("/children")
    @Timed
    public List<ChildDTO> getAllChildren() {
        log.debug("REST request to get all Children");
        return childService.findAll();
        }

    /**
     * GET  /children/:id : get the "id" child.
     *
     * @param id the id of the childDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the childDTO, or with status 404 (Not Found)
     */
    @GetMapping("/children/{id}")
    @Timed
    public ResponseEntity<ChildDTO> getChild(@PathVariable Long id) {
        log.debug("REST request to get Child : {}", id);
        ChildDTO childDTO = childService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(childDTO));
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
        childService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/children?query=:query : search for the child corresponding
     * to the query.
     *
     * @param query the query of the child search
     * @return the result of the search
     */
    @GetMapping("/_search/children")
    @Timed
    public List<ChildDTO> searchChildren(@RequestParam String query) {
        log.debug("REST request to search Children for query {}", query);
        return childService.search(query);
    }

}
