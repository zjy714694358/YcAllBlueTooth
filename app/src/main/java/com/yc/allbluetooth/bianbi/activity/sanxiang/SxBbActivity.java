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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.danxiang.DxBbActivity;
import com.yc.allbluetooth.bianbi.util.LianjieZubie;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StrToAsc;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.Locale;

public class SxBbActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEdgy;
    private EditText etEddy;
    private EditText etFjzs;
    private EditText etFjjj;
    private EditText etRwbh;
    private TextView tvLjzbYi;
    private TextView tvLjzbEr;
    private TextView tvLjzbSan;
    private LinearLayout llLjzbYi;
    private LinearLayout llLjzbEr;
    private LinearLayout llLjzbSan;
    private TextView tvCeshi;
    private TextView tvFanhui;

    int diyi = 0;
    String TAG = "DxBbActivity";

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
                    if(StringUtils.isEquals(Config.ymType,"bianbiSxBb")){
                        String msgStr = msg.obj.toString();
                        Log.i(TAG, msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                            newMsgStr = msgStr;
                            Log.e(TAG,newMsgStr);
                            if(diyi==0){
                                if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"70")){
                                    Log.e(TAG,"----70----------");
                                    sendDataByBle(SendUtil.ShuruSizijieSend("71", "0.4"), "");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"71")){
                                    Log.e(TAG,"----71----------");
                                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("72", "03"), "");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"72")){
                                    sendDataByBle(SendUtil.ShuruSizijieSend("73", "2.5"), "");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"73")){//D-Y-Z
                                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("74", "01"), "");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"74")){//d-y-z
                                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("75", "00"), "");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"75")){//组别号：0-11-未知
                                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("76", "0B"), "");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"76")){
                                    //sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("6d",""),"");
                                    sendDataByBle(SendUtil.initSendBianbiNew8("6d", StrToAsc.ToAscii("A123456",8)),"");
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
        setContentView(R.layout.activity_sx_bb);
        ActivityCollector.addActivity(this);
        Config.ymType = "bianbiSxBb";
        initModel();
        initView();
        initSend();
    }
    public void initView(){
        etEdgy = findViewById(R.id.etSxCsEdgy);
        etEddy = findViewById(R.id.etSxCsEddy);
        etFjzs = findViewById(R.id.etSxCsFjzs);
        etFjjj = findViewById(R.id.etSxCsFjjj);
        etRwbh = findViewById(R.id.etSxCsRwbh);
        tvLjzbYi = findViewById(R.id.tvSxCsLjzbYi);
        tvLjzbEr = findViewById(R.id.tvSxCsLjzbEr);
        tvLjzbSan = findViewById(R.id.tvSxCsLjzbSan);
        llLjzbYi = findViewById(R.id.llSxCsLjzbYiJiantou);
        llLjzbEr = findViewById(R.id.llSxCsLjzbErJiantou);
        llLjzbSan = findViewById(R.id.llSxCsLjzbSanJiantou);
        tvCeshi = findViewById(R.id.tvSxCsCeshi);
        tvFanhui = findViewById(R.id.tvSxCsFanhui);
        llLjzbYi.setOnClickListener(this);
        llLjzbEr.setOnClickListener(this);
        llLjzbSan.setOnClickListener(this);
        tvCeshi.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);

        etEdgy.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        diyi = 1;
                        String edgy = etEdgy.getText().toString();
                        if(StringUtils.isEmpty(edgy)){
                            etEdgy.setText("10.00");
                            Config.bbEdgy = "10";
                            sendDataByBle(SendUtil.ShuruSizijieSend("70", "10.00"), "");
                        }else {
                            Config.bbEdgy = edgy;
                            sendDataByBle(SendUtil.ShuruSizijieSend("70", edgy), "");
                        }
                        break;
                }
                return false;
            }
        });
        etEddy.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        diyi = 1;
                        String eddy = etEddy.getText().toString();
                        if(StringUtils.isEmpty(eddy)){
                            etEddy.setText("0.4");
                            Config.bbEddy = "0.4";
                            sendDataByBle(SendUtil.ShuruSizijieSend("71", "0.4"), "");
                        }else {
                            Config.bbEddy = eddy;
                            sendDataByBle(SendUtil.ShuruSizijieSend("71", eddy), "");
                        }
                        break;
                }
                return false;
            }
        });
        etFjzs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        diyi = 1;
                        String fjzs = etFjzs.getText().toString();
                        if(StringUtils.noEmpty(fjzs)){
                            Config.bbFjjj = StringUtils.strToInt(fjzs)+"";
                            sendDataByBle(SendUtil.dlzkShujuSend("72", StringUtils.strToInt(fjzs)),"");
                        }else{
                            etFjzs.setText("03");
                            Config.bbFjjj = "03";
                            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("72", "03"), "");
                        }
                        break;
                }
                return false;
            }
        });
        etFjjj.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        diyi = 1;
                        String fjjj = etFjjj.getText().toString();
                        if(StringUtils.isEmpty(fjjj)){
                            etFjjj.setText("2.5");
                            Config.bbFjjj = "2.5";
                            sendDataByBle(SendUtil.ShuruSizijieSend("73", "2.5"), "");
                        }else {
                            Config.bbFjjj = fjjj;
                            sendDataByBle(SendUtil.ShuruSizijieSend("73", fjjj), "");
                        }
                        break;
                }
                return false;
            }
        });
        etRwbh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i){
                    case EditorInfo.IME_ACTION_DONE://输入框，完成按钮点击事件
                        diyi = 1;
                        String rwbh = etRwbh.getText().toString();
                        //sendDataByBle(SendUtil.yiqibianhaoSend_std("6d", rwbh),"");
                        sendDataByBle(SendUtil.initSendBianbiNew8("6d", StrToAsc.ToAscii(rwbh,8)),"");
                        break;
                }
                return false;
            }
        });
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(SxBbActivity.this);
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
        //sendDataByBle(SendUtil.ShuruSizijieSend("70", "10.00"), "");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                sendDataByBle(SendUtil.ShuruSizijieSend("70", "10.00"), "");
            }
        }, 100);//0.1秒后执行Runnable中的run方法
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llSxCsLjzbYiJiantou){//联结组别一切换
            String tvzbYiStr = tvLjzbYi.getText().toString();
            //LianjieZubie lianjieZubie = new LianjieZubie();
            String zhiling = LianjieZubie.getYi(SxBbActivity.this,tvLjzbYi,tvzbYiStr);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("74",zhiling),"");
        } else if (v.getId() == R.id.llSxCsLjzbErJiantou) {//联结组别二切换
            String tvzbErStr = tvLjzbEr.getText().toString();
            //LianjieZubie lianjieZubie = new LianjieZubie();
            String zhiling = LianjieZubie.getEr(SxBbActivity.this,tvLjzbEr,tvzbErStr);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("75",zhiling),"");
        } else if (v.getId() == R.id.llSxCsLjzbSanJiantou) {//联结组别三切换
            String tvzbSanStr = tvLjzbSan.getText().toString();
            //LianjieZubie lianjieZubie = new LianjieZubie();
            String zhiling = LianjieZubie.getSan(SxBbActivity.this,tvLjzbSan,tvzbSanStr);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("76",zhiling),"");
        } else if (v.getId() == R.id.tvSxCsCeshi) {
            //sendDataByBle(SendUtil.initSend("77"),"");
            finish();
            startActivity(new Intent(SxBbActivity.this, SxBbCeshiActivity.class));
        } else if (v.getId() == R.id.tvSxCsFanhui) {
            sendDataByBle(SendUtil.initSend("78"),"");
            finish();
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