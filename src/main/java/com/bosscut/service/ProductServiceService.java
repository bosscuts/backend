package com.bosscut.service;

import com.bosscut.entity.ProductService;

import java.util.List;

public interface ProductServiceService {
    List<ProductService> findByType(String type);
    List<ProductService> findAll();

    List<ProductService> findAllByIds(List<Long> ids);
}
