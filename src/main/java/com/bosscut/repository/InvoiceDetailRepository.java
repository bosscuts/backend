package com.bosscut.repository;

import com.bosscut.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link InvoiceDetail} entity.
 */
@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Long> {

}
