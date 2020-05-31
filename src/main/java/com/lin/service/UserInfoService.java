package com.lin.service;

import com.lin.po.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserInfoService {
    User findByUsername(String s);
    boolean saveUser(User user);
    Role findRole(String s);
    User findById(Long id);
    boolean saveConcern(Concern concern);
    Concern findConcern(Long userId,Long useredId);
    User getLinkUser();
    boolean deleteConcern(Concern concern);
    BlackList findBlackList(Long userId,Long useredId);
    boolean saveBlackList(BlackList blackList);
    boolean deleteBlackList(BlackList blackList);
    Page<User> listUser(String query, Pageable pageable);
    Teacher findTeacherByIdNumber(Long IdNumber);
    User findByEmail(String email);
    List<User> listUser(String query);
}
