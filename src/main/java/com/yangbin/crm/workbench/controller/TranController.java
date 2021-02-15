package com.yangbin.crm.workbench.controller;

import com.sun.corba.se.spi.servicecontext.ServiceContext;
import com.yangbin.crm.settings.domain.User;
import com.yangbin.crm.settings.service.UserService;
import com.yangbin.crm.util.DateTimeUtil;
import com.yangbin.crm.util.UUIDUtil;
import com.yangbin.crm.workbench.dao.CustomerDao;
import com.yangbin.crm.workbench.domain.*;
import com.yangbin.crm.workbench.service.ActivityService;
import com.yangbin.crm.workbench.service.ClueService;
import com.yangbin.crm.workbench.service.ContactsService;
import com.yangbin.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/workbench/tran")
@Controller
public class TranController {

    @Resource
    private TranService tranService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private CustomerDao customerDao;

    @RequestMapping("/getTranList.do")
    @ResponseBody
    public Map<String,Object> getTranList(String pageNoStr,String pageSizeStr,String owner,String name,String company,
                                          String stage,String transactionType,String source,String contacts){
        Map<String,Object> mapIn = new HashMap<>();
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        int ignoreCount = (pageNo-1)*pageSize;
        mapIn.put("pageSize",pageSize);
        mapIn.put("ignoreCount",ignoreCount);
        mapIn.put("owner",owner);
        mapIn.put("name",name);
        mapIn.put("company",company);
        mapIn.put("stage",stage);
        mapIn.put("transactionType",transactionType);
        mapIn.put("source",source);
        mapIn.put("contacts",contacts);

        Map<String,Object> map = tranService.getTranList(mapIn);
        return map;
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    public ModelAndView detail(String id,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        Tran tran = tranService.getTranById(id);
        String stage = tran.getStage();
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        tran.setPossibility(pMap.get(stage));
        mv.addObject("tran",tran);
        mv.setViewName("/transaction/detail");
        return mv;
    }

    @RequestMapping("/getRemarkList.do")
    @ResponseBody
    public List<TranRemark> getRemarkList(String tranId){
        List<TranRemark> remarkList = tranService.getRemarkList(tranId);
        return remarkList;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(TranRemark tranRemark, HttpServletRequest request){
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        tranRemark.setId(UUIDUtil.getUUID());
        tranRemark.setEditFlag("0");
        tranRemark.setCreateTime(DateTimeUtil.getSysTime());
        tranRemark.setCreateBy(createBy);
        Map<String,Object> map = tranService.saveRemark(tranRemark);
        return map;
    }


    @RequestMapping("/getTranHistoryList.do")
    @ResponseBody
    public List<TranHistory> getTranHistoryList(String tranId,HttpServletRequest request){
        List<TranHistory> historyList = tranService.getTranHistoryList(tranId);
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        List<TranHistory> result = new ArrayList<>();
        for (int i = 0; i < historyList.size(); i++) {
            TranHistory tranHistory = historyList.get(i);
            tranHistory.setPossibility(pMap.get(tranHistory.getStage()));
            result.add(tranHistory);
        }
        return result;
    }

    @RequestMapping("/createTran.do")
    public ModelAndView createTran(){
        ModelAndView mv = new ModelAndView();
        List<User> uList = userService.getUserList();
        mv.addObject("uList",uList);
        mv.setViewName("/transaction/save");
        return mv;
    }

    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        List<Activity> activityList = activityService.getActivityListByName(aname);
        return activityList;
    }

    @RequestMapping("/getContactsListByName.do")
    @ResponseBody
    public List<Contacts> getContactsListByName(String cname){
        List<Contacts> contactsList = contactsService.getContactsListByName(cname);
        return contactsList;
    }

    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        List<String> customerNameList = customerDao.getCustomerName(name);
        return customerNameList;
    }

    @RequestMapping("/saveTran.do")
    public ModelAndView saveTran(Tran tran ,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setCreateBy(createBy);
        boolean flag = tranService.saveTran(tran);
        mv.setViewName("/transaction/index");
        return mv;
    }

    @RequestMapping("/changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(Tran tran,HttpServletRequest request){
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        tran.setEditBy(editBy);
        tran.setEditTime(DateTimeUtil.getSysTime());
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        boolean flag = tranService.changeStage(tran);
        Map<String,Object> map = new HashMap<>();
        map.put("tran",tran);
        map.put("success",flag);
        return map;
    }
}
