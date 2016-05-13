package com.my.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.my.adapter.MessageAdapter;
import com.my.beans.SmsByPersonBean;
import com.my.activity.MainActivity;
import com.my.phonebook.R;

/**
 * Created by dllo on 15/12/23.
 */
public class SmsBroadcast extends BroadcastReceiver {

    private MessageAdapter messageAdapter;
    private SmsByPersonBean smsByPersonBean;

    @Override
    public void onReceive(Context context, Intent intent) {
        //静态广播的方法
        messageAdapter = new MessageAdapter(context);

        Object[] pduses = (Object[]) intent.getExtras().get("pdus");
        String number = "";
        String smsbody = "";
        for(Object pdus : pduses){
            byte[] pdusmessage = (byte[]) pdus;
            SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);
            number = sms.getOriginatingAddress();   //发送短信的手机号码
            smsbody += sms.getMessageBody();   //短息内容
        }

        messageAdapter.addSms(smsbody,number,1);
        //发送一条通知
        sendNotification(number,smsbody,context);
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
