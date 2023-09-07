package com.yc.allbluetooth.dlzk.activity;

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
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.Locale;

public class DlzkLingxuErActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvTime;
    private TextView tvUan;
    private TextView tvIan;
    private TextView tvFrq;
    private TextView tvXiangweijiao;
    private TextView tvP;
    private TextView tvQ;
    private TextView tvZ;
    private TextView tvX;
    private TextView tvR;
    private TextView tvL;
    private TextView tvCos;
    private TextView tvSuoping;
    private TextView tvBaocun;
    private TextView tvChongce;
    private TextView tvDayin;
    private TextView tvFanhui;

    View viewSave;
    View viewDy;
    AlertDialog TipsSave;
    AlertDialog TipsDayin;

    int zhilingCishu = 0;//记录、判断多少次后自动锁屏

    private String TAG = "DlzkDxErActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    int jinruI = 0;

    //int sjcd;//数据长度
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
    String zkdy;//阻抗电压(无功功率：Q)
    String zkdyBfs;//阻抗电压百分数
    String crcJy;//Crc校验

    String tfxx;

    int btnType = 0;//保存1；打印2；

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
                    if(StringUtils.isEquals(Config.ymType,"dlzkLxEr")){

                    }
                    String msgStr = msg.obj.toString();
                    Log.e(TAG,msgStr);
//                    jinruI=jinruI+1;
//                    if(jinruI%2==0) {
                        if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                            newMsgStr = msgStr;
                            Log.e(TAG, newMsgStr);
                        } else {
                            if(!IndexOfAndSubStr.isIndexOf(newMsgStr,msgStr)){
                                newMsgStr = newMsgStr+msgStr;
                                Log.e(TAG+2,newMsgStr);
                            }
//                            newMsgStr = newMsgStr + msgStr;
//                            //可以
//                            Log.e(TAG + "可以", newMsgStr);
                        }
 //                   }
                    //sjcd = ShiOrShiliu.parseInt(StringUtils.subStrStartToEnd(newMsgStr,4,6));
                    if(newMsgStr.length()==16){
                        String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,12);
                        byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                        crcJy = StringUtils.subStrStartToEnd(newMsgStr,12,16);
                        if(newMsgStr.length()==16 && CrcUtil.CrcIsOk(bytesSx,crcJy)){
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);//6:数据性质，突发信息
                            tfxx = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                            if(StringUtils.isEquals("06",sjxz)){
                                Log.e(TAG,"突发信息06");
                                if(StringUtils.isEquals("02",tfxx)){
                                    Log.e(TAG,"保存");
                                    //TipsSave.show();
                                    Toast.makeText(DlzkLingxuErActivity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
                                }else if(StringUtils.isEquals("07",tfxx)){
                                    Log.e(TAG,"打印");
                                    //TipsDayin.show();
                                    Toast.makeText(DlzkLingxuErActivity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
                                }else if(StringUtils.isEquals("11", ShiOrShiliu.parseInt(tfxx).toString())){
                                    Log.e(TAG,"保存完成");
                                    TipsSave.cancel();
                                }else if(StringUtils.isEquals("12", ShiOrShiliu.parseInt(tfxx).toString())){
                                    Log.e(TAG,"打印完成");
                                    TipsDayin.cancel();
                                }else if(ShiOrShiliu.parseInt(tfxx)>11){//打印完毕、保存完毕
                                    if(btnType==1){//保存
                                        Log.e(TAG,"保存完毕");
                                        TipsSave.cancel();
                                    }else if(btnType==2){//打印
                                        Log.e(TAG,"打印完毕");
                                        TipsDayin.cancel();
                                    }
                                }
                            }
                        }
                    }else if(newMsgStr.length()==116){
                        String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,112);
                        byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                        crcJy = StringUtils.subStrStartToEnd(newMsgStr,112,116);
                        if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
                            zhilingCishu = zhilingCishu +1;
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
                            String dy2 = StringUtils.gaodiHuanBaoliuWuwei(dy);
                            tvUan.setText(dy2);
                            //电流
                            String dl2 = StringUtils.gaodiHuanBaoliuWuwei(dl);
                            tvIan.setText(dl2);
                            //频率
                            String pl2 = StringUtils.gaodiHuanBaoliuWuwei(pl);
                            tvFrq.setText(pl2);
                            //角度
                            String jd2 = StringUtils.gaodiHuanBaoliuWuwei(jd);
                            tvXiangweijiao.setText(jd2);
                            //功率
                            String gl2 = StringUtils.gaodiHuanBaoliuWuwei(gl);
                            tvP.setText(gl2);
                            //功率因数
                            String glys2 = StringUtils.gaodiHuanBaoliuWuwei(glys);
                            tvCos.setText(glys2);
                            //阻抗
                            String zk2 = StringUtils.gaodiHuanBaoliuWuwei(zk);
                            tvZ.setText(zk2);
                            //感抗
                            String gk2 = StringUtils.gaodiHuanBaoliuWuwei(gk);
                            tvX.setText(gk2);
                            //电阻
                            String dz2 = StringUtils.gaodiHuanBaoliuWuwei(dz);
                            tvR.setText(dz2);
                            //电感
                            String dg2 = StringUtils.gaodiHuanBaoliuWuwei(dg);
                            tvL.setText(dg2);
                            //阻抗电压(Q)
                            String zkdy2 = StringUtils.gaodiHuanBaoliuWuwei(zkdy);
                            tvQ.setText(zkdy2);
