package com.lin.service.impl;

import com.lin.dao.CommentRepository;
import com.lin.po.Comment;
import com.lin.po.User;
import com.lin.service.CommentService;
import com.lin.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImlp implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public List<Comment> getListCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"commentTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Override
    @Transactional
    public Comment saveComment(Comment comment,Long parentCommentId) {
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        } else {
            comment.setParentComment(null);
        }
        User linkUser = userInfoService.getLinkUser();
        comment.setUser(linkUser);
        comment.setCommentTime(new Date());
        return commentRepository.save(comment);
    }

    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentList = new ArrayList<>();
        for(Comment comment:comments){
            Comment temp = new Comment();
            BeanUtils.copyProperties(comment,temp);
            commentList.add(temp);
        }
        combineChildren(commentList);
        return commentList;
    }

    private void combineChildren(List<Comment> comments){
        for(Comment comment:comments){
            List<Comment> replys = comment.getReplyCommentList();
            for(Comment reply:replys){
                recursively(reply);
            }
            comment.setReplyCommentList(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();

    private void recursively(Comment comment){
        tempReplys.add(comment);
        if(comment.getReplyCommentList().size()>0){
            List<Comment> replys = comment.getReplyCommentList();
            for (Comment reply:replys){
                tempReplys.add(reply);
                if(reply.getReplyCommentList().size()>0){
                    recursively(reply);
                }
            }
        }
    }

    @Override
    public List<Comment> getList() {
        User linkUser = userInfoService.getLinkUser();
        Sort sort = Sort.by(Sort.Direction.ASC,"commentTime");
        List<Comment> comments = commentRepository.findByUserIdAndParentCommentNull(linkUser.getId(),sort);
        return eachComment(comments);
    }

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> ListCommentByBlogId(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogId(blogId);
        return comments;
    }
}
