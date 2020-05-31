package com.lin.service.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lin.dao.BlogRepository;
import com.lin.dao.CollectRepository;
import com.lin.dao.CommentRepository;
import com.lin.dao.ModuleRepository;
import com.lin.po.*;
import com.lin.po.Module;
import com.lin.service.BlogService;
import com.lin.service.CommentService;
import com.lin.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {



    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CommentService commentService;

    @Override
    @Transactional
    public void saveBlog(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public List<Blog> findFirst10Blog(String manuscript,Module module,int index,int size) {
        Pageable pageable = PageRequest.of(index,size);
        List<Blog> blogs = blogRepository.findFirst10ByManuscriptAndModuleIdAndOpenOrderByUpdateTimeDesc(manuscript,module.getId(),"open",pageable);
        return blogs;
    }

    @Override
    public Blog findById(Long id) {
        return blogRepository.findById(id).get();
    }

    @Override
    public int getOpenBlogAmount(String manuscript, Module module) {
        return blogRepository.countByManuscriptAndModuleIdAndOpen(manuscript,module.getId(),"open");

    }

    @Override
    public Collect saveCollect(Collect collect) {
        return collectRepository.save(collect);
    }

    @Override
    public Collect findCollect(Blog blog, User user) {
        return collectRepository.findByCollectedBidAndCollectUid(blog.getId(),user.getId());
    }

    @Override
    public void deleteCollect(Collect collect) {
        collectRepository.delete(collect);
    }

    public void deleteChilComment( List<Comment> commentList){
        for(Comment comment: commentList){
            commentService.deleteComment(comment);
        }
    }

    @Override
    public void deleteBlog(Blog blog) {
        List<Comment> commentList = commentService.ListCommentByBlogId(blog.getId());
        deleteChilComment(commentList);
        blogRepository.delete(blog);
    }

    @Override
    public List<Blog> getPersonBlogList(Long userId, User visitUser) {
        if(visitUser==null){
            List<Blog> blogs = blogRepository.findAllByUser_IdAndOpenAndManuscriptOrderByUpdateTimeDesc(userId,"open","发布");
            return blogs;
        } else {
            if(visitUser.getId()!= userId){
                List<Blog> blogs = blogRepository.findAllByUser_IdAndOpenNotAndManuscriptOrderByUpdateTimeDesc(userId, "", "发布");
                return blogs;
            } else {
                List<Blog> blogs = blogRepository.findAllByUser_IdAndOpenAndManuscriptOrderByUpdateTimeDesc(userId,"open","发布");
                return blogs;
            }
        }
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery("%"+query+"%",pageable);
    }
}
