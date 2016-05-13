package com.my.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.my.adapter.AllPersonExAdapter;

/**
 * Created by dllo on 15/12/28.
 * 自定义组件,索引条
 */
public class IndexView extends View {

    public static final String[] WORDS = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int preHeight = 18; //每一个字母的高度
    private ExpandableListView expandableListView; //我们需要设置索引的ListView
    private Paint mPaint;   //画笔
    private int blankHeight;    //空白的高度,留着计算
    private AllPersonExAdapter allPersonExAdapter;

    //设置ListView
    public void setExpandableListView(ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
        allPersonExAdapter = (AllPersonExAdapter) expandableListView.getExpandableListAdapter();   //获得ListView关联的Adapter
    }

    public IndexView(Context context) {
        super(context);
        init();
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        //文字的对齐方式是居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        //将高度从dp装换成px
        preHeight = dpToPx(preHeight, getContext());
        //设置字体大小的时候,需要px
        mPaint.setTextSize(preHeight);
        mPaint.setAntiAlias(true);  //抗锯齿
    }

    //在该生命周期中,会测量控件的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getMeasuredHeight() 获得该组件的高度
        blankHeight = (getMeasuredHeight() - (WORDS.length * preHeight)) / (WORDS.length) - 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < WORDS.length; i++) {
            //在该组件上画文字
            canvas.drawText(WORDS[i], getMeasuredWidth() / 2, (i + 1) * (preHeight + blankHeight), mPaint);
        }
    }

    //该方法会处理用户的点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当用户按下的时候
            int index = (int) (event.getY() / (preHeight + blankHeight));
            if (index >= WORDS.length) {
                //做一个修正
                index = WORDS.length - 1;
            } else if (index < 0) {
                index = 0;
            }
            //通过字母 获得listView的条数,并滚动到该位置
            int pos = allPersonExAdapter.getIndexFromString(WORDS[index]);
            if (pos >= 0) {
                expandableListView.setSelection(pos);    //ListView滚动到pos
            }
        }
        return super.onTouchEvent(event);
    }

    //将dp装换成px
    private int dpToPx(int dp, Context context) {
        int px = 0;
        //获得屏幕的dpi,需要通过Resources下得DisplayMetrics中得densityDpi
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // +0.5f是为了四舍五入
        px = (int) (dp * metrics.densityDpi / 160f + 0.5f);
        return px;
    }
}
