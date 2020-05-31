package com.lin.web;


import com.lin.po.Comment;
import com.lin.po.User;
import com.lin.service.BlogService;
import com.lin.service.CommentService;
import com.lin.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/blog/comments/{blogId}")
    public String Comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.getListCommentByBlogId(blogId));
        return "blog::commentList";
    }

    @PostMapping("/user/addComment")
    public String addComment(@RequestParam("parentCommentId")Long parentCommentId,
                             @RequestParam("blogId")Long blogId,
                             @RequestParam("content")String content){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBlog( blogService.findById(blogId));
        commentService.saveComment(comment,parentCommentId);
        return "redirect:/blog/comments/"+ blogId;
    }

    @GetMapping("/user/showComment")
    public String showComment(Model model){
        List<Comment> comments = commentService.getList();
        model.addAttribute("comments",comments);
        return "user/showComment";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId")Long commentId,Model model){
        Comment comment = commentService.findById(commentId);
        commentService.deleteComment(comment);
        List<Comment> comments = commentService.getList();
        model.addAttribute("comments",comments);
        return "user/showComment::main";
    }

}
