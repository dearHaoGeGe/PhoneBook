package com.my.adapter;


import android.content.Context;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.phonebook.CallRecordingClass;
import com.my.phonebook.R;

import java.util.ArrayList;

public class CallRecordingBaseAdapter extends BaseAdapter {

    private ArrayList<CallRecordingClass> datas;
    private Context context;

    public CallRecordingBaseAdapter(ArrayList<CallRecordingClass> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    // 获得数量
    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    // 获得数据内容
    @Override
    public Object getItem(int i) {
        return datas.size() > 0 && datas != null ? datas.get(i) : 0;
    }

    // 得到当前位置的值
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            // 加载行布局文件
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_fragment_callrecording, null);
            // 将行布局中的id与缓存类属性绑定
            holder.iv1 = (ImageView) view.findViewById(R.id.item_listview_fragment_callrecording_im01);
            holder.number = (TextView) view.findViewById(R.id.item_listview_fragment_callrecording_tv01);
            holder.place = (TextView) view.findViewById(R.id.item_listview_fragment_callrecording_tv02);
            holder.time = (TextView) view.findViewById(R.id.item_listview_fragment_callrecording_tv03);
            // 将缓存类对象设置到布局中
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 创建一个CallRecordingClass对象获取每一行的数据
        CallRecordingClass callRecordingClass = (CallRecordingClass) getItem(i);
        if (callRecordingClass != null) {
            // 把CallRecordingClass类中得数据给缓存类
            holder.iv1.setImageResource(callRecordingClass.getImageid());
            holder.time.setText(callRecordingClass.getTime());
            holder.place.setText(callRecordingClass.getPlace());
            // 判断如果name没有存，则输出号码；如果name为储存则输出name
            if (callRecordingClass.getName() == null) {
                holder.number.setText(callRecordingClass.getNumber());
            } else {
                holder.number.setText(callRecordingClass.getName());
            }

        }

        return view;
    }

    // 缓存内部类
    private class ViewHolder {
        private ImageView iv1;
        private TextView name, number, place, time;
    }
}
