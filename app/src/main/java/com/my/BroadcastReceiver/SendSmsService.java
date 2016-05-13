package com.my.BroadcastReceiver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.my.adapter.SmsDetailAdapter;
import com.my.tool.MyValues;

/**
 * 定时发送短信的服务
 */
public class SendSmsService extends Service{

    MBinder mBinder = new MBinder();
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    //5秒后发送的Toast
                    //发送完短信之后回调吐司
                    Toast.makeText(SendSmsService.this, "短息已经发送", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //手动输入的延迟时间Toast
                    Toast.makeText(SendSmsService.this, "短息通过手动输入的方式发送", Toast.LENGTH_SHORT).show();
                    break;
            }

            stopSelf();
            return false;
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MBinder extends Binder{

        public void sendSms(final String number, final String smsbody){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, smsbody, null, null);
                        //告诉handler,回调what值为1的Callback
                        handler.sendEmptyMessage(1);
//                        //发送广播
//                        Intent intent = new Intent();
//                        intent.setAction("com.my.phonebook.NOTICE");
//                        sendBroadcast(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * 重载发送短息方法,可以设定延迟的时间
         * @param number
         * @param smsbody
         * @param sleepTime 延迟时间
         */
        public void sendSms(final String number, final String smsbody, final int sleepTime){
            final int mm = sleepTime * 60 *1000;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //线程sleep的时间是传递过来的
                        Thread.sleep(mm);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, smsbody, null, null);
                        //告诉handler,回调what值为2的Callback
                        handler.sendEmptyMessage(2);
                        //发送广播
                        Intent sendBroadcastIntent = new Intent();
                        sendBroadcastIntent.setAction(MyValues.REFRESH_UI_BROADCAST);
                        //向intent里添加数据
                        sendBroadcastIntent.putExtra("number", number);
                        sendBroadcastIntent.putExtra("smsbody", smsbody);
                        //发送
                        sendBroadcast(sendBroadcastIntent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
