package com.yc.allbluetooth.youzai.util;

import android.util.Log;

import com.yc.allbluetooth.crc.CrcUtil;

import java.math.BigInteger;

/**
 * Date:2023/7/19 9:30
 * author:jingyu zheng
 */
public class CrcAll {
    /**
     * 要发送的指令，结尾添加CRC校验
     * @param sendStr   要发送的不带CRC校验指令
     * @param type      0：不带CRC；1：带CRC
     * @return
     */
    public static String crcAdd(String sendStr,int type){
        String crcAllStr = "";
        if(type==0){
            crcAllStr = sendStr;
        }else{
            byte[] bytesStdSave = new BigInteger(sendStr, 16).toByteArray();
            String crcSave = CrcUtil.getTableCRC(bytesStdSave);
            crcAllStr = sendStr + crcSave;
        }
        Log.e("fasong:",crcAllStr);
        return crcAllStr;
    }
}
