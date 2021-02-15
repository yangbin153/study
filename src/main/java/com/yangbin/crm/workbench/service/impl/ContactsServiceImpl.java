package com.yangbin.crm.workbench.service.impl;

import com.yangbin.crm.workbench.dao.ContactsDao;
import com.yangbin.crm.workbench.domain.Contacts;
import com.yangbin.crm.workbench.service.ContactsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    @Resource
    private ContactsDao contactsDao;

    @Override
    public List<Contacts> getContactsListByName(String cname) {
        List<Contacts> contactsList = contactsDao.getContactsListByName(cname);

        return contactsList;
    }
}
