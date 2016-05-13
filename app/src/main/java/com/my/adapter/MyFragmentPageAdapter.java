package com.my.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.phonebook.R;

import java.util.ArrayList;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> data;
    String [] title = {"拨号","通话记录","联系人","短信"};
    // 设置最下面的四个
    private int [] imageview = {R.drawable.dial,R.drawable.callrecording,R.drawable.person,R.drawable.message};
    private Context context;

    public MyFragmentPageAdapter(FragmentManager fm,ArrayList<Fragment> data,Context context) {
        super(fm);
        this.data = data;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return title[position];
        return null;
    }

    public View getTabView(int position){

        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout,null);
        TextView textView = (TextView) view.findViewById(R.id.tab_layout_tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_layout_im);

        textView.setText(title[position]);
        imageView.setImageResource(imageview[position]);

        return view;

    }
}
