package com.yc.allbluetooth.std.util;

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
                BigDecimal clB2 = xiaoshuYunsuan.xiaoshu("1000");//常量
                BigDecimal clB3 = xiaoshuYunsuan.xiaoshu("1000000");//常量
                BigDecimal a0B1 = xiaoshuYunsuan.xiaoshu(a0orabStr2);
                boolean aoBl =  xiaoshuYunsuan.bijiao(a0B1,clB2);
                if(aoBl){//大于1000
                    if(!xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1，小于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB2)+"";
                        textView.setText("A");
                    }else if(xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB3)+"";
                        textView.setText("KA");
                    }
                }else{//小于1000
                    a0orabStr = a0orabStr2+"";
                    //textView.setText("A");
                }
            } catch (HexUtil.NumberFormatException e) {
                e.printStackTrace();
            }
        }else if(StringUtils.isEquals(type,"01")){//电阻
            try {
                String a0orabHl = HexUtil.reverseHex(a0orab);
                a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl,16));
                a0orabStr2 = StringUtils.shierweiYouxiaoStr(a0orabF+"");//四位===>12位

                XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
                BigDecimal clB2 = xiaoshuYunsuan.xiaoshu("1000");//常量   Ω
                BigDecimal clB3 = xiaoshuYunsuan.xiaoshu("1000000");//常量    KΩ
                BigDecimal a0B1 = xiaoshuYunsuan.xiaoshu(a0orabStr2);
                boolean aoBl =  xiaoshuYunsuan.bijiao(a0B1,clB2);//与1000作比较
                boolean aoBl2 =  xiaoshuYunsuan.bijiao(a0B1,xiaoshuYunsuan.xiaoshu("1"));//与1作比较
                if(aoBl){//大于1000
                    if(!xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1，小于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB2)+"";
                        textView.setText("Ω");
                    }else if(xiaoshuYunsuan.bijiao(xiaoshuYunsuan.xiaoshuChu(a0B1,clB2),clB2)){//除以1000后，大于1000
                        a0orabStr = xiaoshuYunsuan.xiaoshuChu(a0B1,clB3)+"";
                        textView.setText("KΩ");
                    }
                }else {//小于1000
                    if(aoBl2){
                        a0orabStr = a0orabStr2;
                        textView.setText("mΩ");
                    }else{
                        a0orabStr = xiaoshuYunsuan.xiaoshuCheng(a0B1,clB2)+"";
                        textView.setText("μΩ");
                    }

                }
            } catch (HexUtil.NumberFormatException e) {
                e.printStackTrace();
            }
        }
        strEnd = StringUtils.siweiYouxiaoStr(a0orabStr);
        return strEnd;
    }
}
