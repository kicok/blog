package com.nanum.blog.config.auth;

import com.nanum.blog.model.User;
import com.nanum.blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 시큐리티 설정에서 loginProcessingUrl("/loginProc") 으로 되어있으므로
    // loginProc 요청이 오면 자동으로 UserDetailService 타입으로 Ioc되어있는 loadUserByUsername 함수가 실행됨

    // 스프링이 로그인 요청을 가로챌때 username, password 변수 2개를 가로채는데
    // password 부분 처리는 알아서 함.
    // username 이 DB에 있는지만 확인해주면 됨.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User principal = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("해당 사용자를 찾을수 없습니다 :" + username));

        System.out.println("해당 사용자" + principal.getUsername());
        System.out.println("해당 getPassword" + principal.getPassword());
        System.out.println("해당 getEmail" + principal.getEmail());
        return new PrincipalDetails(principal); // 시큐리티 세션에 유저정보가 저장이 됨.
    }
}
