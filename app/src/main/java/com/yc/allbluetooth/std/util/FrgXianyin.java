package com.yc.allbluetooth.std.util;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Date:2022/11/9 15:46
 * author:jingyu zheng
 */
public class FrgXianyin {
    /**
     *
     * @param type  1.三通道；2.YN；3.DY
     * @param llAll 直阻测试首页
     * @param llStdYi   三通道点击开始测试后布局
     * @param llYnYi    YN点击开始测试后的布局
     * @param llDyYi    DY点击开始测试后的布局
     */
    public void start(int type, LinearLayout llAll,LinearLayout llStdYi,LinearLayout llYnYi,LinearLayout llDyYi){
        if(type == 1){
            llAll.setVisibility(View.GONE);
            llStdYi.setVisibility(View.VISIBLE);
        }else if(type == 2){
            llAll.setVisibility(View.GONE);
            llYnYi.setVisibility(View.VISIBLE);
        }else if(type == 3){
            llAll.setVisibility(View.GONE);
            llDyYi.setVisibility(View.VISIBLE);
        }
    }
    public void stop(int type, LinearLayout llAll,LinearLayout llStdYi,LinearLayout llStdEr,LinearLayout llYnYi,LinearLayout llDyYi){
        if(type == 1){
            llAll.setVisibility(View.VISIBLE);
            llStdYi.setVisibility(View.GONE);
        }else if(type == 2){
            llAll.setVisibility(View.VISIBLE);
            llYnYi.setVisibility(View.GONE);
        }else if(type == 3){
            llAll.setVisibility(View.VISIBLE);
            llDyYi.setVisibility(View.GONE);
        }
    }
}
