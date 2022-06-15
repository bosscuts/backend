package com.bosscut.service.impl;

import com.bosscut.entity.ProductService;
import com.bosscut.repository.ProductServiceRepository;
import com.bosscut.service.ProductServiceService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceServiceImpl implements ProductServiceService {
    private final ProductServiceRepository productServiceRepository;

    public ProductServiceServiceImpl(ProductServiceRepository productServiceRepository) {
        this.productServiceRepository = productServiceRepository;
    }

    public List<ProductService> findByType(String type) {
        return productServiceRepository.findByType(type).orElse(Collections.emptyList());
    }

    @Override
    public List<ProductService> findAll() {
        return productServiceRepository.findAll();
    }

}
