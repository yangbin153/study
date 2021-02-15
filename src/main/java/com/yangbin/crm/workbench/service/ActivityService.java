package com.yangbin.crm.workbench.service;

import com.yangbin.crm.vo.PageListVo;
import com.yangbin.crm.workbench.domain.Activity;
import com.yangbin.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean saveActivity(Activity activity);

    PageListVo<Activity> getPageList(Map<String, Object> map);

    Activity getActivityById(String id);

    boolean updateActivity(Activity activity);

    boolean deleteActivity(String[] ids);

    Activity detail(String id);

    List<ActivityRemark> getRemarkList(String aId);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean deleteRemark(String id);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByName(String aname);
}
