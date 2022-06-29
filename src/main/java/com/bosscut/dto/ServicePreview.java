package com.bosscut.dto;

public class ServicePreview {

    private String serviceProductName;
    private Integer quantity;
    private Integer totalPrice;

    public ServicePreview() {
    }

    public ServicePreview(String serviceProductName, Integer quantity, Integer totalPrice) {
        this.serviceProductName = serviceProductName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getServiceProductName() {
        return serviceProductName;
    }

    public void setServiceProductName(String serviceProductName) {
        this.serviceProductName = serviceProductName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}
