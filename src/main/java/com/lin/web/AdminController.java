package com.lin.web;


import com.lin.po.Blog;
import com.lin.po.Inform;
import com.lin.po.Report;
import com.lin.po.User;
import com.lin.service.BlogService;
import com.lin.service.InformService;
import com.lin.service.ReportService;
import com.lin.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private InformService informService;

    @GetMapping("/admin/userAudit")
    public String showUser(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("admin",linkUser);
        return "/admin/userAudit";
    }

    @PostMapping("/admin/searchUser")
    public String searchUser(@RequestParam("search")String search,Model model){
        model.addAttribute("users",userInfoService.listUser(search));
        model.addAttribute("search",search);
        return "/admin/userAudit";
    }

    @PostMapping("/admin/lockUser")
    public String deleteUser(@RequestParam("userId")Long userId,
                                @RequestParam("search")String search,
                                @RequestParam("type")String type,
                                Model model){
        User user = userInfoService.findById(userId);
        if(type.equals("封号"))
            user.setLocked((byte) 0);
        else
            user.setLocked((byte)1);
        userInfoService.saveUser(user);
        model.addAttribute("users",userInfoService.listUser(search));
        model.addAttribute("search",search);
        return "/admin/userAudit::user";
    }

    @GetMapping("/admin/blogAudit")
    public String showBlog(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("admin",linkUser);
        return "/admin/blogAudit";
    }

    @PostMapping("/admin/searchBlog")
    public String searchBlog(@RequestParam("search")String search,Model model){
        Pageable pageable = PageRequest.of(0,20);
        model.addAttribute("page",blogService.listBlog(search,pageable));
        model.addAttribute("search",search);
        return "/admin/blogAudit";
    }

    @PostMapping("/admin/deleteBlog")
    public String deleteConcern(@RequestParam("blogId")Long blogId,
                                @RequestParam("search")String search,
                                Model model){
        Blog blog = blogService.findById(blogId);
        blogService.deleteBlog(blog);
        Pageable pageable = PageRequest.of(0,20);
        model.addAttribute("page",blogService.listBlog(search,pageable));
        model.addAttribute("search",search);
        return "/admin/blogAudit::blog";
    }

    @GetMapping("/admin/reportAudit")
    public String showReport(Model model){
        List<Report> reports = reportService.findAll();
        model.addAttribute("reports",reports);
        return "/admin/reportAudit";
    }

    @PostMapping("/admin/deleteReport")
    public String deleteReport(@RequestParam("reportId")Long reportId,
                                Model model){
        Report byId = reportService.findById(reportId);
        reportService.deleteReport(byId);
        List<Report> reports = reportService.findAll();
        model.addAttribute("reports",reports);
        return "/admin/reportAudit::report";
    }

    @GetMapping("/admin/informAudit")
    public String sendInform(Model model){
        return "/admin/sendInform";
    }

    @PostMapping("/admin/searchInform")
    public String searchInform(@RequestParam("search")String search,Model model){
        model.addAttribute("users",userInfoService.listUser(search));
        model.addAttribute("search",search);
        return "/admin/sendInform";
    }

    @PostMapping("/admin/inform")
    public String saveInform(@RequestParam("userId")Long userId,
                             @RequestParam("content")String content,
                             @RequestParam("search")String search,
                             Model model){
        User linkUser = userInfoService.getLinkUser();
        User user = userInfoService.findById(userId);
        Inform inform = new Inform();
        inform.setContent(content);
        inform.setIssueTime(new Date());
        inform.setReaded((byte)0);
        inform.setUser(linkUser);
        inform.setUsered(user);
        informService.saveInform(inform);
        model.addAttribute("users",userInfoService.listUser(search));
        model.addAttribute("search",search);
        return "/admin/sendInform::user";
    }

}
