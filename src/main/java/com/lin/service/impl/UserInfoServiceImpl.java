package com.lin.service.impl;

import com.lin.dao.*;
import com.lin.po.*;
import com.lin.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class UserInfoServiceImpl implements UserInfoService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private BlackListRepository blackListRepository;

    @Autowired
    private ConcernRepository concernRepository;

    @Override
    public User findByUsername(String s) {
        User user = userRepository.findByUsername(s);
        return user;
    }

    @Override
    public Role findRole(String s) {
        return roleRepository.findByRole(s);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class,Error.class})
    public boolean saveUser(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public boolean saveConcern(Concern concern) {
        concernRepository.save(concern);
        return true;
    }

    @Override
    public Concern findConcern(Long userId, Long useredId) {
        return concernRepository.findByConcern_IdAndConcern_Id(userId,useredId);
    }

    @Override
    public User getLinkUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = findByUsername(username);
        return user;
    }

    @Override
    @Transactional
    public boolean deleteConcern(Concern concern) {
        concernRepository.delete(concern);
        return true;
    }

    @Override
    public BlackList findBlackList(Long userId, Long useredId) {
        return blackListRepository.findByBlackList_IdAndBlackList_Id(userId,useredId);
    }

    @Override
    @Transactional
    public boolean saveBlackList(BlackList blackList) {
        blackListRepository.save(blackList);
        return true;
    }

    @Override
    public boolean deleteBlackList(BlackList blackList) {
        blackListRepository.delete(blackList);
        return true;
    }

    @Override
    public Page<User> listUser(String query, Pageable pageable) {
        return userRepository.findByQuery("%"+query+"%",pageable);
    }

    @Override
    public Teacher findTeacherByIdNumber(Long IdNumber) {
        return teacherRepository.findByIdNumber(IdNumber);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> listUser(String query) {
        char c = query.charAt(0);
        if(c=='#'){
            query = query.substring(1);
            List<User> userList = new ArrayList<>();
            userList.add(userRepository.findById(Long.valueOf(query)).get());
            return userList;
        }
        return userRepository.findByQuery("%"+query+"%");
    }
}
