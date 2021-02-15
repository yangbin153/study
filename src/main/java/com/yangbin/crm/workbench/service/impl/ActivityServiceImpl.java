package com.yangbin.crm.workbench.service.impl;

import com.yangbin.crm.vo.PageListVo;
import com.yangbin.crm.workbench.dao.ActivityDao;
import com.yangbin.crm.workbench.dao.ActivityRemarkDao;
import com.yangbin.crm.workbench.domain.Activity;
import com.yangbin.crm.workbench.domain.ActivityRemark;
import com.yangbin.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityRemarkDao activityRemarkDao;


    @Override
    public boolean saveActivity(Activity activity) {
        boolean flag = true;
        int count = activityDao.saveActivity(activity);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PageListVo<Activity> getPageList(Map<String, Object> map) {



        List<Activity> dataList = activityDao.getActivityList(map);
        int total = activityDao.getTotal(map);

        PageListVo<Activity> pageListVo = new PageListVo<>();
        pageListVo.setDateList(dataList);
        pageListVo.setTotal(total);

        return pageListVo;
    }



    @Override
    public Activity getActivityById(String id) {
        Activity activity = activityDao.getActivityById(id);
        return activity;
    }

    @Override
    public boolean updateActivity(Activity activity) {
        boolean flag = true;
        int count = activityDao.updateActivity(activity);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean deleteActivity(String[] ids) {
        boolean flag = true;

        int count1 = activityRemarkDao.getRemarkCount(ids);
        int count2 = activityRemarkDao.deleteRemarkByIds(ids);

        if (count1 != count2){
            flag = false;
        }

        int count3 = activityDao.deleteActivityByIds(ids);
        if (count3 != ids.length){
            flag = false;
        }
        return flag;

    }

    @Override
    public Activity detail(String id) {

        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkList(String aId) {
        List<ActivityRemark> remarkList = activityRemarkDao.getRemarkList(aId);


        return remarkList;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count != 1 ){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {

        boolean flag = true;



        int count = activityRemarkDao.updateRemark(activityRemark);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> activityList = activityDao.getActivityListByName(aname);

        return activityList;
    }
}
