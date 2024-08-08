package com.yc.allbluetooth.dtd10c.activity;


import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.dtd10c.util.DianliuDianzuDw;
import com.yc.allbluetooth.dtd10c.util.Qiehuan;
import com.yc.allbluetooth.dtd10c.util.Xianshi;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

public class Dtd10cZzCs2Activity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ZzCs2Activity";

    private TextView tvTime;
    private TextView tvCsDl;
    private TextView tvCsDlDw;
    private TextView tvJishi;
    private TextView tvDz;
    private TextView tvDzDw;
    private TextView tvZsDz;
    private TextView tvZsDzDw;
    private TextView tvRz;
    private TextView tvRzCl;
    private TextView tvXb;
    private EditText etSpwd;
    private EditText etFj;
    private EditText etZsWd;
    private LinearLayout llBc;
    private LinearLayout llDy;
    private LinearLayout llTz;
    private Button btnBc;
    private Button btnDy;
    private Button btnTz;
    private TextView tvBc;
    private TextView tvDy;
    private TextView tvTz;

    int jinType = 0;//进入这个页面后，第几次执行这个方法，获取下位机数据更新手机页面属性，只执行一次

    View viewFd;
    View viewDy;
    View viewSave;
    AlertDialog TipsFangdian;
    AlertDialog TipsDayin;
    AlertDialog TipsSave;
    int btnType = 0;//保存1；打印2；停止3；


    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";

    String sjxz;//数据性质
    String sjxh;//数据序号
    String csxw;//测试相位
    String tfxx;//突发信息
    String cswd;//测试温度
    String fjwz;//分接位置
    String csdl;//测试电流
    String raozu;//年==》绕组6高压；7中压；8低压
    String zswd;//月==》折算温度
    String rzcl;//日==》绕组材料==9铜；10铝
    String shi;//时
    String fen;//分
    String miao;//秒
    String a0orab;//A0或者ab测试数据，单精度浮点型
    String b0orbc;//B0或者bc测试数据，单精度浮点型
    String c0orca;//C0或者ac测试数据，单精度浮点型
    String crcJy;//Crc校验

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
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"dtdZzCsEr")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                            newMsgStr = msgStr;
                            Log.e(TAG,newMsgStr);
                        }else{
                            newMsgStr = newMsgStr+msgStr;
                            //可以
                            Log.e(TAG+"可以",newMsgStr);
                        }
                        if(newMsgStr.length()==40){
                            Log.e(TAG+"最后一次电流",newMsgStr);
                            String kaitou = StringUtils.subStrStartToEnd(newMsgStr,0,4);
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                            if(StringUtils.isEquals(kaitou,"6677")&&StringUtils.isEquals(sjxz,"00")){//判断是否是电流值（自动）最后一次
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr,32,40);
                                String dlType = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                                if(StringUtils.isEquals("03", csdl) ||StringUtils.isEquals("05", csdl) || StringUtils.isEquals("06", csdl)){
                                    String a0orabHl = HexUtil.reverseHex(a0orab);
                                    float a0orabF = 0;
                                    try {
                                        a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl, 16));
                                    } catch (HexUtil.NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF + "");
                                    tvCsDl.setText(a0orabStr2);
                                    tvCsDlDw.setText("mA");
                                }else{
                                    tvCsDl.setText(DianliuDianzuDw.dw("00", a0orab, tvCsDlDw));
                                }

                            }
                        }
                        if(newMsgStr.length()==62){//>40
                            //可以
                            Log.e("diaoyueNew2=62", "new1:"+newMsgStr);
                            //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                            //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                            //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                            //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                            //0B00010101==>A0,B0,C0
                            //0B00101010==>ab,bc,ca
                            sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,10);
                            csxw = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                            tfxx = StringUtils.subStrStartToEnd(newMsgStr,12,14);
                            cswd = StringUtils.subStrStartToEnd(newMsgStr,14,16);
                            fjwz = StringUtils.subStrStartToEnd(newMsgStr,16,18);
                            csdl = StringUtils.subStrStartToEnd(newMsgStr,18,20);
                            raozu = StringUtils.subStrStartToEnd(newMsgStr,20,22);
                            zswd = StringUtils.subStrStartToEnd(newMsgStr,22,24);
                            rzcl = StringUtils.subStrStartToEnd(newMsgStr,24,26);
                            shi = StringUtils.subStrStartToEnd(newMsgStr,26,28);
                            fen = StringUtils.subStrStartToEnd(newMsgStr,28,30);
                            miao = StringUtils.subStrStartToEnd(newMsgStr,30,32);
                            a0orab = StringUtils.subStrStartToEnd(newMsgStr,32,40);
                            b0orbc = StringUtils.subStrStartToEnd(newMsgStr,40,48);
                            c0orca = StringUtils.subStrStartToEnd(newMsgStr,48,56);
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,56,60);


                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,56);
                            byte[] bytesStdSave = new BigInteger(crcAll, 16).toByteArray();
                            if(CrcUtil.CrcIsOk(bytesStdSave,crcJy)) {
                                Log.e(TAG, "crc==true");
//                            Log.e(TAG+"1"+"a", a0orab);
//                            Log.e(TAG+"2"+"a", a0orab);
                                float a0orabF = 0;
                                String a0orabHl = HexUtil.reverseHex(a0orab);
                                try {
                                    a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF + "");
                                //Log.e(TAG+"2"+"a转换后", a0orabStr2);
                                if (StringUtils.isEquals("00", sjxz)) {//电流值
                                    Log.e(TAG + "2" + "测试电流", csdl);
                                    Log.e(TAG + "2" + "电流转换后", a0orabStr2);
                                    if (StringUtils.isEquals("03", csdl) ||StringUtils.isEquals("05", csdl) || StringUtils.isEquals("06", csdl)) {
                                        Log.e(TAG + "毫安", csdl);
                                        tvCsDl.setText(a0orabStr2);
                                        tvCsDlDw.setText("mA");
                                    } else {
                                        tvCsDl.setText(DianliuDianzuDw.dw("00", a0orab, tvCsDlDw));
                                    }

                                } else if (StringUtils.isEquals("01", sjxz)) {//电阻值
                                    Log.e(TAG + "2" + "电阻转换后", a0orabStr2);
                                    tvDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDzDw));
                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                            xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd2)));//少测试温度
                                    tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvZsDzDw))) + ""));
                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    //TipsFangdian.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.fangdiantanchuang), Toast.LENGTH_SHORT).show();
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发信息
                                    Log.e(TAG, "突发信息2：" + tfxx);
                                    Log.e("btnType==03突发", btnType + "");

                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02

                                        Log.e(TAG, "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu += 1;
                                            Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }

                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG, "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG, "保存");
                                        //if(!isFinishing()){
                                            //TipsSave.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                        //}else{
