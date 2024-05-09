package com.yc.allbluetooth.huilu.activity;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import org.w3c.dom.Text;

import java.util.Locale;

public class HlDianzuceshiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSydl50;
    private TextView tvSydl100;
    private TextView tvCssc1;
    private TextView tvCssc3;
    private TextView tvCssc10;
    private TextView tvCssc60;
    private TextView tvCsscLianxu;
    private EditText etSybh;
    private TextView tvKscs;
    private TextView tvFanhui;
    String shiyanDl = "";
    String ceshiSc = "";

    private String TAG = "HlDianzuceshiActivity";

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
                    break;
                case Config.BLUETOOTH_GETDATA:
                    String msgStr = msg.obj.toString();
                    Log.e(TAG, "Home:"+msgStr);
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
        setContentView(R.layout.activity_hl_dianzuceshi);
        Config.ymType = "hlDianzuceshi";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
    }
    public void initView(){
        tvSydl50 = findViewById(R.id.tvHlDzcsSydl50);
        tvSydl100 = findViewById(R.id.tvHlDzcsSydl100);
        tvCssc1 = findViewById(R.id.tvHlDzcsCssc1);
        tvCssc3 = findViewById(R.id.tvHlDzcsCssc3);
        tvCssc10 = findViewById(R.id.tvHlDzcsCssc10);
        tvCssc60 = findViewById(R.id.tvHlDzcsCssc60);
        tvCsscLianxu = findViewById(R.id.tvHlDzcsCsscLianxu);
        etSybh = findViewById(R.id.etHlDzcsSybh);
        tvKscs = findViewById(R.id.tvHlDzcsKaishiceshi);
        tvFanhui = findViewById(R.id.tvHlDzcsFanhui);
        tvSydl50.setOnClickListener(this);
        tvSydl100.setOnClickListener(this);
        tvCssc1.setOnClickListener(this);
        tvCssc3.setOnClickListener(this);
        tvCssc10.setOnClickListener(this);
        tvCssc60.setOnClickListener(this);
        tvCsscLianxu.setOnClickListener(this);
        tvKscs.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        etSybh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i){
                    case EditorInfo.IME_ACTION_DONE:
                        String sybh = etSybh.getText().toString();
                        sendDataByBle(SendUtil.yiqibianhaoSend_std("6d", sybh),"");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvHlDzcsSydl50){
            shiyanDl = "";
            tvSydl50.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvSydl100.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.initSend(shiyanDl),"");
        } else if (view.getId() == R.id.tvHlDzcsSydl100) {
            shiyanDl = "";
            tvSydl50.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl100.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            sendDataByBle(SendUtil.initSend(shiyanDl),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc1) {
            ceshiSc = "";
            tvCssc1.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc3.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.initSend(ceshiSc),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc3) {
            ceshiSc = "";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc3.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.initSend(ceshiSc),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc10) {
            ceshiSc = "";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc3.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.initSend(ceshiSc),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc60) {
            ceshiSc = "";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc3.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.initSend(ceshiSc),"");
        }else if (view.getId() == R.id.tvHlDzcsCsscLianxu) {
            ceshiSc = "";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc3.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            sendDataByBle(SendUtil.initSend(ceshiSc),"");
        }else if (view.getId() == R.id.tvHlDzcsKaishiceshi) {
            sendDataByBle(SendUtil.initSend(""),"");
            startActivity(new Intent(HlDianzuceshiActivity.this, HlDianzuceshi2Activity.class));
        }
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(HlDianzuceshiActivity.this);
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
        Log.e(TAG,"onDestroy()");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }
}