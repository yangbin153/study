package com.yangbin.crm.workbench.service.impl;

import com.yangbin.crm.util.DateTimeUtil;
import com.yangbin.crm.util.UUIDUtil;
import com.yangbin.crm.workbench.dao.CustomerDao;
import com.yangbin.crm.workbench.dao.TranDao;
import com.yangbin.crm.workbench.dao.TranHistoryDao;
import com.yangbin.crm.workbench.dao.TranRemarkDao;
import com.yangbin.crm.workbench.domain.Customer;
import com.yangbin.crm.workbench.domain.Tran;
import com.yangbin.crm.workbench.domain.TranHistory;
import com.yangbin.crm.workbench.domain.TranRemark;
import com.yangbin.crm.workbench.service.TranService;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;
    @Resource
    private TranRemarkDao tranRemarkDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count1 = tranDao.changeStage(tran);
        if (count1 != 1){
            flag = false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setPossibility(tran.getPossibility());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        int count2 = tranHistoryDao.saveTranHistory(tranHistory);
        if (count2 !=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getTranList(Map<String, Object> mapIn) {
        Map<String, Object> map = new HashMap<>();
        int total = tranDao.getTotal(mapIn);
        List<Tran> tranList = tranDao.getTranList(mapIn);
        map.put("total",total);
        map.put("tranList",tranList);
        return map;
    }

    @Override
    public Map<String, Object> saveRemark(TranRemark tranRemark) {
        boolean flag = true;
        int count = tranRemarkDao.saveRemark(tranRemark);
        if (count != 1){
            flag = false;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("remark",tranRemark);
        return map;
    }

    @Override
    public boolean saveTran(Tran tran) {
        boolean flag = true;
        String customerName = tran.getCustomerId();
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer ==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());
            customer.setContactSummary(tran.getContactSummary());
            int count1 = customerDao.saveCustomer(customer);
            if (count1 != 1){
                flag = false;
            }
        }
        tran.setCustomerId(customer.getId());
        int count2 = tranDao.saveTran(tran);
        if (count2 !=1){
            flag = false;
        }

        //创建交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        int count3 = tranHistoryDao.saveTranHistory(tranHistory);
        if (count3 !=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<TranHistory> getTranHistoryList(String tranId) {
        List<TranHistory> historyList = tranHistoryDao.getTranHistoryList(tranId);

        return historyList;
    }

    @Override
    public List<TranRemark> getRemarkList(String tranId) {
        List<TranRemark> remarkList = tranRemarkDao.getRemarkList(tranId);
        return remarkList;
    }

    @Override
    public Tran getTranById(String id) {
        Tran tran = tranDao.getTranById(id);

        return tran;
    }
}
