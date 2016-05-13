package com.my;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.my.adapter.MessageAdapter;
import com.my.adapter.SmsDetailAdapter;
/**/

/**
 * Created by dllo on 15/12/24.
 */
public class MessageService extends Service {

    SendMessageBinder binder = new SendMessageBinder();

    //第一次创建调用
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MessageService", "onCreate()");
    }

    //每调用一次startService()的时候,都会执行的生命周期
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("MessageService", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    //绑定时候用
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //最后一个生命周期,在服务销毁的时候调用
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MessageService", "onDestroy()");
    }

    public class SendMessageBinder extends Binder {

        private Handler handler;

        public void SendMessageBinder(final String number, final String smsbody, final SmsDetailAdapter smsDetailAdapter) {
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Toast.makeText(MessageService.this, "还有5秒发送短信", Toast.LENGTH_SHORT).show();
                    smsDetailAdapter.addsendSms(smsbody);
                    return false;
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(number, null, smsbody, null, null);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
