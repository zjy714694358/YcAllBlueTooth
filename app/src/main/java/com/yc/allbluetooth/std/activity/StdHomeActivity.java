package com.yc.allbluetooth.std.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.fragment.StdDyJlNewFragment;
import com.yc.allbluetooth.std.fragment.StdShijianShezhiFragment;
import com.yc.allbluetooth.std.fragment.StdXiaociGongnengFragment;
import com.yc.allbluetooth.std.fragment.StdXitongShezhiFragment;
import com.yc.allbluetooth.std.fragment.StdZhizuCeshiNewFragment;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.Locale;

public class StdHomeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llZzcs;
    LinearLayout llXcgn;
    LinearLayout llDyjl;
    LinearLayout llSjsz;
    LinearLayout llXtsz;
    LinearLayout llFanhui;
    Button btnZzcs;
    Button btnXcgn;
    Button btnDyjl;
    Button btnSjsz;
    Button btnXtsz;
    TextView tvTime;


    //List<Fragment>fragmentList = new ArrayList<>();

    BleConnectUtil bleConnectUtil;
    String TAG = "Home";
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
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    //GetTime getTime = new GetTime();
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    String msgStr = msg.obj.toString();
                    Log.i(TAG, "std_Home:" + msgStr);

//                    if (msgStr.length() < 1) {//长度11
//                        //Toast.makeText(HomeActivity.this, "返回指令位数错误！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
//                            newMsgStr = msgStr;
//                        } else {
//                            newMsgStr = newMsgStr + msgStr;
//                        }
//                    }
                    break;
                case Config.BLUETOOTH_SEND_CHAOSHI:
                    regainBleDataCount = 0;
                    bleFlag = false;
                    mHandler.removeCallbacks(checkConnetRunnable);

                    Toast.makeText(StdHomeActivity.this, "超时请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case Config.BLUETOOTH_LIANJIE_DUANKAI:
                    bleConnectUtil.disConnect();
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
        if ("zh".equals(Config.zyType)) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);

        setContentView(R.layout.activity_std_home);
        Log.e(TAG, "进入.....................Home");

        bleConnectUtil = new BleConnectUtil(StdHomeActivity.this);
        Log.e(TAG + 1, bleConnectUtil.wsDeviceAddress);
        Log.e(TAG + 2, Config.lyAddress);
        Config.ymType = "stdHome";
        //bleConnectUtil.wsDeviceAddress = "94:A9:A8:32:76:49";
        if (!bleConnectUtil.isConnected() && StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)) {
            //if(!bleConnectUtil.isConnected()&&StringUtils.noEmpty(Config.ynA0Csdz)){
            Log.e(TAG, Config.lyAddress);
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//HC-08：94:A9:A8:32:76:49
            //bleConnectUtil.connect(Config.lyAddress,10,10);//HC-08：94:A9:A8:32:76:49
            Log.e(TAG, "进入...Home1");
            bleConnectUtil.setCallback(blecallback);
            Log.e(TAG, "进入...Home2");
        }
        //sendDataByBle("68866B010006060555B589","");
        //StdZhizuCeshiFragment zhizuCeshiFragment = StdZhizuCeshiFragment.newInstance("","");
        StdZhizuCeshiNewFragment zhizuCeshiFragment = StdZhizuCeshiNewFragment.newInstance("", "");
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, zhizuCeshiFragment).commit();
        addFragemntToShow(0, zhizuCeshiFragment);
        initView();
        tbTime();
        new TimeThread().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //bleConnectUtil.disConnect();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//        }
