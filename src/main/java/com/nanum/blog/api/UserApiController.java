package com.nanum.blog.api;

import com.nanum.blog.dto.ResponseDto;
import com.nanum.blog.model.User;
import com.nanum.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserApiController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    public UserApiController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // 회원 가입
    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user){
        System.out.println("UserApiController: save 호출됨:" + user.toString());

        int result =  userService.join(user);
      return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
      // HttpStatus.OK 로 성공에 대한 결과만 리턴한다. 실패에 대한 리턴은 GlobalExceptionHandler 에서 대신한다.

    }

    // 회원 수정
    @PutMapping("/user")
    public ResponseDto<Integer> update(@RequestBody User user){

        userService.update(user);

        // 여기서는 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됐음.
        // 하지만 세션값은 변경되지 않은 상태이기 때문에 직접 세션값을 변경해야함
        // 세션 등록

        // 아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        // AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리하도록 한다.
        Authentication authentication = authenticationManager.authenticate(token);

        // 실제 SecurityContext 에 authentication 정보를 등록한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    // 모든 회원 호출
    @GetMapping("/users")
    public List<User> allUsers(){
        return userService.allUsers();
    }

//    @PostMapping("/api/user/login")
//    public User login(@RequestBody User user, HttpSession session){
//        System.out.println("UserApiController: login 호출됨:" + user.toString());
//        User principal = userService.login(user);
//
//        if(principal!=null){
//            session.setAttribute("principal", principal);
//        }
//
//        return principal;
//        //System.out.println(principal.);
//
//       // return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//    }
}
