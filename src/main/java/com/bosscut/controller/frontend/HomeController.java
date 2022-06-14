package com.bosscut.controller.frontend;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping({"/index"})
    public String dashboard(@RequestParam(defaultValue = "") String keyword,
                            @PageableDefault(50) Pageable pageable,
                            Model model,
                            HttpServletRequest servletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        AuthorizedUser authorizedUser = null;
//        if (authentication != null && authentication.getPrincipal() instanceof AuthorizedUser) {
//            authorizedUser = (AuthorizedUser) authentication.getPrincipal();
//        }
        Object o = authentication.getPrincipal();
        System.out.println(authentication.getPrincipal());
        return "index";
    }

    @GetMapping({"/res"})
    public String res(@RequestParam(defaultValue = "") String keyword,
                            @PageableDefault(50) Pageable pageable,
                            Model model,
                            HttpServletRequest servletRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object o = authentication.getPrincipal();
        System.out.println(authentication.getPrincipal());
        return "frontend/res";
    }
}