//        if(bleConnectUtil.mBluetoothGatt!=null){
//            bleConnectUtil.mBluetoothGatt.close();
//        }
        //bleConnectUtil.disConnect();
        // 移除所有的handler 消息.
        //mHandler.removeCallbacksAndMessages(null);
        Log.e(TAG, "onDestroy()");
        Log.e(TAG, "onDestroy");
    }

    public void initView() {

        llZzcs = findViewById(R.id.llZhizuCeshi);
        llXcgn = findViewById(R.id.llXiaociGongneng);
        llDyjl = findViewById(R.id.llDiaoyueJilu);
        llSjsz = findViewById(R.id.llShijianShezhi);
        llXtsz = findViewById(R.id.llXitongShezhi);
        tvTime = findViewById(R.id.tvHomeTime);
        llFanhui = findViewById(R.id.llFanhui);

        btnZzcs = findViewById(R.id.btnZhizuCeshi);
        btnXcgn = findViewById(R.id.btnXiaociGongneng);
        btnDyjl = findViewById(R.id.btnDiaoyueJilu);
        btnSjsz = findViewById(R.id.btnShijianShezhi);
        btnXtsz = findViewById(R.id.btnXitongShezhi);

        llZzcs.setOnClickListener(this);
        llXcgn.setOnClickListener(this);
        llDyjl.setOnClickListener(this);
        llSjsz.setOnClickListener(this);
        llXtsz.setOnClickListener(this);
        llFanhui.setOnClickListener(this);

        btnZzcs.setOnClickListener(this);
        btnXcgn.setOnClickListener(this);
        btnDyjl.setOnClickListener(this);
        btnSjsz.setOnClickListener(this);
        btnXtsz.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnZhizuCeshi:
                //sendDataByBle(SendUtil.initSend("73"),"");//先发送返回
                //bleConnectUtil.disConnect();
                // 移除所有的handler 消息.
                //mHandler.removeCallbacksAndMessages(null);
                //Config.homeQiehuan = 1;
                //StdZhizuCeshiFragment zhizuCeshiFragment = StdZhizuCeshiFragment.newInstance("","");
                StdZhizuCeshiNewFragment zhizuCeshiFragment = StdZhizuCeshiNewFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, zhizuCeshiFragment).commit();
                addFragemntToShow(1, zhizuCeshiFragment);
                break;
            case R.id.btnXiaociGongneng:
                //bleConnectUtil.disConnect();
                // 移除所有的handler 消息.
                //mHandler.removeCallbacksAndMessages(null);

                StdXiaociGongnengFragment xiaociGongnengFragment = StdXiaociGongnengFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, xiaociGongnengFragment).commit();
                addFragemntToShow(1, xiaociGongnengFragment);
                break;
            case R.id.btnDiaoyueJilu://调阅记录，数据管理
                //bleConnectUtil.disConnect();
                // 移除所有的handler 消息.
                //mHandler.removeCallbacksAndMessages(null);

                //DiaoyueJiluFragment diaoyueJiluFragment = DiaoyueJiluFragment.newInstance("","");
                //DyJlListFragment dyJlListFragment = DyJlListFragment.newInstance("","");
                //TestFragment diaoyueJiluFragment = TestFragment.newInstance("","");

                StdDyJlNewFragment dyJlListFragment1 = StdDyJlNewFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, dyJlListFragment1).commit();
                addFragemntToShow(1, dyJlListFragment1);
                break;
            case R.id.btnShijianShezhi:
                //bleConnectUtil.disConnect();
                // 移除所有的handler 消息.
                //mHandler.removeCallbacksAndMessages(null);
                //Config.fragmentList.remove(0);
                StdShijianShezhiFragment shijianShezhiFragment = StdShijianShezhiFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, shijianShezhiFragment).commit();
                addFragemntToShow(1, shijianShezhiFragment);
                break;
            case R.id.btnXitongShezhi:
                //bleConnectUtil.disConnect();
                // 移除所有的handler 消息.
                //mHandler.removeCallbacksAndMessages(null);

                StdXitongShezhiFragment xitongShezhiFragment = StdXitongShezhiFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, xitongShezhiFragment).commit();
                addFragemntToShow(1, xitongShezhiFragment);
                break;
            case R.id.llFanhui:
                finish();
                break;
        }
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

        String strStdCsAllStdSave = "6886"+"71"+nian+yue+ri+shi+fen+miao+"0000";//下发时间0x71
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        sendDataByBle(sendAllYnSave,"");
    }
    /**
     * 屏幕左下角时间显示，每隔一秒执行一次
     */
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
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
            Log.e(TAG, "onSuccessSend: ");
        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e(TAG, "onDisconnect: ");
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
                Log.e("--->1", "currentSendAllOrder:" + currentSendAllOrder);
                sData = CheckUtils.hex2byte(currentSendOrder);
                Log.e("--->2", "currentSendAllOrder:" + sData);
                //if(BleConnectUtil.mBluetoothGattCharacteristic==null){
                mBluetoothGattCharacteristic.setValue(sData);
                Log.e("--->3", "currentSendAllOrder:" + mBluetoothGattCharacteristic.getUuid().toString());
                //}
                isSuccess[0] = bleConnectUtil.sendData(mBluetoothGattCharacteristic);
                Log.e("--->4", "currentSendAllOrder:" + isSuccess[0]);
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
                        mHandler.sendEmptyMessage(1111);
                    }
                    Log.e("--->", "是否发送成功2：" + isSuccess[0]);
                }
            }, (currentSendAllOrder.length() / 40 + 1) * 15);
        }
    }

    /**
     * 隐藏所有已加入的fragment
     */
    private void setAllFragmentToHideen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < Config.fragmentList.size(); i++) {
            Fragment fragment = Config.fragmentList.get(i);
            if (fragment.isAdded()) {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 替换中间的fragment
     *
     * @param type 0是第一个，默认显示的；1代表不是首次进入程序，点击切换过
     * @param addedFragment
     */
    public void addFragemntToShow(int type, Fragment addedFragment) {
        if (null == addedFragment) {
            return;
        }
        if (type != 0) {
            // 隐藏所有fragment
            setAllFragmentToHideen();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 已经添加fragment
        if (addedFragment.isAdded()) {
            transaction.show(addedFragment);
        } else { // 新加入的fragment
            transaction.add(R.id.frameHome, addedFragment);
            Config.fragmentList.add(addedFragment);
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, keyCode + "," + KeyEvent.KEYCODE_BACK);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 阻止返回键事件
            Log.e(TAG, "back.....");
            //return true;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            }
            bleConnectUtil.mBluetoothGatt.close();
        }
        return super.onKeyDown(keyCode, event);
    }
}