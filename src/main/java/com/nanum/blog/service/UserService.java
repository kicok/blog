package com.nanum.blog.service;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.RoleRepository;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.repository.UserRoleRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 권한 주기
    @Transactional
    public UserRole roleMake2(User user, RoleType roleType){
        if(roleType == null) roleType = RoleType.USER;
        RoleEntity roleEntity =  roleRepository.findByName(roleType);
        if(roleEntity == null) { // 기존 Role 테이블에 user 에 관한 role 이 없다면
            roleEntity = new RoleEntity();
            roleEntity.setName(roleType);
        //    roleEntity = roleRepository.save(roleEntity);
        }

        // userRole 내에 user 와 roleEntity 의 관계를 기록한다.
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleEntity);
       // userRoleRepository.save(userRole);

        return userRole;
    }

    @Transactional
    public List<UserRole> roleMake(User user, List<RoleType> roleTypes){
        if(roleTypes == null){
            roleTypes.add(RoleType.USER);
        }

        List<UserRole> userRoles = new ArrayList<UserRole>();
        for( RoleType roleType : roleTypes){
            RoleEntity roleEntity =  roleRepository.findByName(roleType);
            if(roleEntity == null) { // 기존 Role 테이블에 user 에 관한 role 이 없다면
                roleEntity = new RoleEntity();
                roleEntity.setName(roleType);
                roleEntity = roleRepository.save(roleEntity);
            }

            // userRole 내에 user 와 roleEntity 의 관계를 기록한다.
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(roleEntity);
            userRoleRepository.save(userRole);
            userRoles.add(userRole);
        }

        return userRoles;
    }

    @Transactional
    public int join(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = userRepository.save(user);

            // userRole 내에 user 와 roleEntity 의 관계를 기록한다.
            roleMake(user, new ArrayList(Arrays.asList(RoleType.USER)));

            System.out.println(user.toString());
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("UserService: 회원가입 실패: " + e.getMessage());
            return -1;
        }
    }

    @Transactional
    public void update(User user){
        // 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
        // select를 해서 User오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서!!
        // 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려줌.
        User persistanceUser = userRepository.findById(user.getId())
                .orElseThrow(()->new UsernameNotFoundException("회원이 존재하지 않습니다."));

        persistanceUser.setEmail(user.getEmail());
        persistanceUser.setNickname(user.getNickname());

        String rawPassword = user.getPassword();
        if(rawPassword!=null && rawPassword.length() >= 4){
            persistanceUser.setPassword(passwordEncoder.encode(rawPassword));
        }

        // 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit 이 자동으로 됨.
        // 영속화된 persistanceUser 객체의 변화가 감지되면 더티체킹이 되어 update문을 날려줌.
    }

//    @Transactional(readOnly = true) // select 할때 트랜잭션 시작, 서비스종료시에 트랜잭션 종료(정합성)
//    public User login(User user){
////       return userRepository.login(user.getUsername(), user.getPassword());
//       return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }


}
