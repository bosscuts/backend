package com.bosscut.repository;

import com.bosscut.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Category} entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
