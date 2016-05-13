package com.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.beans.SingleSmsBean;
import com.my.custom.SlidingMenuView;
import com.my.interfaces.MyOnClickListener;
import com.my.beans.SmsByPersonBean;
import com.my.beans.SmsData;
import com.my.phonebook.R;
import com.my.tool.Utils;

import java.text.SimpleDateFormat;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> implements SlidingMenuView.SlidingMenuListener {

    private Context context;
    private SmsData smsData;
    private MyOnClickListener myOnClickListener;
    private SlidingMenuView mMenu;  //侧滑菜单

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    public MessageAdapter(Context context) {
        this.context = context;
        smsData = new SmsData(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fragment_message, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_message_name.setText(smsData.getSmsByPersonBeans().get(position).getName());
        holder.tv_message_content.setText(smsData.getSmsByPersonBeans().get(position).getSingleSmsBeans().get(0).getSmsBody());
        //将布局挤出屏幕
        holder.linearLayout.getLayoutParams().width = Utils.getScreenWidth(context);

        //添加行点击监听
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMenu != null){
                    closeMenu();
                }
                int pos = holder.getLayoutPosition();
                myOnClickListener.myOnClick(position);
            }

        });

        holder.menuTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "当前有 "+ smsData.getSmsByPersonBeans().get(position).getSize(), Toast.LENGTH_SHORT).show();
            }
        });

//        //接口监听
//        if (myOnClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = holder.getLayoutPosition();
//                    myOnClickListener.myOnClick(pos);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return smsData.getCount();
    }

    @Override
    public void onMenuIsOpen(SlidingMenuView slidingMenuView) {
        mMenu = slidingMenuView;
    }

    @Override
    public void onDownOrMove(SlidingMenuView slidingMenuView) {
        if(menuIsOpen()){
            if(mMenu != slidingMenuView){
                closeMenu();
            }
        }
    }

    private void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }

    private boolean menuIsOpen() {
        return mMenu != null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message_name;
        TextView tv_message_content;
        LinearLayout linearLayout;
        TextView menuTv;    //侧滑菜单

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_message_name = (TextView) itemView.findViewById(R.id.item_message_tv_name);
            tv_message_content = (TextView) itemView.findViewById(R.id.item_message_tv_content);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.item_LinearLayout);
            menuTv = (TextView) itemView.findViewById(R.id.tv_menu);    //侧滑菜单
            //实现监听方法
            ((SlidingMenuView) itemView).setSlidingMenuListener(MessageAdapter.this);
        }
    }

    public SmsByPersonBean getSmsByPersonBean(int position) {
        return smsData.getSmsByPersonBeans().get(position);
    }

    /**
     * 向adapter中添加数据
     *
     * @param body   短息的内容
     * @param number 短信的电话号码
     */
    public void addSms(String body, String number) {
        //创建出要加入的数据类
        SingleSmsBean singleSmsBean = new SingleSmsBean();
        singleSmsBean.setType(2);   //设置该条的类型是发送
        singleSmsBean.setSmsBody(body); //设置短息内容
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //获取系统时间,并格式化
        String date = sDateFormat.format(new java.util.Date()); //设置时间

        //向smsData里添加数据
        smsData.addSingleSms(singleSmsBean, number);
        notifyDataSetChanged(); //通知适配器刷新数据
    }

    /**
     * 向adapter中添加数据
     *
     * @param body   短息的内容
     * @param number 短信的电话号码
     * @param type   类型
     */
    public void addSms(String body, String number, int type) {
        //创建出要加入的数据类
        SingleSmsBean singleSmsBean = new SingleSmsBean();
        singleSmsBean.setType(type);   //设置该条的类型是发送
        singleSmsBean.setSmsBody(body); //设置短息内容
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //获取系统时间,并格式化
        String date = sDateFormat.format(new java.util.Date()); //设置时间

        //向smsData里添加数据
        smsData.addSingleSms(singleSmsBean, number);
        notifyDataSetChanged(); //通知适配器刷新数据
    }
}
