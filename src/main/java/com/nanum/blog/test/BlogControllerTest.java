package com.nanum.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 스프핑이 com.nanum.blog 패키지 이하의 모든 파일을 스캔해서 new 하는것은 아니고
// 특정 어노테이션이 붙어있는 클래스 파일들을 new 해서 (IOC) 스프링 컨테이너에 관리해줍니다.
@RestController
public class BlogControllerTest {

    @GetMapping("/test/hello")
    public String hello(){
        return "hello Spring boot";
    }
}
