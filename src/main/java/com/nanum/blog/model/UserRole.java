package com.nanum.blog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class UserRole {
// User 와 RoleEntity 의 N:M 관계를 UserRole 을 통해 OneToMany 로 해결

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleEntity role;
}
