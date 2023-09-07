package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/7 13:45
 * author:jingyu zheng
 * YN测试相别集合
 */
public class YnXbList {
    public void Xb(String str, TextView textView){
        if(StringUtils.isEquals(str,"A0")){
            textView.setText("B0");
        }else if(StringUtils.isEquals(str,"B0")){
            textView.setText("C0");
        }else if(StringUtils.isEquals(str,"C0")){
            textView.setText("A0");
        }
    }
}
