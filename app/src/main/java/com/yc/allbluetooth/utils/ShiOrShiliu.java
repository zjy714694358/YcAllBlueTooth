package com.yc.allbluetooth.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Locale;

/**
 * 十进制和十六进制互转
 */
public class ShiOrShiliu {

    /**
     * 字符串(一整个数：十进制转十六进制)转化成为16进制字符串
     * @param integer
     * @return
     */
    public static String toHexString(Integer integer){
        String str = Integer.toHexString(integer);
        return str;
    }
    /**
     * 字符串(一整个数：十进制转十六进制)转化成为16进制字符串
     * 小于16开头补零
     * @param integer
     * @return
     */
    public static String toHexStringBl(Integer integer){
        String str = "";
        if(integer<16){
            str = "0"+Integer.toHexString(integer);
        }else{
            str = Integer.toHexString(integer);
        }
        return str;
    }

    /**
     * 小于10前面补0
     * @param integer
     * @return
     */
    public static String xiaoyushiBl(Integer integer){
        String str = "";
        if(integer<10){
            str = "0"+integer;
        }else{
            str = integer+"";
        }
        return str;
    }

    /**
     * 字符串（逐一）转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    /**
     * 16进制字符串转化为10进制
     * @param str
     * @return
     */
    public static Integer parseInt(String str){
        Integer integer = Integer.parseInt(str,16);
        return integer;
    }
    /**
     * 将16进制转换为二进制
     *
     * @param hexString
     * @param type 0代表正常输出；1代表补够8位的
     * @return
     */
    public static String hexString2binaryString(String hexString,int type) {
        //16进制转10进制
        BigInteger sint = new BigInteger(hexString, 16);
        //10进制转2进制
        String result = sint.toString(2);
        //字符串反转
        //return new StringBuilder(result).reverse().toString();
        if(type==0){
            return result;
        }else if(type==1){
            if(result.length()==1){
                result = "0000000"+result;
            }else if(result.length()==2){
                result = "000000"+result;
            }else if(result.length()==3){
                result = "00000"+result;
            }else if(result.length()==4){
                result = "0000"+result;
            }else if(result.length()==5){
                result = "000"+result;
            }else if(result.length()==6){
                result = "00"+result;
            }else if(result.length()==7){
                result = "0"+result;
            }else if(result.length()==8){
                return result;
            }
        }
        //字符串正常输出
        return result;
    }

    /**
     * 中文转十六进制
     * 短路阻抗使用（gb2312）
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String enUnicode(String str) throws UnsupportedEncodingException {
        String st = "";
        byte[] by = str.getBytes("gb2312");
        for (int i = 0; i < by.length; i++) {
            String strs = Integer.toHexString(by[i]& 0XFF);
            st += strs.toUpperCase(Locale.ROOT);
        }
        return st;
    }
    /**
     * 十六进制转浮点型小数（四个有效数字）
     * @param hexStr
     * @return
     */
    public static String hexToFloatSi(String hexStr){
        float hexF = 0;
        String str = "" ;
        String hexHl = HexUtil.reverseHex(hexStr);
        try {
            hexF = Float.intBitsToFloat((int) HexUtil.parseLong(hexHl,16));
            str = StringUtils.siweiYouxiaoStr(hexF+"");
        } catch (HexUtil.NumberFormatException e) {
            e.printStackTrace();
        }
        return str;
    }
    /**
     * 十六进制转浮点型小数（五个有效数字）
     * @param hexStr
     * @return
     */
    public static String hexToFloatWu(String hexStr){
        float hexF = 0;
        String str = "" ;
        String hexHl = HexUtil.reverseHex(hexStr);
        try {
            hexF = Float.intBitsToFloat((int) HexUtil.parseLong(hexHl,16));
            str = StringUtils.wuweiYouxiaoStr(hexF+"");
        } catch (HexUtil.NumberFormatException e) {
            e.printStackTrace();
        }
        return str;
    }
}
