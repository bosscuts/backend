package com.bosscut.controller;

import com.bosscut.entity.ProductService;
import com.bosscut.entity.User;
import com.bosscut.enums.ProductServiceType;
import com.bosscut.enums.UserLevel;
import com.bosscut.service.ProductServiceService;
import com.bosscut.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final ProductServiceService productServiceService;

    public HomeController(UserService userService, ProductServiceService productServiceService) {
        this.userService = userService;
        this.productServiceService = productServiceService;
    }

    @GetMapping({"/"})
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<User> users = userService.getUserByLevel(UserLevel.ASSISTANT.getName());
        List<ProductService> productServices = productServiceService.findAll();

        List<ProductService> products = productServices.stream()
                .filter(p -> p.getType().equals(ProductServiceType.PRODUCT.getName()))
                .collect(Collectors.toList());

        List<ProductService> services = productServices.stream()
                .filter(p -> p.getType().equals(ProductServiceType.SERVICE.getName()))
                .collect(Collectors.toList());

        model.addAttribute("assistants", users);
        model.addAttribute("services", productServices);
        model.addAttribute("products", products);
        model.addAttribute("services", services);
        return "index";
    }
}
