package com.yangbin.crm.workbench.service;

import com.yangbin.crm.workbench.domain.Tran;
import com.yangbin.crm.workbench.domain.TranHistory;
import com.yangbin.crm.workbench.domain.TranRemark;

import java.util.List;
import java.util.Map;

public interface TranService {
    Map<String, Object> getTranList(Map<String, Object> mapIn);

    Tran getTranById(String id);

    List<TranRemark> getRemarkList(String tranId);

    Map<String, Object> saveRemark(TranRemark tranRemark);

    List<TranHistory> getTranHistoryList(String tranId);

    boolean saveTran(Tran tran);

    boolean changeStage(Tran tran);
}
