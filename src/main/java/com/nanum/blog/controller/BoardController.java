package com.nanum.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping({"", "/"})
    public String index(){

        return "index";
    }

    // User 권한 필요
    @GetMapping("/board/saveForm")
    public String saveForm(){

        return "board/saveForm";
    }
}
