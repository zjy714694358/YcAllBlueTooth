package com.yc.allbluetooth.std.util;

import android.widget.TextView;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/17 14:59
 * author:jingyu zheng
 */
public class DianliuDianzuTv {
    /**
     *
     * @param sjxz  数据性质：=0  测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2 放电电流，单位（A），=3下位机突发信息
     * @param csxw  测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
     * @param dlOrdz    电流或者电阻值
     * @param textView  自己所在的TV
     */
    public void dianliu(String sjxz, String csxw, float dlOrdz,TextView textView){
        if(StringUtils.isEquals(sjxz,"00")){//电流
            if(StringUtils.isEquals(csxw,"00")){//A0
                textView.setText(FloatLen.Float(dlOrdz)+"");
            }
        }
    }
}
