package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkDao {
    List<TranRemark> getRemarkList(String tranId);

    int saveRemark(TranRemark tranRemark);
}
