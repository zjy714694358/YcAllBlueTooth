package com.yc.allbluetooth.dlzk.util;

import android.widget.TextView;

import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2023/4/12 9:20
 * author:jingyu zheng
 * 短路阻抗切换
 */
public class DlzkQiehuan {
    /**
     * 选择电源：内部、外部
     * @param str
     * @param textView
     * @return
     */
    public static String xzdy(String str, TextView textView){
        String zhiling = "";
        if(StringUtils.isEquals("外部",str)){
            textView.setText("内部");
            zhiling = "00";
        }else if(StringUtils.isEquals("内部",str)){
            textView.setText("外部");
            zhiling = "01";
        }else if(StringUtils.isEquals("Inside",str)){//内
            textView.setText("External");
            zhiling = "00";
        }else if(StringUtils.isEquals("External",str)){//外
            textView.setText("Inside");
            zhiling = "01";
        }
        return zhiling;
    }

    /**
     * 测量位置：高低、高中、中低
     * @param str
     * @param textView
     * @return
     */
    public static String clwz(String str, TextView textView){
        String zhiling = "";
        if(StringUtils.isEquals("高-低",str)){
            textView.setText("高-中");
            zhiling = "05";
        }else if(StringUtils.isEquals("高-中",str)){
            textView.setText("中-低");
            zhiling = "06";
        }else if(StringUtils.isEquals("中-低",str)){//内
            textView.setText("高-低");
            zhiling = "04";
        }else if(StringUtils.isEquals("H-L",str)){//外
            textView.setText("H-M");
            zhiling = "05";
        }else if(StringUtils.isEquals("H-M",str)){//外
            textView.setText("M-L");
            zhiling = "06";
        }else if(StringUtils.isEquals("M-L",str)){//外
            textView.setText("H-L");
            zhiling = "04";
        }
        return zhiling;
    }
    /**
     * 测量模式：自动、手动
     * @param str
     * @param textView
     * @return
     */
    public static String clms(String str, TextView textView){
        String zhiling = "";
        if(StringUtils.isEquals("自动",str)){
            textView.setText("手动");
            zhiling = "02";
            Config.Clms = 1;
        }else if(StringUtils.isEquals("手动",str)){
            textView.setText("自动");
            zhiling = "03";
            Config.Clms = 0;
        }else if(StringUtils.isEquals("Automatic",str)){//自动
            textView.setText("Manual");
            zhiling = "02";
            Config.Clms = 1;
        }else if(StringUtils.isEquals("Manual",str)){//手动
            textView.setText("Automatic");
            zhiling = "03";
            Config.Clms = 0;
        }
        return zhiling;
    }
    /**
     * 测量接线：Y/Y联结、Y/△联结、△/Y联结（AZ-BX-CY）、△/Y联结（AY-BZ-CX）、△/△联结(AZ-BX-CY)--------△/△联结(AY-BZ-CX)
     * @param str
     * @param textView
     * @return
     */
    public static String cljx(String str, TextView textView){//------△/△联结(AZ-BX-CY)--------△/△联结(AY-BZ-CX)-----
        String zhiling = "";
        if(StringUtils.isEquals("Y/Y联结",str)){
            textView.setText("Y/△联结");
            zhiling = "08";
        }else if(StringUtils.isEquals("Y/△联结",str)){
            textView.setText("△/Y联结(AZ-BX-CY)");
            zhiling = "09";
        }else if(StringUtils.isEquals("△/Y联结(AZ-BX-CY)",str)){
            textView.setText("△/Y联结(AY-BZ-CX)");
            zhiling = "0A";
        }else if(StringUtils.isEquals("△/Y联结(AY-BZ-CX)",str)){
            textView.setText("△/△联结(AZ-BX-CY)");
            zhiling = "0B";
        }else if(StringUtils.isEquals("△/△联结(AZ-BX-CY)",str)){
            textView.setText("△/△联结(AY-BZ-CX)");
            zhiling = "0C";
        }else if(StringUtils.isEquals("△/△联结(AY-BZ-CX)",str)){
            textView.setText("Y/Y联结");
            zhiling = "07";
        }else if(StringUtils.isEquals("Y/Yjoin",str)){//*****************************
            textView.setText("Y/△join");
            zhiling = "08";
        }else if(StringUtils.isEquals("Y/△join",str)){
            textView.setText("△/Yjoin(AZ-BX-CY)");
            zhiling = "09";
        }else if(StringUtils.isEquals("△/Yjoin(AZ-BX-CY)",str)){
            textView.setText("△/Yjoin(AY-BZ-CX)");
            zhiling = "0A";
        }else if(StringUtils.isEquals("△/Yjoin(AY-BZ-CX)",str)){
            textView.setText("△/△join(AZ-BX-CY)");
            zhiling = "0B";
        }else if(StringUtils.isEquals("△/△join(AZ-BX-CY)",str)){
            textView.setText("△/△join(AY-BZ-CX)");
            zhiling = "0C";
        }else if(StringUtils.isEquals("△/△join(AY-BZ-CX)",str)){
            textView.setText("Y/Yjoin");
            zhiling = "07";
        }
        return zhiling;
    }
    /**
     * 变压器类型：单相、三相(Yn/Y、Yn/△)
     * @param str
     * @param textView
     * @param textView2
     * @return
     */
    public static String byqlx(String str, TextView textView,TextView textView2){
        String zhiling = "";
        if(StringUtils.isEquals("单相",str)){
            textView.setText("三相(Yn/Y、Yn/△)");
            zhiling = "04";
        }else if(StringUtils.isEquals("三相(Yn/Y、Yn/△)",str)){
            textView.setText("单相");
            textView2.setText("A0");
            zhiling = "03";
        }else if(StringUtils.isEquals("Single-phase",str)){//单相
            textView.setText("Three phase(Yn/Y、Yn/△)");
            zhiling = "04";
        }else if(StringUtils.isEquals("Three phase(Yn/Y、Yn/△)",str)){//三相
            textView.setText("Single-phase");
            textView2.setText("A0");
            zhiling = "03";
        }
        return zhiling;
    }
    /**
     * 测量相别：A0、B0、C0
     * @param str
     * @param textView
     * @return
     */
    public static String clxb(String str, TextView textView){
        String zhiling = "";
        if(StringUtils.isEquals("A0",str)){
            textView.setText("B0");
            zhiling = "01";
        }else if(StringUtils.isEquals("B0",str)){
            textView.setText("C0");
            zhiling = "02";
        }else if(StringUtils.isEquals("C0",str)){//自动
            textView.setText("A0");
            zhiling = "00";
        }
        return zhiling;
    }
}
