package com.nanum.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {

    @GetMapping("/")
    public String index(String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
}
