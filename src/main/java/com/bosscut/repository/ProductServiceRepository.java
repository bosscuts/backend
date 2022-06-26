package com.bosscut.repository;

import com.bosscut.entity.ProductService;
import com.bosscut.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link ProductService} entity.
 */
@Repository
public interface ProductServiceRepository extends JpaRepository<ProductService, Long> {
    Optional<List<ProductService>> findByType(String type);

    @Query("SELECT ps FROM ProductService ps WHERE ps.productServiceId IN (:ids)")
    Optional<List<ProductService>> findAllByIds(List<Long> ids);
}
