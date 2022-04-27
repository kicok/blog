package com.nanum.blog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class UserRole {
// User 와 RoleEntity 의 N:M 관계를 UserRole 을 통해 OneToMany 로 해결
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleEntity role;
}
