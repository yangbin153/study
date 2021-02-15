package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    List<ActivityRemark> getRemarkList(String aId);

    int saveRemark(ActivityRemark activityRemark);

    int deleteRemark(String id);

    int updateRemark(ActivityRemark activityRemark);

    int getRemarkCount(String[] ids);

    int deleteRemarkByIds(String[] ids);
}
