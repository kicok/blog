package com.nanum.blog.test;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.RoleRepository;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DummyControllerTest  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @PostMapping("/dummy/join")
    public String join(User user){

        user = userRepository.save(user);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleType.USER.getMessage());
        roleEntity = roleRepository.save(roleEntity);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleEntity);
        userRoleRepository.save(userRole);

        return "회원가입 완료";
    }
}
