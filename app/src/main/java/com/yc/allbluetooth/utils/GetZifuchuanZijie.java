package com.yc.allbluetooth.utils;

import java.io.UnsupportedEncodingException;

/**
 * @Author ZJY
 * @Date 2024/4/15 9:36
 */
public class GetZifuchuanZijie {
    public int getZijie(String str) throws UnsupportedEncodingException {
        //String str = "110kV 枫泾变电站";
        int zijie = str.getBytes("UTF-8").length;
        return zijie;
    }
}
