package com.yangbin.crm.settings.controller;

import com.yangbin.crm.exception.LoginException;
import com.yangbin.crm.settings.domain.DicValue;
import com.yangbin.crm.settings.domain.User;
import com.yangbin.crm.settings.service.DicService;
import com.yangbin.crm.settings.service.UserService;
import com.yangbin.crm.util.DateTimeUtil;
import com.yangbin.crm.util.MD5Util;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/settings/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private DicService dicService;

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String, Object> login(String loginAct, String loginPwd, HttpServletRequest request) throws LoginException {
        String loginPwdEncryption = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();
        Map<String,String> map = new ManagedMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwdEncryption);
        map.put("ip",ip);
        User user = userService.login(map);
        request.getSession().setAttribute("user",user);
        Map<String,Object> data = new HashMap<>();
        if (user == null){
            throw new LoginException("用户名或密码错误");
        }
        String nowTime = DateTimeUtil.getSysTime();
        String expireTime = user.getExpireTime();
        if (nowTime.compareTo(expireTime)>0){
            throw new LoginException("该账号权限已过期");
        }
        if ("0".equals(user.getLockState())){
            throw new LoginException("该账号已被锁定");
        }
        if (!user.getAllowIps().contains(ip)){
            throw new LoginException("非法登录ip");
        }
        data.put("success",true);
        return data;
    }

    public void getDicValue(HttpServletRequest request){

        ServletContext application = request.getServletContext();
        Map<String,Object> pMap = new HashMap<>();
        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> set = map.keySet();
        for (String key:set) {
            application.setAttribute(key,map.get(key));
        }
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility.properties");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        request.getServletContext().setAttribute("pMap",pMap);
    }
}
