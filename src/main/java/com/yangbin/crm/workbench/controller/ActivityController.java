package com.yangbin.crm.workbench.controller;

import com.yangbin.crm.settings.domain.User;
import com.yangbin.crm.settings.service.UserService;
import com.yangbin.crm.util.DateTimeUtil;
import com.yangbin.crm.util.UUIDUtil;
import com.yangbin.crm.vo.PageListVo;
import com.yangbin.crm.workbench.domain.Activity;
import com.yangbin.crm.workbench.domain.ActivityRemark;
import com.yangbin.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private UserService userService;

    @Resource
    private ActivityService activityService;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }


    @RequestMapping("/saveActivity.do")
    @ResponseBody
    public boolean saveActivity(Activity activity, HttpServletRequest request){

        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        boolean flag = activityService.saveActivity(activity);
        return flag;
    }

    @RequestMapping("/getPageList.do")
    @ResponseBody
    public PageListVo<Activity> getPageList(String pageStrNo, String pageStrSize, String name, String owner,
                                            String startDate, String endDate){
        int pageNo = Integer.valueOf(pageStrNo);
        int pageSize = Integer.valueOf(pageStrSize);
        int pageCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("pageCount",pageCount);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        PageListVo<Activity> result = activityService.getPageList(map);
        return result;
    }

    @RequestMapping("/getActivityAndUserList.do")
    @ResponseBody
    public Map<String,Object> getActivityAndUserList(String id){
        Map<String,Object> map =new HashMap<>();
        List<User> uList = userService.getUserList();
        Activity activity = activityService.getActivityById(id);
        map.put("uList",uList);
        map.put("activity",activity);
        return map;
    }

    @RequestMapping("/updateActivity.do")
    @ResponseBody
    public boolean updateActivity(Activity activity, HttpServletRequest request){

        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = activityService.updateActivity(activity);
        return flag;
    }

    @RequestMapping("/deleteActivity.do")
    @ResponseBody
    public boolean deleteActivity(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        boolean flag = activityService.deleteActivity(ids);
        return flag;
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Activity activity = activityService.detail(id);
        mv.addObject("activity",activity);
        mv.setViewName("/activity/detail");
        return mv;
    }

    @RequestMapping("/getRemarkList.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkList(String aId){
        List<ActivityRemark> remarkList = activityService.getRemarkList(aId);
        return remarkList;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(String activityId,String noteContent,HttpServletRequest request){
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setActivityId(activityId);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditFlag("0");
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setId(UUIDUtil.getUUID());
        boolean flag = activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("remark",activityRemark);
        return map;
    }

    @RequestMapping("/deleteRemark.do")
    @ResponseBody
    public boolean deleteRemark(String id){
        boolean flag = activityService.deleteRemark(id);
        return flag;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(String id,String noteContent,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setNoteContent(noteContent);
        activityRemark.setId(id);
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        activityRemark.setEditFlag("1");
        boolean flag = activityService.updateRemark(activityRemark);
        map.put("success",flag);
        map.put("remark",activityRemark);
        return map;
    }
}
