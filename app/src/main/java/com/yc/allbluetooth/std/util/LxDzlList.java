package com.yc.allbluetooth.std.util;

import android.util.Log;
import android.widget.TextView;

import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/7 11:00
 * author:jingyu zheng
 * 零线电阻状态集合
 */
public class LxDzlList {
    /**
     * 零线电阻开关状态，ON、OFF给TV赋值
     * @param str
     * @param textView
     * @return
     */
    public void DzType(String str, TextView textView){
        String dzType = "";
        if(StringUtils.isEquals(str,"OFF")){
            dzType = "ON";
            Log.e("",dzType);
            Config.isStdLingxian = 1;
        }else {
            dzType = "OFF";
            Config.isStdLingxian = 0;
            Log.e("",dzType);
        }
        textView.setText(dzType);
    }
}
