package com.yc.allbluetooth.huilu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @Date 2024/7/26 11:29
 * @Author ZJY
 * @email 714694358@qq.com
 * 禁用vp左右滑动
 */
public class NonSwipeableViewPager extends ViewPager {

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 禁用触摸事件
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 禁用拦截触摸事件
        return false;
    }

    // 如果需要，可以添加其他需要的构造函数和方法
}