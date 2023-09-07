package com.yc.allbluetooth.dtd10c.util;

import android.app.Activity;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2023/3/27 10:08
 * author:jingyu zheng
 */
public class Xianshi {
    /**
     * 相别(根据返回值显示对应的相别)
     * @param str   00~05
     * @param textView    要显示的TV
     */
    public static void xiangbie(String str, TextView textView){
        if(StringUtils.isEquals("00",str)){
            textView.setText("A0");
        }else if(StringUtils.isEquals("01",str)){
            textView.setText("B0");
        }else if(StringUtils.isEquals("02",str)){
            textView.setText("C0");
        }else if(StringUtils.isEquals("03",str)){
            textView.setText("AB");
        }else if(StringUtils.isEquals("04",str)){
            textView.setText("BC");
        }else if(StringUtils.isEquals("05",str)){
            textView.setText("CA");
        }
    }
    /**
     * 绕组(高中低压6-7-8)
     * @param str   06~08
     * @param textView    要显示的TV
     * @param activity    当前页面
     */
    public static void raozu(String str, TextView textView, Activity activity){
        if(StringUtils.isEquals("06",str)){
            textView.setText(activity.getString(R.string.gaoya));
        }else if(StringUtils.isEquals("07",str)){
            textView.setText(activity.getString(R.string.zhongya));
        }else if(StringUtils.isEquals("08",str)){
            textView.setText(activity.getString(R.string.diya));
        }
    }
    /**
     * 绕组材料(铜铝-9、10)
     * @param str   09-0A
     * @param textView    要显示的TV
     * @param activity    当前页面
     */
    public static void rzcl(String str, TextView textView, Activity activity){
        if(StringUtils.isEquals("09",str)){
            textView.setText(activity.getString(R.string.tong));
        }else if(StringUtils.isEquals("0A",str)){
            textView.setText(activity.getString(R.string.lv));
        }
    }
}