//                                            viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
//                                            TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
//                                            TipsSave.show();
//                                        }
//                                        if(TipsSave!=null){
//                                            TipsSave.show();
//                                        }else{
//                                            viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
//                                            TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
//                                            TipsSave.show();
//                                        }

                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG, "完成");
                                        Log.e("btnType==>完成关闭", btnType + "");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG, "保存2");
                                            //TipsSave.cancel();
                                            if (Config.bphlType == 1) {
                                                String rzStr = tvRz.getText().toString();
                                                String fjStr = etFj.getText().toString();
                                                String xbStr = tvXb.getText().toString();
                                                String dzStr = tvDz.getText().toString();
                                                String dzDwStr = tvDzDw.getText().toString();
                                                String zsdzStr = tvZsDz.getText().toString();
                                                String zsdzDwStr = tvZsDzDw.getText().toString();
                                                if (StringUtils.isEquals(Config.RzValue, rzStr) && StringUtils.isEquals(Config.FjValue, fjStr)) {
                                                    Log.e("=============1","1");
                                                    Log.e("====", "绕组、分接符合");
                                                    if (StringUtils.isEquals("A0", xbStr)) {
                                                        Config.A0Value = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("B0", xbStr)) {
                                                        Config.B0Value = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("C0", xbStr)) {
                                                        Config.C0Value = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("AB", xbStr)) {
                                                        Config.ABValue = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("BC", xbStr)) {
                                                        Config.BCValue = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("CA", xbStr)) {
                                                        Config.CAValue = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    }
                                                    Log.e(TAG+"A0",Config.A0Value+","+Config.B0Value+","+Config.C0Value);
                                                    Log.e(TAG+"AB",Config.ABValue+","+Config.BCValue+","+Config.CAValue);
                                                    if (StringUtils.noEmpty(Config.A0Value) && StringUtils.noEmpty(Config.B0Value) && StringUtils.noEmpty(Config.C0Value)) {
                                                        Config.TzType = 0;
                                                        Config.CsDlValue = tvCsDl.getText().toString();
                                                        Config.CsDlValueDw = tvCsDlDw.getText().toString();
                                                        Config.CsXb = xbStr;
                                                        finish();
                                                        startActivity(new Intent(Dtd10cZzCs2Activity.this, Dtd10cZzCs3Activity.class));
                                                    } else if (StringUtils.noEmpty(Config.ABValue) && StringUtils.noEmpty(Config.BCValue) && StringUtils.noEmpty(Config.CAValue)) {
                                                        Config.TzType = 1;
                                                        Config.CsDlValue = tvCsDl.getText().toString();
                                                        Config.CsDlValueDw = tvCsDlDw.getText().toString();
                                                        Config.CsXb = xbStr;
                                                        finish();
                                                        startActivity(new Intent(Dtd10cZzCs2Activity.this, Dtd10cZzCs3Activity.class));
                                                    }
                                                } else {
                                                    Log.e("=============1","2");
                                                    Log.e(TAG,"绕组："+rzStr);
                                                    Config.RzValue = rzStr;
                                                    Config.FjValue = fjStr;
                                                    Log.e(TAG,"绕组2："+Config.RzValue);
                                                    if (StringUtils.isEquals("A0", xbStr)) {
                                                        Config.A0Value = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("B0", xbStr)) {
                                                        Config.B0Value = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("C0", xbStr)) {
                                                        Config.C0Value = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("AB", xbStr)) {
                                                        Config.ABValue = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("BC", xbStr)) {
                                                        Config.BCValue = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("CA", xbStr)) {
                                                        Config.CAValue = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    }
                                                }
                                            }
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG, "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            Config.fdTcCishu = 0;
                                            finish();
                                        } else {
                                            TipsFangdian.cancel();
                                            TipsDayin.cancel();
                                            TipsSave.cancel();
                                        }
                                    }
                                }
                                Log.e(TAG + "1" + "b", b0orbc);
                                float b0orbcF = 0;
                                String b0orbcHl = HexUtil.reverseHex(b0orbc);
                                try {
                                    b0orbcF = Float.intBitsToFloat((int) HexUtil.parseLong(b0orbcHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String b0orbcStr2 = StringUtils.siweiYouxiaoStr(b0orbcF + "");
                                Log.e(TAG + "2" + "b转换后", b0orbcStr2);


                                Log.e(TAG + "1" + "c", c0orca);
                                float c0orcaF = 0;
                                String c0orcaHl = HexUtil.reverseHex(c0orca);
                                try {
                                    c0orcaF = Float.intBitsToFloat((int) HexUtil.parseLong(c0orcaHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String c0orcaStr2 = StringUtils.siweiYouxiaoStr(c0orcaF + "");
                                Log.e(TAG + "2" + "c转换后", c0orcaStr2);

                                Log.e(TAG + "1", "数据序号：" + sjxh);
                                /*************************************************************************/
                                Log.e(TAG + "1", "测试相位：" + csxw);
                                Log.e(TAG + "1", "测试温度：" + cswd);
                                Log.e(TAG + "1", "分接位置：" + fjwz);
                                Log.e(TAG + "1", "绕组：" + raozu);
                                Log.e(TAG + "1", "折算温度：" + zswd);
                                Log.e(TAG + "1", "绕组材料：" + rzcl);
                                if (jinType < 2) {//第一次执行，获取下位机属性
                                    Xianshi.raozu(raozu, tvRz, Dtd10cZzCs2Activity.this);
                                    Xianshi.xiangbie(csxw, tvXb);
                                    if (ShiOrShiliu.parseInt(fjwz) < 10) {
                                        etFj.setText("0" + ShiOrShiliu.parseInt(fjwz));
                                    } else {
                                        etFj.setText(ShiOrShiliu.parseInt(fjwz) + "");
                                    }
                                    if (ShiOrShiliu.parseInt(cswd) < 10) {
                                        etSpwd.setText("0" + ShiOrShiliu.parseInt(cswd));
                                    } else {
                                        etSpwd.setText(ShiOrShiliu.parseInt(cswd) + "");
                                    }
                                    Config.cswd2 = ShiOrShiliu.parseInt(cswd)+"";
                                    if (ShiOrShiliu.parseInt(zswd) < 10) {
                                        etZsWd.setText("0" + ShiOrShiliu.parseInt(zswd));
                                    } else {
                                        etZsWd.setText(ShiOrShiliu.parseInt(zswd) + "");
                                    }
                                    Xianshi.rzcl(rzcl, tvRzCl, Dtd10cZzCs2Activity.this);
                                    jinType = jinType +1;
                                }
                            } else {
                                Log.e(TAG, "crc==false");
                            }


                        }else if(newMsgStr.length()==88){
                            //可以
                            Log.e("diaoyueNew2=88", "new1:"+newMsgStr);
                            newMsgStr = StringUtils.subStrStart(newMsgStr,26);
                            //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                            //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                            //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                            //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                            //0B00010101==>A0,B0,C0
                            //0B00101010==>ab,bc,ca
                            sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,10);
                            csxw = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                            tfxx = StringUtils.subStrStartToEnd(newMsgStr,12,14);
                            cswd = StringUtils.subStrStartToEnd(newMsgStr,14,16);
                            fjwz = StringUtils.subStrStartToEnd(newMsgStr,16,18);
                            csdl = StringUtils.subStrStartToEnd(newMsgStr,18,20);
                            raozu = StringUtils.subStrStartToEnd(newMsgStr,20,22);
                            zswd = StringUtils.subStrStartToEnd(newMsgStr,22,24);
                            rzcl = StringUtils.subStrStartToEnd(newMsgStr,24,26);
                            shi = StringUtils.subStrStartToEnd(newMsgStr,26,28);
                            fen = StringUtils.subStrStartToEnd(newMsgStr,28,30);
                            miao = StringUtils.subStrStartToEnd(newMsgStr,30,32);
                            a0orab = StringUtils.subStrStartToEnd(newMsgStr,32,40);
                            b0orbc = StringUtils.subStrStartToEnd(newMsgStr,40,48);
                            c0orca = StringUtils.subStrStartToEnd(newMsgStr,48,56);
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,56,60);


                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,56);
                            byte[] bytesStdSave = new BigInteger(crcAll, 16).toByteArray();
                            if(CrcUtil.CrcIsOk(bytesStdSave,crcJy)) {
                                Log.e(TAG, "crc==true");
//                            Log.e(TAG+"1"+"a", a0orab);
//                            Log.e(TAG+"2"+"a", a0orab);
                                float a0orabF = 0;
                                String a0orabHl = HexUtil.reverseHex(a0orab);
                                try {
                                    a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF + "");
                                //Log.e(TAG+"2"+"a转换后", a0orabStr2);
                                if (StringUtils.isEquals("00", sjxz)) {//电流值
                                    Log.e(TAG + "2" + "电流转换后", a0orabStr2);
                                    if (StringUtils.isEquals("05", csdl) || StringUtils.isEquals("06", csdl)) {
                                        tvCsDl.setText(a0orabStr2);
                                        tvCsDlDw.setText("mA");
                                    } else {
                                        tvCsDl.setText(DianliuDianzuDw.dw("00", a0orab, tvCsDlDw));
                                    }

                                } else if (StringUtils.isEquals("01", sjxz)) {//电阻值
                                    Log.e(TAG + "2" + "电阻转换后", a0orabStr2);
                                    tvDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDzDw));
                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                            xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd2)));//少测试温度
                                    tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvZsDzDw))) + ""));
                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    //TipsFangdian.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发信息
                                    Log.e(TAG, "突发信息2：" + tfxx);
                                    Log.e("btnType==03突发", btnType + "");

                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02

                                        Log.e(TAG, "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu += 1;
                                            Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG, "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG, "保存");

                                        //if(!isFinishing()){
                                            //TipsSave.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
//                                        }else{
//                                            viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
//                                            TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
//                                            TipsSave.show();
//                                        }
//                                        if(TipsSave!=null){
//                                            TipsSave.show();
//                                        }else{
//                                            viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
//                                            TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
//                                            TipsSave.show();
//                                        }
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG, "完成");
                                        Log.e("btnType==>完成关闭", btnType + "");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG, "保存2");
                                            TipsSave.cancel();
                                            if (Config.bphlType == 1) {
                                                String rzStr = tvRz.getText().toString();
                                                String fjStr = etFj.getText().toString();
                                                String xbStr = tvXb.getText().toString();
                                                String dzStr = tvDz.getText().toString();
                                                String dzDwStr = tvDzDw.getText().toString();
                                                String zsdzStr = tvZsDz.getText().toString();
                                                String zsdzDwStr = tvZsDzDw.getText().toString();
                                                if (StringUtils.isEquals(Config.RzValue, rzStr) && StringUtils.isEquals(Config.FjValue, fjStr)) {
                                                    Log.e("=============1","3");
                                                    Log.e("====", "绕组、分接符合");
                                                    if (StringUtils.isEquals("A0", xbStr)) {
                                                        Config.A0Value = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("B0", xbStr)) {
                                                        Config.B0Value = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("C0", xbStr)) {
                                                        Config.C0Value = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("AB", xbStr)) {
                                                        Config.ABValue = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("BC", xbStr)) {
                                                        Config.BCValue = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("CA", xbStr)) {
                                                        Config.CAValue = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    }
                                                    Log.e(TAG+"A02",Config.A0Value+","+Config.B0Value+","+Config.C0Value);
                                                    Log.e(TAG+"AB2",Config.ABValue+","+Config.BCValue+","+Config.CAValue);
                                                    if (StringUtils.noEmpty(Config.A0Value) && StringUtils.noEmpty(Config.B0Value) && StringUtils.noEmpty(Config.C0Value)) {
                                                        Config.TzType = 0;
                                                        Config.CsDlValue = tvCsDl.getText().toString();
                                                        Config.CsDlValueDw = tvCsDlDw.getText().toString();
                                                        Config.CsXb = xbStr;
                                                        finish();
                                                        startActivity(new Intent(Dtd10cZzCs2Activity.this, Dtd10cZzCs3Activity.class));
                                                    } else if (StringUtils.noEmpty(Config.ABValue) && StringUtils.noEmpty(Config.BCValue) && StringUtils.noEmpty(Config.CAValue)) {
                                                        Config.TzType = 1;
                                                        Config.CsDlValue = tvCsDl.getText().toString();
                                                        Config.CsDlValueDw = tvCsDlDw.getText().toString();
                                                        Config.CsXb = xbStr;
                                                        finish();
                                                        startActivity(new Intent(Dtd10cZzCs2Activity.this, Dtd10cZzCs3Activity.class));
                                                    }
                                                } else {
                                                    Log.e("=============1","4");
                                                    Config.RzValue = rzStr;
                                                    Config.FjValue = fjStr;

                                                    if (StringUtils.isEquals("A0", xbStr)) {
                                                        Config.A0Value = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("B0", xbStr)) {
                                                        Config.B0Value = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("C0", xbStr)) {
                                                        Config.C0Value = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("AB", xbStr)) {
                                                        Config.ABValue = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("BC", xbStr)) {
                                                        Config.BCValue = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("CA", xbStr)) {
                                                        Config.CAValue = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    }
                                                }
                                            }
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG, "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            finish();
                                        } else {
                                            TipsFangdian.cancel();
                                            TipsDayin.cancel();
                                            TipsSave.cancel();
                                        }
                                    }
                                }
                                Log.e(TAG + "1" + "b", b0orbc);
                                float b0orbcF = 0;
                                String b0orbcHl = HexUtil.reverseHex(b0orbc);
                                try {
                                    b0orbcF = Float.intBitsToFloat((int) HexUtil.parseLong(b0orbcHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String b0orbcStr2 = StringUtils.siweiYouxiaoStr(b0orbcF + "");
                                Log.e(TAG + "2" + "b转换后", b0orbcStr2);


                                Log.e(TAG + "1" + "c", c0orca);
                                float c0orcaF = 0;
                                String c0orcaHl = HexUtil.reverseHex(c0orca);
                                try {
                                    c0orcaF = Float.intBitsToFloat((int) HexUtil.parseLong(c0orcaHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String c0orcaStr2 = StringUtils.siweiYouxiaoStr(c0orcaF + "");
                                Log.e(TAG + "2" + "c转换后", c0orcaStr2);

                                Log.e(TAG + "1", "数据序号：" + sjxh);
                                /*************************************************************************/
                                Log.e(TAG + "2", "测试相位：" + csxw);
                                Log.e(TAG + "2", "测试温度：" + cswd);
                                Log.e(TAG + "2", "分接位置：" + fjwz);
                                Log.e(TAG + "2", "绕组：" + raozu);
                                Log.e(TAG + "2", "折算温度：" + zswd);
                                Log.e(TAG + "2", "绕组材料：" + rzcl);
                                if (jinType < 2) {//第一次执行，获取下位机属性
                                    Xianshi.raozu(raozu, tvRz, Dtd10cZzCs2Activity.this);
                                    Xianshi.xiangbie(csxw, tvXb);
                                    if (ShiOrShiliu.parseInt(fjwz) < 10) {
                                        etFj.setText("0" + ShiOrShiliu.parseInt(fjwz));
                                    } else {
                                        etFj.setText(ShiOrShiliu.parseInt(fjwz) + "");
                                    }
                                    if (ShiOrShiliu.parseInt(cswd) < 10) {
                                        etSpwd.setText("0" + ShiOrShiliu.parseInt(cswd));
                                    } else {
                                        etSpwd.setText(ShiOrShiliu.parseInt(cswd) + "");
                                    }
                                    Config.cswd2 = ShiOrShiliu.parseInt(cswd)+"";
                                    if (ShiOrShiliu.parseInt(zswd) < 10) {
                                        etZsWd.setText("0" + ShiOrShiliu.parseInt(zswd));
                                    } else {
                                        etZsWd.setText(ShiOrShiliu.parseInt(zswd) + "");
                                    }
                                    Xianshi.rzcl(rzcl, tvRzCl, Dtd10cZzCs2Activity.this);
                                    jinType = jinType+1;
                                }
                            } else {
                                Log.e(TAG, "crc==false");
                            }
                            //tvCsDl.setText(DianliuDianzuDw.dw("00", a0orab, tvCsDlDw));
                        } else if(newMsgStr.length()==124){//>40
                            //可以
                            Log.e("diaoyueNew2=124", "new1:"+newMsgStr);
                            //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                            String newMsgStr2 = StringUtils.subStrStartToEnd(newMsgStr,62,124);
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr2,4,6);
                            //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                            //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                            //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                            //0B00010101==>A0,B0,C0
                            //0B00101010==>ab,bc,ca
                            sjxh = StringUtils.subStrStartToEnd(newMsgStr2,6,10);
                            csxw = StringUtils.subStrStartToEnd(newMsgStr2,10,12);
                            tfxx = StringUtils.subStrStartToEnd(newMsgStr2,12,14);
                            cswd = StringUtils.subStrStartToEnd(newMsgStr2,14,16);
                            fjwz = StringUtils.subStrStartToEnd(newMsgStr2,16,18);
                            csdl = StringUtils.subStrStartToEnd(newMsgStr2,18,20);
                            raozu = StringUtils.subStrStartToEnd(newMsgStr2,20,22);
                            zswd = StringUtils.subStrStartToEnd(newMsgStr2,22,24);
                            rzcl = StringUtils.subStrStartToEnd(newMsgStr2,24,26);
                            shi = StringUtils.subStrStartToEnd(newMsgStr2,26,28);
                            fen = StringUtils.subStrStartToEnd(newMsgStr2,28,30);
                            miao = StringUtils.subStrStartToEnd(newMsgStr2,30,32);
                            a0orab = StringUtils.subStrStartToEnd(newMsgStr2,32,40);
                            b0orbc = StringUtils.subStrStartToEnd(newMsgStr2,40,48);
                            c0orca = StringUtils.subStrStartToEnd(newMsgStr2,48,56);
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr2,56,60);


                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr2,0,56);
                            byte[] bytesStdSave = new BigInteger(crcAll, 16).toByteArray();
                            if(CrcUtil.CrcIsOk(bytesStdSave,crcJy)) {
                                Log.e(TAG, "crc==true");
//                            Log.e(TAG+"1"+"a", a0orab);
//                            Log.e(TAG+"2"+"a", a0orab);
                                float a0orabF = 0;
                                String a0orabHl = HexUtil.reverseHex(a0orab);
                                try {
                                    a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF + "");
                                //Log.e(TAG+"2"+"a转换后", a0orabStr2);
                                if (StringUtils.isEquals("00", sjxz)) {//电流值
                                    Log.e(TAG + "2" + "测试电流", csdl);
                                    Log.e(TAG + "2" + "电流转换后", a0orabStr2);
                                    if (StringUtils.isEquals("03", csdl) ||StringUtils.isEquals("05", csdl) || StringUtils.isEquals("06", csdl)) {
                                        Log.e(TAG + "毫安", csdl);
                                        tvCsDl.setText(a0orabStr2);
                                        tvCsDlDw.setText("mA");
                                    } else {
                                        tvCsDl.setText(DianliuDianzuDw.dw("00", a0orab, tvCsDlDw));
                                    }

                                } else if (StringUtils.isEquals("01", sjxz)) {//电阻值
                                    Log.e(TAG + "2" + "电阻转换后", a0orabStr2);
                                    tvDz.setText(DianliuDianzuDw.dw("01", a0orab, tvDzDw));
                                    XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                    BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                            xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd2)));//少测试温度
                                    tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", a0orab, tvZsDzDw))) + ""));
                                } else if (StringUtils.isEquals("02", sjxz)) {//放电电流
                                    //TipsFangdian.show();
                                    Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.fangdiantanchuang), Toast.LENGTH_SHORT).show();
                                } else if (StringUtils.isEquals("03", sjxz)) {//下位机突发信息
                                    Log.e(TAG, "突发信息2：" + tfxx);
                                    Log.e("btnType==03突发", btnType + "");

                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电02

                                        Log.e(TAG, "放电");
                                        //TipsFangdian.show();
                                        if(Config.fdTcCishu<3){
                                            Config.fdTcCishu += 1;
                                            Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                        }

                                    } else if (StringUtils.isEquals("07", tfxx)) {//正在打印，目前02
                                        Log.e(TAG, "打印");
                                        //TipsDayin.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())) {//保存成功==>"0B"0F
                                        Log.e(TAG, "保存");
                                        //if(!isFinishing()){
                                        //TipsSave.show();
                                        Toast.makeText(Dtd10cZzCs2Activity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                        //}else{
//                                            viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
//                                            TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
//                                            TipsSave.show();
//                                        }
//                                        if(TipsSave!=null){
//                                            TipsSave.show();
//                                        }else{
//                                            viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
//                                            TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
//                                            TipsSave.show();
//                                        }

                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕、打印完毕、保存完毕//&&StringUtils.isEquals("69",tfxxType)==>0F
                                        Log.e(TAG, "完成");
                                        Log.e("btnType==>完成关闭", btnType + "");
                                        if (btnType == 1) {//保存
                                            Log.e(TAG, "保存2");
                                            //TipsSave.cancel();
                                            if (Config.bphlType == 1) {
                                                String rzStr = tvRz.getText().toString();
                                                String fjStr = etFj.getText().toString();
                                                String xbStr = tvXb.getText().toString();
                                                String dzStr = tvDz.getText().toString();
                                                String dzDwStr = tvDzDw.getText().toString();
                                                String zsdzStr = tvZsDz.getText().toString();
                                                String zsdzDwStr = tvZsDzDw.getText().toString();
                                                if (StringUtils.isEquals(Config.RzValue, rzStr) && StringUtils.isEquals(Config.FjValue, fjStr)) {
                                                    Log.e("=============1","1");
                                                    Log.e("====", "绕组、分接符合");
                                                    if (StringUtils.isEquals("A0", xbStr)) {
                                                        Config.A0Value = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("B0", xbStr)) {
                                                        Config.B0Value = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("C0", xbStr)) {
                                                        Config.C0Value = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("AB", xbStr)) {
                                                        Config.ABValue = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("BC", xbStr)) {
                                                        Config.BCValue = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("CA", xbStr)) {
                                                        Config.CAValue = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    }
                                                    Log.e(TAG+"A0",Config.A0Value+","+Config.B0Value+","+Config.C0Value);
                                                    Log.e(TAG+"AB",Config.ABValue+","+Config.BCValue+","+Config.CAValue);
                                                    if (StringUtils.noEmpty(Config.A0Value) && StringUtils.noEmpty(Config.B0Value) && StringUtils.noEmpty(Config.C0Value)) {
                                                        Config.TzType = 0;
                                                        Config.CsDlValue = tvCsDl.getText().toString();
                                                        Config.CsDlValueDw = tvCsDlDw.getText().toString();
                                                        Config.CsXb = xbStr;
                                                        finish();
                                                        startActivity(new Intent(Dtd10cZzCs2Activity.this, Dtd10cZzCs3Activity.class));
                                                    } else if (StringUtils.noEmpty(Config.ABValue) && StringUtils.noEmpty(Config.BCValue) && StringUtils.noEmpty(Config.CAValue)) {
                                                        Config.TzType = 1;
                                                        Config.CsDlValue = tvCsDl.getText().toString();
                                                        Config.CsDlValueDw = tvCsDlDw.getText().toString();
                                                        Config.CsXb = xbStr;
                                                        finish();
                                                        startActivity(new Intent(Dtd10cZzCs2Activity.this, Dtd10cZzCs3Activity.class));
                                                    }
                                                } else {
                                                    Log.e("=============1","2");
                                                    Log.e(TAG,"绕组："+rzStr);
                                                    Config.RzValue = rzStr;
                                                    Config.FjValue = fjStr;
                                                    Log.e(TAG,"绕组2："+Config.RzValue);
                                                    if (StringUtils.isEquals("A0", xbStr)) {
                                                        Config.A0Value = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("B0", xbStr)) {
                                                        Config.B0Value = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("C0", xbStr)) {
                                                        Config.C0Value = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.ABValue = "";
                                                        Config.BCValue = "";
                                                        Config.CAValue = "";
                                                    } else if (StringUtils.isEquals("AB", xbStr)) {
                                                        Config.ABValue = dzStr;
                                                        Config.AValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("BC", xbStr)) {
                                                        Config.BCValue = dzStr;
                                                        Config.BValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    } else if (StringUtils.isEquals("CA", xbStr)) {
                                                        Config.CAValue = dzStr;
                                                        Config.CValueDw = dzDwStr;

                                                        Config.A0Value = "";
                                                        Config.B0Value = "";
                                                        Config.C0Value = "";
                                                    }
                                                }
                                            }
                                        } else if (btnType == 2) {//打印
                                            Log.e(TAG, "打印2");
                                            TipsDayin.cancel();
                                        } else if (btnType == 3) {//停止
                                            Log.e(TAG, "放电2");
                                            TipsFangdian.cancel();
                                            Config.fdTcCishu = 0;
                                            finish();
                                        } else {
                                            TipsFangdian.cancel();
                                            TipsDayin.cancel();
                                            TipsSave.cancel();
                                        }
                                    }
                                }
                                Log.e(TAG + "1" + "b", b0orbc);
                                float b0orbcF = 0;
                                String b0orbcHl = HexUtil.reverseHex(b0orbc);
                                try {
                                    b0orbcF = Float.intBitsToFloat((int) HexUtil.parseLong(b0orbcHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String b0orbcStr2 = StringUtils.siweiYouxiaoStr(b0orbcF + "");
                                Log.e(TAG + "2" + "b转换后", b0orbcStr2);


                                Log.e(TAG + "1" + "c", c0orca);
                                float c0orcaF = 0;
                                String c0orcaHl = HexUtil.reverseHex(c0orca);
                                try {
                                    c0orcaF = Float.intBitsToFloat((int) HexUtil.parseLong(c0orcaHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String c0orcaStr2 = StringUtils.siweiYouxiaoStr(c0orcaF + "");
                                Log.e(TAG + "2" + "c转换后", c0orcaStr2);

                                Log.e(TAG + "1", "数据序号：" + sjxh);
                                /*************************************************************************/
                                Log.e(TAG + "1", "测试相位：" + csxw);
                                Log.e(TAG + "1", "测试温度：" + cswd);
                                Log.e(TAG + "1", "分接位置：" + fjwz);
                                Log.e(TAG + "1", "绕组：" + raozu);
                                Log.e(TAG + "1", "折算温度：" + zswd);
                                Log.e(TAG + "1", "绕组材料：" + rzcl);
                                if (jinType < 2) {//第一次执行，获取下位机属性
                                    Xianshi.raozu(raozu, tvRz, Dtd10cZzCs2Activity.this);
                                    Xianshi.xiangbie(csxw, tvXb);
                                    if (ShiOrShiliu.parseInt(fjwz) < 10) {
                                        etFj.setText("0" + ShiOrShiliu.parseInt(fjwz));
                                    } else {
                                        etFj.setText(ShiOrShiliu.parseInt(fjwz) + "");
                                    }
                                    if (ShiOrShiliu.parseInt(cswd) < 10) {
                                        etSpwd.setText("0" + ShiOrShiliu.parseInt(cswd));
                                    } else {
                                        etSpwd.setText(ShiOrShiliu.parseInt(cswd) + "");
                                    }
                                    Config.cswd2 = ShiOrShiliu.parseInt(cswd)+"";
                                    if (ShiOrShiliu.parseInt(zswd) < 10) {
                                        etZsWd.setText("0" + ShiOrShiliu.parseInt(zswd));
                                    } else {
                                        etZsWd.setText(ShiOrShiliu.parseInt(zswd) + "");
                                    }
                                    Xianshi.rzcl(rzcl, tvRzCl, Dtd10cZzCs2Activity.this);
                                    jinType = jinType +1;
                                }
                            } else {
                                Log.e(TAG, "crc==false");
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
//        Language language = new Language();
//        if(Config.CN_US_TYPE==2){
//            language.translateText(ZzCs2Activity.this,"",1);
//        }else{
//            language.translateText(ZzCs2Activity.this,"",2);
//        }
        Resources resources = this.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if("zh".equals(Config.zyType)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_dtd10c_zz_cs2);
        Config.ymType = "dtdZzCsEr";
        ActivityCollector.addActivity(this);
        initView();
        initModel();
        new TimeThread().start();
    }
    public void initView(){
        tvTime = findViewById(R.id.tvZzCs2Time);
        tvCsDl = findViewById(R.id.tvZzCs2CsDl);
        tvCsDlDw = findViewById(R.id.tvZzCs2CsDlDw);
        tvJishi = findViewById(R.id.tvZzCs2Js);
        tvDz = findViewById(R.id.tvZzCs2CsDz);
        tvDzDw = findViewById(R.id.tvZzCs2CsDzDw);
        tvZsDz = findViewById(R.id.tvZzCs2ZsDz);
        tvZsDzDw = findViewById(R.id.tvZzCs2ZsDzDw);
        tvRz = findViewById(R.id.tvZzCs2Rz);
        tvRzCl = findViewById(R.id.tvZzCs2RzCl);
        tvXb = findViewById(R.id.tvZzCs2Xb);
        etSpwd = findViewById(R.id.etZzCs2SpWd);
        etFj = findViewById(R.id.etZzCs2Fj);
        etZsWd = findViewById(R.id.etZzCs2ZsWd);
        llBc = findViewById(R.id.llZzCs2Bc);
        llDy = findViewById(R.id.llZzCs2Dy);
        llTz = findViewById(R.id.llZzCs2Tz);

//        btnBc = findViewById(R.id.btnZzCs2Bc);
//        btnDy = findViewById(R.id.btnZzCs2Dy);
//        btnTz = findViewById(R.id.btnZzCs2Tz);
        tvBc = findViewById(R.id.tvZzCs2Bc);
        tvDy = findViewById(R.id.tvZzCs2Dy);
        tvTz = findViewById(R.id.tvZzCs2Tz);


        tvRz.setOnClickListener(this);
        tvRzCl.setOnClickListener(this);
        tvXb.setOnClickListener(this);
//        llBc.setOnClickListener(this);
//        llDy.setOnClickListener(this);
//        llTz.setOnClickListener(this);
        tvBc.setOnClickListener(this);
        tvDy.setOnClickListener(this);
        tvTz.setOnClickListener(this);

        viewFd = LayoutInflater.from(this).inflate(R.layout.dialog_fangdian, null, false);
        TipsFangdian = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewFd).create();
        viewDy = LayoutInflater.from(this).inflate(R.layout.dialog_dayin, null, false);
        TipsDayin = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewDy).create();
        viewSave = LayoutInflater.from(this).inflate(R.layout.dialog_save, null, false);
        TipsSave = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewSave).create();

        TipsFangdian.setCanceledOnTouchOutside(false);//点周围禁止关闭弹窗
        TipsDayin.setCanceledOnTouchOutside(false);
        TipsSave.setCanceledOnTouchOutside(false);

//        EditorAction editorAction = new EditorAction();
//
//        editorAction.bulingFj(etFj);
//        editorAction.bulingZswd(etZsWd,Config.rzczInt,etSpwd,tvDz,tvZsDz);
//        editorAction.bulingCswd(etSpwd,Config.rzczInt,etZsWd,tvDz,tvZsDz);

        etFj.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etFj.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etFj.setText("00");
                            sendDataByBle(SendUtil.bianhuanSend("82","00"),"");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etFj.setText("01");
                            sendDataByBle(SendUtil.bianhuanSend("82","01"),"");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etFj.setText("02");
                            sendDataByBle(SendUtil.bianhuanSend("82","02"),"");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etFj.setText("03");
                            sendDataByBle(SendUtil.bianhuanSend("82","03"),"");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etFj.setText("04");
                            sendDataByBle(SendUtil.bianhuanSend("82","04"),"");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etFj.setText("05");
                            sendDataByBle(SendUtil.bianhuanSend("82","05"),"");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etFj.setText("06");
                            sendDataByBle(SendUtil.bianhuanSend("82","06"),"");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etFj.setText("07");
                            sendDataByBle(SendUtil.bianhuanSend("82","07"),"");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etFj.setText("08");
                            sendDataByBle(SendUtil.bianhuanSend("82","08"),"");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etFj.setText("09");
                            sendDataByBle(SendUtil.bianhuanSend("82","09"),"");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etFj.getText().toString())<10){
                            etFj.setText("0"+etStr);
                            sendDataByBle(SendUtil.bianhuanSend("82",etFj.getText().toString()),"");
                        }else if(StringUtils.isEmpty(etStr)){
                            etFj.setText("01");
                        }else if(StringUtils.strToInt(etFj.getText().toString())>=10&&StringUtils.strToInt(etFj.getText().toString())<=15){
                            sendDataByBle(SendUtil.bianhuanSend("82","0"+ShiOrShiliu.toHexString(StringUtils.strToInt(etFj.getText().toString()))),"");
                        }else if(StringUtils.strToInt(etFj.getText().toString())>=16){
                            sendDataByBle(SendUtil.bianhuanSend("82",ShiOrShiliu.toHexString(StringUtils.strToInt(etFj.getText().toString()))),"");
                        }
                        break;
                }
                return false;
            }
        });
        etSpwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etSpwd.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etSpwd.setText("00");
                            sendDataByBle(SendUtil.bianhuanSend("84","00"),"");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etSpwd.setText("01");
                            sendDataByBle(SendUtil.bianhuanSend("84","01"),"");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etSpwd.setText("02");
                            sendDataByBle(SendUtil.bianhuanSend("84","02"),"");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etSpwd.setText("03");
                            sendDataByBle(SendUtil.bianhuanSend("84","03"),"");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etSpwd.setText("04");
                            sendDataByBle(SendUtil.bianhuanSend("84","04"),"");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etSpwd.setText("05");
                            sendDataByBle(SendUtil.bianhuanSend("84","05"),"");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etSpwd.setText("06");
                            sendDataByBle(SendUtil.bianhuanSend("84","06"),"");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etSpwd.setText("07");
                            sendDataByBle(SendUtil.bianhuanSend("84","07"),"");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etSpwd.setText("08");
                            sendDataByBle(SendUtil.bianhuanSend("84","08"),"");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etSpwd.setText("09");
                            sendDataByBle(SendUtil.bianhuanSend("84","09"),"");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etSpwd.getText().toString())<10){
                            etSpwd.setText("0"+etStr);
                            sendDataByBle(SendUtil.bianhuanSend("84",etSpwd.getText().toString()),"");
                        }else if(StringUtils.isEmpty(etStr)){
                            etSpwd.setText("20");
                        }else if(StringUtils.strToInt(etSpwd.getText().toString())>=10&&StringUtils.strToInt(etSpwd.getText().toString())<=15){
                            sendDataByBle(SendUtil.bianhuanSend("84","0"+ShiOrShiliu.toHexString(StringUtils.strToInt(etSpwd.getText().toString()))),"");
                        }else if(StringUtils.strToInt(etSpwd.getText().toString())>=16){
                            sendDataByBle(SendUtil.bianhuanSend("84",ShiOrShiliu.toHexString(StringUtils.strToInt(etSpwd.getText().toString()))),"");
                        }
                        String cswd = etSpwd.getText().toString();
                        String zswd = etZsWd.getText().toString();
                        String csDzStr = tvDz.getText().toString();
                        if(StringUtils.noEmpty(cswd)&&StringUtils.noEmpty(csDzStr)) {
                            XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                            BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(zswd)),
                                    xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(cswd)));//少测试温度
                            tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(csDzStr)) + ""));
                            Config.cswd2 = cswd;
                        }
                        break;
                }
                return false;
            }
        });
        etZsWd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etZsWd.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etZsWd.setText("00");
                            sendDataByBle(SendUtil.bianhuanSend("85","00"),"");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etZsWd.setText("01");
                            sendDataByBle(SendUtil.bianhuanSend("85","01"),"");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etZsWd.setText("02");
                            sendDataByBle(SendUtil.bianhuanSend("85","02"),"");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etZsWd.setText("03");
                            sendDataByBle(SendUtil.bianhuanSend("85","03"),"");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etZsWd.setText("04");
                            sendDataByBle(SendUtil.bianhuanSend("85","04"),"");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etZsWd.setText("05");
                            sendDataByBle(SendUtil.bianhuanSend("85","05"),"");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etZsWd.setText("06");
                            sendDataByBle(SendUtil.bianhuanSend("85","06"),"");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etZsWd.setText("07");
                            sendDataByBle(SendUtil.bianhuanSend("85","07"),"");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etZsWd.setText("08");
                            sendDataByBle(SendUtil.bianhuanSend("85","08"),"");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etZsWd.setText("09");
                            sendDataByBle(SendUtil.bianhuanSend("85","09"),"");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etZsWd.getText().toString())<10){
                            etZsWd.setText("0"+etStr);
                            sendDataByBle(SendUtil.bianhuanSend("85",etZsWd.getText().toString()),"");
                        }else if(StringUtils.isEmpty(etStr)){
                            etZsWd.setText("75");
                        }else if(StringUtils.strToInt(etZsWd.getText().toString())>=10&&StringUtils.strToInt(etZsWd.getText().toString())<=15){
                            sendDataByBle(SendUtil.bianhuanSend("85","0"+ShiOrShiliu.toHexString(StringUtils.strToInt(etZsWd.getText().toString()))),"");
                        }else if(StringUtils.strToInt(etZsWd.getText().toString())>=16){
                            sendDataByBle(SendUtil.bianhuanSend("85",ShiOrShiliu.toHexString(StringUtils.strToInt(etZsWd.getText().toString()))),"");
                        }
                        String cswd = etSpwd.getText().toString();
                        String zswd = etZsWd.getText().toString();
                        String csDzStr = tvDz.getText().toString();
                        if(StringUtils.noEmpty(cswd)&&StringUtils.noEmpty(csDzStr)){
                            XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                            BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(zswd)),
                                    xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd2)));//少测试温度
                            tvZsDz.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(csDzStr))+""));
                            Config.zswd = zswd;
                        }

                        break;
                }
                return false;
            }
        });
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(Dtd10cZzCs2Activity.this);
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
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
            Log.e("ZzCs2", "onSuccessSend: ");

        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("ZzCs2", "onDisconnect: ");
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
    public void sendDataByBle(final String currentSendAllOrder, String title) {
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
    public void onClick(View v) {
        Qiehuan qiehuan = new Qiehuan();
        switch (v.getId()){
            case R.id.tvZzCs2Rz:
                String tvRzStr = tvRz.getText().toString();
                sendDataByBle(SendUtil.bianhuanSend("80",qiehuan.raozu(tvRzStr,tvRz)),"");
                break;
            case R.id.tvZzCs2RzCl:
                String tvRzClStr = tvRzCl.getText().toString();
                //qiehuan.raozucailiao(tvRzClStr,tvRzCl);
                if(StringUtils.isEquals("铜",tvRzClStr)){
                    Config.rzczInt = 225;//铝
                }else{
                    Config.rzczInt = 235;//铜
                }
                sendDataByBle(SendUtil.bianhuanSend("83",qiehuan.raozucailiao(tvRzClStr,tvRzCl)),"");
                break;
            case R.id.tvZzCs2Xb:
                String tvXbStr = tvXb.getText().toString();

                sendDataByBle(SendUtil.bianhuanSend("81",qiehuan.xiangbie(tvXbStr,tvXb)),"");
                break;
            //case R.id.llZzCs2Bc:
            case R.id.tvZzCs2Bc:
                btnType = 1;
                Log.e("=======================","baocun"+btnType);
                //发送保存指令,如果是第三次保存，跳转到综合不平衡率页面
                sendDataByBle(SendUtil.initSend("86"),"");


                //sendDataByBle(SendUtil.bianhuanSend("85","14"),"");

//                finish();
//                startActivity(new Intent(ZzCs2Activity.this,ZzCs3Activity.class));
                break;
            //case R.id.llZzCs2Dy:
            case R.id.tvZzCs2Dy:
                btnType = 2;
                Log.e("=======================","dayin"+btnType);
                //发送打印指令
                sendDataByBle(SendUtil.initSend("87"),"");
                break;
            //case R.id.llZzCs2Tz:
            case R.id.tvZzCs2Tz:
                btnType = 3;
                Log.e("=======================","tingzhi"+btnType);
                Config.fdTcCishu = 0;
//                //发送停止指令
                sendDataByBle(SendUtil.initSend("88"),"");
                //finish();
                break;
        }
    }

    /**
     * 屏幕右下角时间显示，每隔一秒执行一次
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewSave = LayoutInflater.from(Dtd10cZzCs2Activity.this).inflate(R.layout.dialog_save, null, false);
        TipsSave = new AlertDialog.Builder(Dtd10cZzCs2Activity.this, R.style.MyDialog).setView(viewSave).create();
    }
}