//                            //阻抗电压百分数
//                            float zkdyBfhF = 0;
//                            String zkdyBfhHl = HexUtil.reverseHex(zkdyBfs);
//                            try {
//                                zkdyBfhF = Float.intBitsToFloat((int) HexUtil.parseLong(zkdyBfhHl, 16));
//                            } catch (HexUtil.NumberFormatException e) {
//                                e.printStackTrace();
//                            }
//                            String zkdyBfh2 = StringUtils.wuweiYouxiaoStr(zkdyBfhF + "");
//                            tvDZkBfh.setText(zkdyBfh2);
                            newMsgStr="";
                            if(zhilingCishu>10&&zhilingCishu%10==6){//16次锁屏，重测加10次===26次
                                tvSuoping.setEnabled(false);
                                tvSuoping.setBackgroundResource(R.drawable.bac_an_yinying_hei_yuanjiao);
                            }
                        }
                    }
                    break;
                case 1000:
                    regainBleDataCount = 0;
                    bleFlag = false;
                    mHandler.removeCallbacks(checkConnetRunnable);

                    Toast.makeText(DlzkLingxuErActivity.this, "超时请重试!", Toast.LENGTH_SHORT).show();
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

        setContentView(R.layout.activity_dlzk_lingxu_er);
        Config.ymType = "dlzkLxEr";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
        new TimeThread().start();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(DlzkLingxuErActivity.this);
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
        tvTime = findViewById(R.id.tvDlzkLingxuErTime);
        tvUan = findViewById(R.id.tvDlzkLingxuErUan);
        tvIan = findViewById(R.id.tvDlzkLingxuErIan);
        tvFrq = findViewById(R.id.tvDlzkLingxuErFrq);
        tvXiangweijiao = findViewById(R.id.tvDlzkLingxuErXiangweijiao);
        tvP = findViewById(R.id.tvDlzkLingxuErP);
        tvQ = findViewById(R.id.tvDlzkLingxuErQ);
        tvCos = findViewById(R.id.tvDlzkLingxuErCos);
        tvZ = findViewById(R.id.tvDlzkLingxuErZ);
        tvX = findViewById(R.id.tvDlzkLingxuErX);
        tvR = findViewById(R.id.tvDlzkLingxuErR);
        tvL = findViewById(R.id.tvDlzkLingxuErL);
        tvCos = findViewById(R.id.tvDlzkLingxuErCos);

        tvSuoping = findViewById(R.id.tvDlzkLingxuErSuoping);
        tvBaocun = findViewById(R.id.tvDlzkLingxuErBaocun);
        tvChongce = findViewById(R.id.tvDlzkLingxuErChongce);
        tvDayin = findViewById(R.id.tvDlzkLingxuErDayin);
        tvFanhui = findViewById(R.id.tvDlzkLingxuErFanhui);
        tvSuoping.setOnClickListener(this);
        tvBaocun.setOnClickListener(this);
        tvChongce.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);

        viewDy = LayoutInflater.from(this).inflate(R.layout.dialog_dayin, null, false);
        TipsDayin = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewDy).create();
        viewSave = LayoutInflater.from(this).inflate(R.layout.dialog_save, null, false);
        TipsSave = new AlertDialog.Builder(this, R.style.MyDialog).setView(viewSave).create();

        TipsDayin.setCanceledOnTouchOutside(false);
        TipsSave.setCanceledOnTouchOutside(false);
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
            case R.id.tvDlzkLingxuErSuoping://锁屏
                sendDataByBle(SendUtil.initSend("80"),"");
                tvSuoping.setEnabled(false);
                tvSuoping.setBackgroundResource(R.drawable.bac_an_yinying_hei_yuanjiao);
                break;
            case R.id.tvDlzkLingxuErBaocun://保存
                btnType = 1;
                sendDataByBle(SendUtil.initSend("85"),"");
                break;
            case R.id.tvDlzkLingxuErChongce://重测
                tvSuoping.setEnabled(true);
                tvSuoping.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
                sendDataByBle(SendUtil.initSend("81"),"");
                break;
            case R.id.tvDlzkLingxuErDayin://打印
                btnType = 2;
                sendDataByBle(SendUtil.initSend("86"),"");
                break;
            case R.id.tvDlzkLingxuErFanhui://返回
                sendDataByBle(SendUtil.initSend("88"),"");
                finish();
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
        Config.ymType = "dlzkLxEr";
//        bleConnectUtil = new BleConnectUtil(DlzkLingxuErActivity.this);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }
}