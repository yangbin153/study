package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int saveTranHistory(TranHistory tranHistory);

    List<TranHistory> getTranHistoryList(String tranId);
}
