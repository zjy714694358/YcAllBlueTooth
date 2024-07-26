package com.yc.allbluetooth.huilu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

/**
 * @Date 2024/7/26 13:09
 * @Author ZJY
 * @email 714694358@qq.com
 */
public class CustomTabLayout extends TabLayout {
    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        final View tabView = tab.getCustomView() != null ? tab.getCustomView() : tab.view;
        if (tabView != null) {
            tabView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true; // 返回true，表示已处理长按事件，不会显示默认的提示文本
                }
            });
        }
    }
}
