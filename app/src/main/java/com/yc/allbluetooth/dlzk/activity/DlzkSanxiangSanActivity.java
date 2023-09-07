package com.yc.allbluetooth.dlzk.activity;



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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

public class DlzkSanxiangSanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTime;
    private TextView tvSjdyAb;
    private TextView tvSjdyBc;
    private TextView tvSjdyCa;
    private TextView tvSjdlAb;
    private TextView tvSjdlBc;
    private TextView tvSjdlCa;
    private TextView tvClxjAb;
    private TextView tvClxjBc;
    private TextView tvClxjCa;
    private TextView tvYgglAb;
    private TextView tvYgglBc;
    private TextView tvYgglCa;
    private TextView tvDlzkAb;
    private TextView tvDlzkBc;
    private TextView tvDlzkCa;
    private TextView tvDlgkAb;
    private TextView tvDlgkBc;
    private TextView tvDlgkCa;
    private TextView tvRzdgAb;
    private TextView tvRzdgBc;
    private TextView tvRzdgCa;
    private TextView tvZkdyAb;
    private TextView tvZkdyBc;
    private TextView tvZkdyCa;
    private TextView tvZkdyZk;
    private TextView tvZkwcDzk;
    private TextView tvBaocun;
    private TextView tvDayin;
    private TextView tvFanhui;
    private TextView tvHxpcClzk;
    private TextView tvHxpcCldk;
    private TextView tvHxpcLdg;
    private TextView tvHxpcZkdy;


    View viewSave;
    View viewDy;
    AlertDialog TipsSave;
    AlertDialog TipsDayin;

    private String TAG = "DlzkSxSanActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    int sjcd = 0;
    String sjxz;
    String tfxx;
    String crcJy;

    int btnType = 0;//保存1；打印2；

    String dy;//电压
    String dl;//电流
    String pl;//频率
    String jd;//角度
    String gl;//功率
    String glys;//功率因数
    String zk;//阻抗
    String gk;//感抗
    String dz;//电阻
    String dg;//电感
    String zkdy;//阻抗电压
    String zkdyBfs;//阻抗电压百分数


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
                    tvTime.setText(GetTime.getTime2());//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    String msgStr = msg.obj.toString();
                    if(StringUtils.isEquals(Config.ymType,"dlzkSxSan")){
                        Log.e(TAG,msgStr);
//                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
//                            newMsgStr = msgStr;
//                            Log.e(TAG,newMsgStr);
//                        }else{
//                            newMsgStr = newMsgStr+msgStr;
//                            //可以
//                            Log.e(TAG+"可以",newMsgStr);
//                        }
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){//IndexOfAndSubStr.isIndexOf(msgStr,"6677")
                            newMsgStr = msgStr;
                            Log.e(TAG+1,newMsgStr);
                        }else{
                            if(!IndexOfAndSubStr.isIndexOf(newMsgStr,msgStr)){
                                newMsgStr = newMsgStr+msgStr;
                                Log.e(TAG+2,newMsgStr);
                            }
                        }
                        //sjcd = ShiOrShiliu.parseInt(StringUtils.subStrStartToEnd(newMsgStr,4,6));
                        if(newMsgStr.length()==112){
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,12);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,12,16);
                            if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);//6:数据性质，突发信息
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                                if(StringUtils.isEquals("06",sjxz)){
                                    Log.e(TAG,"突发信息06");
                                    if(StringUtils.isEquals("02",tfxx)){
                                        Log.e(TAG,"保存");
                                        //TipsSave.show();
                                        Toast.makeText(DlzkSanxiangSanActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                    }else if(StringUtils.isEquals("07",tfxx)){
                                        Log.e(TAG,"打印");
                                        //TipsDayin.show();
                                        Toast.makeText(DlzkSanxiangSanActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                    }else if(StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())){
                                        Log.e(TAG,"保存完成");
                                        //TipsSave.cancel();
                                    }else if(StringUtils.isEquals("12", ShiOrShiliu.parseInt(tfxx).toString())){
                                        Log.e(TAG,"打印完成");
                                        //TipsDayin.cancel();
                                    }else if(ShiOrShiliu.parseInt(tfxx)>11){//打印完毕、保存完毕
                                        if(btnType==1){//保存
                                            Log.e(TAG,"保存完毕");
                                            // TipsSave.cancel();
                                        }else if(btnType==2){//打印
                                            Log.e(TAG,"打印完毕");
                                            // TipsDayin.cancel();
                                        }
                                    }
                                }
                            }
                        }

