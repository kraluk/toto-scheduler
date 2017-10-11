package com.kraluk.totoscheduler.repository.search;

import com.kraluk.totoscheduler.domain.Child;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Child entity.
 */
public interface ChildSearchRepository extends ElasticsearchRepository<Child, Long> {
}
