package com.my.fragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.my.adapter.CallRecordingBaseAdapter;
import com.my.phonebook.CallRecordingClass;
import com.my.phonebook.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallRecordingFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private CallRecordingBaseAdapter adapter;
    private ContentResolver cr;
    /**
     * @type 通话类型
     * @number 电话号码
     * @time 呼叫时间
     * @name 联系人
     * @duration 通话时间，单位s
     */
    private String type, number, time, name, duration;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_callrecording, null);

        listView = (ListView) view.findViewById(R.id.fragment_callrecording_listview);
        // 添加点击事件
        // listView.setOnItemClickListener(this);

        ArrayList<CallRecordingClass> data = new ArrayList<>();

        cr = getActivity().getContentResolver();
        // 最后一个参数是，正序排序电话号码
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CallLog.Calls calls = new CallLog.Calls();
                // 号码
                number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                // 呼叫类型
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                    case CallLog.Calls.INCOMING_TYPE:
                        type = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        type = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        type = "未接";
                        break;
                    default:
                        type = "挂断";
                        break;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                // 呼出时间
                time = sdf.format(date);
                // 联系人
                name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                // 通话时间，单位s
                duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                
                data.add(new CallRecordingClass(name, number, "位置", time, R.mipmap.conversation_01));
            }
        }
        cursor.close();

        adapter = new CallRecordingBaseAdapter(data, this.getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        return view;
    }

    // 添加通话记录点击方法
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // 点击拨打电话  要先得到位置之后才能根据位置判断出电话号是什么
        CallRecordingClass callRecordingClass = (CallRecordingClass) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callRecordingClass.getNumber()));
        startActivity(intent);
    }
}