package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/7 11:00
 * author:jingyu zheng
 * D(Y)测试电流集合
 */
public class DyCsDlList {
    /**
     * 电流值、电阻值，直接写到对应的TV文本中
     * @param str   点击前选中的电流值
     * @param tvDl  电流值所对应TextView
     * @param tvDz  电阻值所对应TextView
     * @return
     */
    public void DlAndDz_40(String str, TextView tvDl,TextView tvDz){
        String dl = "";
        String dz = "";
        if(StringUtils.isEquals(str,"40A")){
            dl = "20A";
            dz = "（0.5mΩ~1Ω）";
        }else if(StringUtils.isEquals(str,"20A")){
            dl = "10A";
            dz = "（1mΩ~2Ω）";
        }else if(StringUtils.isEquals(str,"10A")){
            dl = "5A";
            dz = "（2mΩ~4Ω）";
        }else if(StringUtils.isEquals(str,"5A")){
            dl = "1A";
            dz = "（10mΩ~20Ω）";
        }else if(StringUtils.isEquals(str,"1A")){
            dl = "200mA";
            dz = "（100mΩ~100Ω）";
        }else if(StringUtils.isEquals(str,"200mA")){
            dl = "20mA";
            dz = "（10Ω~20KΩ）";
        }else if(StringUtils.isEquals(str,"20mA")){
            dl = "40A";
            dz = "（0.25mΩ~0.5Ω）";
        }
        tvDl.setText(dl);
        tvDz.setText(dz);
    }
    /**
     * 电流值、电阻值，直接写到对应的TV文本中
     ** 设备：50A（36）
     * @param str   点击前选中的电流值
     * @param tvDl  电流值所对应TextView
     * @param tvDz  电阻值所对应TextView
     * @return
     */
    public void DlAndDz_50(String str, TextView tvDl,TextView tvDz){
        String dl = "";
        String dz = "";
        if(StringUtils.isEquals(str,"50A")){
            dl = "20A";
            dz = "（0.5mΩ~1Ω）";
        }else if(StringUtils.isEquals(str,"20A")){
            dl = "10A";
            dz = "（1mΩ~2Ω）";
        }else if(StringUtils.isEquals(str,"10A")){
            dl = "5A";
            dz = "（2mΩ~4Ω）";
        }else if(StringUtils.isEquals(str,"5A")){
            dl = "1A";
            dz = "（10mΩ~20Ω）";
        }else if(StringUtils.isEquals(str,"1A")){
            dl = "200mA";
            dz = "（100mΩ~100Ω）";
        }else if(StringUtils.isEquals(str,"200mA")){
            dl = "20mA";
            dz = "（10Ω~20KΩ）";
        }else if(StringUtils.isEquals(str,"20mA")){
            dl = "50A";
            dz = "（0.2mΩ~0.2Ω）";
        }
        tvDl.setText(dl);
        tvDz.setText(dz);
    }
    /**
     * 电流值、电阻值，直接写到对应的TV文本中
     ** 设备：20A（34）
     * @param str   点击前选中的电流值
     * @param tvDl  电流值所对应TextView
     * @param tvDz  电阻值所对应TextView
     * @return
     */
    public void DlAndDz_20(String str, TextView tvDl,TextView tvDz){
        String dl = "";
        String dz = "";
        if(StringUtils.isEquals(str,"20A")){
            dl = "10A";
            dz = "（1mΩ~2Ω）";
        }else if(StringUtils.isEquals(str,"10A")){
            dl = "5A";
            dz = "（2mΩ~4Ω）";
        }else if(StringUtils.isEquals(str,"5A")){
            dl = "1A";
            dz = "（10mΩ~20Ω）";
        }else if(StringUtils.isEquals(str,"1A")){
            dl = "200mA";
            dz = "（100mΩ~100Ω）";
        }else if(StringUtils.isEquals(str,"200mA")){
            dl = "20mA";
            dz = "（10Ω~20KΩ）";
        }else if(StringUtils.isEquals(str,"20mA")){
            dl = "20A";
            dz = "（0.5mΩ~1Ω）";
        }
        tvDl.setText(dl);
        tvDz.setText(dz);
    }
    /**
     * 电流值、电阻值，直接写到对应的TV文本中
     ** 设备：10A（31）
     * @param str   点击前选中的电流值
     * @param tvDl  电流值所对应TextView
     * @param tvDz  电阻值所对应TextView
     * @return
     */
    public void DlAndDz_10(String str, TextView tvDl,TextView tvDz){
        String dl = "";
        String dz = "";
        if(StringUtils.isEquals(str,"10A")){
            dl = "5A";
            dz = "（2mΩ~4Ω）";
        }else if(StringUtils.isEquals(str,"5A")){
            dl = "1A";
            dz = "（10mΩ~20Ω）";
        }else if(StringUtils.isEquals(str,"1A")){
            dl = "200mA";
            dz = "（100mΩ~100Ω）";
        }else if(StringUtils.isEquals(str,"200mA")){
            dl = "20mA";
            dz = "（10Ω~20KΩ）";
        }else if(StringUtils.isEquals(str,"20mA")){
            dl = "10A";
            dz = "（1mΩ~2Ω）";
        }
        tvDl.setText(dl);
        tvDz.setText(dz);
    }
    /**
     * 与电流想对应的电阻值
     * @param str
     * @return
     */
    public static String Csdz(String str){
        String dz = "";
        if(StringUtils.isEquals(str,"20A+20A")){
            dz = "1mΩ~0.6Ω";
        }else if(StringUtils.isEquals(str,"10A+10A")){
            dz = "2mΩ~1.2Ω";
        }else if(StringUtils.isEquals(str,"5A+5A")){
            dz = "10mΩ~6Ω";
        }else if(StringUtils.isEquals(str,"1A+1A")){
            dz = "0.1Ω~30Ω";
        }else if(StringUtils.isEquals(str,"0.2A+0.2A")){
            dz = "10Ω~200Ω";
        }else if(StringUtils.isEquals(str,"0.02A+0.02A")){
            dz = "0.5mΩ~0.3Ω";
        }
        return dz;
    }
}
