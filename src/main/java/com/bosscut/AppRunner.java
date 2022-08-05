package com.bosscut;

import com.bosscut.entity.Product;
import com.bosscut.repository.ProductRepository;
import com.bosscut.service.MailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppRunner implements CommandLineRunner {

    private final MailService mailService;
    private final ProductRepository productRepository;
    public AppRunner(MailService mailService, ProductRepository productRepository) {
        this.mailService = mailService;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        List<Product> productList = productRepository.findAll();
//        productList.forEach(product -> {
//            product.setPrice(1000);
//        });
//        Product p = new Product();
//        p.setProductName("test");
//        p.setPrice(1111);
//        p.setPriceOld(111111);
//
//        productList.add(p);
//
//        productRepository.saveAll(productList);

//        mailService.sendEmail("project.devskill@gmail.com", "sdsdsds", "sdsdsdsdsd", Boolean.FALSE, Boolean.FALSE);
    }
}
