package com.bosscut.controller;

import com.bosscut.entity.CrawlUrl;
import com.bosscut.repository.CrawlRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/crawl-url")
public class CrawlUrlController {
    private final CrawlRepository crawlRepository;
    public CrawlUrlController(CrawlRepository crawlRepository) {
        this.crawlRepository = crawlRepository;
    }

    @GetMapping({"/"})
    @Transactional
    public String list(HttpSession session) {
        List<CrawlUrl> crawlUrls = crawlRepository.findAll();

        session.setAttribute("crawlUrls", crawlUrls);
        return "frontend/crawlUrl/index";
    }

    @GetMapping({"/register"})
    @Transactional
    public String register() {
        return "frontend/crawlUrl/register";
    }

    @PostMapping
    @Transactional
    public String createUrl(@RequestParam String url, @RequestParam String type) {
        CrawlUrl entity = new CrawlUrl();
        entity.setUrl(url);
        entity.setType(type);
        crawlRepository.save(entity);
        return "redirect:/";
    }

    @GetMapping({"/delete/{crawlUrlId}"})
    @Transactional
    public String deleteUrl(@PathVariable Long crawlUrlId) {
        CrawlUrl entity = crawlRepository.findById(crawlUrlId).orElse(null);
        if (Objects.nonNull(entity)) {
            crawlRepository.delete(entity);
        }
        return "redirect:/";
    }
}
