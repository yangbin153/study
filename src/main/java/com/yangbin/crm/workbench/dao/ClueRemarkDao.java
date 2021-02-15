package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int getCount(String[] ids);

    int deleteRemark(String[] ids);

    List<ClueRemark> getClueRemarkList(String clueId);

    int saveRemark(ClueRemark clueRemark);

    int deleteRemarkByClueId(String clueId);
}
