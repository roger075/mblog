package com.lin.dao;

import com.lin.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
    List<Comment> findByUserIdAndParentCommentNull(Long userId,Sort sort);
    List<Comment> findByBlogId(Long blogId);

}
