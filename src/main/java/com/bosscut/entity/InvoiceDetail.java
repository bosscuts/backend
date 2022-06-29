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

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "staff_id")
    private Long staffId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "description")
    private String description;

    public InvoiceDetail() {
    }

    public InvoiceDetail(Long invoiceDetailId, Long invoiceId, Long productServiceId, Integer quantity, Integer amount) {
        this.invoiceDetailId = invoiceDetailId;
        this.invoiceId = invoiceId;
        this.productServiceId = productServiceId;
        this.quantity = quantity;
        this.amount = amount;
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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
