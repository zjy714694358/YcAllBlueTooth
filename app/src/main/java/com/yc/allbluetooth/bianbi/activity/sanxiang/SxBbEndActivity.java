package com.yc.allbluetooth.bianbi.activity.sanxiang;

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
import android.widget.ImageView;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.BbHomeActivity;
import com.yc.allbluetooth.bianbi.activity.danxiang.DxBbEndActivity;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.Locale;

public class SxBbEndActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBbA;
    private TextView tvZbA;
    private TextView tvJdA;
    private TextView tvWcA;
    private TextView tvBbB;
    private TextView tvZbB;
    private TextView tvJdB;
    private TextView tvWcB;
    private TextView tvBbC;
    private TextView tvZbC;
    private TextView tvJdC;
    private TextView tvWcC;
    private TextView tvFj;
    private TextView tvFjzhi;
    private TextView tvLjfs;
    private TextView tvChongce;
    private TextView tvBaocun;
    private TextView tvDayin;
    private TextView tvFanhui;
    private ImageView ivZbsl;//组别矢量图

    String TAG = "DxBbEndActivity";
    String crcJy = "";
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
                    //tvTime.setText(GetTime.getTime2());//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"bianbiDxBbEnd")){
                        String msgStr = msg.obj.toString();
                        Log.i(TAG, msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                            newMsgStr = msgStr;
                            Log.e(TAG,newMsgStr);
                        }else{
                            newMsgStr = newMsgStr+msgStr;
                            //可以
                            Log.e(TAG+"可以",newMsgStr);
                        }
                        if(newMsgStr.length() == 44){
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,40);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,40,44);
                            if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
//                                csdl = StringUtils.subStrStartToEnd(newMsgStr,16,24);//测试电流
//                                String dl = StringUtils.gaodiHuanBaoliuShierwei(csdl);
//                                tvCsdl.setText(StringUtils.wuweiYouxiaoStr(dl));
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
        setContentView(R.layout.activity_sx_bb_end);
        ActivityCollector.addActivity(this);
        Config.ymType = "bianbiDxBbEnd";
        initModel();
        initView();
    }
    public void initView(){
        tvBbA = findViewById(R.id.tvSxBbEndBianbiA);
        tvZbA = findViewById(R.id.tvSxBbEndZabiA);
        tvJdA = findViewById(R.id.tvSxBbEndJiaoduA);
        tvWcA = findViewById(R.id.tvSxBbEndWuchaA);
        tvBbB = findViewById(R.id.tvSxBbEndBianbiB);
        tvZbB = findViewById(R.id.tvSxBbEndZabiB);
        tvJdB = findViewById(R.id.tvSxBbEndJiaoduB);
        tvWcB = findViewById(R.id.tvSxBbEndWuchaB);
        tvBbC = findViewById(R.id.tvSxBbEndBianbiC);
        tvZbC = findViewById(R.id.tvSxBbEndZabiC);
        tvJdC = findViewById(R.id.tvSxBbEndJiaoduC);
        tvWcC = findViewById(R.id.tvSxBbEndWuchaC);
        tvFj = findViewById(R.id.tvSxBbEndFenjie);
        tvFjzhi = findViewById(R.id.tvSxBbEndFenjiezhi);
        tvLjfs = findViewById(R.id.tvSxBbEndLjfs);
        tvChongce = findViewById(R.id.tvSxBbEndChongce);
        tvBaocun = findViewById(R.id.tvSxBbEndBaocun);
        tvDayin = findViewById(R.id.tvSxBbEndDayin);
        tvFanhui = findViewById(R.id.tvSxBbEndFanhui);
        ivZbsl = findViewById(R.id.ivSxBbEndZbsl);
        tvChongce.setOnClickListener(this);
        tvBaocun.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        //ivZbsl.setImageResource(R.drawable.slt01);
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(SxBbEndActivity.this);
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
        ActivityCollector.removeActivity(SxBbEndActivity.this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvSxBbEndChongce){//重测
            sendDataByBle(SendUtil.initSendStd("79"),"");
        } else if (v.getId() == R.id.tvSxBbEndBaocun) {//保存
            sendDataByBle(SendUtil.initSendStd("80"),"");
        } else if (v.getId() == R.id.tvSxBbEndDayin) {//打印
            sendDataByBle(SendUtil.initSendStd("81"),"");
        } else if (v.getId() == R.id.tvSxBbEndFanhui) {//返回
            sendDataByBle(SendUtil.initSendStd("82"),"");
            finish();
            startActivity(new Intent(SxBbEndActivity.this, BbHomeActivity.class));
        }
    }
}