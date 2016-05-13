package com.my.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.my.adapter.MyFragmentPageAdapter;
import com.my.fragment.CallRecordingFragment;
import com.my.fragment.DialFragment;
import com.my.fragment.PersonActivityfragment;
import com.my.fragment.MessageFragment;
import com.my.phonebook.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Fragment> data;
    private ViewPager viewPager;
    private MyFragmentPageAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        data = new ArrayList<>();
        data.add(new DialFragment());
        data.add(new CallRecordingFragment());
        data.add(new PersonActivityfragment());
        data.add(new MessageFragment());

        adapter = new MyFragmentPageAdapter(getSupportFragmentManager(),data,this);
        viewPager.setAdapter(adapter);

        // 设置选中前的颜色和不选中的颜色
        tabLayout.setTabTextColors(Color.GRAY, Color.GREEN);

        tabLayout.setupWithViewPager(viewPager);
        // 最下面选中的颜色
        tabLayout.setSelectedTabIndicatorColor(Color.alpha(1));

        for (int i = 0;i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null){
                tab.setCustomView(adapter.getTabView(i));
            }
        }
    }
}
