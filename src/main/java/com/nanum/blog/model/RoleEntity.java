package com.nanum.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoleId;

    // 데이터베이스에는 RoleType 이라는게 없으므로 EnumType.STRING 으로 타입을 지정해줌
    @Enumerated(EnumType.STRING)
    @Column(unique=true)
    private RoleType name;

}
