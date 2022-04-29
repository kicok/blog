package com.nanum.blog.test;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.RoleRepository;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.repository.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@RestController
public class DummyControllerTest  {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public DummyControllerTest(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @PostMapping("/dummy/join")
    @Transactional
    public String join(User user){

        RoleEntity roleEntity =  roleRepository.findByName(RoleType.USER);
        if(roleEntity == null) {
            roleEntity = new RoleEntity();
            roleEntity.setName(RoleType.USER);
            roleEntity = roleRepository.save(roleEntity);
        }

        user = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleEntity);
        userRoleRepository.save(userRole);

        return "회원가입 완료";
    }

    // http://localhost:8080/blog/dummy/user/5
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id){
        // findById(id) 는 Optional 을 리턴한다.
        // 결과값이 없어서 user가 null 이면 현재 메서드의 리턴값인 User를 리턴할수 없으므로
        // 일단 Optional로 User 를 감싸서 가져오고 null인지 아닌지를 별도로 판단해야함

        // orElseGet 객체가 없으면 새로운 비어있는 객체를 넣는다.
//        User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//            @Override
//            public User get() {
//                return new User();
//            }
//        });

        // orElseThrow 객체가 없으면 IllegalArgumentException 을 날린다. (잘못된 인수)
//        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//            @Override
//            public IllegalArgumentException get() {
//                return new IllegalArgumentException("해당 유저는 없습니다. id :" + id);
//            }
//        });

        // 람다식으로 변경
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 사용자는 없습니다."));

        return user;

    }
}
