package com.yc.allbluetooth.dtd10c.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2023/2/28 10:52
 * author:jingyu zheng
 * 切换集合
 */
public class Qiehuan {
    /**
     * 绕组切换
     * @param str   当前显示的绕组值（高、中、低压）
     * @param textView  要显示的TV
     */
    public static String  raozu(String str, TextView textView){
        String rzZhiling = "";
        if(StringUtils.isEquals("高压",str)){
            rzZhiling = "07";
            textView.setText("中压");
        }else if(StringUtils.isEquals("中压",str)){
            textView.setText("低压");
            rzZhiling = "08";
        }else if(StringUtils.isEquals("低压",str)){
            textView.setText("高压");
            rzZhiling = "06";
        }else if(StringUtils.isEquals("HV",str)){
            textView.setText("MV");
            rzZhiling = "07";
        }else if(StringUtils.isEquals("MV",str)){
            textView.setText("LV");
            rzZhiling = "08";
        }else if(StringUtils.isEquals("LV",str)){
            textView.setText("HV");
            rzZhiling = "06";
        }
        return rzZhiling;
    }

    /**
     * 绕组材料
     * @param str   当前显示绕组材料值（铜、铝）
     * @param textView    要显示的TV
     */
    public static String raozucailiao(String str, TextView textView){
        String rzClZhiling = "";
        if(StringUtils.isEquals("铜",str)){
            textView.setText("铝");
            rzClZhiling = "0a";
        }else if(StringUtils.isEquals("铝",str)){
            textView.setText("铜");
            rzClZhiling = "09";
        }else if(StringUtils.isEquals("CU",str)){
            textView.setText("AL");
            rzClZhiling = "0a";
        }else if(StringUtils.isEquals("AL",str)){
            textView.setText("CU");
            rzClZhiling = "09";
        }
        return rzClZhiling;
    }
    /**
     * 相别
     * @param str   A、B、C六个
     * @param textView    要显示的TV
     */
    public static String xiangbie(String str, TextView textView){
        String xbZhiling = "";
        if(StringUtils.isEquals("A0",str)){
            textView.setText("B0");
            xbZhiling = "01";
        }else if(StringUtils.isEquals("B0",str)){
            textView.setText("C0");
            xbZhiling = "02";
        }else if(StringUtils.isEquals("C0",str)){
            textView.setText("AB");
            xbZhiling = "03";
        }else if(StringUtils.isEquals("AB",str)){
            textView.setText("BC");
            xbZhiling = "04";
        }else if(StringUtils.isEquals("BC",str)){
            textView.setText("CA");
            xbZhiling = "05";
        }else if(StringUtils.isEquals("CA",str)){
            textView.setText("A0");
            xbZhiling = "00";
        }
        return xbZhiling;
    }
}
