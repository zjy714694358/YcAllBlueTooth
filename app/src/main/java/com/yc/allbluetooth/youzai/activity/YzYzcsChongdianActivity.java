package com.yc.allbluetooth.youzai.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;
import com.yc.allbluetooth.youzai.util.CrcAll;
import com.yc.allbluetooth.youzai.util.VerticalProgressBar;

import java.util.Locale;

public class YzYzcsChongdianActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvKscs;
    private TextView tvFanhui;
    private TextView tvTime;
    private VerticalProgressBar vtProA;
    private VerticalProgressBar vtProB;
    private VerticalProgressBar vtProC;
    private TextView tvA;
    private TextView tvB;
    private TextView tvC;
    int iA = 0;

    private String TAG = "YzYzcsChongdianActivity";

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
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"yzYzcsChongdian")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "chongdian:"+msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"FEEF"+Config.yzBenjiAddress+"20")){//返回测试状态
                            String typeStr = StringUtils.subStrStartToEnd(msgStr, 12, 14);
                            if(StringUtils.isEquals("01",typeStr)){//正在充电
                                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"230000fddf",Config.yzCrcTYpe),"");
                            }else if(StringUtils.isEquals("02",typeStr)){//正在测试

                            }else if(StringUtils.isEquals("03",typeStr)){//测试完成，波形

                            }else if(StringUtils.isEquals("00",typeStr)){//未开始测试

                            }
                        }else if(IndexOfAndSubStr.isIndexOf(msgStr,"FEEF"+Config.yzBenjiAddress+"23")){//返回电流值
                            String aStr = StringUtils.subStrStartToEnd(msgStr,12,16);
                            String bStr = StringUtils.subStrStartToEnd(msgStr,16,20);
                            String cStr = StringUtils.subStrStartToEnd(msgStr,20,24);
                            if(StringUtils.isEquals("0000",aStr)&&StringUtils.isEquals("0000",bStr)&&StringUtils.isEquals("0000",cStr)){//ABC三相电流都为0，继续获取
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        /**
                                         *要执行的操作
                                         */
                                        sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"230000fddf",Config.yzCrcTYpe), "");
                                    }
                                }, 500);//0.5秒后执行Runnable中的run方法
                            }else{//有最少一相电流不为0
                                XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                String aStr2 = (float) xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(aStr)) + ""), xsys.xiaoshu("0.01"))+"A";
                                String bStr2 = (float) xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(bStr)) + ""), xsys.xiaoshu("0.01"))+"A";
                                String cStr2 = (float) xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(cStr)) + ""), xsys.xiaoshu("0.01"))+"A";

                                if(!StringUtils.isEquals("0.00A",aStr2)&&!StringUtils.isEquals("0.0A",aStr2)){
                                    tvA.setText(aStr2);
                                    vtProA.setProgress(80);
                                }
                                if(!StringUtils.isEquals("0.00A",bStr2)&&!StringUtils.isEquals("0.0A",bStr2)){
                                    tvB.setText(bStr2);
                                    vtProB.setProgress(80);
                                }
                                if(!StringUtils.isEquals("0.00A",cStr2)&&!StringUtils.isEquals("0.0A",cStr2)){
                                    tvC.setText(cStr2);
                                    vtProC.setProgress(80);
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

        setContentView(R.layout.activity_yz_yzcs_chongdian);
        Config.ymType = "yzYzcsChongdian";
        ActivityCollector.addActivity(this);
        Log.e(TAG,"jin...........");
        initModel();
        initView();
        new TimeThread().start();
    }
    public void initModel(){
        //sendDataByBle("feef04200000fddf","");//读取测试状态
        bleConnectUtil = new BleConnectUtil(YzYzcsChongdianActivity.this);
        if (!bleConnectUtil.isConnected() && StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)) {
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"200000fddf",Config.yzCrcTYpe), "");
            }
        }, 1000);//1秒后执行Runnable中的run方法
    }
    public void initView(){
        tvKscs = findViewById(R.id.tvYzYzcsCdKscs);
        tvFanhui = findViewById(R.id.tvYzYzcsCdFanhui);
        tvTime = findViewById(R.id.tvYzYzcsCdTime);

        tvA = findViewById(R.id.tvYzYzcsCdA);
        tvB = findViewById(R.id.tvYzYzcsCdB);
        tvC = findViewById(R.id.tvYzYzcsCdC);

        vtProA = findViewById(R.id.vtProA);
        vtProB = findViewById(R.id.vtProB);
        vtProC = findViewById(R.id.vtProC);

        vtProA.setColor(Color.rgb(255, 255, 0));
        vtProB.setColor(Color.rgb(0, 128, 0));
        vtProC.setColor(Color.rgb(255, 0, 0));
//        vtProA.setProgress(80);
//        vtProB.setProgress(80);
//        vtProC.setProgress(80);
        tvKscs.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvYzYzcsCdKscs://开始测试
  //              Log.e("===",iA+"");
 //               iA += 10;
//                vtProA.setProgress(iA);
//                vtProB.setProgress(iA);
//                vtProC.setProgress(iA);
                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"2201000200fddf",Config.yzCrcTYpe),"");
                finish();
                startActivity(new Intent(YzYzcsChongdianActivity.this,YzYzcsKaishiCsActivity.class));
                break;
            case R.id.tvYzYzcsCdFanhui:
                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"e00000fddf",Config.yzCrcTYpe),"");
                finish();
                Intent it = new Intent();
                it.setClass(YzYzcsChongdianActivity.this, YzHomeActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                startActivity(it);
                break;
        }
    }
    /**
     * 屏幕左下角时间显示，每隔一秒执行一次
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
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }
}