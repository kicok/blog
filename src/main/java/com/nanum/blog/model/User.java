package com.nanum.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nanum.blog.model.common.TimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

//@DynamicInsert //insert시에 null인 필드를 제외시켜준다.
//@Data
//@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private int id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String nickname;


    /**
     * 권한 연계
     */
//    @Fetch(value = FetchMode.SELECT)
    // fetch = FetchType.LAZY 일 경우에 failed to lazily initialize a collection of role: com.nanum.blog.model.User.roles, could not initialize proxy - no Session
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // { CascadeType.PERSIST, CascadeType.REMOVE }와 동일
    private List<UserRole> roles;

}
