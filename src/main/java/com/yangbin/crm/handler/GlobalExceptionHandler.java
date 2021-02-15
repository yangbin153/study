package com.yangbin.crm.handler;

import com.yangbin.crm.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public Map<String,Object> loginException(Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }


}
