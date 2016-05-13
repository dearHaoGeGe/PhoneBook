package com.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.phonebook.PersonClass;
import com.my.phonebook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllo on 15/12/31.
 */
public class GroupSmsSendAdapter extends RecyclerView.Adapter<GroupSmsSendAdapter.MyViewHolder> {

    private Context context;
    private List<PersonClass> personClassList;
    private List<Boolean> checkStates;

    public GroupSmsSendAdapter(Context context,List<PersonClass> personClassList) {
        this.context = context;
        this.personClassList = personClassList;
        initData();
    }

    //初始化checkStates的数据
    private void initData() {
        checkStates = new ArrayList<>();
        for (int i = 0; i < personClassList.size(); i++) {
            checkStates.add(false); //默认不被选中
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_groupsmssend,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_name.setText(personClassList.get(position).getName());
        holder.tv_number.setText(personClassList.get(position).getNumber());
        holder.iv_head.setImageBitmap(personClassList.get(position).getImageid());
        holder.checkBox.setChecked(checkStates.get(position));//设置开始的时候的选中状态
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos=holder.getLayoutPosition();
                checkStates.set(pos,isChecked);
            }
        });
    }

    public List<String> getCheckedData(){
        List<String> number=new ArrayList<>();
        for (int i = 0; i < checkStates.size(); i++) {
            if(checkStates.get(i)){
                number.add(personClassList.get(i).getNumber());
            }
        }
        return number;
    }

    @Override
    public int getItemCount() {
        return personClassList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_number;
        ImageView iv_head;
        CheckBox checkBox;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.name_person_groupsmssend);
            tv_number = (TextView) itemView.findViewById(R.id.number_person_groupsmssend);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_person_groupsmssend);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_person_groupsmssend);
        }
    }

    public void addData(PersonClass personClass, int pos){
        personClassList.add(pos,personClass);
        notifyItemInserted(pos);
    }
}
