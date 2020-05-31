package com.lin.service;

import com.lin.po.*;
import com.lin.po.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogService {
    List<Blog> findFirst10Blog(String manuscript, Module module, int index, int size);
    void saveBlog(Blog blog);
    Blog findById(Long id);
    int getOpenBlogAmount(String manuscript, Module module);
    Collect saveCollect(Collect collect);
    Collect findCollect(Blog blog, User user);
    void deleteCollect(Collect collect);
    List<Blog> getPersonBlogList(Long userId,User visitUser);
    Page<Blog> listBlog(String query, Pageable pageable);
    void deleteBlog(Blog blog);
}
