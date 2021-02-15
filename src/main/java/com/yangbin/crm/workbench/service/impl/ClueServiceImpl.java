package com.yangbin.crm.workbench.service.impl;

import com.yangbin.crm.util.DateTimeUtil;
import com.yangbin.crm.util.UUIDUtil;
import com.yangbin.crm.workbench.dao.*;
import com.yangbin.crm.workbench.domain.*;
import com.yangbin.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ActivityDao activityDao;
    @Resource
    private CustomerDao customerDao;
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public Map<String, Object> getCluePageList(Map<String, Object> map) {
        Map<String,Object> dataMap = new HashMap<>();
        List<Clue> clueList = clueDao.getClueList(map);
        int total = clueDao.getTotal(map);
        dataMap.put("clueList",clueList);
        dataMap.put("total",total);

        return dataMap;
    }

    @Override
    public boolean saveClue(Clue clue) {
        boolean flag = true;
        int count = clueDao.saveClue(clue);
        if (count != 1 ){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue getClueById(String id) {
        Clue clue = clueDao.getClueById(id);

        return clue;
    }

    @Override
    public boolean updateClue(Clue clue) {
        boolean flag = true;
        int count = clueDao.updateClue(clue);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean deleteClue(String[] ids) {
        boolean flag = true;

        int count1 = clueRemarkDao.getCount(ids);
        int count2 = clueRemarkDao.deleteRemark(ids);
        if (count1 != count2){
            flag = false;
        }
        int count3 = clueActivityRelationDao.getCountByClueId(ids);
        int count4 = clueActivityRelationDao.deleteRelation(ids);
        if (count3 != count4){
            flag = false;
        }
        int count5 = clueDao.deleteClue(ids);
        if (count5 != ids.length){
            flag = false;
        }



        return flag;
    }

    @Override
    public List<ClueRemark> getClueRemarkList(String clueId) {
        List<ClueRemark> clueRemarkList = clueRemarkDao.getClueRemarkList(clueId);

        return clueRemarkList;
    }

    @Override
    public Map<String, Object> saveRemark(ClueRemark clueRemark) {
        Map<String,Object> map = new HashMap<>();
        boolean flag = true;
        int count = clueRemarkDao.saveRemark(clueRemark);
        if (count !=1){
            flag = false;
        }
        map.put("success",flag);
        map.put("remark",clueRemark);
        return map;
    }

    @Override
    public List<Activity> getBundList(String clueId) {

        List<Activity> activityList = activityDao.getActivityListByClueId(clueId);
        return activityList;
    }

    @Override
    public List<Activity> getActivityListByNameAndClueId(Map<String, String> map) {

        List<Activity> activityList = activityDao.getActivityListByNameAndClueId(map);

        return activityList;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }


    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        System.out.println("线索转换开始");
        boolean flag = true;
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getClueById(clueId);
        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setName(company);
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setDescription(clue.getDescription());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            int count1 = customerDao.saveCustomer(customer);
            if (count1 != 1){
                flag = false;
            }
        }
        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setMphone(clue.getMphone());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCustomerId(customer.getId());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        int count2 = contactsDao.saveContacts(contacts);
        if (count2 !=1){
            flag = false;
        }
        // (4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarks = clueRemarkDao.getClueRemarkList(clueId);
        CustomerRemark customerRemark = new CustomerRemark();
        ContactsRemark contactsRemark = new ContactsRemark();
        for (ClueRemark clueRemark:clueRemarks) {
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            int count3 = customerRemarkDao.saveCustomerRemark(customerRemark);
            if (count3 != 1){
                flag = false;
            }
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setEditFlag("0");
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setContactsId(contacts.getId());
            int count4 = contactsRemarkDao.saveContactsRemark(contactsRemark);
            if (count4 != 1){
                flag = false;
            }
        }
        // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationDao.getRelationsByClueId(clueId);
        ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
        for (ClueActivityRelation clueActivityRelation :clueActivityRelations) {
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            int count5 = contactsActivityRelationDao.saveContactsActivityRelation(contactsActivityRelation);
            if (count5 != 1){
                flag = false;
            }
        }
        // (6) 如果有创建交易需求，创建一条交易
        // (7) 如果创建了交易，则创建一条该交易下的交易历史
        if (tran !=null){
            int count6 = tranDao.saveTran(tran);
            if (count6 != 1){
                flag = false;
            }
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            int count7 = tranHistoryDao.saveTranHistory(tranHistory);
            if (count7 != 1){
                flag = false;
            }
        }
        // (8) 删除线索备注
        int count8 = clueRemarkDao.deleteRemarkByClueId(clueId);
        if (count8 != 1){
            flag = false;
        }
        // (9) 删除线索和市场活动的关系
        int count9 = clueActivityRelationDao.deleteRelationByClueId(clueId);
        if (count9 != 1){
            flag = false;
        }
        // (10) 删除线索
        int count10 = clueDao.deleteClueById(clueId);
        if (count10 !=1){
            flag = false;
        }
        System.out.println("线索转换结束");
        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] aids) {
        boolean flag = true;
        ClueActivityRelation car = new ClueActivityRelation();
        for (String aid:aids) {
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(aid);
            int count = clueActivityRelationDao.bund(car);
            if (count != 1){
                flag = false;
            }
        }
        return flag;
    }
}
