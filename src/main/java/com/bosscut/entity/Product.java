package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Product
 */
@Entity
@Table(name = "product")
public class Product extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "price")
    private Integer price;

    @Column(name = "price_old")
    private Integer priceOld;

    @Column(name = "percent_sale")
    private Integer percentSale;

    @Column(name = "description")
    private String description;

    public Product(Long productId, String productName, String productCode, Integer price, Integer priceOld, Integer percentSale, String description) {
        this.productId = productId;
        this.price = price;
        this.priceOld = priceOld;
        this.percentSale = percentSale;
        this.productName = productName;
        this.productCode = productCode;
        this.description = description;
    }

    public Product() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPriceOld() {
        return priceOld;
    }

    public Integer getPercentSale() {
        return percentSale;
    }

    public void setPercentSale(Integer percentSale) {
        this.percentSale = percentSale;
    }

    public void setPriceOld(Integer priceOld) {
        this.priceOld = priceOld;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
