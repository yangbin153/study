package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    int getCountByClueId(String[] ids);

    int deleteRelation(String[] ids);

    int bund(ClueActivityRelation car);

    int unbund(String id);

    List<ClueActivityRelation> getRelationsByClueId(String clueId);

    int deleteRelationByClueId(String clueId);
}
