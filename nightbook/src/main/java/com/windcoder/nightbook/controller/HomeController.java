package com.windcoder.nightbook.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-09-28
 * Time: 21:13 下午
 */
@Controller
@RequestMapping("/*")
public class HomeController {


    private static Logger logger = LogManager.getLogger(HomeController.class);

    @RequestMapping("home")
    public String home(HttpServletRequest request) {
        logger.error("home.do");
        return "home";
    }


    @RequestMapping("admin/home")
    public String adminHome(HttpServletRequest request) {
        logger.error("admin/home ");
//        Subject subject = SecurityUtils.getSubject();
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute("username", "哈哈");
        return "admin/home";
    }
}
