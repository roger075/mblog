package com.lin.web;


import com.lin.po.*;
import com.lin.po.Module;
import com.lin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class IndexController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InformService informService;

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;


    @GetMapping("/")
    public String index(Model model){
        List<Module> moduleList = moduleService.findName();
        User linkUser = userInfoService.getLinkUser();
        if(linkUser!=null){
            List<Role> roles = linkUser.getRoles();
            if(roles!=null){
                for (Role role:roles){
                    if(role.getRole().equals("ADMIN")){
                        model.addAttribute("admin",1);
                        break;
                    }
                }
            }
        }
        model.addAttribute("moduleList",moduleList);
        model.addAttribute("moduleActive",moduleList.get(0).getContent());
        Map<Long,String> topFiveTag = tagService.getTopFiveTag();
        model.addAttribute("topFiveTag",topFiveTag);
        List<Blog> blogList = blogService.findFirst10Blog("发布",moduleList.get(0),0, 10);
        model.addAttribute("blogList",blogList);
        model.addAttribute("page",1);
        int a = blogService.getOpenBlogAmount("发布",moduleList.get(0));
        int b = a%10;
        model.addAttribute("pageSum",b>0?a/10+1:a/10);
        System.out.println("------index------");
        return "index";
    }


    @PostMapping("/index")
    public String indexPage(@RequestParam("type")String type,
                            @RequestParam("page")String page,
                            Model model){
        List<Module> moduleList = moduleService.findName();
        Module module = moduleService.findModuleByName(type);
        model.addAttribute("moduleList",moduleList);
        model.addAttribute("moduleActive",type);
        Map<Long,String> topFiveTag = tagService.getTopFiveTag();
        model.addAttribute("topFiveTag",topFiveTag);
        List<Blog> blogList = blogService.findFirst10Blog("发布",module,Integer.valueOf(page)-1, 10);
        model.addAttribute("blogList",blogList);
        model.addAttribute("page",page);
        int a = blogService.getOpenBlogAmount("发布",module);
        int b = a%10;
        model.addAttribute("pageSum",b>0?a/10+1:a/10);
        return "index::main";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchContent")String searchContent,
                         Model model){
        Sort sort = Sort.by("updateTime");
        Pageable pageable = PageRequest.of(0,10,sort);
        Page<Blog> blogPage = blogService.listBlog(searchContent,pageable);
        model.addAttribute("page",blogService.listBlog(searchContent,pageable));
        model.addAttribute("query",searchContent);
        model.addAttribute("typeActive","博文");
        return "search";
    }

    @PostMapping("/search")
    public String searchChange(@RequestParam("searchContent")String searchContent,
                         @RequestParam("type")String type,
                         @RequestParam("page")int page,
                         Model model){
        model.addAttribute("query",searchContent);
        model.addAttribute("typeActive",type);
        if(type.equals("博文")){
            Sort sort = Sort.by("updateTime");
            Pageable pageable = PageRequest.of(page-1,10,sort);
            model.addAttribute("page",blogService.listBlog(searchContent,pageable));
        } else {
            Pageable pageable = PageRequest.of(page-1,10);
            model.addAttribute("page",userInfoService.listUser(searchContent,pageable));
        }
        return "search::main";
    }






    @PostMapping("/index/getUser")
    @ResponseBody
    public Map<String,String> getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Map<String,String> map = new HashMap<>();
        if(username.equals("anonymousUser")){
            map.put("message",username);
            return map;
        } else {
            User user = userInfoService.findByUsername(username);
            map.put("message","获取成功");
            map.put("headPortrait",user.getHeadPortrait());
            map.put("informs",informService.getUnReadInform(user.getId())+"");
            return map;
        }
    }


}
