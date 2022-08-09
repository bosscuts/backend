package com.bosscut.repository;

import com.bosscut.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Salary} entity.
 */
@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    Optional<List<Salary>> findAllByStaffIdIn(List<String> staffIds);
}
