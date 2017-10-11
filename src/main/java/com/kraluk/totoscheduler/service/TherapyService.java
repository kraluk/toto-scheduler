package com.kraluk.totoscheduler.service;

import com.kraluk.totoscheduler.domain.Therapy;
import com.kraluk.totoscheduler.repository.TherapyRepository;
import com.kraluk.totoscheduler.repository.search.TherapySearchRepository;
import com.kraluk.totoscheduler.service.dto.TherapyDTO;
import com.kraluk.totoscheduler.service.mapper.TherapyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Therapy.
 */
@Service
@Transactional
public class TherapyService {

    private final Logger log = LoggerFactory.getLogger(TherapyService.class);

    private final TherapyRepository therapyRepository;

    private final TherapyMapper therapyMapper;

    private final TherapySearchRepository therapySearchRepository;

    public TherapyService(TherapyRepository therapyRepository, TherapyMapper therapyMapper, TherapySearchRepository therapySearchRepository) {
        this.therapyRepository = therapyRepository;
        this.therapyMapper = therapyMapper;
        this.therapySearchRepository = therapySearchRepository;
    }

    /**
     * Save a therapy.
     *
     * @param therapyDTO the entity to save
     * @return the persisted entity
     */
    public TherapyDTO save(TherapyDTO therapyDTO) {
        log.debug("Request to save Therapy : {}", therapyDTO);
        Therapy therapy = therapyMapper.toEntity(therapyDTO);
        therapy = therapyRepository.save(therapy);
        TherapyDTO result = therapyMapper.toDto(therapy);
        therapySearchRepository.save(therapy);
        return result;
    }

    /**
     *  Get all the therapies.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TherapyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Therapies");
        return therapyRepository.findAll(pageable)
            .map(therapyMapper::toDto);
    }

    /**
     *  Get one therapy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TherapyDTO findOne(Long id) {
        log.debug("Request to get Therapy : {}", id);
        Therapy therapy = therapyRepository.findOne(id);
        return therapyMapper.toDto(therapy);
    }

    /**
     *  Delete the  therapy by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Therapy : {}", id);
        therapyRepository.delete(id);
        therapySearchRepository.delete(id);
    }

    /**
     * Search for the therapy corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TherapyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Therapies for query {}", query);
        Page<Therapy> result = therapySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(therapyMapper::toDto);
    }
}
