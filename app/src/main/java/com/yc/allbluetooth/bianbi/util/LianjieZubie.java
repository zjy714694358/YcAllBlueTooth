package com.yc.allbluetooth.bianbi.util;

import android.app.Activity;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * @Author ZJY
 * @Date 2024/4/29 11:48
 */
public class LianjieZubie {
    public static String  getYi(Activity activity, TextView textView,String tvStr){
        String zhiling = "";
        if(StringUtils.isEquals(tvStr,activity.getString(R.string.weizhi))){
            textView.setText("D");
            zhiling = "01";
        } else if (StringUtils.isEquals(tvStr,"D")){
            textView.setText("Y");
            zhiling = "00";
        }else if (StringUtils.isEquals(tvStr,"Y")){
            textView.setText("Z");
            zhiling = "02";
        }else if (StringUtils.isEquals(tvStr,"Z")){
            textView.setText(activity.getString(R.string.weizhi));
            zhiling = "03";
        }
        return zhiling;
    }
    public static String getEr(Activity activity, TextView textView,String tvStr){
        String zhiling = "";
        if(StringUtils.isEquals(tvStr,activity.getString(R.string.weizhi))){
            textView.setText("d");
            zhiling = "01";
        } else if (StringUtils.isEquals(tvStr,"d")){
            textView.setText("y");
            zhiling = "00";
        }else if (StringUtils.isEquals(tvStr,"y")){
            textView.setText("z");
            zhiling = "02";
        }else if (StringUtils.isEquals(tvStr,"z")){
            textView.setText(activity.getString(R.string.weizhi));
            zhiling = "03";
        }
        return zhiling;
    }
    public static String getSan(Activity activity, TextView textView,String tvStr){
        String zhiling = "";
        if(StringUtils.isEquals(tvStr,activity.getString(R.string.weizhi))){
            textView.setText("00");
            zhiling = "00";
        } else if (StringUtils.isEquals(tvStr,"00")){
            textView.setText("01");
            zhiling = "01";
        }else if (StringUtils.isEquals(tvStr,"01")){
            textView.setText("02");
            zhiling = "02";
        }else if (StringUtils.isEquals(tvStr,"02")){
            textView.setText("03");
            zhiling = "03";
        }else if (StringUtils.isEquals(tvStr,"03")){
            textView.setText("04");
            zhiling = "04";
        }else if (StringUtils.isEquals(tvStr,"04")){
            textView.setText("05");
            zhiling = "05";
        }else if (StringUtils.isEquals(tvStr,"05")){
            textView.setText("06");
            zhiling = "06";
        }else if (StringUtils.isEquals(tvStr,"06")){
            textView.setText("07");
            zhiling = "07";
        }else if (StringUtils.isEquals(tvStr,"07")){
            textView.setText("08");
            zhiling = "08";
        }else if (StringUtils.isEquals(tvStr,"08")){
            textView.setText("09");
            zhiling = "09";
        }else if (StringUtils.isEquals(tvStr,"09")){
            textView.setText("10");
            zhiling = "10";
        }else if (StringUtils.isEquals(tvStr,"10")){
            textView.setText("11");
            zhiling = "11";
        }else if (StringUtils.isEquals(tvStr,"11")){
            textView.setText(activity.getString(R.string.weizhi));
            zhiling = "12";
        }
        return zhiling;
    }
}
