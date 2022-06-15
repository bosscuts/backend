package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ProductService.
 */
@Entity
@Table(name = "product_service")
public class ProductService extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productServiceId;

    @Column(name = "product_service_name", length = 100, nullable = false)
    private String productServiceName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    public ProductService(Long productServiceId, String productServiceName, Integer price, Integer quantity, String type, String description) {
        this.productServiceId = productServiceId;
        this.productServiceName = productServiceName;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.description = description;
    }

    public ProductService() {

    }

    public Long getProductServiceId() {
        return productServiceId;
    }

    public void setProductServiceId(Long productServiceId) {
        this.productServiceId = productServiceId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
