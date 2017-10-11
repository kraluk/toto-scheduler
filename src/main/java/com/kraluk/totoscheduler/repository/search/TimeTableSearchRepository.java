package com.kraluk.totoscheduler.repository.search;

import com.kraluk.totoscheduler.domain.TimeTable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TimeTable entity.
 */
public interface TimeTableSearchRepository extends ElasticsearchRepository<TimeTable, Long> {
}
