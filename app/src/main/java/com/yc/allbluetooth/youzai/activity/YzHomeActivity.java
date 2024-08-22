package com.yc.allbluetooth.youzai.activity;

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
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.youzai.util.CrcAll;

import java.util.Locale;

public class YzHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llYzCs;
    private LinearLayout llSjCy;
    private LinearLayout llSjSz;
    private LinearLayout llSySm;
    private TextView tvVer;
    private TextView tvTime;

    private String TAG = "YzHomeActivity";
    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    String csLongStr = "";//参数长度字符串
    int csLong = 0;//参数字节个数

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
                    if(StringUtils.isEquals(Config.ymType,"yzHome")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "YzHome:"+msgStr);
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

        setContentView(R.layout.activity_yz_home);
        Config.ymType = "yzHome";
        ActivityCollector.addActivity(this);

        //Log.e(TAG,CsszQiehuan.getSpbh("12305"));
//        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
//        Log.e(TAG, HexUtil.reverseHex("6300"));
//        Log.e(TAG,ShiOrShiliu.parseInt(HexUtil.reverseHex("6300"))+"");
//        Log.e(TAG,(float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex("6300"))+""),xsys.xiaoshu("0.01"))+"");
        initModel();
        initView();
        new TimeThread().start();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(YzHomeActivity.this);
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
        tbTime();
    }
    public void initView(){
        llYzCs = findViewById(R.id.llYzHomeYzCs);
        llSjCy = findViewById(R.id.llYzHomeSjCy);
        llSjSz = findViewById(R.id.llYzHomeSjSz);
        llSySm = findViewById(R.id.llYzHomeSySm);
        tvVer = findViewById(R.id.tvYzHomeVer);
        tvTime = findViewById(R.id.tvYzHomeTime);
        llYzCs.setOnClickListener(this);
        llSjCy.setOnClickListener(this);
        llSjSz.setOnClickListener(this);
        llSySm.setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                tbTime();
            }
        }, 1000);//3秒后执行Runnable中的run方法
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llYzHomeYzCs:
                startActivity(new Intent(YzHomeActivity.this,YzYzcsCsszActivity.class));
                break;
            case R.id.llYzHomeSjCy:
                startActivity(new Intent(YzHomeActivity.this,YzSjcyActivity.class));
                break;
            case R.id.llYzHomeSjSz:
                startActivity(new Intent(YzHomeActivity.this,YzSjszActivity.class));
                break;
            case R.id.llYzHomeSySm:
                startActivity(new Intent(YzHomeActivity.this,YzSysmActivity.class));
                break;
        }
    }
    public void tbTime(){
        String timeStr = GetTime.getTime(3);
        String nian = StringUtils.subStrStartToEnd(timeStr,0,2);
        String yue = StringUtils.subStrStartToEnd(timeStr,2,4);
        String ri = StringUtils.subStrStartToEnd(timeStr,4,6);
        String shi = StringUtils.subStrStartToEnd(timeStr,8,10);
        String fen = StringUtils.subStrStartToEnd(timeStr,10,12);
        String miao = StringUtils.subStrStartToEnd(timeStr,12,14);

        int nianInt = StringUtils.strToInt(nian);
        int yueInt = StringUtils.strToInt(yue);
        int riInt = StringUtils.strToInt(ri);
        int shiInt = StringUtils.strToInt(shi);
        int fenInt = StringUtils.strToInt(fen);
        int miaoInt = StringUtils.strToInt(miao);
        Log.e(TAG,nianInt+"");
        String nianHex = ShiOrShiliu.toHexStringBl(nianInt);
        String yueHex = ShiOrShiliu.toHexStringBl(yueInt);
        String riHex = ShiOrShiliu.toHexStringBl(riInt);
        String shiHex = ShiOrShiliu.toHexStringBl(shiInt);
        String fenHex = ShiOrShiliu.toHexStringBl(fenInt);
        String miaoHex = ShiOrShiliu.toHexStringBl(miaoInt);
        Log.e(TAG,nianHex+"");
        sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"B00700"+nianHex+yueHex+riHex+"00"+shiHex+fenHex+miaoHex+"fddf",Config.yzCrcTYpe),"");
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }
}