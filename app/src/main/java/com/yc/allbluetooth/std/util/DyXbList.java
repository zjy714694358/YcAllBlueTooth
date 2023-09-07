package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/7 13:45
 * author:jingyu zheng
 * D(Y)测试相别集合
 */
public class DyXbList {
    public void Xb(String str, TextView textView){
        if(StringUtils.isEquals(str,"ab")){
            textView.setText("bc");
        }else if(StringUtils.isEquals(str,"bc")){
            textView.setText("ca");
        }else if(StringUtils.isEquals(str,"ca")){
            textView.setText("ab");
        }
    }
}
