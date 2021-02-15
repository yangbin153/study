package com.yangbin.crm.settings.service;

import com.yangbin.crm.settings.domain.DicValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("dicService")
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
