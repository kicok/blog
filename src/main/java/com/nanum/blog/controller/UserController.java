package com.nanum.blog.controller;

import com.nanum.blog.config.auth.Annotation.AuthUser;
import com.nanum.blog.config.auth.PrincipalDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/auth/joinForm")
    public String joinForm(){
        return "/user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "/user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(@AuthUser PrincipalDetails principalDetails, Model model){

        model.addAttribute("principal", principalDetails);

        return "user/updateForm";
    }
}
