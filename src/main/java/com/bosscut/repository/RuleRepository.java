package com.bosscut.repository;

import com.bosscut.entity.Customer;
import com.bosscut.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Rule} entity.
 */
@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

}
