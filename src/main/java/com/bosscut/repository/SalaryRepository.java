package com.bosscut.repository;

import com.bosscut.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Salary} entity.
 */
@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

}
