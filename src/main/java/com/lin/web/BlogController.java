package com.lin.web;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lin.po.*;
import com.lin.service.*;


import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ModuleService moduleService;

    @GetMapping("/user/write")
    public String writeBlog(){
        return "/user/write";
    }


    @PostMapping("/user/saveBlog")
    @ResponseBody
    @Transactional
    public Map<String,String> saveBlog(@RequestParam(name = "copyright") String copyright,
                                       @RequestParam String title,
                                       @RequestParam(name = "markdown-text-markdown-doc") String content,
                                       @RequestParam String module,
                                       @RequestParam String tag1,
                                       @RequestParam String tag2,
                                       @RequestParam String tag3,
                                       @RequestParam String tag4,
                                       @RequestParam String tag5,
                                       @RequestParam(name = "frequency") String open,
                                       @RequestParam String comment,
                                       @RequestParam(name = "digest") String digest,
                                       @RequestParam("manuscript")String manuscript){
        Map<String,String> map = new HashMap<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Blog blog = new Blog(title,null,copyright,content,new Date(),0L,0L,open,comment,moduleService.findModuleByName(module));
        blog.setUser(user);
        blog.setDigest(digest);
        List<Tag>  tagList = new ArrayList<>();
        tagService.tagAddList(tagList,tag1);
        tagService.tagAddList(tagList,tag2);
        tagService.tagAddList(tagList,tag3);
        tagService.tagAddList(tagList,tag4);
        tagService.tagAddList(tagList,tag5);
        blog.setTagList(tagList);
        blog.setManuscript(manuscript);
        blogService.saveBlog(blog);
        map.put("message","success");
        return map;
    }


    @PostMapping("/user/modifyBlog")
    @ResponseBody
    @Transactional
    public Map<String,String> modifyBlog(@RequestParam(name = "copyright") String copyright,
                                       @RequestParam String title,
                                       @RequestParam(name = "markdown-text-markdown-doc") String content,
                                       @RequestParam String module,
                                       @RequestParam String tag1,
                                       @RequestParam String tag2,
                                       @RequestParam String tag3,
                                       @RequestParam String tag4,
                                       @RequestParam String tag5,
                                       @RequestParam(name = "frequency") String open,
                                       @RequestParam String comment,
                                       @RequestParam(name = "digest") String digest,
                                       @RequestParam("manuscript")String manuscript,
                                       @RequestParam("blogId")Long blogId){
        Map<String,String> map = new HashMap<>();
        User user = userInfoService.getLinkUser();
        Blog blog = blogService.findById(blogId);
        blog.setDigest(digest);
        blog.setManuscript(manuscript);
        blog.setComment(comment);
        blog.setOpen(open);
        blog.setModule(moduleService.findModuleByName(module));
        blog.setTitle(title);
        blog.setOriginal(copyright);
        blog.setUpdateTime(new Date());
        blog.setContent(content);
        List<Tag>  tagList = new ArrayList<>();
        tagService.tagAddList(tagList,tag1);
        tagService.tagAddList(tagList,tag2);
        tagService.tagAddList(tagList,tag3);
        tagService.tagAddList(tagList,tag4);
        tagService.tagAddList(tagList,tag5);
        blog.setTagList(tagList);
        blogService.saveBlog(blog);
        map.put("message","success");
        return map;
    }

    @GetMapping("/blog/user")
    public String personBlog(@RequestParam(name = "userId")Long userId,
                             Model model){
        User usered = userInfoService.findById(userId);
        if(usered!=null){
            User user = userInfoService.getLinkUser();
            model.addAttribute("username",usered.getNickname());
            model.addAttribute("userId",usered.getId());
            model.addAttribute("userFans",usered.getConcernedList().size());
            model.addAttribute("userHeadPortrait",usered.getHeadPortrait());
            model.addAttribute("email",usered.getEmail());
            model.addAttribute("userDescription",usered.getDescription());
            if(user==null){
                model.addAttribute("concern","关注");
                model.addAttribute("blackList","拉黑");
            } else {
                Concern concern = userInfoService.findConcern(user.getId(), usered.getId());
                BlackList blackList = userInfoService.findBlackList(user.getId(),usered.getId());
                if(concern==null){
                    model.addAttribute("concern","关注");
                } else {
                    model.addAttribute("concern","已关注");
                }
                if(blackList==null){
                    model.addAttribute("blackList","拉黑");
                } else {
                    model.addAttribute("blackList","已拉黑");
                }
            }
            model.addAttribute("blogList",blogService.getPersonBlogList(userId,user));
        }
        return "personBlog";
    }





    @GetMapping("/blog/Detail")
    @Transactional
    public String getBlog(@RequestParam String blogId, Model model){
        Blog blog = blogService.findById(Long.valueOf(blogId));
        blog.increaseViews();
        blogService.saveBlog(blog);
        User user = userInfoService.getLinkUser();
        if(user==null){
            model.addAttribute("concern","关注");
            model.addAttribute("blackList","拉黑");
            model.addAttribute("collect","收藏");
            model.addAttribute("report","举报");
        } else {
            Concern concern = userInfoService.findConcern(user.getId(), blog.getUser().getId());
            BlackList blackList = userInfoService.findBlackList(user.getId(),blog.getUser().getId());
            Collect collect = blogService.findCollect(blog,user);
            Report report = reportService.findReport(blog,user);
            if(concern==null){
                model.addAttribute("concern","关注");
            } else {
                model.addAttribute("concern","已关注");
            }
            if(blackList==null){
                model.addAttribute("blackList","拉黑");
            } else {
                model.addAttribute("blackList","已拉黑");
            }
            if(collect==null)
                model.addAttribute("collect","收藏");
            else
                model.addAttribute("collect","已收藏");
            if(report==null)
                model.addAttribute("report","举报");
            else
                model.addAttribute("report","已举报");
        }
        String comment = blog.getComment();
        if(Integer.valueOf(comment)==1)
            model.addAttribute("comments",commentService.getListCommentByBlogId(blog.getId()));
        else
            model.addAttribute("comments",0);
        model.addAttribute("blogId",blogId);
        model.addAttribute("email",blog.getUser().getEmail());
        model.addAttribute("blogAmount","文章:"+blog.getUser().getBlogList().size());
        model.addAttribute("title",blog.getTitle());
        model.addAttribute("username",blog.getUser().getNickname());
        model.addAttribute("userId",blog.getUser().getId());
        model.addAttribute("userFans",blog.getUser().getConcernedList().size());
        model.addAttribute("userHeadPortrait",blog.getUser().getHeadPortrait());
        model.addAttribute("content",blog.getContent());
        model.addAttribute("views",blog.getViews());
        model.addAttribute("likes",blog.getLikes());
        model.addAttribute("tagList",blog.getTagList());
        model.addAttribute("updateTime",blog.getUpdateTime());
        model.addAttribute("copyright",blog.getOriginal());
        model.addAttribute("userDescription",blog.getUser().getDescription());
        model.addAttribute("collects",blog.getCollectedList().size());
        model.addAttribute("teacher",blog.getUser().getTeacher());
        return "blog.html";
    }

    @PostMapping("/blog/getTags")
    @ResponseBody
    public String getTags(){
        List<String> tagList = tagService.getTags();
        String json = JSON.toJSONString(tagList);
        return json;
    }

    @PostMapping("/user/collect")
    @ResponseBody
    @Transactional
    public Map<String,String> addBlackList(@RequestParam("blogId") String blogId,
                                           @RequestParam("type") int type){
        Blog blog = blogService.findById(Long.valueOf(blogId));
        User user = userInfoService.getLinkUser();
        Map<String, String> map = new HashMap<>();
        if(user.getId()==blog.getUser().getId()){
            map.put("message","本人不可以收藏自己的博文！");
            return map;
        }
        if (type == 1) {
            Collect collect = new Collect();
            collect.setCreatTime(new Date());
            collect.setBlog(blog);
            collect.setUser(user);
            blogService.saveCollect(collect);
            map.put("message", "收藏成功");
            return map;
        } else {
            Collect collect = blogService.findCollect(blog, user);
            blogService.deleteCollect(collect);
            map.put("message", "取消收藏成功");
            return map;
        }
    }

    @PostMapping("/user/report")
    @ResponseBody
    @Transactional
    public Map<String,String> addBlogLikes(@RequestParam("blogId") String blogId,
                                           @RequestParam("type") int type,
                                           @RequestParam("reportContent") String reportContent){
        Blog blog = blogService.findById(Long.valueOf(blogId));
        User user = userInfoService.getLinkUser();
        Map<String, String> map = new HashMap<>();
        if(user.getId()==blog.getUser().getId()){
            map.put("message","本人不可以举报自己！");
            return map;
        }
        if (type == 1) {
            Report report = new Report();
            report.setBloged(blog);
            report.setContent(reportContent);
            report.setReportTime(new Date());
            report.setUser(user);
            reportService.saveReport(report);
            map.put("message", "举报成功");
            return map;
        } else {
            Report report = reportService.findReport(blog,user);
            reportService.deleteReport(report);
            map.put("message", "取消举报成功");
            return map;
        }
    }

    @PostMapping("/user/like")
    @ResponseBody
    @Transactional
    public Map<String,String> addBlogLikes(@RequestParam("blogId") String blogId,
                                           @RequestParam("type") int type){
        Blog blog = blogService.findById(Long.valueOf(blogId));
        User user = userInfoService.getLinkUser();
        Map<String, String> map = new HashMap<>();
        if(user.getId()==blog.getUser().getId()){
            map.put("message","本人不可以点赞自己的博文！");
            return map;
        }
        if (type == 1) {
            blog.increaseLikes();
            blogService.saveBlog(blog);
            map.put("message", "点赞成功");
            return map;
        } else {
            blog.decreaseLikes();
            blogService.saveBlog(blog);
            map.put("message", "取消点赞成功");
            return map;
        }
    }

    @GetMapping("/user/showBlog")
    public String showBlog(Model model){
        User linkUser = userInfoService.getLinkUser();
        List<Blog> blogList = linkUser.getBlogList();
        Collections.reverse(blogList);
        model.addAttribute("blogs",blogList);
        return "/user/showBlog";
    }

    @PostMapping("/user/deleteBlog")
    public String deleteBlog(@RequestParam("blogId")Long blogId,Model model){
        Blog blog = blogService.findById(blogId);
        blogService.deleteBlog(blog);
        User linkUser = userInfoService.getLinkUser();
        List<Blog> blogList = linkUser.getBlogList();
        Collections.reverse(blogList);
        model.addAttribute("blogs",blogList);
        return "/user/showBlog::main";
    }

    @GetMapping("/modifyBlog")
    public String toModifyBlog(@RequestParam("blogId")Long blogId,Model model){
        Blog blog = blogService.findById(blogId);
        model.addAttribute("blog",blog);
        return "/user/modifyBlog";
    }




    @RequestMapping(value = "/upload/img")
    @ResponseBody
    public JSONObject upLoadImg(@RequestParam(value = "editormd-image-file",required = true) MultipartFile file,
                                HttpServletRequest request, HttpServletResponse response) throws Exception  {
        String trueFileName = file.getOriginalFilename();

        String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));

        String fileName = System.currentTimeMillis()+"_"+Math.random()*1e4+suffix;

        String path = "D:/IdeaProject/mblog/src/main/resources/static/upload/img";
        System.out.println(path);

        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


        JSONObject res = new JSONObject();
        res.put("url", "/upload/img/"+fileName);
        res.put("success", 1);
        res.put("message", "upload success!");

        return res;
    }


    @GetMapping("/upload/img/{PicPath}")
    public void showPicture(@PathVariable(name = "PicPath")String picPath,HttpServletResponse response){
        try {
            String fileName = picPath;//图片名字
            String path="D:/IdeaProject/mblog/src/main/resources/static/upload/img/"+fileName;
            // 以byte流的方式打开文件 d:\1.gif
            FileInputStream hFile;
            hFile = new FileInputStream(path);
            //得到文件大小
            int i=hFile.available();
            byte data[]=new byte[i];
            //读数据
            hFile.read(data);
            response.setHeader("Content-Type","image/jpeg");
            //得到向客户端输出二进制数据的对象
            OutputStream toClient=response.getOutputStream();
            //输出数据
            toClient.write(data);
            toClient.flush();
            toClient.close();
            hFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
