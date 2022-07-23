package com.bosscut.controller;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.repository.CrawlRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    private final CrawlRepository crawlRepository;

    public HomeController(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }

    @GetMapping({"/"})
    @Transactional
    public String dashboard(Model model) {
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();

        model.addAttribute("crawlUrls", crawlUrls);
        return "index";
    }
}
