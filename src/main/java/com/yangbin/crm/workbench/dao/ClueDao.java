package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    List<Clue> getClueList(Map<String, Object> map);

    int getTotal(Map<String, Object> map);

    int saveClue(Clue clue);

    Clue getClueById(String id);

    int updateClue(Clue clue);

    Clue detail(String id);

    int deleteClue(String[] ids);

    int deleteClueById(String clueId);
}
