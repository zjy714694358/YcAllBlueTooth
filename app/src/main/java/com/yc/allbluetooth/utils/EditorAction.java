package com.yc.allbluetooth.utils;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yc.allbluetooth.config.Config;

import java.math.BigDecimal;

/**
 * Date:2022/11/14 10:47
 * author:jingyu zheng
 *
 * 输入框点击确定后，判断各种情况
 */
public class EditorAction {
    /**
     * 分接：输入内容小于10前面补0
     * @param editText
     */
    public void bulingFj(EditText editText){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            editText.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            editText.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            editText.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            editText.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            editText.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            editText.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            editText.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            editText.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            editText.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            editText.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            editText.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr)){
                            editText.setText("03");
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 测试温度（单）：YN or DY
     * @param editText  所在输入框
     * @param tvCsDz    页面展示的测试电阻TV
     * @param tvZsDz    需要计算的折算电阻TV
     */
    public void bulingWdDan(EditText editText,TextView tvCsDz,TextView tvZsDz){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            editText.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            editText.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            editText.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            editText.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            editText.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            editText.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            editText.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            editText.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            editText.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            editText.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            editText.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr) ){
                            editText.setText("20");
                        }
                        Config.cswd = editText.getText().toString();
                        String csDzStr = tvCsDz.getText().toString();
                        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                        BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                        tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(csDzStr))+""));
                        break;
                }
                return false;
            }
        });
    }
    /**
     * 测试温度（单）：10C
     * @param etCswd  测试温度（试品温度）输入框
     * @param rzczInt  绕组材质：铜、铝
     * @param etZswd    折算温度输入框
     * @param tvCsDz    页面展示的测试电阻TV
     * @param tvZsDz    需要计算的折算电阻TV
     */
    public void bulingCswd(EditText etCswd,int rzczInt,EditText etZswd, TextView tvCsDz,TextView tvZsDz){
        etCswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etCswd.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etCswd.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etCswd.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etCswd.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etCswd.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etCswd.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etCswd.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etCswd.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etCswd.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etCswd.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etCswd.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            etCswd.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr) ){
                            etCswd.setText("20");
                        }
                        String cswd = etCswd.getText().toString();
                        String zswd = etZswd.getText().toString();
                        String csDzStr = tvCsDz.getText().toString();
                        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                        BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(rzczInt + ""), xsys.xiaoshu(zswd)),
                                xsys.xiaoshuJia(xsys.xiaoshu(rzczInt + ""), xsys.xiaoshu(cswd)));//少测试温度
                        tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(csDzStr))+""));
                        Config.cswd = cswd;
                        break;
                }
                return false;
            }
        });
    }
    /**
     * 折算温度（单）：10C
     * @param etZswd  折算温度输入框
     * @param rzczInt  绕组材质：铜、铝
     * @param etCswd    测试温度输入框
     * @param tvCsDz    页面展示的测试电阻TV
     * @param tvZsDz    需要计算的折算电阻TV
     */
    public void bulingZswd(EditText etZswd,int rzczInt,EditText etCswd, TextView tvCsDz,TextView tvZsDz){
        etZswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etZswd.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etZswd.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etZswd.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etZswd.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etZswd.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etZswd.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etZswd.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etZswd.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etZswd.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etZswd.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etZswd.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            etZswd.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr) ){
                            etZswd.setText("75");
                        }
                        String zswd = etZswd.getText().toString();
                        String cswd = etCswd.getText().toString();
                        String csDzStr = tvCsDz.getText().toString();
                        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                        BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(rzczInt + ""), xsys.xiaoshu(zswd)),
                                xsys.xiaoshuJia(xsys.xiaoshu(rzczInt + ""), xsys.xiaoshu(cswd)));//少测试温度
                        tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(csDzStr))+""));
                        Config.zswd = zswd;
                        //ZzCs2Activity zzCs2Activity = new ZzCs2Activity();
                        //sendDataByBle(SendUtil.bianhuanSend("83",qiehuan.raozucailiao(tvRzClStr,tvRzCl)),"");
                        //zzCs2Activity.sendDataByBle(SendUtil.bianhuanSend("85",ShiOrShiliu.strTo16(zswd)),"");
                        break;
                }
                return false;
            }
        });
    }
    /**
     * 三通道测试温度
     * @param editText  所在输入框
     * @param tvA0CsDz  A0测试电阻TV
     * @param tvA0ZsDz  A0折算电阻TV
     * @param tvB0CsDz  B0测试电阻TV
     * @param tvB0ZsDz  B0折算电阻TV
     * @param tvC0CsDz  C0测试电阻TV
     * @param tvC0ZsDz  C0折算电阻TV
     */
    public void bulingWdStd(EditText editText,TextView tvA0CsDz,TextView tvA0ZsDz,TextView tvB0CsDz,TextView tvB0ZsDz,TextView tvC0CsDz,TextView tvC0ZsDz){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            editText.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            editText.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            editText.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            editText.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            editText.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            editText.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            editText.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            editText.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            editText.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            editText.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            editText.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr) ){
                            editText.setText("20");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)>=10){
                            editText.setText(etStr);
                        }
                        Config.cswd = editText.getText().toString();

                        String a0CsDzStr = tvA0CsDz.getText().toString();
                        String b0CsDzStr = tvB0CsDz.getText().toString();
                        String c0CsDzStr = tvC0CsDz.getText().toString();
                        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                        BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                        tvA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(a0CsDzStr))+""));
                        tvB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(b0CsDzStr))+""));
                        tvC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(c0CsDzStr))+""));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 系统设置折算温度设置
     * @param editText  折算温度输入框Et
     */
    public void bulingWdXtszWd(EditText editText){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            editText.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            editText.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            editText.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            editText.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            editText.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            editText.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            editText.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            editText.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            editText.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            editText.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            editText.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr) ){
                            editText.setText("75");
                        }
                        Config.zswd = editText.getText().toString();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 系统设置仪器设置试品编号（仪器编号）
     * @param editText  试品编号输入框Et
     */
    public void spbh(EditText editText){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            editText.setText("00");
                        }else if(StringUtils.isEquals("01",etStr)){
                            editText.setText("01");
                        }else if(StringUtils.isEquals("02",etStr)){
                            editText.setText("02");
                        }else if(StringUtils.isEquals("03",etStr)){
                            editText.setText("03");
                        }else if(StringUtils.isEquals("04",etStr)){
                            editText.setText("04");
                        }else if(StringUtils.isEquals("05",etStr)){
                            editText.setText("05");
                        }else if(StringUtils.isEquals("06",etStr)){
                            editText.setText("06");
                        }else if(StringUtils.isEquals("07",etStr)){
                            editText.setText("07");
                        }else if(StringUtils.isEquals("08",etStr)){
                            editText.setText("08");
                        }else if(StringUtils.isEquals("09",etStr)){
                            editText.setText("09");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etStr)<10){
                            editText.setText("0"+etStr);
                        }else if(StringUtils.isEmpty(etStr) ){
                            editText.setText("20");
                        }
                        Config.zswd = editText.getText().toString();
                        break;
                }
                return false;
            }
        });
    }
    /**
     * 输入年份内容
     * @param editText
     * @param nianStr
     */
    public void nian(EditText editText,String nianStr){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.noEmpty(etStr)){
                            if(StringUtils.isEquals("00",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("01",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("02",etStr)){
                                editText.setText("02");
                            }else if(StringUtils.isEquals("03",etStr)){
                                editText.setText("03");
                            }else if(StringUtils.isEquals("04",etStr)){
                                editText.setText("04");
                            }else if(StringUtils.isEquals("05",etStr)){
                                editText.setText("05");
                            }else if(StringUtils.isEquals("06",etStr)){
                                editText.setText("06");
                            }else if(StringUtils.isEquals("07",etStr)){
                                editText.setText("07");
                            }else if(StringUtils.isEquals("08",etStr)){
                                editText.setText("08");
                            }else if(StringUtils.isEquals("09",etStr)){
                                editText.setText("09");
                            }else if(StringUtils.strToInt(etStr)<10){
                                editText.setText("0"+etStr);
                            }
                        }else{
                            editText.setText(nianStr);
                        }

                        break;
                }
                return false;
            }
        });
    }
    /**
     * 输入月份内容不大于12
     * @param editText
     */
    public void yue(EditText editText,String yueStr){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.noEmpty(etStr)){
                            if(StringUtils.isEquals("00",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("01",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("02",etStr)){
                                editText.setText("02");
                            }else if(StringUtils.isEquals("03",etStr)){
                                editText.setText("03");
                            }else if(StringUtils.isEquals("04",etStr)){
                                editText.setText("04");
                            }else if(StringUtils.isEquals("05",etStr)){
                                editText.setText("05");
                            }else if(StringUtils.isEquals("06",etStr)){
                                editText.setText("06");
                            }else if(StringUtils.isEquals("07",etStr)){
                                editText.setText("07");
                            }else if(StringUtils.isEquals("08",etStr)){
                                editText.setText("08");
                            }else if(StringUtils.isEquals("09",etStr)){
                                editText.setText("09");
                            }else if(StringUtils.strToInt(etStr)<10){
                                editText.setText("0"+etStr);
                            }else if(StringUtils.strToInt(etStr)>12){
                                editText.setText("12");
                            }
                        }else{
                            editText.setText(yueStr);
                        }

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 输入日期内容不大于31
     * @param editText
     */
    public void ri(EditText editText,EditText etNian,EditText etYue,String riStr){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        String etNianStr = etNian.getText().toString();
                        String etYueStr = etYue.getText().toString();
                        int nian = Integer.valueOf("20"+etNianStr).intValue();
                        int yue = Integer.valueOf(etYueStr).intValue();
                        if(StringUtils.noEmpty(etStr)){
                            if(StringUtils.isEquals("00",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("01",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("02",etStr)){
                                editText.setText("02");
                            }else if(StringUtils.isEquals("03",etStr)){
                                editText.setText("03");
                            }else if(StringUtils.isEquals("04",etStr)){
                                editText.setText("04");
                            }else if(StringUtils.isEquals("05",etStr)){
                                editText.setText("05");
                            }else if(StringUtils.isEquals("06",etStr)){
                                editText.setText("06");
                            }else if(StringUtils.isEquals("07",etStr)){
                                editText.setText("07");
                            }else if(StringUtils.isEquals("08",etStr)){
                                editText.setText("08");
                            }else if(StringUtils.isEquals("09",etStr)){
                                editText.setText("09");
                            }else if(StringUtils.strToInt(etStr)<10){
                                editText.setText("0"+etStr);
                            }else if(StringUtils.strToInt(etStr)>28){
                                editText.setText(getMonthOfDay(nian,yue)+"");
                            }
                        }else {
                            editText.setText(riStr);
                        }

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 输入小时内容不大于24
     * @param editText
     */
    public void shi(EditText editText,String shiStr){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.noEmpty(etStr)){
                            if(StringUtils.isEquals("00",etStr)){
                                editText.setText("00");
                            }else if(StringUtils.isEquals("01",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("02",etStr)){
                                editText.setText("02");
                            }else if(StringUtils.isEquals("03",etStr)){
                                editText.setText("03");
                            }else if(StringUtils.isEquals("04",etStr)){
                                editText.setText("04");
                            }else if(StringUtils.isEquals("05",etStr)){
                                editText.setText("05");
                            }else if(StringUtils.isEquals("06",etStr)){
                                editText.setText("06");
                            }else if(StringUtils.isEquals("07",etStr)){
                                editText.setText("07");
                            }else if(StringUtils.isEquals("08",etStr)){
                                editText.setText("08");
                            }else if(StringUtils.isEquals("09",etStr)){
                                editText.setText("09");
                            }else if(StringUtils.strToInt(etStr)<10){
                                editText.setText("0"+etStr);
                            }else if(StringUtils.strToInt(etStr)>23){
                                editText.setText("23");
                            }
                        }else{
                            editText.setText(shiStr);
                        }

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 输入分钟内容不大于59
     * @param editText
     */
    public void fen(EditText editText,String fenStr){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.noEmpty(etStr)) {
                            if (StringUtils.isEquals("00", etStr)) {
                                editText.setText("00");
                            } else if (StringUtils.isEquals("01", etStr)) {
                                editText.setText("01");
                            } else if (StringUtils.isEquals("02", etStr)) {
                                editText.setText("02");
                            } else if (StringUtils.isEquals("03", etStr)) {
                                editText.setText("03");
                            } else if (StringUtils.isEquals("04", etStr)) {
                                editText.setText("04");
                            } else if (StringUtils.isEquals("05", etStr)) {
                                editText.setText("05");
                            } else if (StringUtils.isEquals("06", etStr)) {
                                editText.setText("06");
                            } else if (StringUtils.isEquals("07", etStr)) {
                                editText.setText("07");
                            } else if (StringUtils.isEquals("08", etStr)) {
                                editText.setText("08");
                            } else if (StringUtils.isEquals("09", etStr)) {
                                editText.setText("09");
                            } else if (StringUtils.strToInt(etStr) < 10) {
                                editText.setText("0" + etStr);
                            } else if (StringUtils.strToInt(etStr) > 59) {
                                editText.setText("59");
                            }
                        }else {
                            editText.setText(fenStr);
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 输入秒数不大于59
     * @param editText
     */
    public void miao(EditText editText,String miaoStr){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = editText.getText().toString();
                        if(StringUtils.noEmpty(etStr)) {
                            if(StringUtils.isEquals("00",etStr)){
                                editText.setText("00");
                            }else if(StringUtils.isEquals("01",etStr)){
                                editText.setText("01");
                            }else if(StringUtils.isEquals("02",etStr)){
                                editText.setText("02");
                            }else if(StringUtils.isEquals("03",etStr)){
                                editText.setText("03");
                            }else if(StringUtils.isEquals("04",etStr)){
                                editText.setText("04");
                            }else if(StringUtils.isEquals("05",etStr)){
                                editText.setText("05");
                            }else if(StringUtils.isEquals("06",etStr)){
                                editText.setText("06");
                            }else if(StringUtils.isEquals("07",etStr)){
                                editText.setText("07");
                            }else if(StringUtils.isEquals("08",etStr)){
                                editText.setText("08");
                            }else if(StringUtils.isEquals("09",etStr)){
                                editText.setText("09");
                            }else if(StringUtils.strToInt(etStr)<10){
                                editText.setText("0"+etStr);
                            }else if(StringUtils.strToInt(etStr)>59){
                                editText.setText("59");
                            }
                        }else{
                            editText.setText(miaoStr);
                        }

                        break;
                }
                return false;
            }
        });
    }
    public static int getMonthOfDay(int year,int month){
        Log.e("nian:",year+",yue:"+month);
        int day = 0;
        if(year%4==0&&year%100!=0||year%400==0){
            day = 29;
        }else{
            day = 28;
        }
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;
        }
        return 0;
    }
}
