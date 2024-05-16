package com.yc.allbluetooth.huilu.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.Locale;

public class HlDianzuceshi2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBianhao;
    private TextView tvDianliu;
    private TextView tvI;
    private TextView tvR;
    private TextView tvChongce;
    private TextView tvBaocun;
    private TextView tvDayin;
    private TextView tvTingzhi;
    private TextView tvFanhui;

    String TAG = "DxBbCeshiActivity";
    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    String sjcd = "";
    String yqlx = "";
    String csdl = "";
    String csdz = "";
    String crcJy = "";

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
                    break;
                case Config.BLUETOOTH_GETDATA:
                    Log.e(TAG,"进入.2..bianbiDxBbCeshi");
                    if(StringUtils.isEquals(Config.ymType,"bianbiDxBbCeshi")){
                        String msgStr = msg.obj.toString();
                        Log.i(TAG, msgStr);
                        if(msgStr.length()!=18&&newMsgStr.length()<50){
                            newMsgStr += msgStr;
                            Log.e(TAG,newMsgStr);
                        }
                        if (newMsgStr.length() == 50) {
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,28);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,28,32);
                            //Log.e("tfxx==1", CrcUtil.getTableCRC(bytesSx));
                            //if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
                            csdl = StringUtils.subStrStartToEnd(newMsgStr,12,20);//测试电流
                            csdz = StringUtils.subStrStartToEnd(newMsgStr,20,28);
                            Log.e(TAG,csdl+","+csdz);
                            String dl = ShiOrShiliu.hexToFloatWuBuhuan(csdl);
                            String dz = ShiOrShiliu.hexToFloatWuBuhuan(csdz);
                            tvI.setText(dl+"A");
                            tvR.setText(dz+"Ω");
                            newMsgStr = "";
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
        setContentView(R.layout.activity_hl_dianzuceshi2);
        Config.ymType = "hlDianzuceshi2";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
    }
    public void  initView(){
        tvBianhao = findViewById(R.id.tvHlDzcs2Bianhao);
        tvDianliu = findViewById(R.id.tvHlDzcs2Dianliu);
        tvI = findViewById(R.id.tvHlDzcs2I);
        tvR = findViewById(R.id.tvHlDzcs2R);
        tvChongce = findViewById(R.id.tvHlDzcs2Chongce);
        tvBaocun = findViewById(R.id.tvHlDzcs2Baocun);
        tvDayin = findViewById(R.id.tvHlDzcs2Dayin);
        tvTingzhi = findViewById(R.id.tvHlDzcs2Tingzhi);
        tvFanhui = findViewById(R.id.tvHlDzcs2Fanhui);
        tvChongce.setOnClickListener(this);
        tvBaocun.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvTingzhi.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        Intent intent = getIntent();
        String dl = intent.getStringExtra("hlCsdl");
        String bh = intent.getStringExtra("hlBianhao");
        tvDianliu.setText(dl);
        tvBianhao.setText(bh);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvHlDzcs2Chongce){//重测
            sendDataByBle(SendUtil.initSendStd("79"),"");
        } else if (view.getId() == R.id.tvHlDzcs2Baocun) {//保存
            sendDataByBle(SendUtil.initSendStd("7a"),"");
            Toast.makeText(HlDianzuceshi2Activity.this,getString(R.string.baocuntanchuang),Toast.LENGTH_SHORT).show();
        }else if (view.getId() == R.id.tvHlDzcs2Dayin) {//打印
            sendDataByBle(SendUtil.initSendStd("7b"),"");
            Toast.makeText(HlDianzuceshi2Activity.this,getString(R.string.dayintanchuang),Toast.LENGTH_SHORT).show();
        }else if (view.getId() == R.id.tvHlDzcs2Tingzhi) {//停止
            sendDataByBle(SendUtil.initSendStd("7c"),"");
            Toast.makeText(HlDianzuceshi2Activity.this,getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
        }else if (view.getId() == R.id.tvHlDzcs2Fanhui) {//返回
            finish();
            //startActivity(new Intent(HlDianzuceshi2Activity.this, HuiluHomeActivity.class));
        }
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(HlDianzuceshi2Activity.this);
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