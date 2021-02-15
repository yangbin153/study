package com.yangbin.crm.settings.service;

import com.yangbin.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User login(Map<String, String> map);

    List<User> getUserList();
}
