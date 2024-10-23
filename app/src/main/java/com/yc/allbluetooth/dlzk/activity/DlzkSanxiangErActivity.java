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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.math.BigInteger;
import java.util.Locale;

public class DlzkSanxiangErActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTime;
    private ImageView ivAb;
    private ImageView ivBc;
    private ImageView ivCa;
    private TextView tvU;
    private TextView tvI;
    private TextView tvFrq;
    private TextView tvXiangweijiao;
    private TextView tvP;
    private TextView tvCos;
    private TextView tvZk;
    private TextView tvXk;
    private TextView tvRk;
    private TextView tvLk;
    private TextView tvZkBfh;
    private TextView tvDZkBfh;
    private TextView tvSuoping;
    private TextView tvChongce;
    private TextView tvFanhui;

    private LinearLayout llAb;
    private LinearLayout llBc;
    private LinearLayout llCa;

    private String TAG = "DlzkSxErActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    String newMsgStr2 = "";
    int jinruI = 0;
    int sjcd;//数据长度
    String sjxz;//数据性质===>0:AB相实时数据，1:BC相实时数据，2:CA相实时数据，3:单相实时数据，4:零序实时数据，
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
    String crcJy;//Crc校验
    String allZkdyBfs;//总阻抗电压百分数
    String allDgAB;//总绕组电感（第三个页面使用）
    String allDgBc;
    String allDgCa;

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
                    if(StringUtils.isEquals(Config.ymType,"dlzkSxEr")){
                        Log.e(TAG+"==",msgStr);
//                    jinruI=jinruI+1;
//                    if(jinruI%2==0){
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){//IndexOfAndSubStr.isIndexOf(msgStr,"6677")
                            newMsgStr = msgStr;
                            Log.e(TAG+1,newMsgStr);
                        }else{
                            if(!IndexOfAndSubStr.isIndexOf(newMsgStr,msgStr)){
                                newMsgStr = newMsgStr+msgStr;
                                Log.e(TAG+2,newMsgStr);
                            }
                        }
                        // }
                        Log.e("=SxEr===========",newMsgStr);
                        //sjcd = ShiOrShiliu.parseInt(StringUtils.subStrStartToEnd(newMsgStr,4,6));

                        if(newMsgStr.length()==116){
                            Log.e(TAG,"===================================进入========================================");
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,112);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,112,116);
                            if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
                                Log.e(TAG,"===================================进入计算========================================");
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);//0:AB相实时数据，1:BC相实时数据，2:CA相实时数据，3:单相实时数据，4:零序实时数据，
                                dy = StringUtils.subStrStartToEnd(newMsgStr,16,24);
                                dl = StringUtils.subStrStartToEnd(newMsgStr,24,32);
                                pl = StringUtils.subStrStartToEnd(newMsgStr,32,40);
                                jd = StringUtils.subStrStartToEnd(newMsgStr,40,48);
                                gl = StringUtils.subStrStartToEnd(newMsgStr,48,56);
                                glys = StringUtils.subStrStartToEnd(newMsgStr,56,64);
                                zk = StringUtils.subStrStartToEnd(newMsgStr,64,72);
                                gk = StringUtils.subStrStartToEnd(newMsgStr,72,80);
                                dz = StringUtils.subStrStartToEnd(newMsgStr,80,88);
                                dg = StringUtils.subStrStartToEnd(newMsgStr,88,96);
                                zkdy = StringUtils.subStrStartToEnd(newMsgStr,96,104);
                                zkdyBfs = StringUtils.subStrStartToEnd(newMsgStr,104,112);

                                //电压
                                String dy2 = StringUtils.gaodiHuanBaoliuShierwei(dy);
                                tvU.setText(StringUtils.wuweiYouxiaoStr(dy2));
                                //电流
                                String dl2 = StringUtils.gaodiHuanBaoliuShierwei(dl);
                                tvI.setText(StringUtils.wuweiYouxiaoStr(dl2));
                                //频率
                                tvFrq.setText(StringUtils.gaodiHuanBaoliuWuwei(pl));
                                //角度
                                String jd2 = StringUtils.gaodiHuanBaoliuShierwei(jd);
                                tvXiangweijiao.setText(StringUtils.wuweiYouxiaoStr(jd2));
                                //功率
                                String gl2 = StringUtils.gaodiHuanBaoliuShierwei(gl);
                                tvP.setText(StringUtils.wuweiYouxiaoStr(gl2));
                                //功率因数
                                tvCos.setText(StringUtils.gaodiHuanBaoliuWuwei(glys));
                                //阻抗
                                String zk2 = StringUtils.gaodiHuanBaoliuShierwei(zk);
                                tvZk.setText(StringUtils.wuweiYouxiaoStr(zk2));
                                //感抗
                                String gk2 = StringUtils.gaodiHuanBaoliuShierwei(gk);
                                tvXk.setText(StringUtils.wuweiYouxiaoStr(gk2));
                                //电阻
                                tvRk.setText(StringUtils.gaodiHuanBaoliuWuwei(dz));
                                //电感
                                String dg2 = StringUtils.gaodiHuanBaoliuShierwei(dg);
                                tvLk.setText(StringUtils.wuweiYouxiaoStr(dg2));
                                //阻抗电压
                                String zkdy2 = StringUtils.gaodiHuanBaoliuShierwei(zkdy);
                                tvZkBfh.setText(StringUtils.wuweiYouxiaoStr(zkdy2));
                                //阻抗电压百分数
                                tvDZkBfh.setText(StringUtils.gaodiHuanBaoliuWuwei(zkdyBfs));

                                if(StringUtils.isEquals("00",sjxz)){//Ab
                                    ivAb.setImageResource(R.drawable.xiaoyuandianzi);
                                    Config.sjdyAb = dy2;
                                    Config.sjdlAb = dl2;
                                    Config.clxjAb = jd2;
                                    Config.ygglAb = gl2;
                                    Config.dlzkAb = zk2;
                                    Config.dlgkAb = gk2;
                                    Config.rzdgAb = dg2;
                                    Log.e("电感===ab",dg2);
                                    Config.zkdyAb = zkdy2;
                                }else if(StringUtils.isEquals("01",sjxz)){//Bc
                                    ivBc.setImageResource(R.drawable.xiaoyuandianzi);
                                    Config.sjdyBc = dy2;
                                    Config.sjdlBc = dl2;
                                    Config.clxjBc = jd2;
                                    Config.ygglBc = gl2;
                                    Config.dlzkBc = zk2;
                                    Config.dlgkBc = gk2;
                                    Config.rzdgBc = dg2;
                                    Log.e("电感===bc",dg2);
                                    Config.zkdyBc = zkdy2;
                                }else if(StringUtils.isEquals("02",sjxz)){//Ca
                                    ivCa.setImageResource(R.drawable.xiaoyuandianzi);
                                    Config.sjdyCa = dy2;
                                    Config.sjdlCa = dl2;
                                    Config.clxjCa = jd2;
                                    Config.ygglCa = gl2;
                                    Config.dlzkCa = zk2;
                                    Config.dlgkCa = gk2;
                                    Config.rzdgCa = dg2;
                                    Log.e("电感===ca",dg2);
                                    Config.zkdyCa = zkdy2;
                                }
