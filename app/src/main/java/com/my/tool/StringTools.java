package com.my.tool;

/**
 * Created by dllo on 15/12/21.
 */
public class StringTools {
    public static  String formatNumber(String num){
        num = num.replace("+86", ""); //去+86
        num = num.replace("-", "");   //去-
        num = num.replace("(","");    //去(
        num = num.replace(")","");    //去)
        num = num.replace(" ","");    //去空格
        return num;
    }
}
