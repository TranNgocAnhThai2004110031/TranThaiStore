package com.tranthai.tranthaistore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tranthai.tranthaistore.model.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT * FROM user u, users_roles ur, role r WHERE u.id = ur.user_id AND u.email LIKE %:email% AND ur.role_id = r.id AND r.name = 'ROLE_USER'", nativeQuery = true)
    List<User> findByEmailContainingIgnoreCase(@Param("email") String email);

    @Query(value = "SELECT * FROM user u, users_roles ur, role r WHERE u.id = ur.user_id AND ur.role_id = r.id AND r.name = 'ROLE_USER'", nativeQuery = true)
    List<User> findAllUserNonAdmin();
}
