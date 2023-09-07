package com.yc.allbluetooth.ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.callback.MyBleCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.entity.EventMsg;
import com.yc.allbluetooth.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

/**
 * Date:2022/11/3 14:22
 * author:jingyu zheng
 */
public class BleConnectUtil {
    private final String TAG = "BleConnectUtil";
    /**********开始连接Service**********/
    private String serviceUuidStr, writeCharactUuid; //notifyCharactUuid;
    public static BluetoothGattCharacteristic mBluetoothGattCharacteristic, mBluetoothGattCharacteristicNotify;

    public static BluetoothAdapter mBluetoothAdapter;
    public BluetoothManager mBluetoothManager;
    public BluetoothGatt mBluetoothGatt;
    public BluetoothDevice device;
    public static String wsDeviceAddress = "";

    public String mDeviceAddress = "";
    public boolean mConnected;

    public View viewFd;
    public View viewDy;
    public View viewSave;
    public AlertDialog TipsFangdian;
    public AlertDialog TipsDayin;
    public AlertDialog TipsSave;

    /**
     * 操作间要有至少15ms的间隔
     */
    private static final int DELAY_TIME = 15;
    private Activity context;
    private MyBleCallBack myBleCallBack;

    Handler handler = new Handler();

    public BleConnectUtil(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        //服务uuid
        serviceUuidStr = "0000ffe0-0000-1000-8000-00805f9b34fb";
        //serviceUuidStr = "0000bbb1-0000-1000-8000-00805f9b34fb";
        //写(透传)通道 uuid
        writeCharactUuid = "0000ffe1-0000-1000-8000-00805f9b34fb";
        //writeCharactUuid = "0000ccc1-0000-1000-8000-00805f9b34fb";
        //通知通道 uuid
        //notifyCharactUuid = "0000ddd1-0000-1000-8000-00805f9b34fb";
        //notifyCharactUuid = "00002901-0000-1000-8000-00805f9b34fb";
        //读
        //notifyCharactUuid = "00002902-0000-1000-8000-00805f9b34fb";

        mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mConnected = false;

        //public void tc(){

        viewFd = LayoutInflater.from(context).inflate(R.layout.dialog_fangdian, null, false);
        TipsFangdian = new AlertDialog.Builder(context, R.style.MyDialog).setView(viewFd).create();
        viewDy = LayoutInflater.from(context).inflate(R.layout.dialog_dayin, null, false);
        TipsDayin = new AlertDialog.Builder(context, R.style.MyDialog).setView(viewDy).create();
        viewSave = LayoutInflater.from(context).inflate(R.layout.dialog_save, null, false);
        TipsSave = new AlertDialog.Builder(context, R.style.MyDialog).setView(viewSave).create();

        TipsFangdian.setCanceledOnTouchOutside(false);//点周围禁止关闭弹窗
        TipsDayin.setCanceledOnTouchOutside(false);
        TipsSave.setCanceledOnTouchOutside(false);
        //}
    }

    /**
     * 判断是否可见
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void bluetoothIsAble(MyBleCallBack myBleCallBack) {
        if (!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "蓝牙不可见");

            //方法二 推荐
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                //进行授权
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_ADVERTISE}, 5);
            }
            context.startActivity(enable);
        } else {
            Log.d(TAG, "蓝牙可见");
            this.myBleCallBack = myBleCallBack;
            scanLeDevice();
        }
    }


    /**
     * 搜索Ble设备
     */
    public void scanLeDevice() {
        Log.i("搜索=", "1111");
        if (mBluetoothAdapter == null) {
            Log.i("搜索=", "1112");
            return;
        }
        //扫描所有设备
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Config.REQUEST_CODE_BLUETOOTH_SCAN);
        }
        Log.i("搜索=", "1113");
        mBluetoothAdapter.startLeScan(mLeScanCallback);

        //扫描同一类设备
