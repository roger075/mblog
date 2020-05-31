package com.lin.dao;

import com.lin.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
    @Query("select u from User u where u.nickname like ?1")
    Page<User> findByQuery(String Query, Pageable pageable);
    User findByEmail(String email);
    @Query("select u from User u where u.nickname like ?1")
    List<User> findByQuery(String Query);
}
