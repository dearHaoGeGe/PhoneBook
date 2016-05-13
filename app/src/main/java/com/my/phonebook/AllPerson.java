package com.my.phonebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dllo on 15/12/14.
 */
public class AllPerson {

    List<String> firstLetter;       // 用于存放首字母的集合
    Map<String,List<PersonClass>> data; // 存放所有的数据

    public AllPerson(){
        firstLetter = new ArrayList<>();
        data = new HashMap<>();
    }

    public List<String> getFirstLetter() {
        return firstLetter;
    }

    /**
     * 添加数据的方法
      * @param personClass  联系人
     */
    public void addContact(PersonClass personClass){
        // 取出首字母
        String first = personClass.getName().substring(0, 1);
        first = first.toUpperCase();
        //ArrayList<Contact> contacts = (ArrayList<Contact>) data.get(first);
        if(firstLetter.contains(first)){ // 有
            // 将该联系人加到对应位置
            data.get(first).add(personClass);
        } else {
            firstLetter.add(first); // 将该首字母加到字母索引里
            ArrayList<PersonClass> contacts = new ArrayList<>();
            contacts.add(personClass);
            data.put(first, contacts);   // 将联系人加到合适位置

            Collections.sort(firstLetter);  // 排序
        }
    }

    // 获得数组
    public int getGroupCount(){
        return firstLetter.size();
    }

    // 由组数获取联系人子项数量
    public int getChildCount(int position){
        String s = firstLetter.get(position);
        // get(s)就是字母(A.B....Z)
        return data.get(s).size();
    }

    // 通过组号来获得组的内容
    public String getGroupName(int position){
        return firstLetter.get(position);
    }

    public PersonClass getChild(int groupPosition,int childPosition){
        String s = firstLetter.get(groupPosition);
        PersonClass personClass = data.get(s).get(childPosition);
        return personClass;
    }
}