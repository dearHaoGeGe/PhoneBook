package com.my.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.my.phonebook.R;

/**
 * Created by dllo on 15/12/30.
 * 为Item写的自定义组件,可以横向滚动,拉出菜单 继承 横向滚动的滚动条
 */
public class SlidingMenuView extends HorizontalScrollView {

    private TextView scrollTv;  //滚动条里的菜单Tv
    private int mScrollWidth;   //滚动条 滚动的范围
    private SlidingMenuListener slidingMenuListener;    //实现接口的对象
    private boolean isOpen = false; //标记菜单是否被打开
    private boolean once = true;   //标记是否是第一次加载

    //设置接口对象
    public void setSlidingMenuListener(SlidingMenuListener slidingMenuListener) {
        this.slidingMenuListener = slidingMenuListener;
    }

    public SlidingMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //取消边界效果
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.setHorizontalScrollBarEnabled(false);  //取消灰色条
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (once) {
            //我们第一次加载的时候,对菜单Tv进行初始化
            //在onMeasure方法里进行,是因为,在该方法执行时,组件的宽高会固定下来
            scrollTv = (TextView) findViewById(R.id.tv_menu);
        }
    }

    //当执行该方法后,滚动条里的组件宽高会被测量
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //我们的滚动条 滚动的范围,固定为菜单Tv的宽度
        mScrollWidth = scrollTv.getWidth();
    }

    //监听用户手势的方法
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //如果用户的手势是滑动
                slidingMenuListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
                //当用户的手指离开屏幕的时候
                //判断已经滑动的距离,来决定是否打开菜单
                changeScrollx();
                return true;
        }
        return super.onTouchEvent(ev);
    }

    //当不在屏幕的时候 关闭
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        closeMenu();
    }

    //判断已经滑动的距离,来决定是否打开菜单
    private void changeScrollx() {
        if (getScrollX() >= (mScrollWidth / 2)) {
            //如果 活动的距离,超过了菜单的一半,则打开菜单
            this.smoothScrollTo(mScrollWidth, 0);
            isOpen = true;
            slidingMenuListener.onMenuIsOpen(this); //回调打开接口
        } else {
            //如果没 滑动到一半的距离,则不打开菜单
            isOpen = false;
            this.smoothScrollTo(0, 0);
        }
    }

    //关闭菜单
    public void closeMenu() {
        if (!isOpen) {
            //如果菜单不是打开的状态,则不做任何操作
            return;
        }
        //如果是打开的状态,则关闭菜单
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }

    //接口
    public interface SlidingMenuListener {
        //菜单被打开时的回调
        void onMenuIsOpen(SlidingMenuView slidingMenuView);

        //当滑动或按下时的回调
        void onDownOrMove(SlidingMenuView slidingMenuView);
    }
}
