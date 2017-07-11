package com.kraluk.totoscheduler.service;

import com.kraluk.totoscheduler.domain.TimeTable;
import com.kraluk.totoscheduler.repository.TimeTableRepository;
import com.kraluk.totoscheduler.service.dto.TimeTableDto;
import com.kraluk.totoscheduler.service.mapper.TimeTableMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing TimeTable.
 */
@Service
@Transactional
public class TimeTableService {
    private static final Logger log = LoggerFactory.getLogger(TimeTableService.class);

    private final TimeTableRepository timeTableRepository;

    private final TimeTableMapper timeTableMapper;

    public TimeTableService(TimeTableRepository timeTableRepository,
                            TimeTableMapper timeTableMapper) {
        this.timeTableRepository = timeTableRepository;
        this.timeTableMapper = timeTableMapper;
    }

    /**
     * Save a timeTable.
     *
     * @param timeTableDto the entity to save
     * @return the persisted entity
     */
    public TimeTableDto save(TimeTableDto timeTableDto) {
        log.debug("Request to save TimeTable : {}", timeTableDto);
        TimeTable timeTable = timeTableMapper.toEntity(timeTableDto);
        timeTable = timeTableRepository.save(timeTable);
        return timeTableMapper.toDto(timeTable);
    }

    /**
     * Get all the timeTables.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TimeTableDto> findAll(Pageable pageable) {
        log.debug("Request to get all TimeTables");
        return timeTableRepository.findAll(pageable)
            .map(timeTableMapper::toDto);
    }

    /**
     * Get one timeTable by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TimeTableDto findOne(Long id) {
        log.debug("Request to get TimeTable : {}", id);
        TimeTable timeTable = timeTableRepository.findOne(id);
        return timeTableMapper.toDto(timeTable);
    }

    /**
     * Delete the  timeTable by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeTable : {}", id);
        timeTableRepository.delete(id);
    }
}
