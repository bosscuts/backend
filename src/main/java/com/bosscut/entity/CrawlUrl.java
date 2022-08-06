package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A crawl_url.
 */
@Entity
@Table(name = "crawl_url")
public class CrawlUrl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crawlUrlId;

    @Column(name = "url")
    private String url;

    @Column(name = "type")
    private String type;

    public CrawlUrl() {
    }

    public Long getCrawlUrlId() {
        return crawlUrlId;
    }

    public void setCrawlUrlId(Long crawlUrlId) {
        this.crawlUrlId = crawlUrlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
