package com.nanum.blog.test;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.RoleRepository;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.repository.UserRoleRepository;
import org.springframework.dao.EmptyResultDataAccessException;
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

    // http://localhost:8080/blog/dummy/user/2
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

    // http://localhost:8080/blog/dummy/users
    @GetMapping("/dummy/users")
    public List<User> list(){
        return userRepository.findAll();
    }

    @GetMapping("/dummy/user")
    public Page<User> pageList(@PageableDefault(size = 1, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<User> pagingUser = userRepository.findAll(pageable);
        List<User> users = pagingUser.getContent(); // contentt 부분만 가져옴(page관련 정보는 제외)
        return pagingUser;
    }


    // http://localhost:8080/blog/dummy/user/1
    @Transactional
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser){
        User user = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("사용자정보를 찾을수 없습니다"));

        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        // dirty checking : 변경된 사항을 감지해서 자동으로 save 한다.
        return user;

//   더티체킹(Dirty Checking)이란 상태 변경 검사이다.
//   JPA에서는 트랜잭션이 끝나는 시점에 변화가 있는 모든 엔티티 객체를 데이터베이스 반영한다.
//   그렇기 때문에 값을 변경한 뒤, save 하지 않더라도 DB에 반영되는 것이다.
//   이러한 상태 변경 검사의 대상은 영속성 컨텍스트가 관리하는 엔티티에만 적용된다.(준영속, 비영속된 객체X)
//
//   더티체킹(Dirty Checking) 원리:
//    · 영속성 컨텍스트란 서버와 DB사이에 존재한다.
//    · JPA는 엔티티를 영속성 컨텍스트에 보관할 때, 최초 상태를 복사해서 저장해둔다.(일종의 스냅샷)
//    · 트랜잭션이 끝나고 flush할 때 스냅샷과 현재 엔티티를 비교해 변경된 엔티티를 찾아낸다.
//    · JPA는 변경된 엔티티를 DB단에 반영하여 한번에 쿼리문을 날려준다.
    }

    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id){
//        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("삭제할 사용자가 존재하지 않습니다"));
//        userRepository.delete(user);
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            return "삭제에 실패하였습니다. 해당 id는 존재하지 않습니다.";
        }

        //userRepository.deleteById(id);
        return "삭제 되었습니다 : " + id;
    }

}
