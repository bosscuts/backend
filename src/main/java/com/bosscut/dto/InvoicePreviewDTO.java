package com.bosscut.dto;

import java.util.List;

public class InvoicePreviewDTO {

    private List<ServicePreviewDTO> servicePreviews;
    private Integer totalAmount;

    public InvoicePreviewDTO() {
    }

    public List<ServicePreviewDTO> getServicePreviews() {
        return servicePreviews;
    }

    public void setServicePreviews(List<ServicePreviewDTO> servicePreviews) {
        this.servicePreviews = servicePreviews;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
}
