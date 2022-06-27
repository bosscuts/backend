package com.bosscut.repository;

import com.bosscut.entity.SalaryCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link SalaryCash} entity.
 */
@Repository
public interface SalaryCashRepository extends JpaRepository<SalaryCash, Long> {

}