//        UUID[] serviceUuids = {UUID.fromString(serviceUuidStr)};
//        mBluetoothAdapter.startLeScan(serviceUuids, mLeScanCallback);
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        if (mBluetoothAdapter != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                //进行授权
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Config.REQUEST_CODE_BLUETOOTH_SCAN);
            }
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * 蓝牙扫描回调接口
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //进行授权
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, Config.REQUEST_CODE_BLUETOOTH_CONNECT);
            }
            if (device.getName() == null) {
                return;
            }
            myBleCallBack.callbleBack(device);
        }
    };

    public void connectBle(BluetoothDevice device) {
        //获取所需地址
        mDeviceAddress = device.getAddress();
        wsDeviceAddress = device.getAddress();
        //SPUtils.put(context, "deviceAddress", device);
        Log.e(TAG, "connectBle: " + mDeviceAddress);
        new connectThread().start();
    }

    /**
     * 连接并且读取数据线程
     */
    private class connectThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                //连接
                if (!connect(mDeviceAddress, 10000, 2)) {
                    disConnect();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().postSticky(new EventMsg(Constants.BLE_CONNECTION_FINISH_MSG));
                        }
                    });
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String last_mac = "";

    // 连接设备
    public boolean connect(String mac, int sectime, int reset_times) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        disConnect();
        for (int i = 0; i < reset_times; i++) {
            initTimeFlag(WORK_onServicesDiscovered);

            if ((mBluetoothGatt != null) && mac.equals(last_mac)) {
                // 当前已经连接好了
                if (mConnected == true) {
                    return true;
                }
                Log.e(TAG, "重连");
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    //进行授权
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
                }
//                if(mBluetoothGatt!=null){
//                    mBluetoothGatt.close();
//                }
                mBluetoothGatt.connect();
            } else {
                Log.e(TAG, "新连接");
                disConnect(); // 新设备进行连接
                device = mBluetoothAdapter.getRemoteDevice(mac);
                Log.e(TAG, "device:" + device);
                if (device == null) {
                    System.out.println("device == null");
                    return false;
                }

                mBluetoothGatt = device.connectGatt(context, false, mGattCallback);//true,断开后自动连接
                if (mBluetoothGatt.connect()){
                    Log.e(">>>蓝牙连接","成功");
                    if(mBluetoothGatt.discoverServices()){
                        Log.e(">>>蓝牙连接","discoverServices已经开始了");
                    }else{
                        Log.e(">>>蓝牙连接","discoverServices没有开始");
                    }
                }else {
                    Log.e(">>>蓝牙连接","失败");
                }
            }

            if (startTimeOut(sectime)) {
                System.out.println("连接超时");
                disConnect();
                continue;
            }

            mConnected = true;
            last_mac = mac;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    // 断开连接
    public boolean disConnect() {
        Log.d(TAG, "mBluetoothGatt:" + mBluetoothGatt);
        if (mBluetoothGatt != null) {
//            setEnableNotify(BleConnectUtil.mBluetoothGattCharacteristicNotify, false);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //进行授权
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
            }
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();//在gattCallback收到STATE_DISCONNECTED后再执行gatt.close();
            mBluetoothGatt = null;
            mConnected = false;
            mDeviceAddress = "";
            return true;
        }
        return false;
    }

    /**
     * 销毁连接
     */
    public void close() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * 查看连接状态
     */
    public boolean isConnected() {
        return mConnected;
    }

    /**
     * BLE回调操作
     */
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            // 连接成功
            if (newState == BluetoothProfile.STATE_CONNECTED) {//2
                Log.e(TAG, "连接成功");
                mConnected = true;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    //进行授权
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
                }
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                System.out.println("STATE_CONNECTED");
                if (work_witch == WORK_onConnectionStateChange) {
                    work_ok_flag = true;
                }
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//0
                Log.d(TAG, "连接断开");
                if (mConnected) {
                    mConnected = false;
                }
                EventBus.getDefault().postSticky(Constants.BLE_CONNECTION_FINISH_MSG);
                if (callback != null) {
                    callback.onDisconnect();
                }
                //                if(mBluetoothGatt!=null){
