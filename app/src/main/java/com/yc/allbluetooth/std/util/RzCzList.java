package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/7 11:00
 * author:jingyu zheng
 * 绕组材质：铜、铝
 */
public class RzCzList {
    /**
     * 铜、铝给TV赋值
     * @param str
     * @param textView
     * @return
     */
    public void Rzcz(String str, TextView textView){
        String rzcz = "";
        if(StringUtils.isEquals(str,"铜")){
            rzcz = "铝";
        }else if(StringUtils.isEquals(str,"铝")){
            rzcz = "铜";
        }else if(StringUtils.isEquals(str,"copper")){
            rzcz = "aluminum";
        }else if(StringUtils.isEquals(str,"aluminum")){
            rzcz = "copper";
        }
        textView.setText(rzcz);
    }
}
