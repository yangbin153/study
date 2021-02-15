package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int saveActivity(Activity activity);

    List<Activity> getActivityList(Map<String, Object> map);

    int getTotal(Map<String, Object> map);

    Activity getActivityById(String id);

    int updateActivity(Activity activity);

    int deleteActivity(String id);

    Activity detail(String id);

    int deleteActivityByIds(String[] ids);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
