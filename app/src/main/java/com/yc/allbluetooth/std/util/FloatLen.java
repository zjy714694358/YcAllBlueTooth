package com.yc.allbluetooth.std.util;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2022/11/17 15:08
 * author:jingyu zheng
 */
public class FloatLen {
    /**
     * 包含小数点儿取前五位，不包含取前四位，总之四位有效数字
     * @param f
     * @return
     */
    public static float Float(float f){
        float fl ;
            String fStr = Float.toString(f);
            if(fStr.length()>4){
                String fStrSub = StringUtils.subStrEnd(fStr,5);
                if(StringUtils.isIndexOf(fStr,".")){
                    fl =  Float.parseFloat(fStrSub);
                }else{
                    fStrSub = StringUtils.subStrEnd(fStr,4);
                    fl =  Float.parseFloat(fStrSub);
                }
                return fl;
            }else{
               fl = f;
            }

        return fl;
    }
}
