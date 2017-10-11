package com.kraluk.totoscheduler.service;

import com.kraluk.totoscheduler.domain.Child;
import com.kraluk.totoscheduler.repository.ChildRepository;
import com.kraluk.totoscheduler.repository.search.ChildSearchRepository;
import com.kraluk.totoscheduler.service.dto.ChildDTO;
import com.kraluk.totoscheduler.service.mapper.ChildMapper;
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
 * Service Implementation for managing Child.
 */
@Service
@Transactional
public class ChildService {

    private final Logger log = LoggerFactory.getLogger(ChildService.class);

    private final ChildRepository childRepository;

    private final ChildMapper childMapper;

    private final ChildSearchRepository childSearchRepository;

    public ChildService(ChildRepository childRepository, ChildMapper childMapper, ChildSearchRepository childSearchRepository) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
        this.childSearchRepository = childSearchRepository;
    }

    /**
     * Save a child.
     *
     * @param childDTO the entity to save
     * @return the persisted entity
     */
    public ChildDTO save(ChildDTO childDTO) {
        log.debug("Request to save Child : {}", childDTO);
        Child child = childMapper.toEntity(childDTO);
        child = childRepository.save(child);
        ChildDTO result = childMapper.toDto(child);
        childSearchRepository.save(child);
        return result;
    }

    /**
     *  Get all the children.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ChildDTO> findAll() {
        log.debug("Request to get all Children");
        return childRepository.findAll().stream()
            .map(childMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one child by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ChildDTO findOne(Long id) {
        log.debug("Request to get Child : {}", id);
        Child child = childRepository.findOne(id);
        return childMapper.toDto(child);
    }

    /**
     *  Delete the  child by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Child : {}", id);
        childRepository.delete(id);
        childSearchRepository.delete(id);
    }

    /**
     * Search for the child corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ChildDTO> search(String query) {
        log.debug("Request to search Children for query {}", query);
        return StreamSupport
            .stream(childSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(childMapper::toDto)
            .collect(Collectors.toList());
    }
}
