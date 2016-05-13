package com.my.beans;

/**
 * 最基本的短信数据类
 */
public class SingleSmsBean {

    private String name;
    private String smsBody; //短信主体
    private String Date;    //短信时间
    private int type;       //短信类型,发送还是接收

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
