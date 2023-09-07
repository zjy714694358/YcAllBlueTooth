package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/7 11:00
 * author:jingyu zheng
 * 助磁状态集合
 */
public class DyZcList {
    /**
     * 助磁开关状态，ON、OFF给TV赋值
     * @param str
     * @param textView
     * @return
     */
    public void ZcType(String str, TextView textView){
        String dzType = "";
        if(StringUtils.isEquals(str,"OFF")){
            dzType = "ON";
        }else {
            dzType = "OFF";
        }
        textView.setText(dzType);
    }
}
