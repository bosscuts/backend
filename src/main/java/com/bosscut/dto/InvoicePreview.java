package com.bosscut.dto;

import java.util.List;

public class InvoicePreview {

    private List<ServicePreview> servicePreviews;
    private Integer totalAmount;

    public InvoicePreview() {
    }

    public List<ServicePreview> getServicePreviews() {
        return servicePreviews;
    }

    public void setServicePreviews(List<ServicePreview> servicePreviews) {
        this.servicePreviews = servicePreviews;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
}
