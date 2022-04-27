package com.nanum.blog.repository;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByName(RoleType name);
}
