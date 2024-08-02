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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.Locale;

public class HlDianzuceshiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSydl50;
    private TextView tvSydl100;
    private TextView tvCssc1;
    private TextView tvCssc3;
    private TextView tvCssc10;
    private TextView tvCssc30;
    private TextView tvCssc60;
    private TextView tvCsscLianxu;
    private EditText etSybh;
    private TextView tvKscs;
    private TextView tvFanhui;
    private TextView tvDyjl;
    private TextView tvSjjz;
    private TextView tvCpsc;
    private TextView tvTime;
    private LinearLayout ll200;
    private LinearLayout ll150;
    private LinearLayout ll100;
    private LinearLayout ll50;
    private TextView tvSydl200;
    private TextView tvSydl150;
    private TextView tvXtsz;
    private LinearLayout ll1S;
    private LinearLayout ll10S;
    private LinearLayout ll30S;
    private LinearLayout ll60S;
    private LinearLayout llLx;
    int diyi = 0;
    /**
     * 测试电流
     */
    String hlCsdl = "200A";
    /**
     * 测试时长
     */
    String hlCssc = "1S";

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
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"hlDianzuceshi")){
                        String msgStr = msg.obj.toString();
                        Log.i(TAG, msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                            newMsgStr = msgStr;
                            Log.e(TAG+2,newMsgStr);
                            if(diyi==0) {
                                Log.e(TAG+3,newMsgStr);
                                if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"70")==true){
                                    Log.e(TAG,"----70------1----");
                                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","01"),"");
                                } else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"71")==true){
                                    //diyi = 1;//进入第一轮发送完毕
                                    Log.e(TAG,"测试时长");
                                    sendDataByBle(SendUtil.yiqibianhaoSend_std("6d", "A123456"),"");
                                }else if(StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,6,8),"6D")==true){
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
        setContentView(R.layout.activity_hl_dianzuceshi);
        Config.ymType = "hlDianzuceshi";
        ActivityCollector.addActivity(this);
        initModel();
        initView();
        Log.e(TAG,"====进入hl=======");
        new TimeThread().start();
        initSend();
    }
    public void initView(){
        tvSydl50 = findViewById(R.id.tvHlDzcsSydl50);
        tvSydl100 = findViewById(R.id.tvHlDzcsSydl100);
        tvSydl150 = findViewById(R.id.tvHlDzcsSydl150);
        tvSydl200 = findViewById(R.id.tvHlDzcsSydl200);
        tvCssc1 = findViewById(R.id.tvHlDzcsCssc1);
        tvCssc3 = findViewById(R.id.tvHlDzcsCssc3);
        tvCssc10 = findViewById(R.id.tvHlDzcsCssc10);
        tvCssc30 = findViewById(R.id.tvHlDzcsCssc30);
        tvCssc60 = findViewById(R.id.tvHlDzcsCssc60);
        tvCsscLianxu = findViewById(R.id.tvHlDzcsCsscLianxu);
        etSybh = findViewById(R.id.etHlDzcsSybh);
        tvKscs = findViewById(R.id.tvHlDzcsKaishiceshi);
        tvFanhui = findViewById(R.id.tvHlDzcsFanhui);
        tvDyjl = findViewById(R.id.tvHlDzcsDiaoyuejilu);
        tvSjjz = findViewById(R.id.tvHlDzcsShijianjiaozheng);
        tvCpsc = findViewById(R.id.tvHlDzcsChanpinshouce);
        tvTime = findViewById(R.id.tvHlDzcsTime);
        ll50 = findViewById(R.id.llHlDzcsSydl50);
        ll100 = findViewById(R.id.llHlDzcsSydl100);
        ll150 = findViewById(R.id.llHlDzcsSydl150);
        ll200 = findViewById(R.id.llHlDzcsSydl200);
        tvXtsz = findViewById(R.id.tvHlDzcsXitongshezhi);

        ll1S = findViewById(R.id.llHlDzcsCssc1);
        ll10S = findViewById(R.id.llHlDzcsCssc10);
        ll30S = findViewById(R.id.llHlDzcsCssc30);
        ll60S = findViewById(R.id.llHlDzcsCssc60);
        llLx = findViewById(R.id.llHlDzcsCsscLianxu);

        tvSydl50.setOnClickListener(this);
        tvSydl100.setOnClickListener(this);
        tvSydl150.setOnClickListener(this);
        tvSydl200.setOnClickListener(this);
        tvCssc1.setOnClickListener(this);
        tvCssc3.setOnClickListener(this);
        tvCssc10.setOnClickListener(this);
        tvCssc30.setOnClickListener(this);
        tvCssc60.setOnClickListener(this);
        tvCsscLianxu.setOnClickListener(this);
        tvKscs.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        tvDyjl.setOnClickListener(this);
        tvSjjz.setOnClickListener(this);
        tvCpsc.setOnClickListener(this);
        tvXtsz.setOnClickListener(this);
        if(StringUtils.isEquals("39",Config.yqlx)){//如果是100A回路（200A：3A），不显示150和200A，并且选中100A，去掉200A背景
            hlCsdl = "100A";
            ll150.setVisibility(View.GONE);
            ll200.setVisibility(View.GONE);
            tvSydl100.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvSydl200.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            ll30S.setVisibility(View.GONE);
            llLx.setVisibility(View.VISIBLE);

        }
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
        if (view.getId() == R.id.tvHlDzcsSydl50){//50A
            diyi = 1;
            hlCsdl = "50A";
            tvSydl50.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvSydl100.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl150.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl200.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            diyi = 1;
            hlCssc = "1S";
            tvCssc1.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            ll30S.setVisibility(View.GONE);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            llLx.setVisibility(View.VISIBLE);

            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("70","01"),"");//1:50;
        } else if (view.getId() == R.id.tvHlDzcsSydl100) {//100A
            diyi = 1;
            hlCsdl = "100A";
            tvSydl50.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl100.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvSydl150.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl200.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);

            diyi = 1;
            hlCssc = "1S";
            tvCssc1.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            ll30S.setVisibility(View.GONE);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            llLx.setVisibility(View.VISIBLE);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("70","02"),"");//2:100
        }else if (view.getId() == R.id.tvHlDzcsSydl150) {//150A
            diyi = 1;
            hlCsdl = "150A";
            tvSydl50.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl100.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl150.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvSydl200.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);

            diyi = 1;
            hlCssc = "1S";
            tvCssc1.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            ll30S.setVisibility(View.VISIBLE);
            llLx.setVisibility(View.GONE);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("70","03"),"");//3:150
        }else if (view.getId() == R.id.tvHlDzcsSydl200) {//200A
            diyi = 1;
            hlCsdl = "200A";
            tvSydl50.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl100.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl150.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvSydl200.setBackgroundResource(R.drawable.btn_lv_yinying_hei);

            diyi = 1;
            hlCssc = "1S";
            tvCssc1.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            ll30S.setVisibility(View.VISIBLE);
            llLx.setVisibility(View.GONE);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("70","04"),"");//4:200
        }else if (view.getId() == R.id.tvHlDzcsCssc1) {//1S
            diyi = 1;
            hlCssc = "1S";
            tvCssc1.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc30.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","01"),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc3) {//3S
            diyi = 1;
            hlCssc = "3S";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","03"),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc10) {//10S
            diyi = 1;
            hlCssc = "10S";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","0A"),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc30) {//30S
            Log.e(TAG,"===================");
            diyi = 1;
            hlCssc = "30S";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","1E"),"");
        }else if (view.getId() == R.id.tvHlDzcsCssc60) {//60S
            diyi = 1;
            hlCssc = "60S";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            tvCsscLianxu.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","3C"),"");
        }else if (view.getId() == R.id.tvHlDzcsCsscLianxu) {//连续
            diyi = 1;
            hlCssc = "";
            tvCssc1.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc30.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc10.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCssc60.setBackgroundResource(R.drawable.yuanjiao_bac_bacg);
            tvCsscLianxu.setBackgroundResource(R.drawable.btn_lv_yinying_hei);
            sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("71","64"),"");
        }else if (view.getId() == R.id.tvHlDzcsKaishiceshi) {//开始测试
            //sendDataByBle(SendUtil.initSend("77"),"");
            String etBhStr = etSybh.getText().toString();
            //finish();
            Intent intent = new Intent(HlDianzuceshiActivity.this, HlDianzuceshi2Activity.class);
            intent.putExtra("hlCsdl",hlCsdl);
            intent.putExtra("hlBianhao",etBhStr);
            intent.putExtra("hlCssc",hlCssc);
            startActivity(intent);
            //startActivity(new Intent(HlDianzuceshiActivity.this, HlDianzuceshi2Activity.class));
        }else if (view.getId() == R.id.tvHlDzcsDiaoyuejilu) {//调阅记录
            startActivity(new Intent(HlDianzuceshiActivity.this, HlDiaoyuejiluActivity.class));
        }else if (view.getId() == R.id.tvHlDzcsShijianjiaozheng) {//时间校正
            startActivity(new Intent(HlDianzuceshiActivity.this, HlShijianjiaozhengActivity.class));
        }else if (view.getId() == R.id.tvHlDzcsChanpinshouce) {//产品手册
            startActivity(new Intent(HlDianzuceshiActivity.this, HlChanpinshouceActivity.class));
        }else if(view.getId() == R.id.tvHlDzcsFanhui){//返回
            //sendDataByBle(SendUtil.initSend("78"),"");
            finish();
        }else if(view.getId() == R.id.tvHlDzcsXitongshezhi){//系统设置
            startActivity(new Intent(HlDianzuceshiActivity.this,HlXitongshezhiActivity.class));
        }
    }
    public void initSend(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                if(StringUtils.isEquals("39",Config.yqlx)){//如果是100A回路（200A：3A），不显示150和200A，并且选中100A，去掉200A背景
                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("70","02"),"");
                }else{
                    sendDataByBle(SendUtil.dlzkCanshuShuruDanzijieSend("70","04"),"");
                }

            }
        }, 70);//3秒后执行Runnable中的run方法
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
        tbTime();

    }

    /**
     * 连上蓝牙后，进主页面先同步一次时间
     */
    public void tbTime(){
        String timeStr = GetTime.getTime(3);
        String nian = StringUtils.subStrStartToEnd(timeStr,0,2);
        String yue = StringUtils.subStrStartToEnd(timeStr,2,4);
        String ri = StringUtils.subStrStartToEnd(timeStr,4,6);
        String shi = StringUtils.subStrStartToEnd(timeStr,8,10);
        String fen = StringUtils.subStrStartToEnd(timeStr,10,12);
        String miao = StringUtils.subStrStartToEnd(timeStr,12,14);

        sendDataByBle(SendUtil.shijianSend("6e", StringUtils.bulingXiaoShiliu(nian)+StringUtils.bulingXiaoShiliu(yue)+
                StringUtils.bulingXiaoShiliu(ri)+StringUtils.bulingXiaoShiliu(shi)+StringUtils.bulingXiaoShiliu(fen)+
                StringUtils.bulingXiaoShiliu(miao)),"");
    }
    /**
     * 屏幕右下角时间显示，每隔一秒执行一次
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