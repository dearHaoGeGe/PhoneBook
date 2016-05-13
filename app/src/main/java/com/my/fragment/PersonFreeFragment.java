package com.my.fragment;


import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.my.adapter.PersonBaseAdapter;
import com.my.phonebook.PersonClass;
import com.my.phonebook.R;

import java.util.ArrayList;

public class PersonFreeFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private ListView listView;
    private PersonBaseAdapter adapter;
    private ContentResolver cr;
    private String name, number;
    private Bitmap personImage;

    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_free, null, false);
        listView = (ListView) view.findViewById(R.id.fragment_person_listview_free);

        // 在listview添加点击事件
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        cr = getActivity().getContentResolver();

        ArrayList<PersonClass> data = new ArrayList<>();

//        for (int i = 0; i < 20; i++)
//        {
//            data.add(new PersonClass(R.mipmap.person_account,"习近平"));
//            data.add(new PersonClass(R.mipmap.person_account,"李克强"));
//            data.add(new PersonClass(R.mipmap.person_account,"奥巴马"));
//            data.add(new PersonClass(R.mipmap.person_account,"希拉里"));
//            data.add(new PersonClass(R.mipmap.person_account,"安倍晋三"));
//        }
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                personImage = GetPersonImage(cursor);


                //去不必要的字段
                number = getDelNumber(number);

                data.add(new PersonClass(personImage, name, number));
            }
        }

        adapter = new PersonBaseAdapter(this.getActivity(), data);
        listView.setAdapter(adapter);

        return view;
    }

    private Bitmap GetPersonImage(Cursor cursorimage) {
        String photoid = cursorimage.getString(cursorimage.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
        if (photoid != null) {
            Cursor c = this.getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data.DATA15}, ContactsContract.Data._ID + "=" + photoid, null,
                    null);
            c.moveToFirst();
            byte[] ss = c.getBlob(0);
            if (ss != null) {
                Bitmap map = BitmapFactory.decodeByteArray(ss, 0, ss.length);
                c.close();
                return map;
            }
            c.close();
            return null;
        }
        return null;
    }

    // 实现点击方法
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PersonClass personClass = (PersonClass) adapterView.getItemAtPosition(i);
        Toast.makeText(getActivity(), "你点击了:"+personClass.getName()+"，在第"+(i+1)+"行", Toast.LENGTH_SHORT).show();
    }

    // 实现长按方法
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "长按当前", Toast.LENGTH_SHORT).show();
        // 返回true没有一次点击事件，返回false有一次点击事件
        return true;
    }

    //去空格、去()、去+86、去-
    public static String getDelNumber(String Number){
        Number = Number.replace("+86", ""); //去+86
        Number = Number.replace("-", "");   //去-
        Number = Number.replace("(","");    //去(
        Number = Number.replace(")","");    //去)
        Number = Number.replace(" ","");    //去空格
        return Number;
    }
}
