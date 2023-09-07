package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Yn或者Dy测试相别
 * Date:2022/11/15 15:22
 * author:jingyu zheng
 */
public class YnOrDyXb {
    public void xb(String str, TextView tvYn,TextView tvDy){
        if(StringUtils.isEquals("A0",str)||StringUtils.isEquals("B0",str)||StringUtils.isEquals("C0",str)){
            tvYn.setText(str);
        }else if(StringUtils.isEquals("ab",str)||StringUtils.isEquals("bc",str)||StringUtils.isEquals("ca",str)){
            tvDy.setText(str);
        }
    }
    public String xbStr(String str){
        String csxb = "";
        if(StringUtils.isEquals("00",str)){
            csxb = "A0";
        }else if(StringUtils.isEquals("01",str)){
            csxb = "B0";
        }else if(StringUtils.isEquals("02",str)){
            csxb = "C0";
        }else if(StringUtils.isEquals("03",str)){
            csxb = "ab";
        }else if(StringUtils.isEquals("04",str)){
            csxb = "bc";
        }else if(StringUtils.isEquals("05",str)){
            csxb = "ca";
        }
        return csxb;
    }
}
