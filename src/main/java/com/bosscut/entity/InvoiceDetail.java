package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A InvoiceDetail.
 */
@Entity
@Table(name = "invoice_detail")
public class InvoiceDetail extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceDetailId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "product_service_id")
    private Long productServiceId;

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "quantity")
    private Integer quantity;

    public InvoiceDetail() {
    }

    public InvoiceDetail(Long invoiceDetailId, Long invoiceId, Long productServiceId, Integer quantity) {
        this.invoiceDetailId = invoiceDetailId;
        this.invoiceId = invoiceId;
        this.productServiceId = productServiceId;
        this.quantity = quantity;
    }

    public Long getInvoiceDetailId() {
        return invoiceDetailId;
    }

    public void setInvoiceDetailId(Long invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getProductServiceId() {
        return productServiceId;
    }

    public void setProductServiceId(Long productServiceId) {
        this.productServiceId = productServiceId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
