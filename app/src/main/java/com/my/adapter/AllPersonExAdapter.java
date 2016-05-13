package com.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.phonebook.AllPerson;
import com.my.phonebook.PersonClass;
import com.my.phonebook.R;

public class AllPersonExAdapter extends BaseExpandableListAdapter{

    private Context context;
    private AllPerson allPerson;

    public AllPersonExAdapter(Context context, AllPerson allPerson) {
        this.context = context;
        this.allPerson = allPerson;
    }

    // 获得组数
    @Override
    public int getGroupCount() {
        return allPerson.getGroupCount();
    }

    @Override
    public int getChildrenCount(int i) {
        return allPerson.getChildCount(i);
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_ex_group,null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }

        groupViewHolder.letterTv.setText(allPerson.getGroupName(i));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_fragment_person,null);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        PersonClass personClass  = allPerson.getChild(i,i1);
        childViewHolder.nameTv.setText(personClass.getName());
        childViewHolder.numberTv.setText(personClass.getNumber());
        childViewHolder.imageIv.setImageBitmap(personClass.getImageid());
        return view;
    }

    //group缓存类
    class GroupViewHolder{
        TextView letterTv;
        public GroupViewHolder(View view){
            letterTv = (TextView) view.findViewById(R.id.tv_group_letter);
        }
    }

    //child缓存类
    class ChildViewHolder{
        TextView nameTv,numberTv;
        ImageView imageIv;
        public ChildViewHolder(View view) {
            nameTv = (TextView) view.findViewById(R.id.fragment_person_name);
            numberTv = (TextView) view.findViewById(R.id.fragment_person_number);
            imageIv = (ImageView) view.findViewById(R.id.fragment_person_iv);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    //自定义组件加的这个方法
    public int getIndexFromString(String s){
        for (int i = 0; i < allPerson.getFirstLetter().size(); i++) {
            if(allPerson.getFirstLetter().get(i).equals(s)){
                return i;
            }
        }
        return -1;
    }
}