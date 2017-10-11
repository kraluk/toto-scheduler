package com.kraluk.totoscheduler.repository.search;

import com.kraluk.totoscheduler.domain.TherapyType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TherapyType entity.
 */
public interface TherapyTypeSearchRepository extends ElasticsearchRepository<TherapyType, Long> {
}
