package com.yc.allbluetooth.utils;

import java.io.UnsupportedEncodingException;

/**
 * @Author ZJY
 * @Date 2024/5/8 10:20
 */
public class StrToAsc {
    /**
     * 字符串转ASCII，不足字节补零
     * @param str
     * @param num 需要的字节数
     * @return
     */
    public static String ToAscii(String str,int num){
        String fsStr = StringToAscii.parseAscii(str);
        GetZifuchuanZijie getZifuchuanZijie = new GetZifuchuanZijie();
        int longInt;
        try {
            longInt = getZifuchuanZijie.getZijie(str);
            //fsStr = HexUtil.reverseHex(fsStr);
            for(int i=0;i<num-longInt;i++){
                fsStr +="00";
            }
            return fsStr;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
