package com.yc.allbluetooth.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.adapter.BlueToothListBaseAdapter;
import com.yc.allbluetooth.bianbi.activity.BbHomeActivity;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dlzk.activity.DlzkHomeActivity;
import com.yc.allbluetooth.dtd10c.activity.Dtd10cHomeActivity;
import com.yc.allbluetooth.entity.BlueTooth;
import com.yc.allbluetooth.entity.EventMsg;
import com.yc.allbluetooth.huilu.activity.HlDianzuceshiActivity;
import com.yc.allbluetooth.std.activity.StdHomeActivity;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.Constants;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.youzai.activity.YzHomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    BluetoothManager bluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    final int REQUEST_CODE = 1;
    final int REQUEST_ENABLE_BT = 0;
    Button btnOpen;
    Button btnClose;
    Button btnScan;
    Button btnDisconnect;
    Button btnZhongYing;
    Boolean isScanning = false;//搜索状态
    ListView lv;
    BluetoothLeScanner bluetoothLeScanner;
    private List<BlueTooth> listDevice;
    private BlueToothListBaseAdapter adapter;
    List<BluetoothDevice> listDevices;

    BleConnectUtil bleConnectUtil;

    private int selectPos;
    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;
    String newMsgStr = "";
    String yqlx = "";
    String bjdz = "";

    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"main")) {
                        String msgStr = msg.obj.toString();
                        Log.e(TAG + "接收2===", msgStr);
                        if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                            //newMsgStr2 = "";
                            newMsgStr = msgStr;
                            Log.e(TAG + 1, newMsgStr);
                        } else if (IndexOfAndSubStr.isIndexOf(msgStr, "FEEF")) {
                            newMsgStr = msgStr;
                            Log.e(TAG + 1, newMsgStr);
                        } else {
                            if (IndexOfAndSubStr.isIndexOf(newMsgStr, msgStr) == false) {
                                newMsgStr = newMsgStr + msgStr;
                            }
                            //可以
                            Log.e(TAG + 2, newMsgStr);
//                        if("FEEF0400240064000019070000000100020000000000000000000000000002000206110102001D336E00FDDF".equals(newMsgStr)){
//                            //sendDataByBle("feef04B00700170606000f1237fddf","");
//                            //sendDataByBle("feef04C00000fddf","");
//                            sendDataByBle("feef04211c0014006009070000000100020031323334415344460000000000000007fddf","");
//                        }
                        }
                        if (IndexOfAndSubStr.isIndexOf(newMsgStr, "6677") && newMsgStr.length() >= 26) {
                            if (StringUtils.isEmpty(yqlx)) {
                                yqlx = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                //yqlx = "34";//只能20
                                Log.e(TAG + "仪器类型", yqlx);
                                Config.yqlx = yqlx;
                                if ((StringUtils.isEquals(yqlx, "31")) || (StringUtils.isEquals(yqlx, "34")) ||
                                        (StringUtils.isEquals(yqlx, "35")) || (StringUtils.isEquals(yqlx, "36"))) {//三通道10、20、40、50
                                    //EventBus.getDefault().unregister(this);
                                    startActivity(new Intent(MainActivity.this, StdHomeActivity.class));
                                } else if (StringUtils.isEquals(yqlx, "32")) {//直阻10C
                                    startActivity(new Intent(MainActivity.this, Dtd10cHomeActivity.class));
                                } else if (StringUtils.isEquals(yqlx, "33")) {//短路阻抗
                                    startActivity(new Intent(MainActivity.this, DlzkHomeActivity.class));
                                }
                            }

                        } else if (IndexOfAndSubStr.isIndexOf(newMsgStr, "FEEF")) {//确定设备型号是有载
                            yqlx = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                            bjdz = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                            Log.e(TAG, "仪器类型:" + yqlx + ",本机地址：" + bjdz);
                            Config.yqlx = yqlx;
                            Config.yzBenjiAddress = bjdz;
                            startActivity(new Intent(MainActivity.this, YzHomeActivity.class));
                        } else if (IndexOfAndSubStr.isIndexOf(newMsgStr, "6677") && newMsgStr.length() == 18) {//确定设备型号
                            yqlx = StringUtils.subStrStartToEnd(newMsgStr, 8, 10);
                            if (StringUtils.isEquals(yqlx, "38")) {//变比
                                Config.yqlx = yqlx;
                                startActivity(new Intent(MainActivity.this, BbHomeActivity.class));
                            } else if (StringUtils.isEquals(yqlx, "39")||StringUtils.isEquals(yqlx,"3A")) {//回路100A：39；200A：3A
                                //startActivity(new Intent(MainActivity.this, HuiluHomeActivity.class));
                                Config.yqlx = yqlx;
                                startActivity(new Intent(MainActivity.this, HlDianzuceshiActivity.class));
                            }
                        } else {//如果不是助磁、直阻、短路阻抗，发送获取有载设备型号指令
                            sendDataByBle("feefaaaa5555fddf", "");//feef..aa5555fddf(..:04/05)
                        }
                        break;
                    }
                /*case 1000:
                    regainBleDataCount = 0;
                    bleFlag = false;
                    handler.removeCallbacks(checkConnetRunnable);

                    Toast.makeText(MainActivity.this, "超时请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case 1111:
                    bleConnectUtil.disConnect();
                    break;*/
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        initView();
        Config.ymType = "main";

    }

    public void initView() {

        bleConnectUtil = new BleConnectUtil(this);
        EventBus.getDefault().register(this);
        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        btnScan = findViewById(R.id.btnScan);
        //btnDisconnect = findViewById(R.id.btnDisconnect);
        btnZhongYing = findViewById(R.id.btnZhongYing);
        lv = findViewById(R.id.lvBlueTooth);
        //btnOpen.setOnClickListener(this);
        //btnClose.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        //btnDisconnect.setOnClickListener(this);
        btnZhongYing.setOnClickListener(this);
        listDevice = new ArrayList<>();
        listDevices = new ArrayList<>();
        adapter = new BlueToothListBaseAdapter(this, listDevice);
        lv.setAdapter(adapter);
        //改变选中后的item背景颜色
        //lv.setSelector(R.color.purple_200);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG + "点击", listDevice.get(position).getName() + "," + listDevice.get(position).getAddress());
                yqlx = "";
                Config.lyAddress = listDevice.get(position).getAddress();
                if (isEnable()) {
                    for (int i = 0; i < listDevice.size(); i++) {
                        listDevice.get(i).setType(getString(R.string.weilianjie));
                    }
                    selectPos = position;
                    listDevice.get(position).setType(getString(R.string.lianjiezhong));
                    adapter.notifyDataSetChanged();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                    }
                    Log.e(TAG + "dev", listDevices.get(position) + "");
                    if (isScanning) {
                        bluetoothLeScanner.stopScan(mScanCallback); //停止扫描
                        isScanning = false;
                        btnScan.setText(R.string.sousuolanya);
                        Log.e(TAG, "stop....");
                    }

                    bleConnectUtil.connectBle(listDevices.get(position));
                } else {
                    Toast.makeText(MainActivity.this, R.string.qingkaiqilanya, Toast.LENGTH_SHORT).show();
                }
            }
        });
        initPermission();
        if (checkBle(this)) {
            //openBluetooth(this, true);
            init();
        } else {
            Log.e(TAG, "不支持蓝牙4.0");
            Toast.makeText(MainActivity.this, "不支持蓝牙4.0", Toast.LENGTH_SHORT).show();

        }
    }

    // todo 蓝牙动态申请权限
    private void initPermission() {
        List<String> mPermissionList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 版本大于等于 Android12 时
            // 只包括蓝牙这部分的权限，其余的需要什么权限自己添加
            Log.e("权限，安卓>=12", "1111111111");

            mPermissionList.add(Manifest.permission.BLUETOOTH_SCAN);
            mPermissionList.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            mPermissionList.add(Manifest.permission.BLUETOOTH_CONNECT);
        } else {
            // Android 版本小于 Android12 及以下版本
            Log.e("权限，安卓<12", "22222222222222222");
            mPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            mPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(MainActivity.this, mPermissionList.toArray(new String[0]), 1001);
        }
        //返回值接收
        ActivityResultLauncher resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.e(TAG, result.getResultCode() + "");//拒绝：0
                if (result.getResultCode() == RESULT_CANCELED) {//0
                    Log.d(TAG, "打开蓝牙失败");
                } else {//-1开启
                    Log.d(TAG, "蓝牙已开启");
                }
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "打开");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                resultLauncher.launch(intent);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                }
                Log.e(TAG, "关闭");
                mBluetoothAdapter.disable();
            }
        });
    }
    /**
     * 检测手机是否支持4.0蓝牙
     * @param context  上下文
     * @return true--支持4.0  false--不支持4.0
     */
    private boolean checkBle(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {  //API 18 Android 4.3
            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                return false;
            }
            mBluetoothAdapter = bluetoothManager.getAdapter();  //BLUETOOTH权限
            if (mBluetoothAdapter == null) {
                return false;
            } else {
                Log.d(TAG, "该设备支持蓝牙4.0");
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取蓝牙状态
     */
    public boolean isEnable() {
        if (mBluetoothAdapter == null) {
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 动态获取打开蓝牙搜索权限，然后打开蓝牙
     */
    public void init() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

//        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            startActivityForResult(intent, REQUEST_ENABLE_BT);
//        }
    }
    //在利用 onActivityResult方法
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_ENABLE_BT) {
//            Log.d(TAG, resultCode + "");
//            if (resultCode == RESULT_CANCELED) {//0
//                Log.d(TAG, "打开蓝牙失败");
//            } else {//-1
//                Log.d(TAG, "蓝牙已开启");
//            }
//        }
//    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "权限开启:"+requestCode);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                //Toast.makeText(this, "权限开启失败", Toast.LENGTH_LONG).show();
                Log.e(TAG, "权限开启失败");
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMsg eventMsg) {
        if(StringUtils.isEquals(Config.ymType,"main")){
            Log.e(TAG+"跳","跳转1");
            Log.e(TAG+"ev",eventMsg.toString());
            Log.e(TAG+"evMsg",eventMsg.getMsg());

            switch (eventMsg.getMsg()) {
                case Constants.BLE_CONNECTION_FINISH_MSG:
                    Log.i("蓝牙连接状态：","蓝牙连接完成");
                    if (bleConnectUtil.isConnected()) {
                        listDevice.get(selectPos).setType(getString(R.string.yilianjie));
                        adapter.notifyDataSetChanged();
                        Log.e(TAG,"跳转");

                        bleConnectUtil.setCallback(blecallback);
                        //发送获取仪器类型，根据不同返回值跳转到不同应用页面；0x31三通道直阻，0x32单通道直阻（10c），0x33短路阻抗
//                    startActivity(new Intent(this, StdHomeActivity.class));
                        Log.e("====","====================");
                        //获取设备类型
                        //sendDataByBle(SendUtil.initSend("5b"),"");
                        //sendDataByBle("feefaaaa5555fddf","");
                        sendIsSbxh();
                        //startActivity(new Intent(this, YzHomeActivity.class));

                        //sendDataByBle("68868900000000000000003EE4","");
                        //sendDataByBle("feef04211c00140060090700000009009A0000000000000000000000000002000206fddf","");//第七位是文档第一位
                        //sendDataByBle("feef04000000fddf","");
                        //startActivity(new Intent(this, StdHomeActivity.class));
                        //startActivity(new Intent(this, DlzkHomeActivity.class));
                    } else {
                        //Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }

    }
    public void scan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_CODE);
        }
        //Handler mHandler = new Handler();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
        }
        listDevices.clear();
        listDevice.clear();
        bluetoothLeScanner.startScan(mScanCallback);
        isScanning = true;
        Log.e(TAG, "scan()....");
        //目前暂定为无限时间搜索，去掉十秒
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                bluetoothLeScanner.stopScan(mScanCallback); //停止扫描
//                isScanning = false;
//                Log.e(TAG, "stop....");
//                btnScan.setText(R.string.sousuolanya);
//            }
//        }, 10000);
    }

    // 扫描结果Callback
    private final ScanCallback mScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //Log.e(TAG,result.toString());
            //Log.e(TAG,result.getScanRecord().getServiceUuids()+"");
            String serviceUuid = result.getScanRecord().getServiceUuids()+"";
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
            }
            BluetoothDevice dev = result.getDevice();//获取BLE设备信息
            // result.getScanRecord() 获取BLE广播数据
            //Log.e(TAG + "地址", dev.toString());


            //Log.e(TAG + "名称", dev.getName() + "");
            //lv.setSelector(R.color.white);
            boolean NoAdd = true;
            for (int i=0;i<listDevice.size();i++) {
                if (listDevice.get(i).getAddress().equals(dev.getAddress())) {
                    NoAdd = false;
                    break;
                }
            }
            if(NoAdd){//搜索到之后，去重添加
                //if(StringUtils.noEmpty(dev.getName())&& IndexOfAndSubStr.isIndexOf(dev.getName(),"YCDI")){
                if(result.getScanRecord().getServiceUuids()!=null){//UUIDs不为空
                    for(int i=0;i<result.getScanRecord().getServiceUuids().size();i++){//可能不只有一个
                        if(StringUtils.isEquals(Config.sousuoUuid,result.getScanRecord().getServiceUuids().get(i)+"")){//如果和设定的搜索UUID匹配成功，加入列表
                            Log.e(TAG,dev.getName()+","+dev.getAlias()+","+dev.getAddress()+","+serviceUuid+","+dev.getType());
                            listDevices.add(dev);
                            BlueTooth blueTooth = new BlueTooth();
                            if(StringUtils.isEquals(dev.getAddress(),"C4:23:04:14:05:A6")){
                                blueTooth.setName("YZ-1A");
                            } else if (StringUtils.isEquals(dev.getAddress(),"C4:23:04:14:05:A2")) {
                                blueTooth.setName("YZ-2A");
                            } else {
                                blueTooth.setName(dev.getName());
                            }
                            blueTooth.setAddress(dev.getAddress());
                            blueTooth.setType(getString(R.string.weilianjie));
                            //Log.e(TAG,dev.getName()+","+dev.getAddress());
                            listDevice.add(blueTooth);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }


                // }

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
            Log.e(TAG+"接收",str);
            Message message = new Message();
            message.obj = str;
            message.what = Config.BLUETOOTH_GETDATA;
            handler.sendMessage(message);
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
            message.what = 1111;
            handler.sendMessage(message);
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
                sData = CheckUtils.hex2byte(currentSendOrder);
                //if(BleConnectUtil.mBluetoothGattCharacteristic==null){
                mBluetoothGattCharacteristic.setValue(sData);
                //}
                isSuccess[0] = bleConnectUtil.sendData(mBluetoothGattCharacteristic);
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isSuccess[0]) {
                        //dialog.dismiss();
                        handler.sendEmptyMessage(1111);
                    }
                    Log.e("--->", "是否发送成功：" + isSuccess[0]);
                }
            }, (currentSendAllOrder.length() / 40 + 1) * 15);
        }
    }

    /**
     * 蓝牙连接检测线程
     */
    Runnable checkConnetRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.e(TAG,"bleFlag1:"+bleFlag);
            if (!bleFlag) {
                Log.e(TAG,"bleFlag2:"+bleFlag);
                //没有在指定时间收到回复
                if (regainBleDataCount > 2) {
                    Log.e(TAG,"bleFlag3:"+bleFlag);
                    handler.sendEmptyMessage(1000);
                } else {
                    Log.e(TAG,"bleFlag4:"+bleFlag);
                    regainBleDataCount++;

                    sendDataByBle(currentSendOrder, "");
                    //这里再次调用此Runnable对象，以实现每三秒实现一次的定时器操作
                    handler.postDelayed(checkConnetRunnable, 3000);
                }
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpen:
                //init();
                break;
            case R.id.btnClose:
                if (isEnable()&&isScanning==false) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    }
                    //bluetoothLeScanner.stopScan(mScanCallback);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mBluetoothAdapter.disable();
                }
                break;
            case R.id.btnScan:
                if (isEnable()) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    }else{
                        if(isScanning){
                            isScanning = false;
                            btnScan.setText(R.string.sousuolanya);
                            bluetoothLeScanner.stopScan(mScanCallback);
                            Log.e(TAG, "stop1....");

                        }else{
                            isScanning = true;
                            btnScan.setText(R.string.tingzhisousuo);
                            scan();
                        }
                    }


                } else {
                    Log.e(TAG, "请开启蓝牙");
                    Toast.makeText(MainActivity.this,R.string.qingkaiqilanya,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnZhongYing:
                //中英文切换
                String sta= getResources().getConfiguration().locale.getLanguage();
                translateText(sta);
                break;

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"Main.....onDestroy");
        if (bleConnectUtil != null) {
            bleConnectUtil.disConnect();
        }
        EventBus.getDefault().unregister(this);
        // 移除所有的handler 消息.
        handler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"Main.....onResume");
        Config.ymType = "main";