//                        if(newMsgStr.length()==116){
//                            Log.e(TAG,"===================================进入========================================");
//                           crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,112);
//                            bytesSx = new BigInteger(crcAll, 16).toByteArray();
//                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,112,116);
//                            if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
//                                Log.e(TAG,"===================================进入计算========================================");
//                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);//0:AB相实时数据，1:BC相实时数据，2:CA相实时数据，3:单相实时数据，4:零序实时数据，
//                                dy = StringUtils.subStrStartToEnd(newMsgStr,16,24);
//                                dl = StringUtils.subStrStartToEnd(newMsgStr,24,32);
//                                pl = StringUtils.subStrStartToEnd(newMsgStr,32,40);
//                                jd = StringUtils.subStrStartToEnd(newMsgStr,40,48);
//                                gl = StringUtils.subStrStartToEnd(newMsgStr,48,56);
//                                glys = StringUtils.subStrStartToEnd(newMsgStr,56,64);
//                                zk = StringUtils.subStrStartToEnd(newMsgStr,64,72);
//                                gk = StringUtils.subStrStartToEnd(newMsgStr,72,80);
//                                dz = StringUtils.subStrStartToEnd(newMsgStr,80,88);
//                                dg = StringUtils.subStrStartToEnd(newMsgStr,88,96);
//                                zkdy = StringUtils.subStrStartToEnd(newMsgStr,96,104);
//                                zkdyBfs = StringUtils.subStrStartToEnd(newMsgStr,104,112);
//
//                                //电压
//                                String dy2 = StringUtils.gaodiHuanBaoliuWuwei(dy);
//                                //电流
//                                String dl2 = StringUtils.gaodiHuanBaoliuWuwei(dl);
//                                //角度
//                                String jd2 = StringUtils.gaodiHuanBaoliuWuwei(jd);
//                                //功率
//                                String gl2 = StringUtils.gaodiHuanBaoliuWuwei(gl);
//                                //阻抗
//                                String zk2 = StringUtils.gaodiHuanBaoliuWuwei(zk);
//                                //感抗
//                                String gk2 = StringUtils.gaodiHuanBaoliuWuwei(gk);
//                                //电感
//                                String dg2 = StringUtils.gaodiHuanBaoliuWuwei(dg);
//                                //阻抗电压
//                                String zkdy2 = StringUtils.gaodiHuanBaoliuWuwei(zkdy);
//                                //阻抗电压百分数
//                                String zkdyBfs2 = StringUtils.gaodiHuanBaoliuWuwei(zkdyBfs);
//
//                                if(StringUtils.isEquals("00",sjxz)){//Ab
//                                    tvSjdyAb.setText(dy2);
//                                    tvSjdlAb.setText(dl2);
//                                    tvClxjAb.setText(jd2);
//                                    tvYgglAb.setText(gl2);
//                                    tvDlzkAb.setText(zk2);
//                                    tvDlgkAb.setText(gk2);
//                                    tvRzdgAb.setText(dg2);
//                                    tvZkdyAb.setText(zkdy2);
//                                }else if(StringUtils.isEquals("01",sjxz)){//Bc
//                                    tvSjdyBc.setText(dy2);
//                                    tvSjdlBc.setText(dl2);
//                                    tvClxjBc.setText(jd2);
//                                    tvYgglBc.setText(gl2);
//                                    tvDlzkBc.setText(zk2);
//                                    tvDlgkBc.setText(gk2);
//                                    tvRzdgBc.setText(dg2);
//                                    tvZkdyBc.setText(zkdy2);
//                                }else if(StringUtils.isEquals("02",sjxz)){//Ca
//                                    tvSjdyCa.setText(dy2);
//                                    tvSjdlCa.setText(dl2);
//                                    tvClxjCa.setText(jd2);
//                                    tvYgglCa.setText(gl2);
//                                    tvDlzkCa.setText(zk2);
//                                    tvDlgkCa.setText(gk2);
//                                    tvRzdgCa.setText(dg2);
//                                    tvZkdyCa.setText(zkdy2);
//                                }
//                                XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
//                                //三相之和取平均数
//                                String zkdyZkBfsStr = StringUtils.wuweiYouxiaoStr(xiaoshuYunsuan.xiaoshuChu(xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshu(tvZkdyAb.getText().toString()),
//                                        xiaoshuYunsuan.xiaoshu(tvZkdyBc.getText().toString())),xiaoshuYunsuan.xiaoshu(tvZkdyCa.getText().toString())),xiaoshuYunsuan.xiaoshu("3")) + "");
//                                tvZkdyZk.setText(zkdyZkBfsStr);//阻抗电压Zk%
//                                if(StringUtils.noEmpty(Config.mpzk)){//阻抗误差DZk%
//                                    tvZkwcDzk.setText(xiaoshuYunsuan.xiaoshuJian(xiaoshuYunsuan.xiaoshu(zkdyZkBfsStr),xiaoshuYunsuan.xiaoshu(Config.mpzk))+"");
//                                }
//                                newMsgStr="";
//                                Log.e(TAG,"===================================计算结束========================================");
//                            }
//
//                        }
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

        setContentView(R.layout.activity_dlzk_sanxiang_san);
        Config.ymType = "dlzkSxSan";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
        new TimeThread().start();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(DlzkSanxiangSanActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }

    }
    public void initView(){
        tvTime = findViewById(R.id.tvDlzkSanxiangSanTime);
        tvSjdyAb = findViewById(R.id.tvDlzkSanxiangSanShijiadianyaAb);
        tvSjdyBc = findViewById(R.id.tvDlzkSanxiangSanShijiadianyaBc);
        tvSjdyCa = findViewById(R.id.tvDlzkSanxiangSanShijiadianyaCa);
        tvSjdlAb = findViewById(R.id.tvDlzkSanxiangSanShijiadianliuAb);
        tvSjdlBc = findViewById(R.id.tvDlzkSanxiangSanShijiadianliuBc);
        tvSjdlCa = findViewById(R.id.tvDlzkSanxiangSanShijiadianliuCa);
        tvClxjAb = findViewById(R.id.tvDlzkSanxiangSanCeliangxiangjiaoAb);
        tvClxjBc = findViewById(R.id.tvDlzkSanxiangSanCeliangxiangjiaoBc);
        tvClxjCa = findViewById(R.id.tvDlzkSanxiangSanCeliangxiangjiaoCa);
        tvYgglAb = findViewById(R.id.tvDlzkSanxiangSanYougonggonglvAb);
        tvYgglBc = findViewById(R.id.tvDlzkSanxiangSanYougonggonglvBc);
        tvYgglCa = findViewById(R.id.tvDlzkSanxiangSanYougonggonglvCa);
        tvDlzkAb = findViewById(R.id.tvDlzkSanxiangSanDuanluzukangAb);
        tvDlzkBc = findViewById(R.id.tvDlzkSanxiangSanDuanluzukangBc);
        tvDlzkCa = findViewById(R.id.tvDlzkSanxiangSanDuanluzukangCa);
        tvDlgkAb = findViewById(R.id.tvDlzkSanxiangSanDuanlugankangAb);
        tvDlgkBc = findViewById(R.id.tvDlzkSanxiangSanDuanlugankangBc);
        tvDlgkCa = findViewById(R.id.tvDlzkSanxiangSanDuanlugankangCa);
        tvRzdgAb = findViewById(R.id.tvDlzkSanxiangSanRaozudianganAb);
        tvRzdgBc = findViewById(R.id.tvDlzkSanxiangSanRaozudianganBc);
        tvRzdgCa = findViewById(R.id.tvDlzkSanxiangSanRaozudianganCa);
        tvZkdyAb = findViewById(R.id.tvDlzkSanxiangSanZukangdianyaAb);
        tvZkdyBc = findViewById(R.id.tvDlzkSanxiangSanZukangdianyaBc);
        tvZkdyCa = findViewById(R.id.tvDlzkSanxiangSanZukangdianyaCa);
        tvZkdyZk = findViewById(R.id.tvDlzkSanxiangSanZukangdianyaZk);
        tvZkwcDzk = findViewById(R.id.tvDlzkSanxiangSanZukangwuchaDZk);
        tvBaocun = findViewById(R.id.tvDlzkSanxiangSanBaocun);
        tvDayin = findViewById(R.id.tvDlzkSanxiangSanDayin);
        tvFanhui = findViewById(R.id.tvDlzkSanxiangSanFanhui);

        tvHxpcClzk = findViewById(R.id.tvDlzkSanxiangSanHxpcClzk);
        tvHxpcCldk = findViewById(R.id.tvDlzkSanxiangSanHxpcCldk);
        tvHxpcLdg = findViewById(R.id.tvDlzkSanxiangSanHxpcLdg);
        tvHxpcZkdy = findViewById(R.id.tvDlzkSanxiangSanHxpcZkdy);

        tvBaocun.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);

        viewDy = LayoutInflater.from(this).inflate(R.layout.dialog_dayin, null, false);
        TipsDayin = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewDy).create();
        viewSave = LayoutInflater.from(this).inflate(R.layout.dialog_save, null, false);
        TipsSave = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewSave).create();

        TipsDayin.setCanceledOnTouchOutside(false);
        TipsSave.setCanceledOnTouchOutside(false);

        tvSjdyAb.setText(StringUtils.wuweiYouxiaoStr(Config.sjdyAb));
        tvSjdlAb.setText(StringUtils.wuweiYouxiaoStr(Config.sjdlAb));
        tvClxjAb.setText(StringUtils.wuweiYouxiaoStr(Config.clxjAb));
        tvYgglAb.setText(StringUtils.wuweiYouxiaoStr(Config.ygglAb));
        tvDlzkAb.setText(StringUtils.wuweiYouxiaoStr(Config.dlzkAb));
        tvDlgkAb.setText(StringUtils.wuweiYouxiaoStr(Config.dlgkAb));
        tvRzdgAb.setText(StringUtils.wuweiYouxiaoStr(Config.rzdgAb));
        tvZkdyAb.setText(StringUtils.wuweiYouxiaoStr(Config.zkdyAb));

        tvSjdyBc.setText(StringUtils.wuweiYouxiaoStr(Config.sjdyBc));
        tvSjdlBc.setText(StringUtils.wuweiYouxiaoStr(Config.sjdlBc));
        tvClxjBc.setText(StringUtils.wuweiYouxiaoStr(Config.clxjBc));
        tvYgglBc.setText(StringUtils.wuweiYouxiaoStr(Config.ygglBc));
        tvDlzkBc.setText(StringUtils.wuweiYouxiaoStr(Config.dlzkBc));
        tvDlgkBc.setText(StringUtils.wuweiYouxiaoStr(Config.dlgkBc));
        tvRzdgBc.setText(StringUtils.wuweiYouxiaoStr(Config.rzdgBc));
        tvZkdyBc.setText(StringUtils.wuweiYouxiaoStr(Config.zkdyBc));

        tvSjdyCa.setText(StringUtils.wuweiYouxiaoStr(Config.sjdyCa));
        tvSjdlCa.setText(StringUtils.wuweiYouxiaoStr(Config.sjdlCa));
        tvClxjCa.setText(StringUtils.wuweiYouxiaoStr(Config.clxjCa));
        tvYgglCa.setText(StringUtils.wuweiYouxiaoStr(Config.ygglCa));
        tvDlzkCa.setText(StringUtils.wuweiYouxiaoStr(Config.dlzkCa));
        tvDlgkCa.setText(StringUtils.wuweiYouxiaoStr(Config.dlgkCa));
        tvRzdgCa.setText(StringUtils.wuweiYouxiaoStr(Config.rzdgCa));
        tvZkdyCa.setText(StringUtils.wuweiYouxiaoStr(Config.zkdyCa));

        XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
        //三相之和取平均数
