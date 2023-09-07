package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/9 9:11
 * author:jingyu zheng
 * 消磁相别集合
 */
public class XiaociXbList {
    public void Xb(String str, TextView textView){
        if(StringUtils.isEquals(str,"A0")){
            textView.setText("B0");
        }else if(StringUtils.isEquals(str,"B0")){
            textView.setText("C0");
        }else if(StringUtils.isEquals(str,"C0")){
            textView.setText("ab");
        }else if(StringUtils.isEquals(str,"ab")){
            textView.setText("bc");
        }else if(StringUtils.isEquals(str,"bc")){
            textView.setText("ca");
        }else if(StringUtils.isEquals(str,"ca")){
            textView.setText("A0");
        }
    }
}
