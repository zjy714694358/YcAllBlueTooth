package com.yc.allbluetooth.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Date:2022/11/7 15:53
 * author:jingyu zheng
 */
public class GetTime {
    //获得当前年⽉⽇时分秒星期
    public static String getTime(int type){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前⽉份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前⽉份的⽇期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒

        if(StringUtils.strToInt(mMonth)<10){
            mMonth = "0"+mMonth;
        }
        if(StringUtils.strToInt(mDay)<10){
            mDay = "0"+mDay;
        }

        if("1".equals(mWay)){
            mWay ="日";
        }else if("2".equals(mWay)){
            mWay ="⼀";
        }else if("3".equals(mWay)){
            mWay ="⼆";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        if(StringUtils.strToInt(mHour)<10){
            mHour = "0"+mHour;
        }
        if(StringUtils.strToInt(mMinute)<10){
            mMinute = "0"+mMinute;
        }
        if(StringUtils.strToInt(mSecond)<10){
            mSecond = "0"+mSecond;
        }

        //return mHour+":"+mMinute+":"+mSecond;
        if(type == 1){//年月日时分秒
            return mYear + "年" + mMonth + "⽉" + mDay+"⽇"+" "+mHour+":"+mMinute+":"+mSecond;
        }else if(type == 2){//当前年月日时分秒星期
            return mYear + "年" + mMonth + "⽉" + mDay+"⽇"+" "+mHour+":"+mMinute+":"+mSecond+" "+"星期"+mWay;
        }else if(type == 3){//当前年月日时分秒，格式：22072900134930
            return StringUtils.strToInt(mYear)-2000+mMonth+mDay+"00"+mHour+mMinute+mSecond;
        }else if(type == 4){
            return mYear + "-" + mMonth + "-" + mDay + " " + mHour + ":" + mMinute + ":" + mSecond;
        }
        return "";
    }

    /**
     * 获取当前系统时间，时分秒
     * @return
     */
    public static String getTime2(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(calendar.getTime());
    }
}
