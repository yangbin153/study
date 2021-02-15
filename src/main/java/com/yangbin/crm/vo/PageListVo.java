package com.yangbin.crm.vo;


import java.util.List;

public class PageListVo<T> {
    private int total;
    private List<T> dateList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDateList() {
        return dateList;
    }

    public void setDateList(List<T> dateList) {
        this.dateList = dateList;
    }
}
