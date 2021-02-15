package com.yangbin.crm.settings.service.impl;

import com.yangbin.crm.settings.dao.UserDao;
import com.yangbin.crm.settings.domain.User;
import com.yangbin.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(Map<String, String> map) {
        User user = userDao.login(map);
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
