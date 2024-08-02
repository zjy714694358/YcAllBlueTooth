package com.yc.allbluetooth.huilu.util;

import com.yc.allbluetooth.utils.XiaoshuYunsuan;

/**
 * @Date 2024/5/20 9:48
 * @Author ZJY
 * @email 714694358@qq.com
 * 回路电流或者电阻单位
 */
public class HlDlOrDzDw {
    /**
     * 电阻毫欧或者微欧
     * @param str
     * @return
     */
    public static String getDzDw(String str){
        String dz = "";
        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
        if(xsys.bijiao(xsys.xiaoshu(str),xsys.xiaoshu("1000"))){
            //dz = str+"m Ω";
            dz = xsys.xiaoshuChu(xsys.xiaoshu(str),xsys.xiaoshu("1000"))+"m Ω";
        }else {
            //dz = xsys.xiaoshuCheng(xsys.xiaoshu(str),xsys.xiaoshu("1000"))+"μ Ω";
            dz = str+"μ Ω";
        }
        return dz;
    }
}