//        String zkdyZkBfsStr = StringUtils.wuweiYouxiaoStr(xiaoshuYunsuan.xiaoshuChu(xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshu(Config.zkdyAb),
//                xiaoshuYunsuan.xiaoshu(Config.zkdyBc)),xiaoshuYunsuan.xiaoshu(Config.zkdyCa)),xiaoshuYunsuan.xiaoshu("3")) + "");
        tvZkdyZk.setText(Config.allZkdyBfs);//阻抗电压Zk%
        if(StringUtils.noEmpty(Config.mpzk)){//阻抗误差DZk%
            tvZkwcDzk.setText(StringUtils.wuweiYouxiaoStr(xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshuJian(xiaoshuYunsuan.xiaoshu(Config.allZkdyBfs2),xiaoshuYunsuan.xiaoshu(Config.mpzk)),xiaoshuYunsuan.xiaoshu(Config.mpzk)),xiaoshuYunsuan.xiaoshu("100"))+""));
        }
        //测量阻抗(短路阻抗)
        BigDecimal bdClzkAb = xiaoshuYunsuan.xiaoshu(Config.dlzkAb);
        BigDecimal bdClzkBc = xiaoshuYunsuan.xiaoshu(Config.dlzkBc);
        BigDecimal bdClzkCa = xiaoshuYunsuan.xiaoshu(Config.dlzkCa);
        BigDecimal bdClzkCha = xiaoshuYunsuan.bijiaoDaxiaoCha(bdClzkAb,bdClzkBc,bdClzkCa);
        BigDecimal bdClzkHe = xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshuJia(bdClzkAb,bdClzkBc),bdClzkCa);
        if(bdClzkHe.compareTo(BigDecimal.ZERO)!=0){
            double bdHxpcClzk = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(xiaoshuYunsuan.xiaoshuCheng(bdClzkCha,xiaoshuYunsuan.xiaoshu("3"))+""),bdClzkHe),xiaoshuYunsuan.xiaoshu("100"));
            tvHxpcClzk.setText(StringUtils.wuweiYouxiaoStr(bdHxpcClzk+""));
        }
        //测量电抗(短路感抗)
        BigDecimal bdCldkAb = xiaoshuYunsuan.xiaoshu(Config.dlgkAb);
        BigDecimal bdCldkBc = xiaoshuYunsuan.xiaoshu(Config.dlgkBc);
        BigDecimal bdCldkCa = xiaoshuYunsuan.xiaoshu(Config.dlgkCa);
        BigDecimal bdCldkCha = xiaoshuYunsuan.bijiaoDaxiaoCha(bdCldkAb,bdCldkBc,bdCldkCa);
        Log.e("===cha",bdCldkCha+"");
        BigDecimal bdCldkHe = xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshuJia(bdCldkAb,bdCldkBc),bdCldkCa);
        Log.e("===he",bdCldkHe+"");
        if(bdCldkHe.compareTo(BigDecimal.ZERO)!=0){
            double bdHxpcCldk = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(xiaoshuYunsuan.xiaoshuCheng(bdCldkCha,xiaoshuYunsuan.xiaoshu("3"))+""),bdCldkHe),xiaoshuYunsuan.xiaoshu("100"));
            tvHxpcCldk.setText(StringUtils.wuweiYouxiaoStr(bdHxpcCldk+""));
        }
        //漏电感(绕组电感)
        BigDecimal bdLdgAb = xiaoshuYunsuan.xiaoshu(Config.rzdgAb);
        BigDecimal bdLdgBc = xiaoshuYunsuan.xiaoshu(Config.rzdgBc);
        BigDecimal bdLdgCa = xiaoshuYunsuan.xiaoshu(Config.rzdgCa);
        BigDecimal bdLdgCha = xiaoshuYunsuan.bijiaoDaxiaoCha(bdLdgAb,bdLdgBc,bdLdgCa);
        BigDecimal bdLdgHe = xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshuJia(bdLdgAb,bdLdgBc),bdLdgCa);
        if(bdLdgHe.compareTo(BigDecimal.ZERO)!=0){
            double bdHxpcLdg = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(xiaoshuYunsuan.xiaoshuCheng(bdLdgCha,xiaoshuYunsuan.xiaoshu("3"))+""),bdLdgHe),xiaoshuYunsuan.xiaoshu("100"));
            tvHxpcLdg.setText(StringUtils.wuweiYouxiaoStr(bdHxpcLdg+""));
        }
        //阻抗电压(阻抗电压)
        BigDecimal bdZkdyAb = xiaoshuYunsuan.xiaoshu(Config.zkdyAb);
        BigDecimal bdZkdyBc = xiaoshuYunsuan.xiaoshu(Config.zkdyBc);
        BigDecimal bdZkdyCa = xiaoshuYunsuan.xiaoshu(Config.zkdyCa);
        BigDecimal bdZkdyCha = xiaoshuYunsuan.bijiaoDaxiaoCha(bdZkdyAb,bdZkdyBc,bdZkdyCa);
        BigDecimal bdZkdyHe = xiaoshuYunsuan.xiaoshuJia(xiaoshuYunsuan.xiaoshuJia(bdZkdyAb,bdZkdyBc),bdZkdyCa);
        if(bdZkdyHe.compareTo(BigDecimal.ZERO)!=0){
            double bdHxpcZkdy = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(xiaoshuYunsuan.xiaoshuCheng(bdZkdyCha,xiaoshuYunsuan.xiaoshu("3"))+""),bdZkdyHe),xiaoshuYunsuan.xiaoshu("100"));
            tvHxpcZkdy.setText(StringUtils.wuweiYouxiaoStr(bdHxpcZkdy+""));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDlzkSanxiangSanBaocun:
                btnType = 1;
                sendDataByBle(SendUtil.initSend("85"),"");
                Toast.makeText(DlzkSanxiangSanActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvDlzkSanxiangSanDayin:
                btnType = 2;
                sendDataByBle(SendUtil.initSend("86"),"");
                Toast.makeText(DlzkSanxiangSanActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvDlzkSanxiangSanFanhui://返回（停止88）
                sendDataByBle(SendUtil.initSend("88"),"");
                finish();
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
        Config.ymType = "dlzkSxSan";
//        bleConnectUtil = new BleConnectUtil(DlzkSanxiangSanActivity.this);
//        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");
        Config.sjdyAb="";
        Config.sjdlAb="";
        Config.clxjAb="";
        Config.ygglAb="";
        Config.dlzkAb="";
        Config.rzdgAb="";
        Config.dlgkAb="";
        Config.zkdyAb="";
        Config.sjdyBc="";
        Config.sjdlBc="";
        Config.clxjBc="";
        Config.ygglBc="";
        Config.dlzkBc="";
        Config.rzdgBc="";
        Config.dlgkBc="";
        Config.zkdyBc="";
        Config.sjdyCa="";
        Config.sjdlCa="";
        Config.clxjCa="";
        Config.ygglCa="";
        Config.dlzkCa="";
        Config.rzdgCa="";
        Config.dlgkCa="";
        Config.zkdyCa="";

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