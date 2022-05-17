package com.nanum.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class BoardController {

    @GetMapping({"", "/"})
    public String index(Model model, HttpSession httpSession){
       // httpSession.

//       Object princiapl = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("user : " + user );
//        if(user!=null){
//            model.addAttribute("princiapl", user);
//        }
        return "index";
    }
}
