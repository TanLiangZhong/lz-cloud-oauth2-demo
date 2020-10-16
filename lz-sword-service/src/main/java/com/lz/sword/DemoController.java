package com.lz.sword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO Description
 *
 * @author Tan
 * @version 1.1, 2020/10/16 12:38
 */
@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @GetMapping
    public String index(HttpServletRequest request) {
        log.info("name: {}", request.getParameter("name"));
        log.info("Authorization: {}", request.getHeader("Authorization"));
        return "demo";
    }

}