//        bleConnectUtil.disConnect();
//        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().postSticky(Constants.BLE_CONNECTION_FINISH_MSG);
    }

    @Override
    protected void onPause() {
        Log.e(TAG,"Main.....onPause");
        //bleConnectUtil.disConnect();
        //handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG,"Main.....onStop");
        bleConnectUtil.disConnect();
        handler.removeCallbacks(checkConnetRunnable);
        handler.removeCallbacksAndMessages(null);

        super.onStop();
    }
    public void translateText(String sta){
        if (sta.equals("zh")){
            Resources resources = this.getResources();// 获得res资源对象
            Configuration config = resources.getConfiguration();// 获得设置对象
            DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = Locale.US; // 英文
            Config.zyType = "en";
            resources.updateConfiguration(config, dm);
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();
            //this.recreate();
        }else {
            //转换为中文
            Resources resources = this.getResources();// 获得res资源对象
            Configuration config = resources.getConfiguration();// 获得设置对象
            DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = Locale.SIMPLIFIED_CHINESE; // 英文
            Config.zyType = "zh";
            resources.updateConfiguration(config, dm);
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();
            //this.recreate();
        }
    }

    /**
     * 蓝牙连接成功后，判断设别型号
     */
    public void sendIsSbxh(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                sendDataByBle(SendUtil.initSend("5b"),"");
            }
        }, 300);//3秒后执行Runnable中的run方法
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                sendDataByBle("feefaaaa5555fddf","");
            }
        }, 500);//3秒后执行Runnable中的run方法
    }
}