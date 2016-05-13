package com.my.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MySQLHelper extends SQLiteOpenHelper implements SQLValues {

    private Context context;
    private String create = "create table "+TABLE_NAME+" ("+"id integer primary key autoincrement,"+PERSON_NAME+" text,"+PERSON_NUMBER+" text)";

    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create);
        Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}