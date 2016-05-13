package com.my.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.my.adapter.GroupSmsSendAdapter;
import com.my.db.DBTool;
import com.my.fragment.PersonFreeFragment;
import com.my.line.DividerItemDecoration;
import com.my.phonebook.PersonClass;
import com.my.phonebook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 15/12/31.
 */
public class GroupSmsSendActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private RecyclerView recyclerView;
    private GroupSmsSendAdapter adapter;
    private List<PersonClass> personClass;
    private ContentResolver contentResolver;
    private String name, number;
    private Bitmap personImage;
    private DBTool dbTool;

    public static void startGroupSmsSendActivity(Context context) {
        Intent intent = new Intent(context, GroupSmsSendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsmssend);

        //实现从MessageFragment到SmsDetailActivity动画效果
        overridePendingTransition(R.anim.smsdetailactivity_in, R.anim.messagefragment_out);

        findViewById(R.id.btn_groupsmssend).setOnClickListener(this);

        contentResolver = getContentResolver();
        personClass = new ArrayList<>();
        addData();
        recyclerView = (RecyclerView) findViewById(R.id.rv_groupsmssend);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        adapter = new GroupSmsSendAdapter(this, personClass);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_groupsmssend:
                //TODO 此处添加群发短信
                showDialogGroupSmsSend();
                break;
        }
    }

    private void showDialogGroupSmsSend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入短信内容:");
        View view = LayoutInflater.from(this).inflate(R.layout.item_showdialoggroupsmssend, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_groupsmssend);
        Log.e("获取EditText内容", "1111111111111");
        Log.e("获取EditText内容", "1" + editText.getText().toString() + "2");
        //String smsbody=editText.getText().toString();
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("smsbody的长度", "" + editText.getText().toString().length());
                Log.e("adapter.getCheckedData().size()", "" + adapter.getCheckedData().size());
                if (editText.getText().toString().length() > 0) {
                    //TODO 此处添加群发短信过程
                    SmsManager manager = SmsManager.getDefault();
                    //用checkBox选中的大小作为循环条件
                    for (int i = 0; i < adapter.getCheckedData().size(); i++) {
                        manager.sendTextMessage(adapter.getCheckedData().get(i), null, editText.getText().toString(), null, null);
                        dbTool = new DBTool(GroupSmsSendActivity.this); //从数据库中查询名字
                        Toast.makeText(GroupSmsSendActivity.this, dbTool.getNameForNumber(adapter.getCheckedData().get(i).toString()) + "发送成功", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(GroupSmsSendActivity.this, "信息内容不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    public void addData() {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                personImage = GetPersonImage(cursor);
                //去不必要的字段
                number = PersonFreeFragment.getDelNumber(number);
                Log.e("name------", name + "," + number);
                personClass.add(new PersonClass(personImage, name, number));
            }
        }
        cursor.close();
    }

    //获取头像方法
    private Bitmap GetPersonImage(Cursor cursorimage) {
        String photoid = cursorimage.getString(cursorimage.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
        if (photoid != null) {
            Cursor c = this.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
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
}
