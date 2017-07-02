package com.kraluk.totoscheduler.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kraluk.totoscheduler.domain.Period;
import com.kraluk.totoscheduler.repository.PeriodRepository;
import com.kraluk.totoscheduler.service.dto.PeriodDto;
import com.kraluk.totoscheduler.service.mapper.PeriodMapper;
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
 * REST controller for managing Period.
 */
@RestController
@RequestMapping("/api")
public class PeriodResource {

    private static final Logger log = LoggerFactory.getLogger(PeriodResource.class);

    private static final String ENTITY_NAME = "period";

    private final PeriodRepository periodRepository;

    private final PeriodMapper periodMapper;

    public PeriodResource(PeriodRepository periodRepository, PeriodMapper periodMapper) {
        this.periodRepository = periodRepository;
        this.periodMapper = periodMapper;
    }

    /**
     * POST  /periods : Create a new period.
     *
     * @param periodDto the periodDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodDto, or with status 400 (Bad Request) if the period has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/periods")
    @Timed
    public ResponseEntity<PeriodDto> createPeriod(@Valid @RequestBody PeriodDto periodDto)
        throws URISyntaxException {
        log.debug("REST request to save Period : {}", periodDto);
        if (periodDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(ENTITY_NAME, "idexists",
                    "A new period cannot already have an ID")).body(null);
        }
        Period period = periodMapper.toEntity(periodDto);
        period = periodRepository.save(period);
        PeriodDto result = periodMapper.toDto(period);
        return ResponseEntity.created(new URI("/api/periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /periods : Updates an existing period.
     *
     * @param periodDto the periodDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated periodDto,
     * or with status 400 (Bad Request) if the periodDto is not valid,
     * or with status 500 (Internal Server Error) if the periodDto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/periods")
    @Timed
    public ResponseEntity<PeriodDto> updatePeriod(@Valid @RequestBody PeriodDto periodDto)
        throws URISyntaxException {
        log.debug("REST request to update Period : {}", periodDto);
        if (periodDto.getId() == null) {
            return createPeriod(periodDto);
        }
        Period period = periodMapper.toEntity(periodDto);
        period = periodRepository.save(period);
        PeriodDto result = periodMapper.toDto(period);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodDto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /periods : get all the periods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of periods in body
     */
    @GetMapping("/periods")
    @Timed
    public ResponseEntity<List<PeriodDto>> getAllPeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Periods");
        Page<Period> page = periodRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/periods");
        return new ResponseEntity<>(periodMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /periods/:id : get the "id" period.
     *
     * @param id the id of the periodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodDTO, or with status 404 (Not Found)
     */
    @GetMapping("/periods/{id}")
    @Timed
    public ResponseEntity<PeriodDto> getPeriod(@PathVariable Long id) {
        log.debug("REST request to get Period : {}", id);
        Period period = periodRepository.findOne(id);
        PeriodDto periodDto = periodMapper.toDto(period);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(periodDto));
    }

    /**
     * DELETE  /periods/:id : delete the "id" period.
     *
     * @param id the id of the periodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/periods/{id}")
    @Timed
    public ResponseEntity<Void> deletePeriod(@PathVariable Long id) {
        log.debug("REST request to delete Period : {}", id);
        periodRepository.delete(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
