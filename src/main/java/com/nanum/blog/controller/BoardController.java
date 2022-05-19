package com.nanum.blog.controller;

import com.nanum.blog.config.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping({"", "/"})
    public String index(Authentication authentication, Model model){
        System.out.println("/test/login ==============");
        PrincipalDetails principal  =  (PrincipalDetails)  authentication.getPrincipal();
        System.out.println("BoardController 로그인 principal " + principal );
        System.out.println("BoardController 11getUsername" + principal.getUsername());
        System.out.println("BoardController 22getPassword" + principal.getPassword());

        model.addAttribute("principal", principal);



        return "index";
    }
}
