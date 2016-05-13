package com.my.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.phonebook.R;

public class DialFragment extends Fragment implements View.OnClickListener {

    private ImageButton main_btn1,main_btn2, main_btn3, main_btn4, main_btn5, main_btn6, main_btn7, main_btn8, main_btn9,
            main_btn_mi, main_btn0, main_btn_jing, main_btn_add_contact_person, main_btn_phone, main_btn_del;
    private TextView main_tv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dial,null);

        main_btn1 = (ImageButton) view.findViewById(R.id.main_btn1);
        main_btn2 = (ImageButton) view.findViewById(R.id.main_btn2);
        main_btn3 = (ImageButton) view.findViewById(R.id.main_btn3);
        main_btn4 = (ImageButton) view.findViewById(R.id.main_btn4);
        main_btn5 = (ImageButton) view.findViewById(R.id.main_btn5);
        main_btn6 = (ImageButton) view.findViewById(R.id.main_btn6);
        main_btn7 = (ImageButton) view.findViewById(R.id.main_btn7);
        main_btn8 = (ImageButton) view.findViewById(R.id.main_btn8);
        main_btn9 = (ImageButton) view.findViewById(R.id.main_btn9);
        main_btn_mi = (ImageButton) view.findViewById(R.id.main_btn_mi);
        main_btn0 = (ImageButton) view.findViewById(R.id.main_btn0);
        main_btn_jing = (ImageButton) view.findViewById(R.id.main_btn_jing);
        main_btn_add_contact_person = (ImageButton) view.findViewById(R.id.main_btn_add_contact_person);
        main_btn_phone = (ImageButton) view.findViewById(R.id.main_btn_phone);
        main_btn_del = (ImageButton) view.findViewById(R.id.main_btn_del);
        main_tv = (TextView) view.findViewById(R.id.main_tv);

        main_btn1.setOnClickListener(this);
        main_btn2.setOnClickListener(this);
        main_btn3.setOnClickListener(this);
        main_btn4.setOnClickListener(this);
        main_btn5.setOnClickListener(this);
        main_btn6.setOnClickListener(this);
        main_btn7.setOnClickListener(this);
        main_btn8.setOnClickListener(this);
        main_btn9.setOnClickListener(this);
        main_btn_mi.setOnClickListener(this);
        main_btn0.setOnClickListener(this);
        main_btn_jing.setOnClickListener(this);
        main_btn_add_contact_person.setOnClickListener(this);
        main_btn_phone.setOnClickListener(this);
        main_btn_del.setOnClickListener(this);
        main_tv.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String phoneNumber = new String();
        phoneNumber = main_tv.getText().toString();
        switch (v.getId()) {
            case R.id.main_btn1:
                phoneNumber += 1;
                break;

            case R.id.main_btn2:
                phoneNumber += 2;
                break;

            case R.id.main_btn3:
                phoneNumber += 3;
                break;

            case R.id.main_btn4:
                phoneNumber += 4;
                break;

            case R.id.main_btn5:
                phoneNumber += 5;
                break;

            case R.id.main_btn6:
                phoneNumber += 6;
                break;

            case R.id.main_btn7:
                phoneNumber += 7;
                break;

            case R.id.main_btn8:
                phoneNumber += 8;
                break;

            case R.id.main_btn9:
                phoneNumber += 9;
                break;

            case R.id.main_btn_mi:
                phoneNumber += "*";
                break;

            case R.id.main_btn0:
                phoneNumber += 0;
                break;

            case R.id.main_btn_jing:
                phoneNumber += "#";
                break;

            case R.id.main_btn_add_contact_person:

                break;

            case R.id.main_btn_phone:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
                break;

            case R.id.main_btn_del:
                if(phoneNumber.length()>0){
                    phoneNumber = phoneNumber.substring(0,phoneNumber.length()-1);
                }
                break;

            default:
                break;
        }
        main_tv.setText(phoneNumber);
    }
}
