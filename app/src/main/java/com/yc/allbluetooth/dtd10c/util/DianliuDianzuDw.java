package com.yc.allbluetooth.dtd10c.util;

import android.util.Log;
import android.widget.TextView;

import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.math.BigDecimal;

/**
 * Date:2022/11/21 8:25
 * author:jingyu zheng
 */
public class DianliuDianzuDw {
    /**
     *
     * @param type  电流：00；电阻：01；
     * @param a0orab    电流或者电阻的十六进制
     * @param textView    电流或者电阻后面的单位TV
     * @return
     */
    public static String dw(String type, String a0orab, TextView textView){
        float a0orabF = 0;
        String a0orabStr = "";
        String a0orabStr2 = "";
        String strEnd = "";
        if(StringUtils.isEquals(type,"00")){//电流
            try {
                String a0orabHl = HexUtil.reverseHex(a0orab);
                a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl,16));
                a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF+"");

                XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
                BigDecimal clB0 = xiaoshuYunsuan.xiaoshu("1");
                BigDecimal clB2 = xiaoshuYunsuan.xiaoshu("1000");//常量
                BigDecimal clB3 = xiaoshuYunsuan.xiaoshu("1000000");//常量
                BigDecimal a0B1 = xiaoshuYunsuan.xiaoshu(a0orabStr2);
                Log.e("===Dl===",a0B1+"");
                boolean aoBl =  xiaoshuYunsuan.bijiao(a0B1,clB2);//大于1000
                boolean a0Bl0 = xiaoshuYunsuan.bijiao(a0B1,clB0);//大于1
                if(aoBl){//大于1000
                    Log.e("===Dl===",a0B1+"");
                    if(!xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1，小于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB2)+"";
                        textView.setText("KA");
                    }/*else if(xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB3)+"";
                        textView.setText("KA");
                    }*/
                }else if(a0Bl0){//小于1000，大于1
                    a0orabStr = a0orabStr2+"";
                    textView.setText("A");
                }else{//小于1
                    a0orabStr = xiaoshuYunsuan.xiaoshuCheng(a0B1,clB2)+"";
                    textView.setText("mA");
                }
            } catch (HexUtil.NumberFormatException e) {
                e.printStackTrace();
            }
        }else if(StringUtils.isEquals(type,"01")){//电阻
            try {
                String a0orabHl = HexUtil.reverseHex(a0orab);
                a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl,16));
                a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF+"");

                XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
                BigDecimal clB2 = xiaoshuYunsuan.xiaoshu("1000");//常量   Ω
                BigDecimal clB3 = xiaoshuYunsuan.xiaoshu("1000000");//常量    KΩ
                BigDecimal a0B1 = xiaoshuYunsuan.xiaoshu(a0orabStr2);
                boolean aoBl =  xiaoshuYunsuan.bijiao(a0B1,clB2);
                if(aoBl){//大于1000
                    if(!xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1，小于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB2)+"";
                        textView.setText("Ω");
                    }else if(xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB3)+"";
                        textView.setText("KΩ");
                    }
                }else{//小于1000
                    a0orabStr = a0orabStr2;
                    textView.setText("mΩ");
                }
            } catch (HexUtil.NumberFormatException e) {
                e.printStackTrace();
            }
        }
        strEnd = StringUtils.siweiYouxiaoStr(a0orabStr);
        return strEnd;
    }
}
