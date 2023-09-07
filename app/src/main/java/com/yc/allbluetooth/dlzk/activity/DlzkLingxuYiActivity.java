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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dlzk.util.DlzkQiehuan;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.EditTextTextChanged;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.Locale;

public class DlzkLingxuYiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTime;
    private EditText etEdrl;
    private TextView tvXzdy;
    private LinearLayout llXzdy;
    private EditText etFjdy;
    private EditText etClwd;
    private EditText etMpzk;
    private EditText etSpbh;
    private EditText etCsry;
    private LinearLayout llCeshi;
    private LinearLayout llFanhui;
    int diyi = 0;

    private String TAG = "DlzkLxYiActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    String newMsgStr2 = "";

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
                    if(StringUtils.isEquals(Config.ymType,"dlzkLxYi")){
                        //Log.e(TAG,msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"66")|| IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                            newMsgStr2 = "";
                            newMsgStr = msgStr;
                            Log.e(TAG,newMsgStr);
                        }else if(IndexOfAndSubStr.isIndexOf(msgStr,"77")){
                            newMsgStr2 = newMsgStr+msgStr;
                            //可以
                            Log.e(TAG+"可以",newMsgStr2);
                            if(diyi==0){
                                if(IndexOfAndSubStr.isIndexOf(newMsgStr2,"667770")){
                                    Log.e(TAG,"----70----------");
                                    sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("71", "10.000"), "");
                                }else if(IndexOfAndSubStr.isIndexOf(newMsgStr2,"667771")){
                                    Log.e(TAG,"----71----------");
                                    sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("72", "4.010"), "");
                                }else if(IndexOfAndSubStr.isIndexOf(newMsgStr2,"667772")){
                                    sendDataByBle(SendUtil.dlzkCanshuShuruBazijieSend("7b", ""), "");
                                }else if(IndexOfAndSubStr.isIndexOf(newMsgStr2,"66777B")){
                                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("75","00"),"");
                                }else if(IndexOfAndSubStr.isIndexOf(newMsgStr2,"667775")){
                                    sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("77", "19.00"), "");
                                }else if(IndexOfAndSubStr.isIndexOf(newMsgStr2,"667777")){
                                    sendDataByBle(SendUtil.dlzkCanshuShuruLiuzijieSend("7d", ""), "");
                                    diyi = 1;//进入第一轮发送完毕
                                    Log.e(TAG,"进入第一轮发送完成");
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

        setContentView(R.layout.activity_dlzk_lingxu_yi);
        Config.ymType = "dlzkLxYi";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
        initSend();
        new TimeThread().start();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(DlzkLingxuYiActivity.this);
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
    public void initSend(){
        Log.e(TAG,"开始发送");
        sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("70", "30.000"), "");
//        sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("71", "10.000"), "");
//        sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("72", "4.010"), "");
//        sendDataByBle(SendUtil.dlzkCanshuShuruBazijieSend("7b", ""), "");
//        sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("75","00"),"");
//        sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("77", "19.00"), "");
//        sendDataByBle(SendUtil.dlzkCanshuShuruLiuzijieSend("7d", ""), "");
    }
    public void initView(){
        tvTime = findViewById(R.id.tvDlzkLingxuYiTime);
        etEdrl = findViewById(R.id.etDlzkLingxuYiEdrl);
        tvXzdy = findViewById(R.id.tvDlzkLingxuYiXzdy);
        llXzdy = findViewById(R.id.llDlzkLingxuYiXzdy);
        etFjdy = findViewById(R.id.etDlzkLingxuYiFjdy);
        etClwd = findViewById(R.id.etDlzkLingxuYiClwd);
        etMpzk = findViewById(R.id.etDlzkLingxuYiMpzk);
        etSpbh = findViewById(R.id.etDlzkLingxuYiSpbh);
        etCsry = findViewById(R.id.etDlzkLingxuYiCsry);
        llCeshi = findViewById(R.id.llDlzkLingxuYiCeshi);
        llFanhui = findViewById(R.id.llDlzkLingxuYiFanhui);

        llXzdy.setOnClickListener(this);
        llCeshi.setOnClickListener(this);
        llFanhui.setOnClickListener(this);

        etEdrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etEdrl.getText().toString();
                        if(StringUtils.noEmpty(etStr)){
                            sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("70", etStr), "");
                        }

                        break;
                }
                return false;
            }
        });
        etFjdy.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etFjdy.getText().toString();
                        if(StringUtils.noEmpty(etStr)){
                            sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("71", etStr), "");
                        }

                        break;
                }
                return false;
            }
        });
        etMpzk.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etMpzk.getText().toString();
                        if(StringUtils.noEmpty(etStr)){
                            sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("72", etStr), "");
                        }

                        break;
                }
                return false;
            }
        });
        etClwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etClwd.getText().toString();
                        if(StringUtils.noEmpty(etStr)){//根据位数不同结尾补零
                            sendDataByBle(SendUtil.dlzkCanshuShuruSizijieSend("77", etStr), "");
                        }
                        break;
                }
                return false;
            }
        });
        etSpbh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etSpbh.getText().toString();
                        //IsChinese isChinese = new IsChinese();

                        //if(isChinese.ischinese(etStr)){//根据位数不同结尾补零
                        sendDataByBle(SendUtil.dlzkCanshuShuruBazijieSend("7b", etStr), "");
                        //}
                        break;
                }
                return false;
            }
        });
        etCsry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etCsry.getText().toString();
                        //IsChinese isChinese = new IsChinese();
                        //if(StringUtils.noEmpty(etStr)&&isChinese.isAllchese(etStr)){//根据位数不同结尾补零
                        sendDataByBle(SendUtil.dlzkCanshuShuruLiuzijieSend("7d", etStr), "");
                        //}
                        break;
                }
                return false;
            }
        });
        etCsry.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged 被执行---->s=" + s + "----start="+ start
                        + "----before="+before + "----count" +count);
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged 被执行---->" + s);
                EditTextTextChanged editTextTextChanged = new EditTextTextChanged();
                editTextTextChanged.after(etCsry,s,temp);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llDlzkLingxuYiXzdy://选择电源
                String xzdyStr = tvXzdy.getText().toString();
                //DlzkQiehuan.xzdy(xzdyStr,tvXzdy);
                sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("75", DlzkQiehuan.xzdy(xzdyStr,tvXzdy)),"");
                break;
            case R.id.llDlzkLingxuYiCeshi:
                sendDataByBle(SendUtil.startCsSend("7c", "00"), "");
                //finish();
                startActivity(new Intent(DlzkLingxuYiActivity.this,DlzkLingxuErActivity.class));
                break;
            case R.id.llDlzkLingxuYiFanhui:
                sendDataByBle(SendUtil.initSend("6e"),"");
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
        Config.ymType = "dlzkLxYi";
//        bleConnectUtil = new BleConnectUtil(DlzkLingxuYiActivity.this);
//        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
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
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.setCallback(null);
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
        //mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }
}