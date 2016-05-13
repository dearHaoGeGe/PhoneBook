package com.my.fragment;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.my.phonebook.R;

public class PersonActivityfragment extends Fragment {

    private TabHost mytabHost;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabhost_person,null);
        mytabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        // 把当前的tabhost加载到主函数中
        mytabHost.setup();
        // 创建标签
        TabHost.TabSpec tab1 = mytabHost.newTabSpec("tab1");
        // 设置标签内容
        tab1.setIndicator("免费");
        // 根据id来绑定视图
        tab1.setContent(R.id.tabhost_person_free);
        // 将标签添加到TabHost里
        mytabHost.addTab(tab1);

        TabHost.TabSpec tab2 = mytabHost.newTabSpec("tab2");
        tab2.setIndicator("全部");
        tab2.setContent(R.id.tabhost_person_all);
        mytabHost.addTab(tab2);

        // 初始化碎片管理器
        fm = getActivity().getSupportFragmentManager();
        // 初始化碎片业务类
        FragmentTransaction transaction = fm.beginTransaction();
        // 替换fragment
        transaction.replace(R.id.tabhost_person_free,new PersonFreeFragment());
        transaction.replace(R.id.tabhost_person_all,new PersonAllFragment());
        // 提交业务
        transaction.commit();

        return view;
    }
}