//                    mBluetoothGatt.close();//待定，连接断开后，关闭gatt
//                }
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "发现设备");
                System.out.println("onServicesDiscovered");
                if (work_witch == WORK_onServicesDiscovered) {
                    work_ok_flag = true;
                }
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                }
                //gatt.requestMtu(512);
                //发现设备，遍历服务，初始化特征
                initBLE(gatt);
            } else {
                System.out.println("onServicesDiscovered fail-->" + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (work_witch == WORK_onCharacteristicRead) {
                    work_ok_flag = true;
                }
                if (callback != null) {
                    callback.onRecive(characteristic);
                }
            } else {

            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            //Log.e(TAG, "onCharacteristicChanged");
            //Log.i(TAG,"onCharacteristicChanged,"+work_witch+","+work_ok_flag);
            if (work_witch == WORK_onCharacteristicChanged) {
                work_ok_flag = true;
            }

            if (callback != null) {
                callback.onRecive(characteristic);
            }
        }

        /**
         * 收到BLE终端写入数据回调
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicWrite");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (work_witch == WORK_onCharacteristicWrite) {
                    work_ok_flag = true;
                }
                if (callback != null) {
                    callback.onSuccessSend();
                }
            } else {
                System.out.println("write fail->" + status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorWrite");
            //context.startActivity(new Intent(context, HomeActivity.class));
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (work_witch == WORK_onDescriptorWrite) {
                    work_ok_flag = true;
                }
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.e(TAG, "onReadRemoteRssi");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (work_witch == WORK_onReadRemoteRssi) {
                    work_ok_flag = true;
                }
                rssi_value = (rssi_value + rssi) / 2;
                // rssi_value = rssi;
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorRead");
            if ((status == BluetoothGatt.GATT_SUCCESS)
                    && (work_witch == WORK_onDescriptorRead)) {
                work_ok_flag = true;
            }
        }
    };

    //初始化特征
    public void initBLE(BluetoothGatt gatt) {
        if (gatt == null) {
            return;
        }
        //遍历所有服务
        for (android.bluetooth.BluetoothGattService BluetoothGattService : gatt.getServices()) {
            Log.e(TAG, "--->BluetoothGattService遍历所有服务:" + BluetoothGattService.getUuid().toString());

            //遍历所有特征
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : BluetoothGattService.getCharacteristics()) {
                //Log.e("---->gattCharacteristic",context.toString());
                //Log.e("---->gattCharacteristic","遍历所有特征："+ bluetoothGattCharacteristic.getUuid().toString());
                String str = bluetoothGattCharacteristic.getUuid().toString();
                if (str.equals(writeCharactUuid)) {
                    mBluetoothGattCharacteristic = bluetoothGattCharacteristic;
                    mBluetoothGattCharacteristicNotify = bluetoothGattCharacteristic;
                }
//                else if (str.equals(notifyCharactUuid)) {
//                    mBluetoothGattCharacteristicNotify = bluetoothGattCharacteristic;
//                }
            }
        }
        //判断是否获取到特征
        if ((null == mBluetoothGattCharacteristic) || (null == mBluetoothGattCharacteristicNotify)) {
            //连接失败
            mConnected = false;
        } else {
            setNotify(mBluetoothGattCharacteristicNotify);
            setEnableNotify(mBluetoothGattCharacteristicNotify, true);
            //连接成功
            mConnected = true;
            try {
                // 刚刚使能上需要等待一下才能
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "mConnected:" + mConnected);
        EventBus.getDefault().postSticky(new EventMsg(Constants.BLE_CONNECTION_FINISH_MSG));
    }

    public List<BluetoothGattService> getServiceList() {
        return mBluetoothGatt.getServices();
    }

    /**
     * 设置可通知
     */
    public boolean setNotify(BluetoothGattCharacteristic data_char) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }

        if (data_char == null) {
            return false;
        }

        if (!isConnected()) {
            return false;
        }

        // 查看是否带有可通知属性，‘&’在java里面这个是一个‘与’运算符，就是二进制中的 同为1才为1;x=5&3,5 = 0101;3 = 0011;x = 0001
        Log.e(TAG,"通知的值对比："+data_char.getProperties()+","+BluetoothGattCharacteristic.PROPERTY_NOTIFY+","+BluetoothGattCharacteristic.PROPERTY_INDICATE);
        if (0 != (data_char.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                //进行授权
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
            }
            mBluetoothGatt.setCharacteristicNotification(data_char, true);
            BluetoothGattDescriptor descriptor = data_char.getDescriptor(
                    UUID.fromString(mBluetoothGattCharacteristic.getDescriptors().get(0).getUuid().toString()));//notifyCharactUuid;0000ffe1-0000-1000-8000-00805f9b34fb
                    //UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
            //mBluetoothGattCharacteristic.getDescriptors().get(0).getUuid().toString();
//            for (BluetoothGattDescriptor descriptor1:mBluetoothGattCharacteristic.getDescriptors()){
//                Log.e(TAG, "BluetoothGattDescriptor: "+descriptor1.getUuid().toString());
//
//
//            }
            if(descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)){
                mBluetoothGatt.writeDescriptor(descriptor);
            };

        } else if (0 != (data_char.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE)) {
            mBluetoothGatt.setCharacteristicNotification(data_char, true);
            BluetoothGattDescriptor descriptor = data_char.getDescriptor(
                    UUID.fromString(mBluetoothGattCharacteristic.getDescriptors().get(0).getUuid().toString()));//notifyCharactUuid;0000ffe1-0000-1000-8000-00805f9b34fb
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
        return true;
    }

    /**
     * 设置允许通知
     */
    public boolean setEnableNotify(BluetoothGattCharacteristic data_char, boolean enable) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }

        if (data_char == null) {
            return false;
        }

        if (!isConnected()) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
        }
        mBluetoothGatt.setCharacteristicNotification(data_char, enable);

        return true;
    }

    /**
     * 读取信息
     */
    public boolean readData(BluetoothGattCharacteristic data_char) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }

        if (data_char == null) {
            return false;
        }

        if (!isConnected()) {
            return false;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
        }
        mBluetoothGatt.readCharacteristic(data_char);

        return true;
    }

    /**
     * 发送并带返回的命令
     */
    public boolean sendCmd(BluetoothGattCharacteristic data_char, int milsec) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }

        if (data_char == null) {
            return false;
        }

        if (!isConnected()) {
            return false;
        }

        initTimeFlag(WORK_onCharacteristicChanged);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
        }
        mBluetoothGatt.setCharacteristicNotification(data_char, true);
        mBluetoothGatt.writeCharacteristic(data_char);

        if (startTimeOut(milsec)) {
            System.out.println("startTimeOut");
            return false;
        }

        mBluetoothGatt.setCharacteristicNotification(data_char, false);

        try { // 发送数据一定要有一些延迟
            Thread.sleep(DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    // 发送数据
    public boolean sendData(BluetoothGattCharacteristic data_char) {
        if (!mBluetoothAdapter.isEnabled()) {
            // 没有打开蓝牙
            return false;
        }

        if (data_char == null) {
            return false;
        }

        if (!isConnected()) {
            return false;
        }
        if (mBluetoothGatt != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //进行授权
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
            }
            mBluetoothGatt.setCharacteristicNotification(data_char, true);
            mBluetoothGatt.writeCharacteristic(data_char);
        } else {
            return false;
        }
        try { // 发送数据一定要有一些延迟
            Thread.sleep(DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 读属性值
     */
    public boolean readDescriptor(BluetoothGattDescriptor descriptor, int milsec) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }

        if (descriptor == null) {
            return false;
        }

        if (!isConnected()) {
            return false;
        }

        initTimeFlag(WORK_onDescriptorRead);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
        }
        mBluetoothGatt.readDescriptor(descriptor);

        if (startTimeOut(milsec)) {
            return false;
        }

        try { // 发送数据一定要有一些延迟
            Thread.sleep(DELAY_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 读取
     */
    private int rssi_value;

    public int getRssi(int milsec) {
        // 没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return 0;
        }
        initTimeFlag(WORK_onReadRemoteRssi);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //进行授权
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 3);
        }
        mBluetoothGatt.readRemoteRssi();

        if (startTimeOut(milsec)) {
            return 0;
        }

        return rssi_value;
    }

    // 回调方法
    private BleConnectionCallBack callback;

    // 设置回调
    public void setCallback(BleConnectionCallBack callback) {
        this.callback = callback;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    // 初始化定时变量
    private int work_witch = 0;
    private final int WORK_onConnectionStateChange = 1;
    private final int WORK_onServicesDiscovered = 2;
    private final int WORK_onCharacteristicRead = 4;
    private final int WORK_onCharacteristicChanged = 5;
    private final int WORK_onCharacteristicWrite = 6;
    private final int WORK_onDescriptorWrite = 7;
    private final int WORK_onReadRemoteRssi = 8;
    private final int WORK_onDescriptorRead = 9;

    private void initTimeFlag(int work_index) {
        work_witch = work_index;
        timeout_flag = false;
        work_ok_flag = false;
    }

    // 开始计时
    private boolean startTimeOut(int minsec) {
        handl.sendEmptyMessageDelayed(HANDLE_TIMEOUT, minsec);
        while (!work_ok_flag) {
            try {
                Thread.sleep(100);//100
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (exit_flag) {
                return true;
            }
            if (timeout_flag) {
                return true;
            }
        }
        handl.removeMessages(HANDLE_TIMEOUT);

        return false;
    }

    // 强制退出
    private boolean exit_flag = false;

    public void exit() {
        disConnect();
        handl.removeMessages(HANDLE_TIMEOUT);
        exit_flag = true;
    }

    // 事件处理
    private static final int HANDLE_TIMEOUT = 0;
    private boolean timeout_flag = false;
    private boolean work_ok_flag = false;
    private Handler handl = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == HANDLE_TIMEOUT) {
                Log.d(TAG, "超时");
                timeout_flag = true;
                return;
            }

        }
    };

}
