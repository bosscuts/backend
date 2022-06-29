package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A invoice.
 */
@Entity
@Table(name = "invoice")
public class Invoice extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @Column(name = "invoice_number", length = 100)
    private String invoiceNumber;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "total_amount_payment")
    private BigDecimal totalAmountPayment;

    @Column(name = "discounted_amount")
    private BigDecimal discountedAmount;

    @Column(name = "total_after_discount")
    private BigDecimal totalAfterDiscount;

    @Column(name = "vat_percentage")
    private Integer vatPercentage;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "invoice_type")
    private String invoiceType;

    @Column(name = "userId")
    private Long userId;

    public Invoice() {
    }

    public Invoice(Long invoiceId, String invoiceNumber, Long customerId, BigDecimal totalAmountPayment,
                   BigDecimal discountedAmount, BigDecimal totalAfterDiscount, Integer vatPercentage, String paymentType, String invoiceType, Long userId) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.totalAmountPayment = totalAmountPayment;
        this.discountedAmount = discountedAmount;
        this.totalAfterDiscount = totalAfterDiscount;
        this.vatPercentage = vatPercentage;
        this.paymentType = paymentType;
        this.invoiceType = invoiceType;
        this.userId = userId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmountPayment() {
        return totalAmountPayment;
    }

    public void setTotalAmountPayment(BigDecimal totalAmountPayment) {
        this.totalAmountPayment = totalAmountPayment;
    }

    public BigDecimal getDiscountedAmount() {
        return discountedAmount;
    }

    public void setDiscountedAmount(BigDecimal discountedAmount) {
        this.discountedAmount = discountedAmount;
    }

    public BigDecimal getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(BigDecimal totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public Integer getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(Integer vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
