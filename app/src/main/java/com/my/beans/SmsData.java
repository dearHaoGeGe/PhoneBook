package com.my.beans;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import com.my.db.DBTool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 短信fragment的所有数据
 */
public class SmsData {
    //所有数据
    private List<SmsByPersonBean> smsByPersonBeans;
    private DBTool dbTool;

    //构造方法
    public SmsData(Context context) {
        smsByPersonBeans = new ArrayList<>();
        dbTool = new DBTool(context);
        initData(context);
    }

    private void initData(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(Telephony.Sms.CONTENT_URI, projection, null, null, "date desc");
        if (cur.moveToFirst()) {
            String name;
            String phoneNumber;
            String smsbody;
            String date;

            int phoneNumberColumn = cur.getColumnIndex(Telephony.Sms.ADDRESS);
            int smsbodyColumn = cur.getColumnIndex(Telephony.Sms.BODY);
            int dateColumn = cur.getColumnIndex(Telephony.Sms.DATE);
            int typeColumn = cur.getColumnIndex(Telephony.Sms.TYPE);

            //do...while否则数据会少一条
            do {
                //phonenumber
                phoneNumber = cur.getString(phoneNumberColumn);
                //去不必要的字段
                phoneNumber = getDelNumber(phoneNumber);

                //smsbody
                smsbody = cur.getString(smsbodyColumn);
                //date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                //SmsByPersonBean smsByPersonBean = getSmsPersonBeanFromNum(phoneNumber);
                Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
                date = dateFormat.format(d);
                //typeId 1为收到的短信   其他的为发送的短信
                int typeId = cur.getInt(typeColumn);

                //从数据库里找名字
                name = dbTool.getNameForNumber(phoneNumber);
                SmsByPersonBean smsByPersonBean = getSmsPersonBeanFromNum(phoneNumber);
                if (smsByPersonBean == null) {
                    //数据类里尚未存储该联系人的任何短信信息
                    smsByPersonBean = new SmsByPersonBean();
                    smsByPersonBean.setNumber(phoneNumber);   //设置电话号码

                    smsByPersonBean.setName(name);
                    smsByPersonBeans.add(smsByPersonBean);
                }

                SingleSmsBean singleSmsBean = new SingleSmsBean();

                singleSmsBean.setDate(date);
                singleSmsBean.setSmsBody(smsbody);
                singleSmsBean.setType(typeId);
                singleSmsBean.setName(name);
                smsByPersonBean.addSingleBean(singleSmsBean);
            } while (cur.moveToNext());
        }

    }

    public List<SmsByPersonBean> getSmsByPersonBeans() {
        return smsByPersonBeans;
    }

    public void setSmsByPersonBeans(List<SmsByPersonBean> smsByPersonBeans) {
        this.smsByPersonBeans = smsByPersonBeans;
    }

    public int getCount() {
        return smsByPersonBeans.size();
    }

    public DBTool getDbTool() {
        return dbTool;
    }

    public void setDbTool(DBTool dbTool) {
        this.dbTool = dbTool;
    }

    //寻找我们的数据类里 是否存在有该号码的联系人
    public SmsByPersonBean getSmsPersonBeanFromNum(String num) {
        getDelNumber(num);
        for (SmsByPersonBean smsByPersonBean : smsByPersonBeans) {
            if (num.equals(smsByPersonBean.getNumber())) {
                return smsByPersonBean;
            }
        }
        return null;
    }

    //去空格、去()、去+86、去-
    public String getDelNumber(String Number){
        Number = Number.replace("+86", ""); //去+86
        Number = Number.replace("-", "");   //去-
        Number = Number.replace("(","");    //去(
        Number = Number.replace(")","");    //去)
        Number = Number.replace(" ","");    //去空格
        return Number;
    }

    /**
     * 向SmsData这个数据里加入新的一条短息
     * @param singleSmsBean 新的短息，包括内容，时间，类型
     * @param number    电话号码
     * 通过电话号码来判断,当前类里是否存在该号码的短信
     * 如果存在，则加入到当前的这个电话号码的smsByPersonBean里
     * 如果不存在，则新建一个smsByPersonBean把短信加到其中
     */
    public void addSingleSms(SingleSmsBean singleSmsBean, String number){
        SmsByPersonBean smsByPersonBean = getSmsPersonBeanFromNum(number);
        if(smsByPersonBean == null){
            //当前没有该号码的联系人得短息
            //新建一条SingleSmsBean
            smsByPersonBean = new SmsByPersonBean();
            //向smsByPersonBean中添加短信信息
            smsByPersonBean.addSingleBean(singleSmsBean);
            //设置SmsByPersonBean的电话号码
            smsByPersonBean.setNumber(number);
            //从数据库里找一个该号码的名字
            String name = dbTool.getNameForNumber(number);
            //设置SmsByPersonBean的名字
            smsByPersonBean.setName(name);
            //把这条新的SmsByPersonBean加到集合里
            smsByPersonBeans.add(smsByPersonBean);
        } else {
            //当前有该号码的短信了
            //就把当前的短信信息加到SmsByPersonBean里
            smsByPersonBean.addSingleBean(singleSmsBean);
        }
    }
}
