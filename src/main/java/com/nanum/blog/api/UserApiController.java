package com.nanum.blog.api;

import com.nanum.blog.dto.ResponseDto;
import com.nanum.blog.model.User;
import com.nanum.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user")
    public ResponseDto<Integer> save(@RequestBody User user){
        System.out.println("UserApiController: save 호출됨:" + user.toString());

        int result =  userService.join(user);
      return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
      // HttpStatus.OK 로 성공에 대한 결과만 리턴한다. 실패에 대한 리턴은 GlobalExceptionHandler 에서 대신한다.

    }
}
