package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int saveContacts(Contacts contacts);

    List<Contacts> getContactsListByName(String cname);
}
