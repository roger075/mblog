package com.lin.web;


import com.alibaba.fastjson.JSONObject;
import com.lin.Utils.MailUitls;
import com.lin.po.*;
import com.lin.service.BlogService;
import com.lin.service.InformService;
import com.lin.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.SQLException;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InformService informService;

    @Autowired
    private BlogService blogService;

    private int code;


    @PostMapping("/user/concern")
    @ResponseBody
    @Transactional
    public Map<String,String> addConcern(@RequestParam("concerned") String concerned,
                                         @RequestParam("type") int type){
        Long userId = Long.valueOf(concerned);
        User usered = userInfoService.findById(userId);
        User user = userInfoService.getLinkUser();
        Map<String, String> map = new HashMap<>();
        if(user.getId()==usered.getId()){
            map.put("message","本人不可以关注自己的博文！");
            return map;
        }
        if (type == 1) {
            Concern concern = new Concern();
            concern.setCreatTime(new Date());
            concern.setUsered(usered);
            concern.setUser(user);
            userInfoService.saveConcern(concern);
            map.put("message", "关注成功");
            return map;
        } else {
            Concern concern = userInfoService.findConcern(user.getId(), usered.getId());
            userInfoService.deleteConcern(concern);
            map.put("message", "取消关注成功");
            return map;
        }
    }

    @PostMapping("/user/blackList")
    @ResponseBody
    @Transactional
    public Map<String,String> addBlackList(@RequestParam("blackListed") String blackListed,
                                         @RequestParam("type") int type){
        Long userId = Long.valueOf(blackListed);
        User usered = userInfoService.findById(userId);
        User user = userInfoService.getLinkUser();
        Map<String, String> map = new HashMap<>();
        if(user.getId()==usered.getId()){
            map.put("message","本人不可以拉黑自己！");
            return map;
        }
        if (type == 1) {
            BlackList blackList = new BlackList();
            blackList.setCreatTime(new Date());
            blackList.setUsered(usered);
            blackList.setUser(user);
            userInfoService.saveBlackList(blackList);
            map.put("message", "拉黑成功");
            return map;
        } else {
            BlackList blackList = userInfoService.findBlackList(user.getId(), usered.getId());
            userInfoService.deleteBlackList(blackList);
            map.put("message", "取消拉黑成功");
            return map;
        }
    }

    @GetMapping("/modifyInfo")
    public String modifyInfo(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("Active","基本信息");
        model.addAttribute("nickname",linkUser.getNickname());
        model.addAttribute("email",linkUser.getEmail());
        model.addAttribute("description",linkUser.getDescription());
        return "/user/modifyInfo";
    }

    @GetMapping("/modifyIdent")
    public String modifyIdent(Model model){
        model.addAttribute("Active","教师认证");
        return "/user/modifyInfo";
    }

    @GetMapping("/modifyPSW")
    public String modifyPSW(Model model){
        model.addAttribute("Active","修改密码");
        return "/user/modifyInfo";
    }



    @PostMapping("/user/modifyUserInfo")
    @Transactional
    public String modify(User user,Model model){
        User linkUser = userInfoService.getLinkUser();
        linkUser.setEmail(user.getEmail());
        linkUser.setDescription(user.getDescription());
        linkUser.setNickname(user.getNickname());
        userInfoService.saveUser(linkUser);
        model.addAttribute("message","修改信息成功");
        model.addAttribute("Active","基本信息");
        model.addAttribute("Active","基本信息");
        model.addAttribute("nickname",linkUser.getNickname());
        model.addAttribute("email",linkUser.getEmail());
        model.addAttribute("description",linkUser.getDescription());
        return "/user/modifyInfo";
    }

    @PostMapping("/user/modifyPSW")
    @Transactional
    public String modifyPWD(@RequestParam("oldPSW")String oldPSW,
                            @RequestParam("newPSW")String newPSW,
                            @RequestParam("rePSW")String rePSW,Model model){
        User linkUser = userInfoService.getLinkUser();
        if(passwordEncoder.matches(oldPSW,linkUser.getPassword())){
            if(!rePSW.equals(newPSW))
                model.addAttribute("message", "修改失败，请重复输入两次新密码");
            else{
                if (newPSW.equals(oldPSW)) {
                    model.addAttribute("message", "新密码和旧密码相同，修改失败");
                } else {
                    linkUser.setPassword(passwordEncoder.encode(newPSW));
                    userInfoService.saveUser(linkUser);
                    model.addAttribute("message","密码修改成功");
                }
            }
        }
        else {
            model.addAttribute("message","旧密码错误");
        }
        model.addAttribute("Active","修改密码");
        return "/user/modifyInfo";
    }

    @PostMapping("/user/identify")
    @Transactional
    public String identifyTeacher(@RequestParam("name")String name,
                                  @RequestParam("IDNumber")String IdNumber,
                                  Model model){
        Teacher teacher = userInfoService.findTeacherByIdNumber(Long.valueOf(IdNumber));
        if(teacher==null){
            model.addAttribute("message","认证失败，身份错误或不存在");
        } else {
            if(name.equals(teacher.getTeacherName())){
                User linkUser = userInfoService.getLinkUser();
                linkUser.setTeacher(teacher);
                userInfoService.saveUser(linkUser);
                model.addAttribute("message","认证成功");
            } else {
                model.addAttribute("message","认证失败，信息不匹配");
            }
        }
        model.addAttribute("Active","教师认证");
        return "/user/modifyInfo";
    }


    @PostMapping("/user/modifyChange")
    public String modifyChange(@RequestParam("type")int type,Model model){
        if(type==0){
            User linkUser = userInfoService.getLinkUser();
            model.addAttribute("Active","基本信息");
            model.addAttribute("nickname",linkUser.getNickname());
            model.addAttribute("email",linkUser.getEmail());
            model.addAttribute("description",linkUser.getDescription());
        } else if(type==1){
            model.addAttribute("Active","修改密码");
        } else {
            model.addAttribute("Active","教师认证");
        }
        return "/user/modifyInfo::main";
    }

    @GetMapping("/user/showInfo")
    public String showUserInfo(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("user",linkUser);
        return "/user/showInfo";
    }

    @GetMapping("/user/showConcern")
    public String showConcern(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("concerns",linkUser.getConcernList());
        return "/user/showConcern";
    }

    @PostMapping("deleteConcern")
    public String deleteConcern(@RequestParam("commentId")Long concernId,Model model){
        User linkUser = userInfoService.getLinkUser();
        Concern concern = userInfoService.findConcern(linkUser.getId(), concernId);
        userInfoService.deleteConcern(concern);
        model.addAttribute("concerns",linkUser.getConcernList());
        return "/user/showConcern::main";
    }




    @GetMapping("/user/showBlackList")
    public String showBlackList(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("blacklists",linkUser.getBlackListedList());
        return "/user/showBlackList";
    }

    @PostMapping("deleteBlackList")
    public String deleteBlackList(@RequestParam("blackedId")Long blackedId,Model model){
        User linkUser = userInfoService.getLinkUser();
        BlackList blackList = userInfoService.findBlackList(linkUser.getId(),blackedId);
        userInfoService.deleteBlackList(blackList);
        model.addAttribute("blacklists",linkUser.getBlackListList());
        return "/user/showBlackList::main";
    }

    @GetMapping("/user/showCollect")
    public String showCollect(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("collects",linkUser.getCollectList());
        return "/user/showCollect";
    }

    @PostMapping("/deleteCollect")
    public String deleteCollect(@RequestParam("commentId")Long collectId,Model model){
        User linkUser = userInfoService.getLinkUser();
        Collect collect = blogService.findCollect(blogService.findById(collectId),linkUser);
        blogService.deleteCollect(collect);
        model.addAttribute("collects",linkUser.getCollectList());
        return "/user/showCollect::main";
    }


    @GetMapping("/user/showInform")
    public String showInform(Model model){
        User linkUser = userInfoService.getLinkUser();
        model.addAttribute("informs",linkUser.getInformedList());
        return "/user/showInform";
    }

    @PostMapping("/readInform")
    public String readInform(@RequestParam("informId")Long informId,Model model){
        User linkUser = userInfoService.getLinkUser();
        Inform byId = informService.findById(informId);
        byId.setReaded((byte)1);
        informService.saveInform(byId);
        model.addAttribute("informs",linkUser.getInformedList());
        return "/user/showInform::main";
    }

    @PostMapping("/deleteInform")
    public String deleteInform(@RequestParam("informId")Long informId,Model model){
        User linkUser = userInfoService.getLinkUser();
        Inform byId = informService.findById(informId);
        informService.deleteInform(byId);
        model.addAttribute("informs",linkUser.getInformedList());
        return "/user/showInform::main";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/sign-up")
    public String signUp(){
        return "sign-up";
    }

    @PostMapping("/sign/save")
    public String sign(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String nickname,
                       @RequestParam String email)throws Exception{
        UserDetails userDetails = userInfoService.findByUsername(username);
        if (userDetails == null) {
            User user = new User();
            List<Role> roles = new ArrayList<>();
            Role role = userInfoService.findRole("USER");
            roles.add(role);
            user.setRoles(roles);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setNickname(nickname);
            user.setHeadPortrait("/upload/initial.jpg");
            user.setEmail(email);
            user.setLocked((byte) 1);
            user.setCreatTime(new Date());
            user.setDescription(null);
            user.setTeacher(null);
            if(userInfoService.saveUser(user)){
                return "login";
            } else {
                SQLException e = new SQLException("数据库错误，注册失败");
                throw e;
            }

        } else {
            SQLException e = new SQLException("用户名已存在，注册失败");
            throw e;
        }
    }

    @PostMapping("/sign/checkUser")
    @ResponseBody
    public Map<String,String> checkUser(@RequestParam String username,@RequestParam("email")String email){
        UserDetails userDetails = userInfoService.findByUsername(username);
        User user = userInfoService.findByEmail(email);
        Map<String,String> map = new HashMap<>();
        if (userDetails != null) {
            map.put("message","用户名已存在");
        }
        else if(user!=null){
            map.put("message","email已存在");
        } else {
            map.put("message","用户可用");
        }
        return map;
    }

    @GetMapping("/sign/retrieve")
    public String retrievePwd(Model model){
        model.addAttribute("type","username");
        model.addAttribute("action","/sign/reset");
        return "retrieve";
    }

    @PostMapping("/sign/reset")
    public String resetPwd(@RequestParam("username")String username,
                           @RequestParam("email")String email,
                           @RequestParam("code")int code,
                           Model model){
        model.addAttribute("username",username);
        model.addAttribute("type","重置");
        model.addAttribute("action","/sign/resetPwd");
        return "retrieve";
    }

    @PostMapping("/sign/resetPwd")
    public String reset(@RequestParam("username")String uername,
                        @RequestParam("password")String password){
        User user = userInfoService.findByUsername(uername);
        user.setPassword(passwordEncoder.encode(password));
        return "login";
    }


    @PostMapping("/sign/sendEmail")
    @ResponseBody
    public Map<String,String> sendEmail(@RequestParam("email")String email,
                                        @RequestParam("username")String username,
                                        Model model){
        Map<String,String> map = new HashMap<>();

        User byUsername = userInfoService.findByUsername(username);
        if(byUsername==null){
            map.put("result","error");
            map.put("message","用户名不存在");
            return map;
        } else {
            if(!email.equals(byUsername.getEmail())){
                map.put("result","error");
                map.put("message","用户名或Email错误");
                return map;
            }
        }
        code = (int)((Math.random()*9+1)*100000);
        MailUitls.sendEmailForResetPwd(email,code);
        map.put("result","success");
        map.put("message",code+"");
        return map;
    }



    @RequestMapping(value = "/upload/headPortrait")
    @ResponseBody
    public JSONObject upLoadImg(@RequestParam(value = "avatar_file",required = true) MultipartFile file,
                                @RequestParam("userId")Long userId) throws Exception  {
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
        User linkUser = userInfoService.findById(userId);
        linkUser.setHeadPortrait("/upload/img/"+fileName);
        userInfoService.saveUser(linkUser);
        JSONObject res = new JSONObject();
        res.put("result", "/upload/img/"+fileName);
        res.put("success", 1);
        res.put("message", "upload success!");
        return res;
    }


}
