package com.yangbin.crm.settings.service.impl;

import com.yangbin.crm.settings.dao.DicTypeDao;
import com.yangbin.crm.settings.dao.DicValueDao;
import com.yangbin.crm.settings.domain.DicType;
import com.yangbin.crm.settings.domain.DicValue;
import com.yangbin.crm.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Autowired
    private DicTypeDao dicTypeDao;
    @Autowired
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();
        List<DicType> dicTypeList = dicTypeDao.getDicType();
        for (DicType dicType:dicTypeList) {
            List<DicValue> dicValueList = dicValueDao.getDicValue(dicType.getCode());
            map.put(dicType.getCode(),dicValueList);
        }

        return map;
    }
}

