package com.yangbin.crm.workbench.controller;

import com.yangbin.crm.settings.domain.User;
import com.yangbin.crm.settings.service.UserService;
import com.yangbin.crm.util.DateTimeUtil;
import com.yangbin.crm.util.UUIDUtil;
import com.yangbin.crm.vo.PageListVo;
import com.yangbin.crm.workbench.domain.Activity;
import com.yangbin.crm.workbench.domain.Clue;
import com.yangbin.crm.workbench.domain.ClueRemark;
import com.yangbin.crm.workbench.domain.Tran;
import com.yangbin.crm.workbench.service.ActivityService;
import com.yangbin.crm.workbench.service.ClueService;
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
@RequestMapping("/workbench/clue")
public class ClueController {

    @Resource
    private ClueService clueService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;

    @RequestMapping("/getCluePageList.do")
    @ResponseBody
    public Map<String,Object> getCluePageList(String strPageNo,String strPageSize,String fullname,String company,
                                            String phone,String source,String mphone,String state){
        int pageNo = Integer.valueOf(strPageNo);
        int pageSize = Integer.valueOf(strPageSize);
        int pageCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("pageCount",pageCount);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("mphone",mphone);
        map.put("stage",state);
        Map<String,Object> dataMap = clueService.getCluePageList(map);
        return dataMap;
    }

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    @RequestMapping("/saveClue.do")
    @ResponseBody
    public boolean saveClue(Clue clue, HttpServletRequest request){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        boolean data = clueService.saveClue(clue);
        return data;
    }

    @RequestMapping("/getUListAndClueById.do")
    @ResponseBody
    public Map<String,Object> getUListAndClueById(String id){
        Map<String,Object> map = new HashMap<>();
        List<User> uList = userService.getUserList();
        Clue clue = clueService.getClueById(id);
        map.put("uList",uList);
        map.put("clue",clue);
        return map;
    }

    @RequestMapping("/updateClue.do")
    @ResponseBody
    public boolean updateClue(Clue clue,HttpServletRequest request){
        clue.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        clue.setEditTime(DateTimeUtil.getSysTime());
        boolean data = clueService.updateClue(clue);
        return data;
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.detail(id);
        mv.addObject("clue",clue);
        mv.setViewName("forward:/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping("/deleteClue.do")
    @ResponseBody
    public boolean deleteClue(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        boolean data = clueService.deleteClue(ids);

        return data;
    }

    @RequestMapping("/getClueRemarkList.do")
    @ResponseBody
    public List<ClueRemark> getClueRemarkList(String clueId){
        List<ClueRemark> clueRemarkList = clueService.getClueRemarkList(clueId);
        return clueRemarkList;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(ClueRemark clueRemark,HttpServletRequest request){
        clueRemark.setId(UUIDUtil.getUUID());
        clueRemark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        clueRemark.setCreateTime(DateTimeUtil.getSysTime());
        clueRemark.setEditFlag("0");
        Map<String,Object> map = clueService.saveRemark(clueRemark);
        return map;
    }

    @RequestMapping("/getBundList.do")
    @ResponseBody
    public List<Activity> getBundList(String clueId){
        List<Activity> activityList = clueService.getBundList(clueId);
        return activityList;
    }

    @RequestMapping("/getActivityListByNameAndClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndClueId(String clueId,String aname){
        Map<String,String> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("aname",aname);
        List<Activity> activityList = clueService.getActivityListByNameAndClueId(map);
        return activityList;
    }

    @RequestMapping("/bund.do")
    @ResponseBody
    public boolean bund(HttpServletRequest request){
        String clueId = request.getParameter("clueId");
        String[] aids = request.getParameterValues("aid");
        boolean flag = clueService.bund(clueId,aids);
        return flag;
    }

    @RequestMapping("/unbund.do")
    @ResponseBody
    public boolean unbund(String id){
        boolean flag = clueService.unbund(id);
        return flag;
    }

    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        List<Activity> activityList = activityService.getActivityListByName(aname);
        return activityList;
    }

    @RequestMapping("/convert.do")
    public ModelAndView convert(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        Tran tran = null;
        if ("a".equals(flag)){//需要创建交易
            tran = new Tran();
            tran.setId(UUIDUtil.getUUID());
            tran.setName(request.getParameter("name"));
            tran.setMoney(request.getParameter("money"));
            tran.setExpectedDate(request.getParameter("expectedDate"));
            tran.setStage(request.getParameter("stage"));
            tran.setCreateBy(createBy);
            tran.setCreateTime(DateTimeUtil.getSysTime());
        }
        boolean flag1 = clueService.convert(clueId,tran,createBy);
        if (flag1){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }/*else {
            mv.setViewName("redirect:/workbench/clue/convert.jsp");
        }*/
        return mv;
    }
}
