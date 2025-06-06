package com.yc.allbluetooth.std.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.std.util.DianliuDianzuDw;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.EditorAction;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.math.BigDecimal;
import java.util.Locale;

public class ZhizuCeshiDyErActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "ZhizuCeshiDyErActivity";

    //Dy第二部分
    private LinearLayout llDyCsEr;
    private TextView tvDyCsErXb;
    private TextView tvDyCsErTime;
    private TextView tvDyCsErA0CsDz;
    private TextView tvDyCsErA0CsDzDw;
    private TextView tvDyCsErA0ZsDz;
    private TextView tvDyCsErA0ZsDzDw;
    private TextView tvDyCsErB0CsDz;
    private TextView tvDyCsErB0CsDzDw;
    private TextView tvDyCsErB0ZsDz;
    private TextView tvDyCsErB0ZsDzDw;
    private TextView tvDyCsErC0CsDz;
    private TextView tvDyCsErC0CsDzDw;
    private TextView tvDyCsErC0ZsDz;
    private TextView tvDyCsErC0ZsDzDw;
    private TextView tvDyCsErDzBphl;
    private EditText etDyCsErDzFj;
    private EditText etDyCsErDzSpwd;
    private LinearLayout llDyCsErDzBaocun;
    private LinearLayout llDyCsErDzDayin;
    private LinearLayout llDyCsErDzStop;

    int btnType = 0;//点击保存、打印、停止按钮对应的状态；保存：1；打印：2；停止：3;换相：4
    int diyi = 0;//第一次进入，先同步分接位置和温度

    View viewFd;
    View viewDy;
    View viewSave;
    AlertDialog TipsFangdian;
    AlertDialog TipsDayin;
    AlertDialog TipsSave;

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";

    String sjxz;//数据性质
    String sjxh;//数据序号
    String csxw;//测试相位
    String tfxx;//突发信息
    String cswd;//测试温度
    String fjwz;//分接位置
    String csdl;//测试电流
    String nian;//年
    String yue;//月
    String ri;//日
    String shi;//时
    String fen;//分
    String miao;//秒
    String a0orab;//A0或者ab测试数据，单精度浮点型
    String b0orbc;//B0或者bc测试数据，单精度浮点型
    String c0orca;//C0或者ac测试数据，单精度浮点型

    String tfxxType;//突发信息状态码

    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;
    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    private static final int msgKey1 = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    //tvTime.setText(GetTime.getTime2());//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    String msgStr = msg.obj.toString();
                    Log.i(TAG, "Home:"+msgStr);
                    if(StringUtils.isEquals(Config.ymType,"dyEr")){
                        if (msgStr.length() == 20 || msgStr.length() > 26||msgStr.length()==6) {
                            if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                                newMsgStr = msgStr;
                                Log.e("zhizuNew1=:", newMsgStr);
                            } else {
                                newMsgStr = newMsgStr + msgStr;
                                //可以
                                Log.e("zhizuNew2=:", newMsgStr);
                            }
                            if (newMsgStr.length() == 60) {//> 40
                                //可以
                                Log.e("dyErzhizu=60", "new:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 8, 10);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 30, 38);
                                Log.e(TAG + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 38, 46);
                                Log.e(TAG + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 46, 54);
                                Log.e(TAG + "c", c0orca);

                                //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                    Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "===========1=======================");
                                XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                        xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                    if(StringUtils.isEquals("03",csxw)){//A0
                                        tvDyCsErXb.setText("ab");
                                        tvDyCsErA0CsDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0CsDzDw));
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0ZsDzDw))) + ""));
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("04",csxw)){//B0
                                        tvDyCsErXb.setText("bc");
                                        tvDyCsErB0CsDz.setText(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0CsDzDw));
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("05",csxw)){//C0
                                        tvDyCsErXb.setText("ca");
                                        tvDyCsErC0CsDz.setText(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0CsDzDw));
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                    }
                                    String tvA0Str = tvDyCsErA0CsDz.getText().toString();
                                    String tvB0Str = tvDyCsErB0CsDz.getText().toString();
                                    String tvC0Str = tvDyCsErC0CsDz.getText().toString();
                                    Log.e("=====dyer======",tvA0Str+tvB0Str+tvC0Str);
                                    if (!StringUtils.isEquals(tvA0Str, "0.000") && !StringUtils.isEquals(tvB0Str, "0.000") && !StringUtils.isEquals(tvC0Str, "0.000")) {
                                        BigDecimal b1 = xsys.xiaoshu(tvA0Str);
                                        BigDecimal b2 = xsys.xiaoshu(tvB0Str);
                                        BigDecimal b3 = xsys.xiaoshu(tvC0Str);
                                        Log.e("=====dyer======",b1+","+b2+","+b3+"");
                                        BigDecimal bCha = xsys.bijiaoDaxiaoCha(b1, b2, b3);
                                        BigDecimal bHe = xsys.xiaoshuJia(xsys.xiaoshuJia(b1, b2), b3);

                                        double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(bCha, xsys.xiaoshuChu(bHe,xsys.xiaoshu("3"))), xsys.xiaoshu("100"));
                                        Log.e("=====dyer======",b0+"");
                                        //最大与最小的差/[(A+B+C)/3]*100%
                                        tvDyCsErDzBphl.setText(b0 + "%");
                                    } else {
                                        tvDyCsErDzBphl.setText("");
                                    }

                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    Log.e(TAG , "电流放电");
                                    //TipsFangdian.show();
                                    if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                    Log.e(TAG , "突发信息1："+tfxx);
                                    tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                    Log.e(TAG , "突发信息2："+tfxx);
                                    Log.e("btnType==03突发",btnType+"");
                                    Log.e("突发config",Config.btnType+"");
                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                        Log.e(TAG , "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG , "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG , "保存");
                                        //TipsSave.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG , "完成");
                                        Log.e("btnType==>完成关闭",btnType+"");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG , "保存2");
                                            TipsSave.cancel();
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG , "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            Config.yemianType2=0;
                                            finish();
                                        }
                                    }
                                }
                            }else if (newMsgStr.length() == 86) {//> 40
                                //可以
                                Log.e("zhizu==86", "new:" + newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr,26);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 8, 10);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 30, 38);
                                Log.e(TAG + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 38, 46);
                                Log.e(TAG + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 46, 54);
                                Log.e(TAG + "c", c0orca);

                                //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                    Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "========2==========================");
                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                            xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                    if(StringUtils.isEquals("03",csxw)){//A0
                                        tvDyCsErXb.setText("ab");
                                        tvDyCsErA0CsDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0CsDzDw));
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0ZsDzDw))) + ""));
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("04",csxw)){//B0
                                        tvDyCsErXb.setText("bc");
                                        tvDyCsErB0CsDz.setText(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0CsDzDw));
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("05",csxw)){//C0
                                        tvDyCsErXb.setText("ca");
                                        tvDyCsErC0CsDz.setText(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0CsDzDw));
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                    }
//                                    tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
//                                    tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
//                                    tvDyCsErA0ZsDz.setText(Config.dyAbCsZsdz);
//                                    tvDyCsErA0ZsDzDw.setText(Config.dyAbCsZsdzDw);
//                                    tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
//                                    tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
//                                    tvDyCsErB0ZsDz.setText(Config.dyBcCsZsdz);
//                                    tvDyCsErB0ZsDzDw.setText(Config.dyBcCsZsdzDw);
//                                    tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
//                                    tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
//                                    tvDyCsErC0ZsDz.setText(Config.dyCaCsZsdz);
//                                    tvDyCsErC0ZsDzDw.setText(Config.dyCaCsZsdzDw);
//
//                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal b1 = xsys.xiaoshu(Config.dyAbCsdz);
                                    BigDecimal b2 = xsys.xiaoshu(Config.dyBcCsdz);
                                    BigDecimal b3 = xsys.xiaoshu(Config.dyCaCsdz);
                                    double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(xsys.bijiaoDaxiaoCha(b1, b2, b3), xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshuJia(b1, b2), b3), xsys.xiaoshu("3"))), xsys.xiaoshu("100"));
                                    //Config.bphl = b0+"%";
                                    Log.e("=========",b0+"");
                                    tvDyCsErDzBphl.setText(b0+"%");
                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    Log.e(TAG , "电流放电");
                                    //TipsFangdian.show();
                                    if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                    Log.e(TAG , "突发信息1："+tfxx);
                                    tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                    Log.e(TAG , "突发信息2："+tfxx);
                                    Log.e("btnType==03突发",btnType+"");
                                    Log.e("突发config",Config.btnType+"");
                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                        Log.e(TAG , "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG , "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG , "保存");
                                        //TipsSave.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG , "完成");
                                        Log.e("btnType==>完成关闭",btnType+"");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG , "保存2");
                                            TipsSave.cancel();
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG , "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            Config.yemianType2=0;
                                            finish();
                                        }
                                    }
                                }
                            }else if (newMsgStr.length() == 120) {//120
                                //可以
                                Log.e("zhizu==120", "new:" + newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr,26);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 8, 10);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 30, 38);
                                Log.e(TAG + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 38, 46);
                                Log.e(TAG + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 46, 54);
                                Log.e(TAG + "c", c0orca);

                                //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                    Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "=========3=========================");
                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                            xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                    if(StringUtils.isEquals("03",csxw)){//A0
                                        tvDyCsErXb.setText("ab");
                                        tvDyCsErA0CsDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0CsDzDw));
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0ZsDzDw))) + ""));
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("04",csxw)){//B0
                                        tvDyCsErXb.setText("bc");
                                        tvDyCsErB0CsDz.setText(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0CsDzDw));
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("05",csxw)){//C0
                                        tvDyCsErXb.setText("ca");
                                        tvDyCsErC0CsDz.setText(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0CsDzDw));
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                    }
//                                    tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
//                                    tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
//                                    tvDyCsErA0ZsDz.setText(Config.dyAbCsZsdz);
//                                    tvDyCsErA0ZsDzDw.setText(Config.dyAbCsZsdzDw);
//                                    tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
//                                    tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
//                                    tvDyCsErB0ZsDz.setText(Config.dyBcCsZsdz);
//                                    tvDyCsErB0ZsDzDw.setText(Config.dyBcCsZsdzDw);
//                                    tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
//                                    tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
//                                    tvDyCsErC0ZsDz.setText(Config.dyCaCsZsdz);
//                                    tvDyCsErC0ZsDzDw.setText(Config.dyCaCsZsdzDw);
//
//                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal b1 = xsys.xiaoshu(Config.dyAbCsdz);
                                    BigDecimal b2 = xsys.xiaoshu(Config.dyBcCsdz);
                                    BigDecimal b3 = xsys.xiaoshu(Config.dyCaCsdz);
                                    double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(xsys.bijiaoDaxiaoCha(b1, b2, b3), xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshuJia(b1, b2), b3), xsys.xiaoshu("3"))), xsys.xiaoshu("100"));
                                    //Config.bphl = b0+"%";
                                    Log.e("=========",b0+"");
                                    tvDyCsErDzBphl.setText(b0+"%");
                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    Log.e(TAG , "电流放电");
                                    //TipsFangdian.show();
                                    if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                    Log.e(TAG , "突发信息1："+tfxx);
                                    tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                    Log.e(TAG , "突发信息2："+tfxx);
                                    Log.e("btnType==03突发",btnType+"");
                                    Log.e("突发config",Config.btnType+"");
                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                        Log.e(TAG , "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG , "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG , "保存");
                                        //TipsSave.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG , "完成");
                                        Log.e("btnType==>完成关闭",btnType+"");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG , "保存2");
                                            TipsSave.cancel();
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG , "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            Config.yemianType2=0;
                                            finish();
                                        }
                                    }
                                }
                            }else if (newMsgStr.length() == 126) {//> 40
                                //可以
                                Log.e("zhizu==126", "new:" + newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr,26);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 8, 10);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 30, 38);
                                Log.e(TAG + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 38, 46);
                                Log.e(TAG + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 46, 54);
                                Log.e(TAG + "c", c0orca);

                                //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                    Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "=========4=========================");
                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                            xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                                    //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                    if(StringUtils.isEquals("03",csxw)){//A0
                                        tvDyCsErXb.setText("ab");
                                        tvDyCsErA0CsDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0CsDzDw));
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvDyCsErA0ZsDzDw))) + ""));
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("04",csxw)){//B0
                                        tvDyCsErXb.setText("bc");
                                        tvDyCsErB0CsDz.setText(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0CsDzDw));
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", b0orbc, tvDyCsErB0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
                                        tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErC0CsDz.getText().toString()))+""));
                                        tvDyCsErC0ZsDzDw.setText(tvDyCsErC0CsDzDw.getText().toString());
                                    }else if(StringUtils.isEquals("05",csxw)){//C0
                                        tvDyCsErXb.setText("ca");
                                        tvDyCsErC0CsDz.setText(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0CsDzDw));
                                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", c0orca, tvDyCsErC0ZsDzDw))) + ""));
                                        tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
                                        tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
                                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErA0CsDz.getText().toString()))+""));
                                        tvDyCsErA0ZsDzDw.setText(tvDyCsErA0CsDzDw.getText().toString());
                                        tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
                                        tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
                                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(tvDyCsErB0CsDz.getText().toString()))+""));
                                        tvDyCsErB0ZsDzDw.setText(tvDyCsErB0CsDzDw.getText().toString());
                                    }
