package com.nanum.blog.service;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.RoleRepository;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public int join(User user){
        try{
            RoleEntity roleEntity =  roleRepository.findByName(RoleType.USER);
            if(roleEntity == null) { // 기존 Role 테이블에 user 에 관한 role 이 없다면
                roleEntity = new RoleEntity();
                roleEntity.setName(RoleType.USER);
                roleEntity = roleRepository.save(roleEntity);
            }

            user = userRepository.save(user);

            // userRole 내에 user 와 roleEntity 의 관계를 기록한다.
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(roleEntity);
            userRoleRepository.save(userRole);

            System.out.println(user.toString());
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("UserService: 회원가입 실패: " + e.getMessage());
            return -1;
        }
    }
}
