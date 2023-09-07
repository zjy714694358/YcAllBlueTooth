package com.yc.allbluetooth.callback;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Date:2022/11/3 14:29
 * author:jingyu zheng
 */
public interface BleConnectionCallBack {
    void onRecive(BluetoothGattCharacteristic data_char);

    void onSuccessSend();

    void onDisconnect();
}