//                                if(StringUtils.noEmpty(Config.sjdyAb)&&StringUtils.noEmpty(Config.sjdyBc)&&StringUtils.noEmpty(Config.sjdyCa)&&StringUtils.noEmpty(Config.sjdlAb)
//                                        &&StringUtils.noEmpty(Config.sjdlBc)&&StringUtils.noEmpty(Config.sjdlCa)&&StringUtils.noEmpty(Config.clxjAb)&&StringUtils.noEmpty(Config.clxjBc)
//                                        &&StringUtils.noEmpty(Config.clxjCa)&&StringUtils.noEmpty(Config.ygglAb)&&StringUtils.noEmpty(Config.ygglBc)&&StringUtils.noEmpty(Config.ygglCa)
//                                        &&StringUtils.noEmpty(Config.dlzkAb)&&StringUtils.noEmpty(Config.dlzkBc)&&StringUtils.noEmpty(Config.dlzkCa)&&StringUtils.noEmpty(Config.dlgkAb)
//                                        &&StringUtils.noEmpty(Config.dlgkBc)&&StringUtils.noEmpty(Config.dlgkCa)&&StringUtils.noEmpty(Config.rzdgAb)&&StringUtils.noEmpty(Config.rzdgBc)
//                                        &&StringUtils.noEmpty(Config.rzdgCa)&&StringUtils.noEmpty(Config.zkdyAb)&&StringUtils.noEmpty(Config.zkdyBc)&&StringUtils.noEmpty(Config.zkdyCa)){//判断是否三个电压值都不为空，跳转到总结果页面
//                                    finish();
//                                    startActivity(new Intent(DlzkSanxiangErActivity.this,DlzkSanxiangSanActivity.class));
//                                }
                                newMsgStr="";
                                Log.e(TAG,"===================================计算结束========================================");
                            }
                        }else if(newMsgStr.length()==44){
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,40);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,40,44);
                            if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                if(StringUtils.isEquals(sjxz,"55")){
                                    allZkdyBfs = StringUtils.subStrStartToEnd(newMsgStr,8,16);
                                    allDgAB = StringUtils.subStrStartToEnd(newMsgStr,16,24);
                                    allDgBc = StringUtils.subStrStartToEnd(newMsgStr,24,32);
                                    allDgCa = StringUtils.subStrStartToEnd(newMsgStr,32,40);
                                    Config.allZkdyBfs = StringUtils.gaodiHuanBaoliuWuwei(allZkdyBfs);
                                    Config.allZkdyBfs2 = StringUtils.gaodiHuanBaoliuShierwei(allZkdyBfs);
                                    //漏电感
                                    Config.rzdgAb = StringUtils.gaodiHuanBaoliuShierwei(allDgAB);
                                    Config.rzdgBc = StringUtils.gaodiHuanBaoliuShierwei(allDgBc);
                                    Config.rzdgCa = StringUtils.gaodiHuanBaoliuShierwei(allDgCa);
                                    XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
                                    //测量电抗
                                    double bdGkAbCheng = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshu(Config.rzdgAb),xiaoshuYunsuan.xiaoshu("314.15926"));
                                    Config.dlgkAb = StringUtils.shierweiYouxiaoStr(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(bdGkAbCheng+""),xiaoshuYunsuan.xiaoshu("1000"))+"");
                                    double bdGkBcCheng = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshu(Config.rzdgBc),xiaoshuYunsuan.xiaoshu("314.15926"));
                                    Config.dlgkBc = StringUtils.shierweiYouxiaoStr(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(bdGkBcCheng+""),xiaoshuYunsuan.xiaoshu("1000"))+"");
                                    double bdGkCaCheng = xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshu(Config.rzdgCa),xiaoshuYunsuan.xiaoshu("314.15926"));
                                    Config.dlgkCa = StringUtils.shierweiYouxiaoStr(xiaoshuYunsuan.xiaoshuChu6(xiaoshuYunsuan.xiaoshu(bdGkCaCheng+""),xiaoshuYunsuan.xiaoshu("1000"))+"");
                                    Log.e("========0",Config.rzdgAb);
                                    Log.e("========1",Config.dlgkAb);
                                    Log.e("========2",Config.dlgkBc);
                                    Log.e("========3",Config.dlgkCa);
                                    Log.e("========33",StringUtils.wuweiYouxiaoStr(bdGkAbCheng+""));
                                    finish();
                                    startActivity(new Intent(DlzkSanxiangErActivity.this,DlzkSanxiangSanActivity.class));
                                }
                            }
                        }
                    }
                    break;
                case 1000:
                    regainBleDataCount = 0;
                    bleFlag = false;
                    mHandler.removeCallbacks(checkConnetRunnable);

                    Toast.makeText(DlzkSanxiangErActivity.this, "超时请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case 1111:
                    bleConnectUtil.disConnect();
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

        setContentView(R.layout.activity_dlzk_sanxiang_er);
        Config.ymType = "dlzkSxEr";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
        new TimeThread().start();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(DlzkSanxiangErActivity.this);
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
        tvTime = findViewById(R.id.tvDlzkSanxiangErTime);
        ivAb = findViewById(R.id.ivDlzkSanxiangErAb);
        ivBc = findViewById(R.id.ivDlzkSanxiangErBc);
        ivCa = findViewById(R.id.ivDlzkSanxiangErCa);
        tvU = findViewById(R.id.tvDlzkSanxiangErU);
        tvI = findViewById(R.id.tvDlzkSanxiangErI);
        tvFrq = findViewById(R.id.tvDlzkSanxiangErFrq);
        tvXiangweijiao = findViewById(R.id.tvDlzkSanxiangErXiangweijiao);
        tvP = findViewById(R.id.tvDlzkSanxiangErP);
        tvCos = findViewById(R.id.tvDlzkSanxiangErCos);
        tvZk = findViewById(R.id.tvDlzkSanxiangErZk);
        tvXk = findViewById(R.id.tvDlzkSanxiangErXk);
        tvRk = findViewById(R.id.tvDlzkSanxiangErRk);
        tvLk = findViewById(R.id.tvDlzkSanxiangErLk);
        tvZkBfh = findViewById(R.id.tvDlzkSanxiangErZkBaifenhao);
        tvDZkBfh = findViewById(R.id.tvDlzkSanxiangErDZkBaifenhao);
        tvSuoping = findViewById(R.id.tvDlzkSanxiangErSuoping);
        tvChongce = findViewById(R.id.tvDlzkSanxiangErChongce);
        tvFanhui = findViewById(R.id.tvDlzkSanxiangErFanhui);
        llAb = findViewById(R.id.llDlzkSanxiangErAb);
        llBc = findViewById(R.id.llDlzkSanxiangErBc);
        llCa = findViewById(R.id.llDlzkSanxiangErCa);

        if(Config.Clms==0){
            llAb.setEnabled(false);
            llBc.setEnabled(false);
            llCa.setEnabled(false);
        }else{
            llAb.setEnabled(true);
            llBc.setEnabled(true);
            llCa.setEnabled(true);
        }
        tvSuoping.setOnClickListener(this);
        tvChongce.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        llAb.setOnClickListener(this);
        llBc.setOnClickListener(this);
        llCa.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDlzkSanxiangErSuoping://锁屏
                sendDataByBle(SendUtil.initSend("80"),"");
                tvSuoping.setEnabled(false);
                tvSuoping.setBackgroundResource(R.drawable.bac_an_yinying_hei_yuanjiao);
                break;
            case R.id.tvDlzkSanxiangErChongce://重测
                tvSuoping.setEnabled(true);
                tvSuoping.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
                sendDataByBle(SendUtil.initSend("81"),"");
                //finish();
                //startActivity(new Intent(DlzkSanxiangErActivity.this,DlzkSanxiangSanActivity.class));
                break;
            case R.id.tvDlzkSanxiangErFanhui://返回(停止88)
                sendDataByBle(SendUtil.initSend("88"),"");
                finish();
                break;
            case R.id.llDlzkSanxiangErAb:
                sendDataByBle(SendUtil.initSend("82"),"");
                break;
            case R.id.llDlzkSanxiangErBc:
                sendDataByBle(SendUtil.initSend("83"),"");
                break;
            case R.id.llDlzkSanxiangErCa:
                sendDataByBle(SendUtil.initSend("84"),"");
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
        Config.ymType = "dlzkSxEr";
//        if(bleConnectUtil!=null){
//            Log.e("-------","不为空");
//            bleConnectUtil.setCallback(blecallback);
//        }else{
//            bleConnectUtil = new BleConnectUtil(DlzkSanxiangErActivity.this);
//            bleConnectUtil.setCallback(blecallback);
//        }
//        bleConnectUtil = new BleConnectUtil(DlzkSanxiangErActivity.this);
//        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause()");
        //bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
        //bleConnectUtil.setCallback(null);
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
        bleConnectUtil.setCallback(null);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
//        bleConnectUtil.disConnect();
//        bleConnectUtil.setCallback(null);
//        mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }
}