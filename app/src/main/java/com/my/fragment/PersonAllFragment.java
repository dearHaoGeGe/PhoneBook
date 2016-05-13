package com.my.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.my.custom.IndexView;
import com.my.adapter.AllPersonExAdapter;
import com.my.db.DBTool;
import com.my.db.MySQLHelper;
import com.my.phonebook.AllPerson;
import com.my.phonebook.PersonClass;
import com.my.phonebook.R;


public class PersonAllFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ExpandableListView expandableListView;
    //private AllPersonExAdapter adapter;
    private ContentResolver cr;
    private String name, number;
    private Bitmap personImage;
    private AllPerson allPerson;    // 数据类
    private AllPersonExAdapter allPersonExAdapter;
    private MySQLHelper mySQLHelper;
    private SQLiteDatabase sqLiteDatabase;  //数据库对象
    private DBTool dbTool;  //定义数据库工具类
    private IndexView indexView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_all, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expandableListView = (ExpandableListView) getActivity().findViewById(R.id.fragment_person_listview);

        //listview添加监听事件
        expandableListView.setOnItemClickListener(this);
        expandableListView.setOnItemLongClickListener(this);

        // 创建我们自己写的helper
        mySQLHelper = new MySQLHelper(getActivity(), "SQLPerson.db", null, 1);

        // 对数据库对象进行赋值
        sqLiteDatabase = mySQLHelper.getWritableDatabase();
        dbTool = new DBTool(getActivity());

        initData();
        allPersonExAdapter = new AllPersonExAdapter(getActivity(), allPerson);

        expandableListView.setAdapter(allPersonExAdapter);

        indexView = (IndexView) getActivity().findViewById(R.id.all_person_index);
        indexView.setExpandableListView(expandableListView);
    }

    // 读取联系人头像方法
    private Bitmap GetPersonImage(Cursor cursorimage) {
        String photoid = cursorimage.getString(cursorimage.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
        if (photoid != null) {
            Cursor c = this.getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.DATA15}, ContactsContract.Data._ID + "=" + photoid, null,
                    null);
            c.moveToFirst();
            byte[] ss = c.getBlob(0);
            if (ss != null) {
                Bitmap map = BitmapFactory.decodeByteArray(ss, 0, ss.length);
                c.close();
                return map;
            }
            c.close();
            return null;
        }
        return null;
    }

    // 实现点击方法
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        PersonClass personClass = (PersonClass) adapterView.getItemAtPosition(i);
        Toast.makeText(getActivity(), "你点击了:" + personClass.getName() + "，在第" + (i + 1) + "行", Toast.LENGTH_SHORT).show();

    }

    // 实现长按方法
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "长按当前", Toast.LENGTH_SHORT).show();
        // 返回true没有一次点击事件，返回false有一次点击事件
        return true;
    }

    // 把name和number的值放在实体类中，并且把值传到allperson类中
    private void initData() {
        allPerson = new AllPerson();
        cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        // 游标移动读取
        if (cursor != null) {
            // 游标向下移动
            while (cursor.moveToNext()) {
                // 获取手机中得联系人
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // 获取手机中得电话号码
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // 获取手机的头像
                personImage = GetPersonImage(cursor);
                //去不必要的字段
                number = getDelNumber(number);


                // 把name、number添加到数据库方法
                addSQL(name, number);

                PersonClass personClass = new PersonClass(personImage,name,number);
                allPerson.addContact(personClass);
                dbTool.insert(number,name);
            }
        }
        cursor.close();
    }

    // 把name、number添加到数据库
    private void addSQL(String name, String number) {
        String SQLAdd = "insert into Person(name,number) values(?,?)";
        String add[] = {name, number};
        sqLiteDatabase.execSQL(SQLAdd, add);
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

}