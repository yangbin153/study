package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int saveTran(Tran tran);

    int getTotal(Map<String, Object> mapIn);

    List<Tran> getTranList(Map<String, Object> mapIn);

    Tran getTranById(String id);

    int changeStage(Tran tran);
}
