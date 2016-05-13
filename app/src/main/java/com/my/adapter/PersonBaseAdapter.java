package com.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.phonebook.PersonClass;
import com.my.phonebook.R;

import java.util.ArrayList;

public class PersonBaseAdapter extends BaseAdapter {

    private ArrayList<PersonClass> datas;
    private Context context;

    public PersonBaseAdapter(Context context, ArrayList<PersonClass> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size() > 0 && datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return datas.size() > 0 && datas != null ? datas.get(i) : 0;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = new ViewHolder();
        if (view == null) {
            // 加载布局文件
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_fragment_person, null);
            // 将布局文件id与缓存类属性绑定
            holder.iv = (ImageView) view.findViewById(R.id.fragment_person_iv);
            holder.tv_name = (TextView) view.findViewById(R.id.fragment_person_name);
            holder.tv_nameber = (TextView) view.findViewById(R.id.fragment_person_number);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 创建一个PersonClass对象获取每一行的数据
        PersonClass personClass = (PersonClass) getItem(i);
        if (personClass != null) {
            holder.iv.setImageBitmap(personClass.getImageid());
            holder.tv_name.setText(personClass.getName());
            holder.tv_nameber.setText(personClass.getNumber());
        }
        return view;
    }


    private class ViewHolder {
        private ImageView iv;
        private TextView tv_name, tv_nameber;
    }
}
