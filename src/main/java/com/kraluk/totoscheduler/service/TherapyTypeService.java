package com.kraluk.totoscheduler.service;

import com.kraluk.totoscheduler.domain.TherapyType;
import com.kraluk.totoscheduler.repository.TherapyTypeRepository;
import com.kraluk.totoscheduler.repository.search.TherapyTypeSearchRepository;
import com.kraluk.totoscheduler.service.dto.TherapyTypeDTO;
import com.kraluk.totoscheduler.service.mapper.TherapyTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TherapyType.
 */
@Service
@Transactional
public class TherapyTypeService {

    private final Logger log = LoggerFactory.getLogger(TherapyTypeService.class);

    private final TherapyTypeRepository therapyTypeRepository;

    private final TherapyTypeMapper therapyTypeMapper;

    private final TherapyTypeSearchRepository therapyTypeSearchRepository;

    public TherapyTypeService(TherapyTypeRepository therapyTypeRepository, TherapyTypeMapper therapyTypeMapper, TherapyTypeSearchRepository therapyTypeSearchRepository) {
        this.therapyTypeRepository = therapyTypeRepository;
        this.therapyTypeMapper = therapyTypeMapper;
        this.therapyTypeSearchRepository = therapyTypeSearchRepository;
    }

    /**
     * Save a therapyType.
     *
     * @param therapyTypeDTO the entity to save
     * @return the persisted entity
     */
    public TherapyTypeDTO save(TherapyTypeDTO therapyTypeDTO) {
        log.debug("Request to save TherapyType : {}", therapyTypeDTO);
        TherapyType therapyType = therapyTypeMapper.toEntity(therapyTypeDTO);
        therapyType = therapyTypeRepository.save(therapyType);
        TherapyTypeDTO result = therapyTypeMapper.toDto(therapyType);
        therapyTypeSearchRepository.save(therapyType);
        return result;
    }

    /**
     *  Get all the therapyTypes.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TherapyTypeDTO> findAll() {
        log.debug("Request to get all TherapyTypes");
        return therapyTypeRepository.findAll().stream()
            .map(therapyTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one therapyType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TherapyTypeDTO findOne(Long id) {
        log.debug("Request to get TherapyType : {}", id);
        TherapyType therapyType = therapyTypeRepository.findOne(id);
        return therapyTypeMapper.toDto(therapyType);
    }

    /**
     *  Delete the  therapyType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TherapyType : {}", id);
        therapyTypeRepository.delete(id);
        therapyTypeSearchRepository.delete(id);
    }

    /**
     * Search for the therapyType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TherapyTypeDTO> search(String query) {
        log.debug("Request to search TherapyTypes for query {}", query);
        return StreamSupport
            .stream(therapyTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(therapyTypeMapper::toDto)
            .collect(Collectors.toList());
    }
}
