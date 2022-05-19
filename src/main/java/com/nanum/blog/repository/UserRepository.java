package com.nanum.blog.repository;

import com.nanum.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 아래의 User login(String username, String password); 와 같은 코드 임
//    User findByUsernameAndPassword(String username, String password);
//
//    @Query(value="SELECT * FROM user WHERE username=?1 AND password=?2", nativeQuery = true)
//    User login(String username, String password);

    // SELECT * FROM user WHERE username = ?1
    public Optional<User> findByUsername(String username);

}
