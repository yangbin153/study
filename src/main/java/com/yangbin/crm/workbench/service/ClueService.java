package com.yangbin.crm.workbench.service;

import com.yangbin.crm.workbench.domain.Activity;
import com.yangbin.crm.workbench.domain.Clue;
import com.yangbin.crm.workbench.domain.ClueRemark;
import com.yangbin.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {

    Map<String, Object> getCluePageList(Map<String, Object> map);

    boolean saveClue(Clue clue);


    Clue getClueById(String id);

    boolean updateClue(Clue clue);

    Clue detail(String id);

    boolean deleteClue(String[] ids);

    List<ClueRemark> getClueRemarkList(String clueId);

    Map<String, Object> saveRemark(ClueRemark clueRemark);

    List<Activity> getBundList(String clueId);

    List<Activity> getActivityListByNameAndClueId(Map<String, String> map);


    boolean bund(String clueId, String[] aids);

    boolean unbund(String id);

    boolean convert(String clueId, Tran tran, String createBy);
}
