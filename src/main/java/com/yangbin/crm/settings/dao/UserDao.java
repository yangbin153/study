package com.yangbin.crm.settings.dao;

import com.yangbin.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);

    List<User> getUserList();
}
