package com.my.fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.my.activity.GroupSmsSendActivity;
import com.my.interfaces.MyOnClickListener;
import com.my.adapter.MessageAdapter;
import com.my.beans.SmsByPersonBean;
import com.my.db.DBTool;
import com.my.line.DividerItemDecoration;
import com.my.activity.MainActivity;
import com.my.phonebook.R;
import com.my.activity.SmsDetailActivity;

public class MessageFragment extends Fragment implements MyOnClickListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DBTool dbTool;
    private Context context;
    private MessageAdapter messageAdapter;
    private SmsBroadReceiver smsBroadReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.message_tool_bar);
        setHasOptionsMenu(true);    //设置toolbar菜单的显示
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.message_tool_bar);
        dbTool = new DBTool(context);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.message_RecyclerView);
        //getMessage();
        messageAdapter = new MessageAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

        //初始化广播对象
        smsBroadReceiver = new SmsBroadReceiver();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.phonebook.SMS_RECEIVED");
        context.registerReceiver(smsBroadReceiver, intentFilter);

        //设置监听接口
        messageAdapter.setMyOnClickListener(this);

        recyclerView.setAdapter(messageAdapter);

        // 设置标题
        toolbar.setTitle("短信");
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void myOnClick(int position) {
        //RecyclerView点击的时候在fragment里回调的方法
        SmsByPersonBean smsByPersonBean = messageAdapter.getSmsByPersonBean(position);  //跳转前拿到的数据
        SmsDetailActivity.startSmsDetailActivity(getActivity(), smsByPersonBean);
        //Toast.makeText(context, "你点击了第" + position + 1 + "行", Toast.LENGTH_SHORT).show();
    }

    //设置toolbar显示
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_message, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //toolbar添加点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sms_send:
                Toast.makeText(getActivity(), "你点击发送", Toast.LENGTH_SHORT).show();
                //弹出发短息的对话框
                showSendSmsDialog();
                break;

            case R.id.sms_other:
                //TODO 点击跳转GroupSmsSendActivity
                GroupSmsSendActivity.startGroupSmsSendActivity(getActivity());
                Toast.makeText(getActivity(), "你点击群发短信", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //创建一个自定义弹框,用来发送短息
    private void showSendSmsDialog() {
        //创建出Builder对象,用来设置Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //设置标题
        builder.setTitle("发送短息");
        //用布局注入者LayoutInflater
        View view = LayoutInflater.from(context).inflate(R.layout.send_fragment_message, null);
        //初始化 电话号码 和内容的EditText
        final EditText number = (EditText) view.findViewById(R.id.send_fragment_message_number);
        final EditText smsbody = (EditText) view.findViewById(R.id.send_fragment_message_body);

        builder.setView(view);  //设置自定义布局
        //点击发送后
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendSms(number.getText().toString(), smsbody.getText().toString());
            }
        });
        //点击取消后
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //让Dialog显示出来
        builder.show();
    }

    /**
     * 发送短息的方法
     *
     * @param number 电话号码
     * @param body   短息内容
     */
    private void sendSms(String number, String body) {
        if (number.length() > 0 && body.length() > 0) {
            //符合规范的号码和内容
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, body, null, null);

            //将最新的这条短息加到adapter里
            messageAdapter.addSms(body, number);
        } else {
            //不符合规范的号码和内容
            Toast.makeText(getActivity(), "号码或内容不对", Toast.LENGTH_SHORT).show();
        }
    }

    //动态广播内部类
    class SmsBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "动态注册广播---收到短信", Toast.LENGTH_SHORT).show();
            //Protocol description unit
            //获得bundle对象
            Bundle bundle = intent.getExtras();
            Object[] object = (Object[]) bundle.get("pdus");
            String format = (String) bundle.get("format");
            SmsMessage sms[] = new SmsMessage[object.length];
            String body = "";
            String number = "";
            sendNotification("name", "smsbody", context);
            for (int i = 0; i < object.length; i++) {
                sms[i] = SmsMessage.createFromPdu((byte[]) object[i], format);
                body += sms[i].getDisplayMessageBody();
            }
        }
    }

    //动态广播必须要取消注册
    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(smsBroadReceiver);
    }

    //发送一条通知
    private void sendNotification(String name, String smsbody, Context context) {
        int i = 0;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent intent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        Notification notification = new Notification.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentText(smsbody)
                                    .setContentTitle(name)
                                    .setContentIntent(intent).build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(i++, notification);
    }
}