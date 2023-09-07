package com.yc.allbluetooth.dtd10c.activity;



import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.Locale;

public class Dtd10cZzCs1Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBack;
    private LinearLayout ll_10A;
    private LinearLayout ll_5A;
    private LinearLayout ll_1A;
    private LinearLayout ll_zd;
    private LinearLayout ll_100mA;
    private LinearLayout ll_10mA;
    private LinearLayout ll_1mA;
    private LinearLayout ll_bphl;
    private TextView tv_bphl;

    private String TAG = "ZzCs1Activity";

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
                case Config.BLUETOOTH_GETDATA:
                    String msgStr = msg.obj.toString();
                    Log.e(TAG, "zzcs1:"+msgStr);
                    //String zhilingStr = StringUtils.subStrStartToEnd(msgStr, 4, 6);
                    //if(StringUtils.isEquals("73",zhilingStr)){
                    //startActivity(new Intent(ZzCs1Activity.this,ZzCs1Activity.class));
                    //finish();
                    //}

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
//            language.translateText(ZzCs1Activity.this,"",1);
//        }else{
//            language.translateText(ZzCs1Activity.this,"",2);
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

        setContentView(R.layout.activity_dtd10c_zz_cs1);
        Config.ymType = "dtdZzCsYi";
        ActivityCollector.addActivity(this);
        initView();
        initModel();
    }
    public void initView(){
        tvBack = findViewById(R.id.tvZzCs1Back);
        ll_10A = findViewById(R.id.llZzCs1_10A);
        ll_5A = findViewById(R.id.llZzCs1_5A);
        ll_1A = findViewById(R.id.llZzCs1_1A);
        ll_zd = findViewById(R.id.llZzCs1_zidong);
        ll_100mA = findViewById(R.id.llZzCs1_100mA);
        ll_10mA = findViewById(R.id.llZzCs1_10mA);
        ll_1mA = findViewById(R.id.llZzCs1_1mA);
        ll_bphl = findViewById(R.id.llZzCs1_bphl);
        tv_bphl = findViewById(R.id.tvZzCs1Bphl);
        tvBack.setOnClickListener(this);
        ll_10A.setOnClickListener(this);
        ll_5A.setOnClickListener(this);
        ll_1A.setOnClickListener(this);
        ll_zd.setOnClickListener(this);
        ll_100mA.setOnClickListener(this);
        ll_10mA.setOnClickListener(this);
        ll_1mA.setOnClickListener(this);
        ll_bphl.setOnClickListener(this);


    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(Dtd10cZzCs1Activity.this);
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
            Log.e("ZzCs1", "onSuccessSend: ");

        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("ZzCs1", "onDisconnect: ");
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
    protected void onDestroy() {
        super.onDestroy();
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvZzCs1Back:
                sendDataByBle(SendUtil.initSend("73"),"");
                finish();
                break;
            case R.id.llZzCs1_10A:
                Config.zzCsDlType = "00";
                sendDataByBle(SendUtil.startCsSend("70","00"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_5A:
                Config.zzCsDlType = "01";
                sendDataByBle(SendUtil.startCsSend("70","01"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_1A:
                Config.zzCsDlType = "02";
                sendDataByBle(SendUtil.startCsSend("70","02"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_zidong:
                Config.zzCsDlType = "04";
                sendDataByBle(SendUtil.startCsSend("70","04"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_100mA:
                Config.zzCsDlType = "03";
                sendDataByBle(SendUtil.startCsSend("70","03"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_10mA:
                Config.zzCsDlType = "05";
                sendDataByBle(SendUtil.startCsSend("70","05"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_1mA:
                Config.zzCsDlType = "06";
                sendDataByBle(SendUtil.startCsSend("70","06"),"");
                startActivity(new Intent(Dtd10cZzCs1Activity.this,Dtd10cZzCs2Activity.class));
                break;
            case R.id.llZzCs1_bphl:
                String tvBphlStr = tv_bphl.getText().toString();
                if(StringUtils.isEquals("OFF",tvBphlStr)){
                    tv_bphl.setText("ON");
                    sendDataByBle(SendUtil.initSend("71"),"");
                    Config.bphlType = 1;
                }else{
                    tv_bphl.setText("OFF");
                    sendDataByBle(SendUtil.initSend("72"),"");
                    Config.bphlType = 0;
                }
                break;
        }
    }
}