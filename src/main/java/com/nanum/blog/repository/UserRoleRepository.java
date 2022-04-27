package com.nanum.blog.repository;

import com.nanum.blog.model.RoleEntity;
import com.nanum.blog.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
