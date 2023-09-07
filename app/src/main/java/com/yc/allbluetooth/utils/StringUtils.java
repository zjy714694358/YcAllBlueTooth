package com.yc.allbluetooth.utils;

import android.util.Log;

/**
 * 作者： zjingyu on 2022/7/15 16:27
 **/
public class StringUtils {
    /**
     * 判断字符串是否一致
     * @param a
     * @param b
     * @return
     */
    public static boolean isEquals(String a,String b){
        boolean b1 = a.equals(b);
        return b1;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        boolean b1 = ("".equals(str)||str==null||str.isEmpty());
        return b1;
    }

    /**
     * 判断字符串是否不为空
     * @param str
     * @return
     */
    public static boolean noEmpty(String str){
        boolean b1 = (!"".equals(str) && str!=null && !str.isEmpty()&&!" ".equals(str));
        return b1;
    }

    /**
     * 判断字符串长度是否相等
     * @param str
     * @param type 要比较的位数
     * @return
     */
    public static boolean isLength(String str,int type){
        boolean b1 = (str.length()==type);
        return b1;
    }

    /**
     * 将字符串转换为数字，必须保证每个字符都是数字
     * @param str1
     * @return
     */
    public static int strToInt(String str1){
        int num1=Integer.parseInt(str1);
        return num1;
    }

    /**
     * 字符串字母全部转为大写
     * @param str
     * @return
     */
    public static String smallToBig(String str){
        String bigStr = str.toUpperCase();
        return bigStr;
    }

    /**
     * 字符串字母全部转为小写
     * @param str
     * @return
     */
    public static String bitToSmall(String str){
        String smallStr = str.toLowerCase();
        return smallStr;
    }
    public static int xiaoshuLen(String str){
        int len = str.split("\\.")[1].length();
        return len;
    }
    /**
     *  截取保留小数点后两位
     */
    public static String dealRateStr(String rateStr) {
        int i = rateStr.indexOf(".");
        //如果没有小数点不
        if (i == -1) {
            return rateStr + ".00";
        }
        //获取小数点的位置
        int num = rateStr.indexOf(".");
        //获取小数点后面的数字 是否有两位 不足两位补足两位
        String afterData = rateStr.replace(rateStr.substring(0, num + 1), "");
        if (afterData.length() < 2) {
            afterData = afterData + "0";
        }
        return rateStr.substring(0, num) + "." + afterData.substring(0, 2);
    }
    /**
     * 判断字符串是否包含指定字符串
     * @param strAll 字符串
     * @param str  被包含字符串
     * @return
     */
    public static boolean isIndexOf(String strAll,String str){
        boolean bl = (strAll.indexOf(str)!=-1);//等于-1表示这个字符串中没有str这个字符
        return bl;
    }

    /**
     * 截取字符串后几位
     * @param strAll 字符串
     * @param i  倒数第几位
     * @return
     */
    public static String subStrEnd(String strAll,int i){
        String subStr = strAll.substring(strAll.length()-i);
        return subStr;
    }

    /**
     * 从第几位开始截取
     * @param strAll 字符串
     * @param i 字符串第几位开始
     * @return
     */
    public static String subStrStart(String strAll,int i){
        String subStr = strAll.substring(i);
        return subStr;
    }

    /**
     * 从第几位到第几位
     * @param strAll 总字符串
     * @param start 开始的位置
     * @param end   结束的位置
     * @return
     */
    public static String subStrStartToEnd(String strAll,int start,int end){
        String subStr = strAll.substring(start,end);
        return subStr;
    }
    public static String fillString(String src, String c, int len, boolean left) {
        if (StringUtils.isEmpty2(src) || len <= src.length()) {
            return src;
        }
        int l = src.length();
        for (int i = 0; i < len - l; i++) {
            if (left) {
                src = c + src;
            } else {
                src += c;
            }
        }
        return src;
    }

    public static boolean isEmpty2(Object content) {
        return content == null || "".equals(content);
    }

