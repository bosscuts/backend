package com.bosscut.repository;

import com.bosscut.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Invoice} entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<List<Invoice>> findByUserId(Long userId);
}
