package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.service.TimeTableService;
import com.kraluk.totoscheduler.service.dto.TimeTableDto;
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
 * REST controller for managing TimeTable.
 */
@RestController
@RequestMapping("/api")
public class TimeTableResource {
    private static final Logger log = LoggerFactory.getLogger(TimeTableResource.class);

    private static final String ENTITY_NAME = "timeTable";

    private final TimeTableService timeTableService;

    public TimeTableResource(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }

    /**
     * POST  /time-tables : Create a new timeTable.
     *
     * @param timeTableDto the timeTableDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeTableDto, or with status 400 (Bad Request) if the timeTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-tables")
    @Timed
    public ResponseEntity<TimeTableDto> createTimeTable(
        @Valid @RequestBody TimeTableDto timeTableDto) throws URISyntaxException {
        log.debug("REST request to save TimeTable : {}", timeTableDto);
        if (timeTableDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new timeTable cannot already have an ID")).body(null);
        }
        TimeTableDto result = timeTableService.save(timeTableDto);
        return ResponseEntity.created(new URI("/api/time-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-tables : Updates an existing timeTable.
     *
     * @param timeTableDto the timeTableDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeTableDto,
     * or with status 400 (Bad Request) if the timeTableDto is not valid,
     * or with status 500 (Internal Server Error) if the timeTableDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-tables")
    @Timed
    public ResponseEntity<TimeTableDto> updateTimeTable(
        @Valid @RequestBody TimeTableDto timeTableDto) throws URISyntaxException {
        log.debug("REST request to update TimeTable : {}", timeTableDto);
        if (timeTableDto.getId() == null) {
            return createTimeTable(timeTableDto);
        }
        TimeTableDto result = timeTableService.save(timeTableDto);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeTableDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-tables : get all the timeTables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of timeTables in body
     */
    @GetMapping("/time-tables")
    @Timed
    public ResponseEntity<List<TimeTableDto>> getAllTimeTables(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TimeTables");
        Page<TimeTableDto> page = timeTableService.findAll(pageable);
        HttpHeaders
            headers =
            PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /time-tables/:id : get the "id" timeTable.
     *
     * @param id the id of the timeTableDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeTableDTO, or with status 404 (Not Found)
     */
    @GetMapping("/time-tables/{id}")
    @Timed
    public ResponseEntity<TimeTableDto> getTimeTable(@PathVariable Long id) {
        log.debug("REST request to get TimeTable : {}", id);
        TimeTableDto timeTableDto = timeTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timeTableDto));
    }

    /**
     * DELETE  /time-tables/:id : delete the "id" timeTable.
     *
     * @param id the id of the timeTableDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id) {
        log.debug("REST request to delete TimeTable : {}", id);
        timeTableService.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