    /**
     * 保留4位有效数字，如果包含小数点儿保留五位（并且结尾四舍五入）
     * @param rateStr
     * @return
     */
    public static String siweiYouxiaoStr(String rateStr) {
        String shuchuStr;
        float fl = 0;
        int i = rateStr.indexOf(".");
        //如果没有小数点不
        if (i == -1) {
            if(rateStr.length()<4){
                shuchuStr = rateStr;
            }else{
                shuchuStr = StringUtils.subStrStartToEnd(rateStr,0,4);
            }
            return shuchuStr;
        }
        //获取小数点的位置
        int num = rateStr.indexOf(".");
        fl =  Float.parseFloat(rateStr);
        if(num==1){
            shuchuStr = String.format("%.3f",fl);
        }else if(num==2){
            shuchuStr = String.format("%.2f",fl);
        }else if(num==3){
            shuchuStr = String.format("%.1f",fl);
        }else{
            shuchuStr = Math.round(fl)+"";
        }
        return shuchuStr;
    }
    /**
     * 保留5位有效数字，如果包含小数点儿保留六位（并且结尾四舍五入）
     * @param rateStr
     * @return
     */
    public static String wuweiYouxiaoStr(String rateStr) {
        String shuchuStr;
        float fl = 0;
        int i = rateStr.indexOf(".");
        //如果没有小数点不
        if (i == -1) {
            //if(rateStr.length()<5){
                shuchuStr = rateStr;
                return shuchuStr;
            //}
//            else{
//                shuchuStr = StringUtils.subStrStartToEnd(rateStr,0,4);
//            }
            //return shuchuStr;
        }
        //获取小数点的位置
        int num = rateStr.indexOf(".");
        fl =  Float.parseFloat(rateStr);
        if(num==1){
            shuchuStr = String.format("%.4f",fl);
        }else if(num==2){
            shuchuStr = String.format("%.3f",fl);
        }else if(num==3){
            shuchuStr = String.format("%.2f",fl);
        }else if(num==4){
            shuchuStr = String.format("%.1f",fl);
        }else{
            shuchuStr = Math.round(fl)+"";
        }
        return shuchuStr;
    }
    /**
     * 保留5位有效数字，如果包含小数点儿保留六位（并且结尾四舍五入）
     * @param rateStr
     * @return
     */
    public static String shierweiYouxiaoStr(String rateStr) {
        String shuchuStr;
        float fl = 0;
        int i = rateStr.indexOf(".");
        //如果没有小数点不
        if (i == -1) {
            if(rateStr.length()<12){
                shuchuStr = rateStr;
            }else{
                shuchuStr = StringUtils.subStrStartToEnd(rateStr,0,11);
            }
            return shuchuStr;
        }
        //获取小数点的位置
        int num = rateStr.indexOf(".");
        fl =  Float.parseFloat(rateStr);
        if(num==1){
            shuchuStr = String.format("%.10f",fl);
        }else if(num==2){
            shuchuStr = String.format("%.9f",fl);
        }else if(num==3){
            shuchuStr = String.format("%.8f",fl);
        }else if(num==4){
            shuchuStr = String.format("%.7f",fl);
        }else if(num==5){
            shuchuStr = String.format("%.6f",fl);
        }else if(num==6){
            shuchuStr = String.format("%.5f",fl);
        }else if(num==7){
            shuchuStr = String.format("%.4f",fl);
        }else if(num==8){
            shuchuStr = String.format("%.3f",fl);
        }else if(num==9){
            shuchuStr = String.format("%.2f",fl);
        }else if(num==10){
            shuchuStr = String.format("%.1f",fl);
        }else{
            shuchuStr = Math.round(fl)+"";
        }
        return shuchuStr;
    }

    /**
     * 不足10补零
     * @param i
     * @return
     */
    public static String buling(int i){
        String str = "";
        if(i<10){
            str = "0"+i;
        }else{
            str = i+"";
        }
        return str;
    }

    /**
     * 字符串转16进制，转十进制后小于十六的，转十六进制后前面补零
     * @param etStr
     * @return
     */
    public static String bulingXiaoShiliu(String etStr){
        String str = "";
        int strI;
        strI = StringUtils.strToInt(etStr);
        if(strI<16){
            str = "0"+ ShiOrShiliu.toHexString(strI);
        }else{
            str = ShiOrShiliu.toHexString(strI);
        }
        return str;
    }

    /**
     * 拿到截取的十六进制字符串，先高低位互换，然后转成保留五位有效数字的字符串展示
     * @param hexStr
     * @return
     */
    public static String gaodiHuanBaoliuWuwei(String hexStr){
        float f0 = 0;
        String hl = HexUtil.reverseHex(hexStr);
        try {
            f0 = Float.intBitsToFloat((int) HexUtil.parseLong(hl, 16));
        } catch (HexUtil.NumberFormatException e) {
            e.printStackTrace();
        }
        Log.e("浮点数==",f0+"");
        String tvStr = StringUtils.wuweiYouxiaoStr(f0 + "");

        return tvStr;
    }
    /**
     * 拿到截取的十六进制字符串，先高低位互换，然后转成保留五位有效数字的字符串展示
     * @param hexStr
     * @return
     */
    public static String gaodiHuanBaoliuShierwei(String hexStr){
        float f0 = 0;
        String hl = HexUtil.reverseHex(hexStr);
        try {
            f0 = Float.intBitsToFloat((int) HexUtil.parseLong(hl, 16));
        } catch (HexUtil.NumberFormatException e) {
            e.printStackTrace();
        }
        String tvStr = StringUtils.shierweiYouxiaoStr(f0 + "");

        return tvStr;
    }
}
