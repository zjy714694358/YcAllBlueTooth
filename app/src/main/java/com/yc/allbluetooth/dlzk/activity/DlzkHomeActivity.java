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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.Locale;

public class DlzkHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTime;
    private LinearLayout llSanxiang;
    private LinearLayout llDanxiang;
    private LinearLayout llLingxu;
    private LinearLayout llShujuchuli;
    private LinearLayout llXitongshezhi;

    private String TAG = "DlzkHomeActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";

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
                    //tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    tvTime.setText(GetTime.getTime2());//年-月-日 时：分：秒
                    break;
//                case Config.BLUETOOTH_GETDATA:
//                    String msgStr = msg.obj.toString();
//                    Log.e(TAG,msgStr);
//                    if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
//                        newMsgStr = msgStr;
//                        Log.e(TAG,newMsgStr);
//                    }else{
//                        newMsgStr = newMsgStr+msgStr;
//                        //可以
//                        Log.e(TAG+"可以",newMsgStr);
//                    }
//                    break;
//                case 1000:
//                    regainBleDataCount = 0;
//                    bleFlag = false;
//                    mHandler.removeCallbacks(checkConnetRunnable);
//
//                    Toast.makeText(DlzkHomeActivity.this, "超时请重试!", Toast.LENGTH_SHORT).show();
//                    break;
//                case 1111:
//                    bleConnectUtil.disConnect();
//                    break;
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

        setContentView(R.layout.activity_dlzk_home);
        Config.ymType = "dlzkHome";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
        new TimeThread().start();

    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(DlzkHomeActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
        tbTime();
    }
    public void initView(){
        tvTime = findViewById(R.id.tvDlzkHomeTime);
        llSanxiang = findViewById(R.id.llDlzkHomeSanxiangDlzk);
        llDanxiang = findViewById(R.id.llDlzkHomeDanxiangDlzk);
        llLingxu = findViewById(R.id.llDlzkHomeLingxuzk);
        llShujuchuli = findViewById(R.id.llDlzkHomeShujuchuli);
        llXitongshezhi = findViewById(R.id.llDlzkHomeXitongshezhi);

        llSanxiang.setOnClickListener(this);
        llDanxiang.setOnClickListener(this);
        llLingxu.setOnClickListener(this);
        llShujuchuli.setOnClickListener(this);
        llXitongshezhi.setOnClickListener(this);
    }

    /**
     * 同步时间
     */
    public void tbTime(){
        String timeStr = GetTime.getTime(3);
        String nian = StringUtils.subStrStartToEnd(timeStr,0,2);
        String yue = StringUtils.subStrStartToEnd(timeStr,2,4);
        String ri = StringUtils.subStrStartToEnd(timeStr,4,6);
        String shi = StringUtils.subStrStartToEnd(timeStr,8,10);
        String fen = StringUtils.subStrStartToEnd(timeStr,10,12);
        String miao = StringUtils.subStrStartToEnd(timeStr,12,14);
        sendDataByBle(SendUtil.shijianSend("87",StringUtils.bulingXiaoShiliu(nian)+StringUtils.bulingXiaoShiliu(yue)+
                StringUtils.bulingXiaoShiliu(ri)+StringUtils.bulingXiaoShiliu(shi)+StringUtils.bulingXiaoShiliu(fen)+
                StringUtils.bulingXiaoShiliu(miao)),"");
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
            case R.id.llDlzkHomeSanxiangDlzk://三相
                sendDataByBle(SendUtil.initSend("6a"),"");
                startActivity(new Intent(DlzkHomeActivity.this,DlzkSanxiangYiActivity.class));
                break;
            case R.id.llDlzkHomeDanxiangDlzk://单相
                sendDataByBle(SendUtil.initSend("6b"),"");
                startActivity(new Intent(DlzkHomeActivity.this,DlzkDanxiangYiActivity.class));
                break;
            case R.id.llDlzkHomeLingxuzk://零序
                sendDataByBle(SendUtil.initSend("6c"),"");
                startActivity(new Intent(DlzkHomeActivity.this,DlzkLingxuYiActivity.class));
                break;
            case R.id.llDlzkHomeShujuchuli://数据
                sendDataByBle(SendUtil.initSend("6d"),"");
                startActivity(new Intent(DlzkHomeActivity.this,DlzkShujuActivity.class));
                break;
            case R.id.llDlzkHomeXitongshezhi:
                startActivity(new Intent(DlzkHomeActivity.this,DlzkXitongActivity.class));
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
            //Log.e(TAG,"收..."+str);

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
//        bleConnectUtil = new BleConnectUtil(DlzkHomeActivity.this);
//        if(StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){//!bleConnectUtil.isConnected()&&
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
//        blecallback=null;
//        mHandler.removeCallbacksAndMessages(null);
 //       bleConnectUtil.setCallback(null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.setCallback(null);
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
//        bleConnectUtil.setCallback(null);
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }
}