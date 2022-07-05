package com.bosscut.repository;

import com.bosscut.entity.InvoiceDetail;
import com.bosscut.repository.custom.InvoiceDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link InvoiceDetail} entity.
 */
@Repository
public interface InvoiceDetailRepository extends InvoiceDetailRepositoryCustom, JpaRepository<InvoiceDetail, Long> {
    Optional<List<InvoiceDetail>> findByStaffId(Long userId);
    Optional<List<InvoiceDetail>> findAllByCreatedDateBetween(Instant startOfDay, Instant endOfDay);
}
