package com.kraluk.totoscheduler.service;

import com.kraluk.totoscheduler.domain.TimeTable;
import com.kraluk.totoscheduler.repository.TimeTableRepository;
import com.kraluk.totoscheduler.repository.search.TimeTableSearchRepository;
import com.kraluk.totoscheduler.service.dto.TimeTableDTO;
import com.kraluk.totoscheduler.service.mapper.TimeTableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TimeTable.
 */
@Service
@Transactional
public class TimeTableService {

    private final Logger log = LoggerFactory.getLogger(TimeTableService.class);

    private final TimeTableRepository timeTableRepository;

    private final TimeTableMapper timeTableMapper;

    private final TimeTableSearchRepository timeTableSearchRepository;

    public TimeTableService(TimeTableRepository timeTableRepository, TimeTableMapper timeTableMapper, TimeTableSearchRepository timeTableSearchRepository) {
        this.timeTableRepository = timeTableRepository;
        this.timeTableMapper = timeTableMapper;
        this.timeTableSearchRepository = timeTableSearchRepository;
    }

    /**
     * Save a timeTable.
     *
     * @param timeTableDTO the entity to save
     * @return the persisted entity
     */
    public TimeTableDTO save(TimeTableDTO timeTableDTO) {
        log.debug("Request to save TimeTable : {}", timeTableDTO);
        TimeTable timeTable = timeTableMapper.toEntity(timeTableDTO);
        timeTable = timeTableRepository.save(timeTable);
        TimeTableDTO result = timeTableMapper.toDto(timeTable);
        timeTableSearchRepository.save(timeTable);
        return result;
    }

    /**
     *  Get all the timeTables.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TimeTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeTables");
        return timeTableRepository.findAll(pageable)
            .map(timeTableMapper::toDto);
    }

    /**
     *  Get one timeTable by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TimeTableDTO findOne(Long id) {
        log.debug("Request to get TimeTable : {}", id);
        TimeTable timeTable = timeTableRepository.findOne(id);
        return timeTableMapper.toDto(timeTable);
    }

    /**
     *  Delete the  timeTable by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeTable : {}", id);
        timeTableRepository.delete(id);
        timeTableSearchRepository.delete(id);
    }

    /**
     * Search for the timeTable corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TimeTableDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TimeTables for query {}", query);
        Page<TimeTable> result = timeTableSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(timeTableMapper::toDto);
    }
}
