package com.my.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.my.BroadcastReceiver.SendSmsService;
import com.my.adapter.SmsDetailAdapter;
import com.my.beans.SmsByPersonBean;
import com.my.phonebook.R;
import com.my.tool.MyValues;

/**
 * 信息页面
 */
public class SmsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EditText smsBodyEt;
    private static SmsByPersonBean smsByPersonBean;
    private SmsDetailAdapter smsDetailAdapter;
    private Handler handler;
    private Intent sendSmsintent;  //要启动服务的
    private SendSmsService.MBinder mBinder; //mBinder对象
    //private SendSmsDynamicBroadcastReceiver sendSmsDynamicBroadcastReceiver;    //动态广播接受者(内部类)
    private RefreshUI refreshUI;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (SendSmsService.MBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //当服务和activity断开连接时回调,非正常的断开
            mBinder = null;
        }
    };

    public static void startSmsDetailActivity(Context context, SmsByPersonBean smsByPersonBean) {
        //静态方法static修饰修饰符要用类名.
        SmsDetailActivity.smsByPersonBean = smsByPersonBean;
        Intent intent = new Intent(context, SmsDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_details_activity);
        //实现从MessageFragment到SmsDetailActivity动画效果
        overridePendingTransition(R.anim.smsdetailactivity_in, R.anim.messagefragment_out);

        recyclerView = (RecyclerView) findViewById(R.id.sms_details_activity_RecyclerView);

        toolbar = (Toolbar) findViewById(R.id.sms_details_activity_toolbar);

        findViewById(R.id.sms_details_activity_btn).setOnClickListener(this);

        smsBodyEt = (EditText) findViewById(R.id.sms_details_activity_et);

        //设置toolbar标题
        toolbar.setTitle(smsByPersonBean.getName());
        setSupportActionBar(toolbar);

        smsDetailAdapter = new SmsDetailAdapter(this, smsByPersonBean);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(smsDetailAdapter);

        //启动服务
        sendSmsintent = new Intent(this, SendSmsService.class);
        bindService(sendSmsintent, connection, BIND_AUTO_CREATE);
        startService(sendSmsintent);

//        //动态广播的注册
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.my.phonebook.NOTICE");
//        sendSmsDynamicBroadcastReceiver = new SendSmsDynamicBroadcastReceiver();
//        registerReceiver(sendSmsDynamicBroadcastReceiver,intentFilter);
        //老师的---
        refreshUI = new RefreshUI();    //初始化
        IntentFilter intentFilter1 = new IntentFilter();
        //注册广播的action
        intentFilter1.addAction(MyValues.REFRESH_UI_BROADCAST);
        //注册
        registerReceiver(refreshUI, intentFilter1);

        //读取草稿未发送的短信
        readWord();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                sendSms();//发送短信
                smsBodyEt.setText("");
                Toast.makeText(SmsDetailActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    //右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_sma_detail, menu);
        return true;
    }

    //右上角菜单内容
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.first:
                //5S后发送
                final String smsbody = smsBodyEt.getText().toString();
                if (smsBodyEt.getText().toString().equals("")) {
                    Toast.makeText(SmsDetailActivity.this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "5秒之后发送短信", Toast.LENGTH_SHORT).show();
                    Log.e("这是联系人电话", smsByPersonBean.getNumber() + "," + smsBodyEt.getText().toString());
                    mBinder.sendSms(smsByPersonBean.getNumber(), smsbody);
                }

                break;
            case R.id.second:
                //延迟发送
                showDialog();
                smsBodyEt.setText("");
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("输入时间(单位秒)");
//                View view = LayoutInflater.from(this).inflate(R.layout.dialog_delay_send, null);
//                final EditText editTextTime = (EditText) view.findViewById(R.id.et_delay_send);
//                builder.setView(view);
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //判断输入时间和发送的短信是否为空
//                        if (smsBodyEt.getText().toString().equals("") || editTextTime.getText().toString().equals("")) {
//                            Toast.makeText(SmsDetailActivity.this, "短息内容不能为空或者时间不能空", Toast.LENGTH_SHORT).show();
//                        } else {    //延迟发送短息
//                            Toast.makeText(SmsDetailActivity.this, editTextTime.getText().toString() + "秒之后发送短信", Toast.LENGTH_SHORT).show();
//                            mBinder.sendSms(smsByPersonBean.getNumber(), smsBodyEt.getText().toString(), Integer.parseInt(editTextTime.getText().toString()) * 1000);
//                        }
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(SmsDetailActivity.this, "你已经取消操作", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                builder.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //弹出一个对话框,输入多长时间后发送
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_delay_send, null);
        final EditText editTextTime = (EditText) view.findViewById(R.id.et_delay_send);
        final String smsbody = smsBodyEt.getText().toString();
        builder.setView(view);  //将布局加到对话框里
        builder.setTitle("多少分钟后发送");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //通过服务发送短息
                mBinder.sendSms(smsByPersonBean.getNumber(), smsbody, Integer.valueOf(editTextTime.getText().toString()));
            }
        });
        builder.setNegativeButton("取消", null);  //取消按钮
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sms_details_activity_btn:
                //如果短息内容不为空
                sendSms();
                //发送成功之后EditText边空
                smsBodyEt.setText("");
                saveWord();
                break;
        }
    }

    //按返回键时保存数据
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((smsBodyEt.getText().toString()).length() > 0) {
            saveWord();
        }
        mBinder = null;
        //取消动态广播的注册
        //unregisterReceiver(sendSmsDynamicBroadcastReceiver);
        unregisterReceiver(refreshUI);  //老师的
    }

    //停止时保存数据
    @Override
    protected void onStop() {
        super.onStop();
        if ((smsBodyEt.getText().toString()).length() > 0) {
            saveWord();
        }
    }

    //发送短息的方法
    private void sendSms() {
        SmsManager manager = SmsManager.getDefault();
        String number = smsByPersonBean.getNumber();    //获取电话号
        String body = smsBodyEt.getText().toString();
        if (body.length() > 0) {  //内容长队大于0，正常长度发送短息
            //发送短信
            manager.sendTextMessage(number, null, body, null, null);
            //调用adapter的方法，将最新发送的短息加到RecyclerView里
            smsDetailAdapter.addsendSms(body);
        } else {
            //没有内容
            Toast.makeText(SmsDetailActivity.this, "请输入短息内容", Toast.LENGTH_SHORT).show();
        }
    }

    //读取草稿未发送的短信
    private void readWord() {
        //文件名要和写入时的文件名保持一致
        SharedPreferences sharedPreferences = getSharedPreferences(smsByPersonBean.getName(), MODE_PRIVATE);
        //通过sharedPreferences的getString方法,获取到之前存储的String
        //接收2个参数，第一个参数是Key：之前保存是定义的
        //第二个参数是默认值，没获取到该key存储的内容时，则返回该值
        String str = sharedPreferences.getString(smsByPersonBean.getName(), "");
        smsBodyEt.setText(str);
    }

    //保存草稿未发送的短信
    private void saveWord() {
        //获得一个SharedPreferences对象,接收2个参数，第一个参数是文件名，第二个参数是文件的权限 通常是MODE_PRIVATE
        /**smsByPersonBean.getName()键值对匹配**/
        SharedPreferences sharedPreferences = getSharedPreferences(smsByPersonBean.getName(), MODE_PRIVATE);
        //通过sharedPreferences的edit()方法，获得一个Editor对象
        //Editor是SharedPreferences得内部类
        //用来编辑需要存储的内容的
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //操作editor对象来保存字符串
        //接收2个参数
        //第一个是Key值，自定义
        //第二个是value，是我们要存储的内容
        editor.putString(smsByPersonBean.getName(), smsBodyEt.getText().toString());
        //保存editor的一系列操作
        editor.commit();
    }

//    //发送信息的动态广播接收者
//    public class SendSmsDynamicBroadcastReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //广播接收到得短信内容让adapter刷新
//            smsDetailAdapter.addsendSms(smsBodyEt.getText().toString());
//            smsBodyEt.setText("");  //信息发送出去之后让内容为空
//        }
//    }

    //老师的---用来刷新UI的广播
    public class RefreshUI extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获得电话号码和短息内容
            String number = intent.getStringExtra("number");
            String smsbody = intent.getStringExtra("smsbody");

            if (smsByPersonBean.getNumber().equals(number)) {
                //如果接收到的电话号码,和我们当前页面的电话号码相匹配的话 则刷新UI
                smsDetailAdapter.addsendSms(smsbody);
            }
        }
    }
}
