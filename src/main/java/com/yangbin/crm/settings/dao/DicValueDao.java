package com.yangbin.crm.settings.dao;

import com.yangbin.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getDicValue(String code);
}
