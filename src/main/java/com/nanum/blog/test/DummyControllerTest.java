package com.nanum.blog.test;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.RoleRepository;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.repository.UserRoleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



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
}
