package com.bosscut.repository;

import com.bosscut.entity.CrawlUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link CrawlUrl} entity.
 */
@Repository
public interface CrawlRepository extends JpaRepository<CrawlUrl, Long> {

}
