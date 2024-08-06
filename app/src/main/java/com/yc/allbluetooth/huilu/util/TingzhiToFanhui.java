package com.yc.allbluetooth.huilu.util;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * @Date 2024/5/17 11:45
 * @Author ZJY
 * @email 714694358@qq.com
 * 根据测试时间，倒计时，停止自动变成返回
 */
public class TingzhiToFanhui {
    Handler handler = new Handler();
    TextView textView1;
    Activity activity1;
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            textView1.setText(activity1.getString(R.string.fanhui));
        }
    };

    public void HlDaojishiKai(Activity activity, TextView textView,String timeStr){
        int timeI = 0;
        if(StringUtils.isEquals(timeStr,"1S")){
            timeI = 1;
        } else if (StringUtils.isEquals(timeStr,"3S")) {
            timeI = 3;
        }else if (StringUtils.isEquals(timeStr,"10S")) {
            timeI = 10;
        }else if (StringUtils.isEquals(timeStr,"30S")) {
            timeI = 30;
        }else if (StringUtils.isEquals(timeStr,"60S")) {
            timeI = 60;
        }
        activity1 = activity;
        textView1 = textView;
        handler.postDelayed(runnable1,timeI*1000);
    }
    public void HlDaojishiGuan(Activity activity, TextView textView){
        activity1 = activity;
        textView1 = textView;
        handler.removeCallbacks(runnable1);
    }
}
