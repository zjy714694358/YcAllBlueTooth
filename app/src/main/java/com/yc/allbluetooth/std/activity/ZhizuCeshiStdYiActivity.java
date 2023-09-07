package com.yc.allbluetooth.std.activity;



import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.util.DianliuDianzuDw;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.Locale;

public class ZhizuCeshiStdYiActivity extends AppCompatActivity implements View.OnClickListener {


    //第二部分(三通道第一部分)，点击开始测试后，第一次页面（测试电流）
    private LinearLayout llStdStartYi;
    private TextView tvStdTime;
    private TextView tvStdA0Dl;
    private TextView tvStdA0DlDw;
    private TextView tvStdB0Dl;
    private TextView tvStdB0DlDw;
    private TextView tvStdC0Dl;
    private TextView tvStdC0DlDw;
    private LinearLayout llStdDlStop;
    View viewFd;
    AlertDialog TipsFangdian;

    private String TAG = "ZhizuCeshiStdYiActivity";

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
                    if(StringUtils.isEquals(Config.ymType,"stdYi")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "stdYi:"+msgStr);

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
                                Log.e("stdYizhizu=60", "new:" + newMsgStr);
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
                                if(Config.tiaozhuan==0) {
                                    //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                    if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                        //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                        //if (StringUtils.isEquals("06", csxw)) {//三通道
                                        tvStdA0Dl.setText(DianliuDianzuDw.dw("00", a0orab, tvStdA0DlDw));
                                        if(!StringUtils.isEquals(b0orbc,"FFFFFFFF")){
                                            tvStdB0Dl.setText(DianliuDianzuDw.dw("00", b0orbc, tvStdB0DlDw));
                                        }
                                        if(!StringUtils.isEquals(c0orca,"FFFFFFFF")){
                                            tvStdC0Dl.setText(DianliuDianzuDw.dw("00", c0orca, tvStdC0DlDw));
                                        }

                                        // }
                                    } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                        Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "==================================");

                                        startActivity(new Intent(ZhizuCeshiStdYiActivity.this, ZhizuCeshiStdErActivity.class));
                                        finish();

                                    } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                        Log.e(TAG, "电流放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                        Log.e(TAG, "突发信息1：" + tfxx);
                                        tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                        Log.e(TAG, "突发信息2：" + tfxx);
                                        Log.e("突发config", Config.btnType + "");
                                        if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                            Log.e(TAG, "放电");
                                            //TipsFangdian.show();
                                            if(Config.fdTcCishu<3){
                                                Config.fdTcCishu+=1;
                                                Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                            Log.e(TAG, "完成");
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            tvStdA0Dl.setText("");
                                            tvStdB0Dl.setText("");
                                            tvStdC0Dl.setText("");
                                            //finish();
                                            Intent it = new Intent();
                                            it.setClass(ZhizuCeshiStdYiActivity.this, StdHomeActivity.class);
                                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                                            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                                            startActivity(it);
                                        }
                                    }
                                }
                            }else if (newMsgStr.length() == 86) {//> 40
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
                                if(Config.tiaozhuan==0) {
                                    //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                    if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                        //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                        //if (StringUtils.isEquals("06", csxw)) {//三通道
                                        tvStdA0Dl.setText(DianliuDianzuDw.dw("00", a0orab, tvStdA0DlDw));
                                        if(!StringUtils.isEquals(b0orbc,"FFFFFFFF")){
                                            tvStdB0Dl.setText(DianliuDianzuDw.dw("00", b0orbc, tvStdB0DlDw));
                                        }
                                        if(!StringUtils.isEquals(c0orca,"FFFFFFFF")){
                                            tvStdC0Dl.setText(DianliuDianzuDw.dw("00", c0orca, tvStdC0DlDw));
                                        }
                                        // }
                                    } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                        Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "==================================");

                                        startActivity(new Intent(ZhizuCeshiStdYiActivity.this, ZhizuCeshiStdErActivity.class));
                                        finish();

                                    } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                        Log.e(TAG, "电流放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                        Log.e(TAG, "突发信息1：" + tfxx);
                                        tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                        Log.e(TAG, "突发信息2：" + tfxx);
                                        Log.e("突发config", Config.btnType + "");
                                        if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                            Log.e(TAG, "放电");
                                            //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                        } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                            Log.e(TAG, "完成");
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            tvStdA0Dl.setText("");
                                            tvStdB0Dl.setText("");
                                            tvStdC0Dl.setText("");
                                            //finish();
                                            Intent it = new Intent();
                                            it.setClass(ZhizuCeshiStdYiActivity.this, StdHomeActivity.class);
                                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                                            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                                            startActivity(it);
                                        }
                                    }
                                }
                            }else if (newMsgStr.length() == 120) {//> 40
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
                                if(Config.tiaozhuan==0) {
                                    //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                    if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                        //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                        //if (StringUtils.isEquals("06", csxw)) {//三通道
                                        tvStdA0Dl.setText(DianliuDianzuDw.dw("00", a0orab, tvStdA0DlDw));
                                        if(!StringUtils.isEquals(b0orbc,"FFFFFFFF")){
                                            tvStdB0Dl.setText(DianliuDianzuDw.dw("00", b0orbc, tvStdB0DlDw));
                                        }
                                        if(!StringUtils.isEquals(c0orca,"FFFFFFFF")){
                                            tvStdC0Dl.setText(DianliuDianzuDw.dw("00", c0orca, tvStdC0DlDw));
                                        }
                                        // }
                                    } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                        Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "==================================");

                                        startActivity(new Intent(ZhizuCeshiStdYiActivity.this, ZhizuCeshiStdErActivity.class));
                                        finish();

                                    } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                        Log.e(TAG, "电流放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                        Log.e(TAG, "突发信息1：" + tfxx);
                                        tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                        Log.e(TAG, "突发信息2：" + tfxx);
                                        Log.e("突发config", Config.btnType + "");
                                        if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                            Log.e(TAG, "放电");
                                            //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu+=1;
                                            Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                        } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                            Log.e(TAG, "完成");
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            tvStdA0Dl.setText("");
                                            tvStdB0Dl.setText("");
                                            tvStdC0Dl.setText("");
                                            //finish();
                                            Intent it = new Intent();
                                            it.setClass(ZhizuCeshiStdYiActivity.this, StdHomeActivity.class);
                                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                                            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                                            startActivity(it);
                                        }
                                    }
                                }
                            }else if (newMsgStr.length() == 126) {//> 40
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
                                if(Config.tiaozhuan==0) {
                                    //0：测试电流；1：电阻；2：放电电流；3：下位机突发信息
                                    if (StringUtils.isEquals("00", sjxz)) {//0：测试电流；
                                        //0:A0;1:B0;2:C0;3:ab;4:bc;5:ca;6:三相
                                        //if (StringUtils.isEquals("06", csxw)) {//三通道
                                        tvStdA0Dl.setText(DianliuDianzuDw.dw("00", a0orab, tvStdA0DlDw));
                                        if(!StringUtils.isEquals(b0orbc,"FFFFFFFF")){
                                            tvStdB0Dl.setText(DianliuDianzuDw.dw("00", b0orbc, tvStdB0DlDw));
                                        }
                                        if(!StringUtils.isEquals(c0orca,"FFFFFFFF")){
                                            tvStdC0Dl.setText(DianliuDianzuDw.dw("00", c0orca, tvStdC0DlDw));
                                        }
                                        // }
                                    } else if (StringUtils.isEquals("01", sjxz)) {//1：电阻；
                                        Log.e(TAG, "==============================================电阻==测试相位:" + csxw + "==================================");

                                        startActivity(new Intent(ZhizuCeshiStdYiActivity.this, ZhizuCeshiStdErActivity.class));
                                        finish();

                                    } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                        Log.e(TAG, "电流放电");
                                        if(TipsFangdian!=null){
                                            //TipsFangdian.show();
                                            if(Config.fdTcCishu<3){
                                                Config.fdTcCishu+=1;
                                                Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            viewFd = LayoutInflater.from(ZhizuCeshiStdYiActivity.this).inflate(R.layout.dialog_fangdian, null, false);
                                            TipsFangdian = new AlertDialog.Builder(ZhizuCeshiStdYiActivity.this, R.style.MyDialog).setView(viewFd).create();
                                            //TipsFangdian.show();
                                            if(Config.fdTcCishu<3){
                                                Config.fdTcCishu+=1;
                                                Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发消息
                                        Log.e(TAG, "突发信息1：" + tfxx);
                                        tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                        Log.e(TAG, "突发信息2：" + tfxx);
                                        Log.e("突发config", Config.btnType + "");
                                        if (StringUtils.isEquals("02", tfxx)) {//正在放电02
                                            Log.e(TAG, "放电");
                                            //TipsFangdian.show();
                                            if(Config.fdTcCishu<3){
                                                Config.fdTcCishu+=1;
                                                Toast.makeText(ZhizuCeshiStdYiActivity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                            Log.e(TAG, "完成");
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            tvStdA0Dl.setText("");
                                            tvStdB0Dl.setText("");
                                            tvStdC0Dl.setText("");
                                            //finish();
                                            Intent it = new Intent();
                                            it.setClass(ZhizuCeshiStdYiActivity.this, StdHomeActivity.class);
                                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                                            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                                            startActivity(it);
                                        }
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "这是返回的第一条验证指令：" + msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr, 4, 6);
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
        Log.e("yemian","进入stdYi");
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_zhizu_ceshi_std_yi);
        initView();
        Config.ymType = "stdYi";
        ActivityCollector.addActivity(this);
        Config.tiaozhuan = 0;//不再跳转
        initModel();

    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(ZhizuCeshiStdYiActivity.this);
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
    }
    public void initView(){
        //三通道一
        llStdStartYi = findViewById(R.id.llZhizuCeshiStdYi);
        tvStdTime = findViewById(R.id.tvZzcsStdDlTime);
        tvStdA0Dl =findViewById(R.id.tvZzcsStdA0Dl);
        tvStdA0DlDw = findViewById(R.id.tvZzcsStdA0DlDw);
        tvStdB0Dl = findViewById(R.id.tvZzcsStdB0Dl);
        tvStdB0DlDw = findViewById(R.id.tvZzcsStdB0DlDw);
        tvStdC0Dl = findViewById(R.id.tvZzcsStdC0Dl);
        tvStdC0DlDw = findViewById(R.id.tvZzcsStdC0DlDw);
        llStdDlStop = findViewById(R.id.llZzcsStdCsDlStop);
        llStdDlStop.setOnClickListener(this);

        viewFd = LayoutInflater.from(this).inflate(R.layout.dialog_fangdian, null, false);
        TipsFangdian = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewFd).create();

        TipsFangdian.setCanceledOnTouchOutside(false);//点周围禁止关闭弹窗
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llZzcsStdCsDlStop:
                Config.fdTcCishu = 0;
                String strStdCsAllStdSave = "6886"+"69"+"000000000000"+"0000";
                byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
                String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
                Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
                String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
                sendDataByBle(sendAllYnSave, "");
                break;
        }
    }

    /**
     * 屏幕右上角时间显示，每隔一秒执行一次
     */
    public class TimeThread extends Thread{
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
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
        Config.ymType = "stdYi";
        Log.i(TAG, Config.ymType);
//        bleConnectUtil = new BleConnectUtil(ZhizuCeshiStdYiActivity.this);
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
 //       mHandler.removeCallbacksAndMessages(null);
 //       bleConnectUtil.setCallback(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
//        bleConnectUtil.disConnect();
   //     mHandler.removeCallbacksAndMessages(null);
   //     bleConnectUtil.setCallback(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");
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