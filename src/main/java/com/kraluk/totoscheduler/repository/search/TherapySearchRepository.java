package com.kraluk.totoscheduler.repository.search;

import com.kraluk.totoscheduler.domain.Therapy;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Therapy entity.
 */
public interface TherapySearchRepository extends ElasticsearchRepository<Therapy, Long> {
}
