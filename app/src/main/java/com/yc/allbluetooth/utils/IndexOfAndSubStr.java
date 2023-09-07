package com.yc.allbluetooth.utils;

/**
 * 作者： zjingyu on 2022/7/13 9:33
 **/
public class IndexOfAndSubStr {
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
}
