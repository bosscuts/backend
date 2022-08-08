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
    private Float price;

    @Column(name = "price_old")
    private Float priceOld;

    @Column(name = "percent_sale")
    private Integer percentSale;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Transient
    private String status;

    @Transient
    private String percentSaleString;

    public Product(Long productId, String productName, String productCode, Float price, Float priceOld, Integer percentSale, String link, String description) {
        this.productId = productId;
        this.price = price;
        this.priceOld = priceOld;
        this.percentSale = percentSale;
        this.productName = productName;
        this.productCode = productCode;
        this.link = link;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Float priceOld) {
        this.priceOld = priceOld;
    }

    public Integer getPercentSale() {
        return percentSale;
    }

    public void setPercentSale(Integer percentSale) {
        this.percentSale = percentSale;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPercentSaleString() {
        return percentSaleString;
    }

    public void setPercentSaleString(String percentSaleString) {
        this.percentSaleString = percentSaleString;
    }
}
