package com.yangbin.crm.workbench.service;

import com.yangbin.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> getContactsListByName(String cname);
}
