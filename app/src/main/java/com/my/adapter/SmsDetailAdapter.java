package com.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.beans.SingleSmsBean;
import com.my.beans.SmsByPersonBean;
import com.my.phonebook.R;

import java.text.SimpleDateFormat;

/**
 * Created by dllo on 15/12/18.
 */
public class SmsDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private SmsByPersonBean smsByPersonBean;

    public SmsDetailAdapter(Context context, SmsByPersonBean smsByPersonBean) {
        this.context = context;
        this.smsByPersonBean = smsByPersonBean;
    }

    public SmsDetailAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_sms_recive, parent, false);
            ReciveViewHolder reciveViewHolder = new ReciveViewHolder(view);
            return reciveViewHolder;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_sms_send, parent, false);
            SendViewHolder sendViewHolder = new SendViewHolder(view);
            return sendViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            ReciveViewHolder reciveViewHolder = (ReciveViewHolder) holder;
            reciveViewHolder.reciveTv.setText(smsByPersonBean.getSingleSmsBeans().get(smsByPersonBean.getSingleSmsBeans().size() - position - 1).getSmsBody());
        } else {
            SendViewHolder sendViewHolder = (SendViewHolder) holder;
            sendViewHolder.sendTv.setText(smsByPersonBean.getSingleSmsBeans().get(smsByPersonBean.getSingleSmsBeans().size() - position - 1).getSmsBody());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return smsByPersonBean.getSingleSmsBeans().get(position).getType();
    }

    @Override
    public int getItemCount() {
        return smsByPersonBean == null ? 0 : smsByPersonBean.getSingleSmsBeans().size();
    }

    class SendViewHolder extends RecyclerView.ViewHolder {
        TextView sendTv;

        public SendViewHolder(View itemView) {
            super(itemView);
            sendTv = (TextView) itemView.findViewById(R.id.item_message_tv_send);
        }
    }

    class ReciveViewHolder extends RecyclerView.ViewHolder {
        TextView reciveTv;

        public ReciveViewHolder(View itemView) {
            super(itemView);
            reciveTv = (TextView) itemView.findViewById(R.id.item_message_tv_recive);
        }
    }

    /**
     * 向RecyclerView，
     *
     * @param body 短息的内容
     */
    public void addsendSms(String body) {
        //创建出要加入的数据类
        SingleSmsBean singleSmsBean = new SingleSmsBean();
        singleSmsBean.setType(2);   //设置该条的类型是发送
        singleSmsBean.setSmsBody(body); //设置短息内容
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //获取系统时间,并格式化
        String date = sDateFormat.format(new java.util.Date());
        singleSmsBean.setDate(date);    //设置时间
        int pos = smsByPersonBean.getSize();  //保存一下当前的位置
        //将新的这一条短息
        smsByPersonBean.addSingleBean(singleSmsBean);
        notifyDataSetChanged(); // 通知 Adapter 加入一条数据在最后位置
    }


}