//                                    tvDyCsErA0CsDz.setText(Config.dyAbCsdz);
//                                    tvDyCsErA0CsDzDw.setText(Config.dyAbCsdzDw);
//                                    tvDyCsErA0ZsDz.setText(Config.dyAbCsZsdz);
//                                    tvDyCsErA0ZsDzDw.setText(Config.dyAbCsZsdzDw);
//                                    tvDyCsErB0CsDz.setText(Config.dyBcCsdz);
//                                    tvDyCsErB0CsDzDw.setText(Config.dyBcCsdzDw);
//                                    tvDyCsErB0ZsDz.setText(Config.dyBcCsZsdz);
//                                    tvDyCsErB0ZsDzDw.setText(Config.dyBcCsZsdzDw);
//                                    tvDyCsErC0CsDz.setText(Config.dyCaCsdz);
//                                    tvDyCsErC0CsDzDw.setText(Config.dyCaCsdzDw);
//                                    tvDyCsErC0ZsDz.setText(Config.dyCaCsZsdz);
//                                    tvDyCsErC0ZsDzDw.setText(Config.dyCaCsZsdzDw);
//
//                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal b1 = xsys.xiaoshu(Config.dyAbCsdz);
                                    BigDecimal b2 = xsys.xiaoshu(Config.dyBcCsdz);
                                    BigDecimal b3 = xsys.xiaoshu(Config.dyCaCsdz);
                                    double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(xsys.bijiaoDaxiaoCha(b1, b2, b3), xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshuJia(b1, b2), b3), xsys.xiaoshu("3"))), xsys.xiaoshu("100"));
                                    //Config.bphl = b0+"%";
                                    Log.e("=========",b0+"");
                                    tvDyCsErDzBphl.setText(b0+"%");
                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    Log.e(TAG , "电流放电");
                                    //TipsFangdian.show();
                                    if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                    Log.e(TAG , "突发信息1："+tfxx);
                                    tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                    Log.e(TAG , "突发信息2："+tfxx);
                                    Log.e("btnType==03突发",btnType+"");
                                    Log.e("突发config",Config.btnType+"");
                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                        Log.e(TAG , "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG , "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG , "保存");
                                        //TipsSave.show();
                                        Toast.makeText(ZhizuCeshiDyErActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG , "完成");
                                        Log.e("btnType==>完成关闭",btnType+"");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG , "保存2");
                                            TipsSave.cancel();
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG , "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            Config.yemianType2=0;
                                            finish();
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "这是返回的第一条验证指令：" + msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr, 4, 6);
                            if(diyi==0){
                                //if(StringUtils.isEquals(msgStr,"6677770000000003000000162A")){
                                if(StringUtils.isEquals(msgStr,"667777000000000300000000ABCE")){//ABCE
                                    sendDataByBle(SendUtil.initSendStdNew("74","14"),"");
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        Resources resources = this.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if("zh".equals(Config.zyType)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_zhizu_ceshi_dy_er);
        Config.ymType = "dyEr";
        ActivityCollector.addActivity(this);
        Config.tiaozhuan = 1;//不再跳转
        Log.e("===",Config.yemianType2+",");
        if(Config.yemianType2==0){
            Config.yemianType2 = 1;
        }else{
            finish();
        }
        Log.e("===",Config.yemianType2+",,");
        Log.e("===",Config.bphl);
        initModel();
        initView();
        initSend();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(ZhizuCeshiDyErActivity.this);
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
    }
    public void initSend(){
        sendDataByBle(SendUtil.initSendStdNew("77","03"),"");
    }
    public void initView(){
        viewFd = LayoutInflater.from(this).inflate(R.layout.dialog_fangdian, null, false);
        TipsFangdian = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewFd).create();
        viewDy = LayoutInflater.from(this).inflate(R.layout.dialog_dayin, null, false);
        TipsDayin = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewDy).create();
        viewSave = LayoutInflater.from(this).inflate(R.layout.dialog_save, null, false);
        TipsSave = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewSave).create();

        TipsFangdian.setCanceledOnTouchOutside(false);//点周围禁止关闭弹窗
        TipsDayin.setCanceledOnTouchOutside(false);
        TipsSave.setCanceledOnTouchOutside(false);
        //Dy(二)
        llDyCsEr = findViewById(R.id.llZhizuCeshiDyEr);
        tvDyCsErXb = findViewById(R.id.tvZzcsDyErCsXb);
        tvDyCsErTime = findViewById(R.id.tvZzcsDyErDzTime);
        tvDyCsErA0CsDz = findViewById(R.id.tvZzcsDyErA0CsDz);
        tvDyCsErA0CsDzDw = findViewById(R.id.tvZzcsDyErA0CsDzDw);
        tvDyCsErA0ZsDz = findViewById(R.id.tvZzcsDyErA0ZsDz);
        tvDyCsErA0ZsDzDw = findViewById(R.id.tvZzcsDyErA0ZsDzDw);
        tvDyCsErB0CsDz = findViewById(R.id.tvZzcsDyErB0CsDz);
        tvDyCsErB0CsDzDw = findViewById(R.id.tvZzcsDyErB0CsDzDw);
        tvDyCsErB0ZsDz = findViewById(R.id.tvZzcsDyErB0ZsDz);
        tvDyCsErB0ZsDzDw = findViewById(R.id.tvZzcsDyErB0ZsDzDw);
        tvDyCsErC0CsDz = findViewById(R.id.tvZzcsDyErC0CsDz);
        tvDyCsErC0CsDzDw = findViewById(R.id.tvZzcsDyErC0CsDzDw);
        tvDyCsErC0ZsDz = findViewById(R.id.tvZzcsDyErC0ZsDz);
        tvDyCsErC0ZsDzDw = findViewById(R.id.tvZzcsDyErC0ZsDzDw);
        tvDyCsErDzBphl = findViewById(R.id.tvZzcsDyErMaxBphl);
        etDyCsErDzFj = findViewById(R.id.etZzcsDyErDzFj);
        etDyCsErDzSpwd = findViewById(R.id.etZzcsDyErDzSpwd);
        llDyCsErDzBaocun = findViewById(R.id.llZzcsDyErCsDzBaocun);
        llDyCsErDzDayin = findViewById(R.id.llZzcsDyErCsDzDayin);
        llDyCsErDzStop = findViewById(R.id.llZzcsDyErCsDzStop);
        EditorAction editorAction = new EditorAction();
        editorAction.bulingFj(etDyCsErDzFj);
        editorAction.bulingWdStd(etDyCsErDzSpwd, tvDyCsErA0CsDz, tvDyCsErA0ZsDz, tvDyCsErB0CsDz, tvDyCsErB0ZsDz, tvDyCsErC0CsDz, tvDyCsErC0ZsDz);
        llDyCsErDzBaocun.setOnClickListener(this);
        llDyCsErDzDayin.setOnClickListener(this);
        llDyCsErDzStop.setOnClickListener(this);

        etDyCsErDzFj.setOnEditorActionListener(new TextView.OnEditorActionListener() {//分接位置
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        diyi=1;
                        String etStr = etDyCsErDzFj.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etDyCsErDzFj.setText("00");
                            sendDataByBle(SendUtil.initSendStdNew("77","00"),"");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etDyCsErDzFj.setText("01");
                            sendDataByBle(SendUtil.initSendStdNew("77","01"),"");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etDyCsErDzFj.setText("02");
                            sendDataByBle(SendUtil.initSendStdNew("77","02"),"");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etDyCsErDzFj.setText("03");
                            sendDataByBle(SendUtil.initSendStdNew("77","03"),"");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etDyCsErDzFj.setText("04");
                            sendDataByBle(SendUtil.initSendStdNew("77","04"),"");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etDyCsErDzFj.setText("05");
                            sendDataByBle(SendUtil.initSendStdNew("77","05"),"");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etDyCsErDzFj.setText("06");
                            sendDataByBle(SendUtil.initSendStdNew("77","06"),"");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etDyCsErDzFj.setText("07");
                            sendDataByBle(SendUtil.initSendStdNew("77","07"),"");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etDyCsErDzFj.setText("08");
                            sendDataByBle(SendUtil.initSendStdNew("77","08"),"");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etDyCsErDzFj.setText("09");
                            sendDataByBle(SendUtil.initSendStdNew("77","09"),"");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etDyCsErDzFj.getText().toString())<10){
                            etDyCsErDzFj.setText("0"+etStr);
                            sendDataByBle(SendUtil.initSendStdNew("77",etDyCsErDzFj.getText().toString()),"");
                        }else if(StringUtils.isEmpty(etStr)){
                            etDyCsErDzFj.setText("02");
                        }else if(StringUtils.strToInt(etDyCsErDzFj.getText().toString())>=10&&StringUtils.strToInt(etDyCsErDzFj.getText().toString())<=15){
                            sendDataByBle(SendUtil.initSendStdNew("77","0"+ ShiOrShiliu.toHexString(StringUtils.strToInt(etDyCsErDzFj.getText().toString()))),"");
                        }else if(StringUtils.strToInt(etDyCsErDzFj.getText().toString())>=16){
                            sendDataByBle(SendUtil.initSendStdNew("77",ShiOrShiliu.toHexString(StringUtils.strToInt(etDyCsErDzFj.getText().toString()))),"");
                        }
                        Config.fjwz = etDyCsErDzFj.getText().toString();
                        break;
                }
                return false;
            }
        });

        etDyCsErDzSpwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {//试品温度
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etDyCsErDzSpwd.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etDyCsErDzSpwd.setText("00");
                            sendDataByBle(SendUtil.initSendStdNew("74","00"),"");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etDyCsErDzSpwd.setText("01");
                            sendDataByBle(SendUtil.initSendStdNew("74","01"),"");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etDyCsErDzSpwd.setText("02");
                            sendDataByBle(SendUtil.initSendStdNew("74","02"),"");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etDyCsErDzSpwd.setText("03");
                            sendDataByBle(SendUtil.initSendStdNew("74","03"),"");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etDyCsErDzSpwd.setText("04");
                            sendDataByBle(SendUtil.initSendStdNew("74","04"),"");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etDyCsErDzSpwd.setText("05");
                            sendDataByBle(SendUtil.initSendStdNew("74","05"),"");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etDyCsErDzSpwd.setText("06");
                            sendDataByBle(SendUtil.initSendStdNew("74","06"),"");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etDyCsErDzSpwd.setText("07");
                            sendDataByBle(SendUtil.initSendStdNew("74","07"),"");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etDyCsErDzSpwd.setText("08");
                            sendDataByBle(SendUtil.initSendStdNew("74","08"),"");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etDyCsErDzSpwd.setText("09");
                            sendDataByBle(SendUtil.initSendStdNew("74","09"),"");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etDyCsErDzSpwd.getText().toString())<10){
                            etDyCsErDzSpwd.setText("0"+etStr);
                            sendDataByBle(SendUtil.initSendStdNew("74",etDyCsErDzSpwd.getText().toString()),"");
                        }else if(StringUtils.isEmpty(etStr)){
                            etDyCsErDzSpwd.setText("20");
                            sendDataByBle(SendUtil.initSendStdNew("74","14"),"");
                        }else if(StringUtils.strToInt(etDyCsErDzSpwd.getText().toString())>=10&&StringUtils.strToInt(etDyCsErDzSpwd.getText().toString())<=15){
                            sendDataByBle(SendUtil.initSendStdNew("74","0"+ ShiOrShiliu.toHexString(StringUtils.strToInt(etDyCsErDzSpwd.getText().toString()))),"");
                        }else if(StringUtils.strToInt(etDyCsErDzSpwd.getText().toString())>=16){
                            sendDataByBle(SendUtil.initSendStdNew("74",ShiOrShiliu.toHexString(StringUtils.strToInt(etDyCsErDzSpwd.getText().toString()))),"");
                        }

                        Config.cswd = etDyCsErDzSpwd.getText().toString();

                        String a0CsDzStr = tvDyCsErA0CsDz.getText().toString();
                        String b0CsDzStr = tvDyCsErB0CsDz.getText().toString();
                        String c0CsDzStr = tvDyCsErC0CsDz.getText().toString();
                        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                        BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                        tvDyCsErA0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(a0CsDzStr))+""));
                        tvDyCsErB0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(b0CsDzStr))+""));
                        tvDyCsErC0ZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(c0CsDzStr))+""));
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llZzcsDyErCsDzBaocun:
                btnType = 1;
                sendDataByBle(SendUtil.initSendStd("6d"), "");
                break;
            case R.id.llZzcsDyErCsDzDayin:
                btnType = 2;
                sendDataByBle(SendUtil.initSendStd("6c"), "");
                break;
            case R.id.llZzcsDyErCsDzStop:
                btnType = 3;
                sendDataByBle(SendUtil.initSendStd("69"), "");
                break;
        }
    }

    /**
     * 蓝牙连接检测线程
     */
    Runnable checkConnetRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (!bleFlag) {
                //没有在指定时间收到回复
                if (regainBleDataCount > 2) {
                    mHandler.sendEmptyMessage(1000);
                } else {
                    regainBleDataCount++;

                    sendDataByBle(currentSendOrder, "");
                    //这里再次调用此Runnable对象，以实现每三秒实现一次的定时器操作
                    mHandler.postDelayed(checkConnetRunnable, 3000);
                }
            }
        }
    };
    /**
     * 设置回调方法
     */
    private BleConnectionCallBack blecallback = new BleConnectionCallBack() {

        @Override
        public void onRecive(BluetoothGattCharacteristic data_char) {
            bleFlag = true;

            //收到的数据
            byte[] receive_byte = data_char.getValue();
            String str = CheckUtils.byte2hex(receive_byte).toString();

            Message message = new Message();
            message.obj = str;
            message.what = Config.BLUETOOTH_GETDATA;
            mHandler.sendMessage(message);
        }

        @Override
        public void onSuccessSend() {
            //数据发送成功
            Log.e("home", "onSuccessSend: ");
        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("home", "onDisconnect: ");
            Message message = new Message();
            message.what = Config.BLUETOOTH_LIANJIE_DUANKAI;
            mHandler.sendMessage(message);
        }
    };
    /**
     * android ble 发送
     * 每条数据长度应保证在20个字节以内
     * 2条数据至少要空15ms
     *
     * @param currentSendAllOrder
     * @param title
     */
    private void sendDataByBle(final String currentSendAllOrder, String title) {
        if (currentSendAllOrder.length() > 0) {
            if (!title.equals("")) {
//                showDialog(title);
                Log.d("--->", title);
            }
            currentSendOrder = currentSendAllOrder;
            final boolean[] isSuccess = new boolean[1];
            if (currentSendAllOrder.length() <= 40) {
                Log.e("--->1", "currentSendAllOrder:"+currentSendAllOrder);
                sData = CheckUtils.hex2byte(currentSendOrder);
                Log.e("--->2", "currentSendAllOrder:"+sData);
                //if(BleConnectUtil.mBluetoothGattCharacteristic==null){
                mBluetoothGattCharacteristic.setValue(sData);
                Log.e("--->3", "currentSendAllOrder:"+mBluetoothGattCharacteristic.getUuid().toString());
                //}
                isSuccess[0] = bleConnectUtil.sendData(mBluetoothGattCharacteristic);
                Log.e("--->4", "currentSendAllOrder:"+ isSuccess[0]);
            } else {
                for (int i = 0; i < currentSendAllOrder.length(); i = i + 40) {
                    final String[] shortOrder = {""};
                    final int finalI = i;

                    if (currentSendAllOrder.length() - i >= 40) {
                        shortOrder[0] = currentSendAllOrder.substring(finalI, finalI + 40);
                    } else {
                        shortOrder[0] = currentSendAllOrder.substring(finalI, currentSendAllOrder.length());
                    }

                    Log.e("--->", "shortOrder[0]2：" + shortOrder[0]);
                    sData = CheckUtils.hex2byte(shortOrder[0]);
                    mBluetoothGattCharacteristic.setValue(sData);
                    isSuccess[0] = bleConnectUtil.sendData(mBluetoothGattCharacteristic);
                }
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isSuccess[0]) {
                        //dialog.dismiss();
                        mHandler.sendEmptyMessage(Config.BLUETOOTH_LIANJIE_DUANKAI);
                    }
                    Log.e("--->", "是否发送成功2：" + isSuccess[0]);
                }
            }, (currentSendAllOrder.length() / 40 + 1) * 15);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.ymType = "dyEr";
//        bleConnectUtil = new BleConnectUtil(ZhizuCeshiDyErActivity.this);
//        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
//        }
//        bleConnectUtil.setCallback(blecallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.setCallback(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.setCallback(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");

        Config.dyAbCsdz = "";
        Config.dyAbCsdzDw="";
        Config.dyAbCsZsdz="";
        Config.dyAbCsZsdzDw="";
        Config.dyBcCsdz="";
        Config.dyBcCsdzDw="";
        Config.dyBcCsZsdz="";
        Config.dyBcCsZsdzDw="";
        Config.dyCaCsdz="";
        Config.dyCaCsdzDw="";
        Config.dyCaCsZsdz="";
        Config.dyCaCsZsdzDw="";
        Config.bphl = "";
        Config.yemianType2=0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }
}