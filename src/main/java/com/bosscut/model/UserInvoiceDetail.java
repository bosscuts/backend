package com.bosscut.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class UserInvoiceDetail {
    @Id
    private Long invoiceDetailId;

    @Column
    private Long invoiceId;

    @Column
    private String invoiceNumber;

    @Column
    private Long customerId;

    @Column
    private Long staffId;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal totalAmountPayment;

    @Column
    private String productServiceName;

    @Column
    private Integer price;

    @Column
    private Long userId;

    @Column
    private String customerName;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String description;

    @Column
    private LocalDateTime createdDate;

    public UserInvoiceDetail() {
    }

    public UserInvoiceDetail(Long invoiceDetailId, Long invoiceId, String invoiceNumber, Long customerId, Long staffId, Integer quantity, BigDecimal totalAmountPayment, String productServiceName, Integer price, Long userId, String customerName, String address, String phone, LocalDateTime createdDate) {
        this.invoiceDetailId = invoiceDetailId;
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.staffId = staffId;
        this.quantity = quantity;
        this.totalAmountPayment = totalAmountPayment;
        this.productServiceName = productServiceName;
        this.price = price;
        this.userId = userId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.createdDate = createdDate;
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

    public BigDecimal getTotalAmountPayment() {
        return totalAmountPayment;
    }

    public void setTotalAmountPayment(BigDecimal totalAmountPayment) {
        this.totalAmountPayment = totalAmountPayment;
    }

    public String getProductServiceName() {
        return productServiceName;
    }

    public void setProductServiceName(String productServiceName) {
        this.productServiceName = productServiceName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
