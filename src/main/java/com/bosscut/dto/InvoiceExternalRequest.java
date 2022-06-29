package com.bosscut.dto;

import java.math.BigDecimal;

public class InvoiceExternalRequest {

    private String strServiceStaff;
    private String customerPhone;
    private BigDecimal totalAmountPayment;

    public String getStrServiceStaff() {
        return strServiceStaff;
    }

    public void setStrServiceStaff(String strServiceStaff) {
        this.strServiceStaff = strServiceStaff;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BigDecimal getTotalAmountPayment() {
        return totalAmountPayment;
    }

    public void setTotalAmountPayment(BigDecimal totalAmountPayment) {
        this.totalAmountPayment = totalAmountPayment;
    }
}
