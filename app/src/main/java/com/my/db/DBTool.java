package com.my.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

//数据库的工具类
public class DBTool implements SQLValues {

    // 数据库的对象，用于操作的
    private SQLiteDatabase sqLiteDatabase;
    private Context context;    // context对象
    private MySQLHelper mySQLHelper;    // helper对象
    private final String TAG = "DBTool";

    // 无参构造方法
    public DBTool(Context context) {
        //this.context = context;
        mySQLHelper = new MySQLHelper(context,"SQLPerson.db",null,1);
        sqLiteDatabase = mySQLHelper.getWritableDatabase(); //数据库可操作的对象
    }

    // 判断数据库里是否有该电话号码
    public boolean isHasNum(String num){
        String sql = "select * from "+TABLE_NAME+" where "+PERSON_NAME+" = ?";
        // 返回符合条件的Cursor对象
        Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{num});
        // 如果数据数量>0则返回true证明存过该号码
        Boolean flag = cursor.getCount() > 0;
        cursor.close(); // 关闭游标
        return flag;
    }

    // 插入号码和名字
    public void insert(String num, String name){
        if(isHasNum(num)){
            // 数据库里有该手机号
            Log.i(TAG,"insert-------->不差入"+name);
            return;
        } else {

            ContentValues values = new ContentValues();
            values.put(PERSON_NAME,name);
            values.put(PERSON_NUMBER,num);
            // 插入数据
            sqLiteDatabase.insert(TABLE_NAME,null,values);
        }
    }

    // 获取数据库的联系人
    public String getNameForNumber(String number){
        String name = number;
        String sql = "select * from "+ TABLE_NAME+" where "+PERSON_NUMBER+" =?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,new String[]{number});
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                name = cursor.getString(cursor.getColumnIndex(PERSON_NAME));
            }
            if(name != null && name.length() > 0){

            } else {
                name = "未知";
            }
        }
        return name;
    }
}