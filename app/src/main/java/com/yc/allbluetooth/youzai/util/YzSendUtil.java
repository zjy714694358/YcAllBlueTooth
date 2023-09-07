package com.yc.allbluetooth.youzai.util;

import android.util.Log;

import com.yc.allbluetooth.utils.BytesToHexString;

/**
 * Date:2023/6/8 9:25
 * author:jingyu zheng
 */
public class YzSendUtil {
    public static String spbhSend(String str){
        String spbhZhiling = "";
        spbhZhiling = BytesToHexString.str2HexStr(str);
        Log.e("spbh==",spbhZhiling);
        return spbhZhiling;
    }
}
