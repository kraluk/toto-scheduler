package com.kraluk.totoscheduler.service;

import com.kraluk.totoscheduler.domain.Therapist;
import com.kraluk.totoscheduler.repository.TherapistRepository;
import com.kraluk.totoscheduler.repository.search.TherapistSearchRepository;
import com.kraluk.totoscheduler.service.dto.TherapistDTO;
import com.kraluk.totoscheduler.service.mapper.TherapistMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Therapist.
 */
@Service
@Transactional
public class TherapistService {

    private final Logger log = LoggerFactory.getLogger(TherapistService.class);

    private final TherapistRepository therapistRepository;

    private final TherapistMapper therapistMapper;

    private final TherapistSearchRepository therapistSearchRepository;

    public TherapistService(TherapistRepository therapistRepository,
                            TherapistMapper therapistMapper,
                            TherapistSearchRepository therapistSearchRepository) {
        this.therapistRepository = therapistRepository;
        this.therapistMapper = therapistMapper;
        this.therapistSearchRepository = therapistSearchRepository;
    }

    /**
     * Save a therapist.
     *
     * @param therapistDTO the entity to save
     * @return the persisted entity
     */
    public TherapistDTO save(TherapistDTO therapistDTO) {
        log.debug("Request to save Therapist : {}", therapistDTO);
        Therapist therapist = therapistMapper.toEntity(therapistDTO);
        therapist = therapistRepository.save(therapist);
        TherapistDTO result = therapistMapper.toDto(therapist);
        therapistSearchRepository.save(therapist);
        return result;
    }

    /**
     * Get all the therapists.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TherapistDTO> findAll() {
        log.debug("Request to get all Therapists");
        return therapistRepository.findAll().stream()
            .map(therapistMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one therapist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TherapistDTO findOne(Long id) {
        log.debug("Request to get Therapist : {}", id);
        Therapist therapist = therapistRepository.findOne(id);
        return therapistMapper.toDto(therapist);
    }

    /**
     * Delete the  therapist by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Therapist : {}", id);
        therapistRepository.delete(id);
        therapistSearchRepository.delete(id);
    }

    /**
     * Search for the therapist corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TherapistDTO> search(String query) {
        log.debug("Request to search Therapists for query {}", query);
        return StreamSupport
            .stream(therapistSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(therapistMapper::toDto)
            .collect(Collectors.toList());
    }
}
