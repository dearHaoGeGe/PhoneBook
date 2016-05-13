package com.my.beans;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过联系人分组的短信数据类
 */
public class SmsByPersonBean {

    List<SingleSmsBean> singleSmsBeans;
    String name;
    String number;

    public List<SingleSmsBean> getSingleSmsBeans() {
        return singleSmsBeans;
    }

    public void setSingleSmsBeans(List<SingleSmsBean> singleSmsBeans) {
        this.singleSmsBeans = singleSmsBeans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    //获得当前对象里短息的条数
    public int getSize(){
        return singleSmsBeans == null ? 0 : singleSmsBeans.size();
    }

    //向短信集合里添加单条短信
    public void addSingleBean(SingleSmsBean singleSmsBean){
        if(singleSmsBeans == null){
            singleSmsBeans = new ArrayList<>();
        }

        singleSmsBeans.add(0,singleSmsBean);
    }
}
