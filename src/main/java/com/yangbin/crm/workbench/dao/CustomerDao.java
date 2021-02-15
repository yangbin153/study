package com.yangbin.crm.workbench.dao;

import com.yangbin.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int saveCustomer(Customer customer);

    List<String> getCustomerName(String name);
}
