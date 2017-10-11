package com.kraluk.totoscheduler.repository.search;

import com.kraluk.totoscheduler.domain.Therapist;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Therapist entity.
 */
public interface TherapistSearchRepository extends ElasticsearchRepository<Therapist, Long> {
}
