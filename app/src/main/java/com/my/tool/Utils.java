package com.my.tool;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by dllo on 15/12/30.
 */
public class Utils {

    //获取屏幕宽度的工具类
    public static  int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return  outMetrics.widthPixels;
    }

}
