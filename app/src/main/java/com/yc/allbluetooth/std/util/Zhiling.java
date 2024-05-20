package com.yc.allbluetooth.std.util;

import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/15 10:12
 * author:jingyu zheng
 */
public class Zhiling {
    /**
     * 测试电流指令
     * @param str
     * @return
     */
    public String ceshidianliu(String str){
        String dl = "";
        if(StringUtils.isEquals("20A+20A",str)||StringUtils.isEquals("25A+25A",str)){
            dl = "00";
        }else if(StringUtils.isEquals("10A+10A",str)){
            dl = "01";
        }else if(StringUtils.isEquals("5A+5A",str)){
            dl = "02";
        }else if(StringUtils.isEquals("1A+1A",str)){
            dl = "03";
        }else if(StringUtils.isEquals("0.2A+0.2A",str)){
            dl = "04";
        }else if(StringUtils.isEquals("0.02A+0.02A",str)){
            dl = "05";
        }else if(StringUtils.isEquals("40A",str)||StringUtils.isEquals("50A",str)){
            dl = "06";
        }else if(StringUtils.isEquals("20A",str)){
            dl = "07";
        }else if(StringUtils.isEquals("10A",str)){
            dl = "08";
        }else if(StringUtils.isEquals("5A",str)){
            dl = "09";
        }else if(StringUtils.isEquals("1A",str)){
            dl = "0A";
        }else if(StringUtils.isEquals("200mA",str)){
            dl = "0B";
        }else if(StringUtils.isEquals("20mA",str)){
            dl = "0C";
        }
        return dl;
    }

    /**
     * 测试方法：=0  三相测试，=1 单相测试， =2  相间测试
     * @param str
     * @return
     */
    public String ceshifangfa(String str){
        String csfs = "";
        if(StringUtils.isEquals("1",str)){
            csfs = "00";
        }else if(StringUtils.isEquals("2",str)){
            csfs = "01";
        }else if(StringUtils.isEquals("3",str)){
            csfs = "02";
        }
        return csfs;
    }
    /**
     * 测试方法：=0  三相测试，=1 单相测试， =2  相间测试
     * @param fragment
     * @param str
     * @return
     */
    public String ceshifangfa2(Fragment fragment,String str){
        String csfs = "";
        if(StringUtils.isEquals(fragment.getString(R.string.zzcs_santongdao_ceshi),str)){
            csfs = "00";
        }else if(StringUtils.isEquals(fragment.getString(R.string.zzcs_yn_ceshi),str)){
            csfs = "01";
        }else if(StringUtils.isEquals(fragment.getString(R.string.zzcs_dy_ceshi),str)){
            csfs = "02";
        }
        return csfs;
    }
    /**
     * 测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca
     * @param str
     * @return
     */
    public String ceshixiangwei(String str){
        String csxw = "";
        if(StringUtils.isEquals("A0",str)){
            csxw = "00";
        }else if(StringUtils.isEquals("B0",str)){
            csxw = "01";
        }else if(StringUtils.isEquals("C0",str)){
            csxw = "02";
        }else if(StringUtils.isEquals("ab",str)){
            csxw = "03";
        }else if(StringUtils.isEquals("bc",str)){
            csxw = "04";
        }else if(StringUtils.isEquals("ca",str)){
            csxw = "05";
        }else{//三相
            csxw = "06";
        }
        return csxw;
    }
    /**
     * 助磁启动：=6  不启动助磁，=7  启动助磁(缺省为6)
     * @param str
     * @return
     */
    public String zhuciqidong(String str){
        String csff = "";
        if(StringUtils.isEquals("OFF",str)){
            csff = "06";
        }else{
            csff = "07";
        }
        return csff;
    }
    /**
     * 零线电阻(中性点电阻测试)：=0(不是8就行)  不启动，=8  启动
     * @param str
     * @return
     */
    public String lingxiandianzuqidong(String str){
        String lxdz = "";
        if(StringUtils.isEquals("OFF",str)){
            lxdz = "00";
        }else{
            lxdz = "08";
        }
        return lxdz;
    }
    /**
     * 零线电阻(中性点电阻测试)：=0(不是8就行)  不启动，=8  启动
     * @param type
     * @return
     */
    public String lingxiandianzuqidong2(int type){
        String lxdz = "";
        if(type==0){
            lxdz = "00";
        }else{
            lxdz = "08";
        }
        return lxdz;
    }
}
