package com.lin.service;

import com.lin.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getListCommentByBlogId(Long blogId);
    Comment saveComment(Comment comment,Long parentCommentId);
    List<Comment> getList();
    Comment findById(Long commentId);
    void deleteComment(Comment comment);
    List<Comment> ListCommentByBlogId(Long blogId);

}
