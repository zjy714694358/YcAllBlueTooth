package com.yc.allbluetooth.bianbi.activity.sanxiang;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.util.LianjieZubie;
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

public class SxBbCeshiActivity extends AppCompatActivity {

    private TextView tvCsdlA;
    private TextView tvCsdlB;
    private TextView tvCsdlC;
    private TextView tvYcdyA;
    private TextView tvYcdyB;
    private TextView tvYcdyC;
    private TextView tvEcdyA;
    private TextView tvEcdyB;
    private TextView tvEcdyC;
    private TextView tvFanhui;

    String crcJy =  "";
    String beiyong = "";//备用===》扩大一百倍的角度
    String sjxz = "";//1电流；2变比匝比
    String csxw = "";
    String csdl = "";
    String bianbi = "0";
    String jixing = "";
    String zabi = "0";
    String ycdy = "";
    String ecdy = "";
    String gyjxlx = "";
    String dyjxlx = "";
    String zubie = "";
    String fenjie = "";
    int jinru = 0;
    String bianbiA = "";
    String bianbiB = "";
    String bianbiC = "";
    String zabiA = "";
    String zabiB = "";
    String zabiC = "";
    String jixingA = "";
    String jixingB = "";
    String jixingC = "";
    String jiaodu = "";
    String jiaoduA = "";
    String jiaoduB = "";
    String jiaoduC = "";
    String lianjiefangshi = "";


    String TAG = "SxBbCeshiActivity";
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
                    if(StringUtils.isEquals(Config.ymType,"bianbiSxBbCeshi")){
                        String msgStr = msg.obj.toString();
                        Log.i(TAG, msgStr);
                        if(msgStr.length()!=18&&newMsgStr.length()<88){
                            newMsgStr += msgStr;
                            Log.e(TAG,newMsgStr);
                        }
                        if (newMsgStr.length() == 88&&jinru!=15) {
                            Log.e(TAG,newMsgStr);
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,40);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            crcJy = StringUtils.subStrStartToEnd(newMsgStr,40,44);
                            //Log.e("tfxx==1", CrcUtil.getTableCRC(bytesSx));
                            //if(CrcUtil.CrcIsOk(bytesSx,crcJy)){
                            beiyong = StringUtils.subStrStartToEnd(newMsgStr,52,56);//扩大一百倍的角度
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,12,14);//数据性质：1、电流电压；2、测试变比匝比
                            csxw = StringUtils.subStrStartToEnd(newMsgStr,14,16);//测试相位0：AB；1：BC；2：CA
                            csdl = StringUtils.subStrStartToEnd(newMsgStr,16,24);//测试电流；测试变比
                            ycdy = StringUtils.subStrStartToEnd(newMsgStr,24,32);//一次电压；测试匝比
                            ecdy = StringUtils.subStrStartToEnd(newMsgStr,32,40);//二次电压;
                            bianbi = StringUtils.subStrStartToEnd(newMsgStr,60,68);//变比;
                            zabi = StringUtils.subStrStartToEnd(newMsgStr,68,76);//匝比
                            jixing = StringUtils.subStrStartToEnd(newMsgStr,68,70);//极性：1：正；0负
                            gyjxlx = StringUtils.subStrStartToEnd(newMsgStr,76,78);//高压侧接线类型
                            dyjxlx = StringUtils.subStrStartToEnd(newMsgStr,78,80);//低压侧接线类型
                            zubie = StringUtils.subStrStartToEnd(newMsgStr,80,82);//组别
                            fenjie = StringUtils.subStrStartToEnd(newMsgStr,82,84);//测试分接
                            /*beiyong = StringUtils.subStrStartToEnd(newMsgStr,8,12);//扩大一百倍的角度
                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,12,14);//数据性质：1、电流电压；2、测试变比匝比
                            csxw = StringUtils.subStrStartToEnd(newMsgStr,14,16);//测试相位0：AB；1：BC；2：CA*/
                            if(StringUtils.isEquals(sjxz,"01")){
                                //sjxz = StringUtils.subStrStartToEnd(newMsgStr,12,14);//数据性质：1、电流电压；2、测试变比匝比
                                csdl = StringUtils.subStrStartToEnd(newMsgStr,16,24);//测试电流；测试变比
                                ycdy = StringUtils.subStrStartToEnd(newMsgStr,24,32);//一次电压；测试匝比
                                ecdy = StringUtils.subStrStartToEnd(newMsgStr,32,40);//二次电压;
                            }else {
                                bianbi = StringUtils.subStrStartToEnd(newMsgStr,16,24);//变比;
                                zabi = StringUtils.subStrStartToEnd(newMsgStr,24,32);//匝比
                                //jixing = StringUtils.subStrStartToEnd(newMsgStr,68,70);//极性：1：正；0负
                                gyjxlx = StringUtils.subStrStartToEnd(newMsgStr,32,34);//高压侧接线类型
                                dyjxlx = StringUtils.subStrStartToEnd(newMsgStr,34,36);//低压侧接线类型
                                zubie = StringUtils.subStrStartToEnd(newMsgStr,36,38);//组别
                                fenjie = StringUtils.subStrStartToEnd(newMsgStr,38,40);//测试分接
                            }
                            Log.e(TAG,csdl+","+ycdy+","+ecdy);
                            String dl = ShiOrShiliu.hexToFloatWuBuhuan(csdl);
                            String ycdyFl = ShiOrShiliu.hexToFloatWuBuhuan(ycdy);
                            String ecdyFl = ShiOrShiliu.hexToFloatWuBuhuan(ecdy);
                            if(StringUtils.isEquals(csxw,"00")){
                                tvCsdlA.setText(dl+"mA");
                                tvYcdyA.setText(ycdyFl+"V");
                                tvEcdyA.setText(ecdyFl+"V");
//                                if(StringUtils.isEquals(jixing,"01")){
//                                    jixingA = "+";
//                                } else if (StringUtils.isEquals(jixing,"00")) {
//                                    jixingA = "-";
//                                }
                                jiaoduA = ShiOrShiliu.parseInt(beiyong)+"";
                                bianbiA = ShiOrShiliu.hexToFloatWuBuhuan(bianbi);
                                zabiA = ShiOrShiliu.hexToFloatWuBuhuan(zabi);
                                Log.e(TAG,beiyong+","+jiaoduA);
                            }else if(StringUtils.isEquals(csxw,"01")){
                                tvCsdlB.setText(dl+"mA");
                                tvYcdyB.setText(ycdyFl+"V");
                                tvEcdyB.setText(ecdyFl+"V");
//                                if(StringUtils.isEquals(jixing,"01")){
//                                    jixingB = "+";
//                                } else if (StringUtils.isEquals(jixing,"00")) {
//                                    jixingB = "-";
//                                }
                                jiaoduB = ShiOrShiliu.parseInt(beiyong)+"";
                                bianbiB = ShiOrShiliu.hexToFloatWuBuhuan(bianbi);
                                zabiB = ShiOrShiliu.hexToFloatWuBuhuan(zabi);
                                Log.e(TAG,beiyong+","+jiaoduB);
                            }else if(StringUtils.isEquals(csxw,"02")){
                                tvCsdlC.setText(dl+"mA");
                                tvYcdyC.setText(ycdyFl+"V");
                                tvEcdyC.setText(ecdyFl+"V");
//                                if(StringUtils.isEquals(jixing,"01")){
//                                    jixingC = "+";
//                                } else if (StringUtils.isEquals(jixing,"00")) {
//                                    jixingC = "-";
//                                }
                                jiaoduC = ShiOrShiliu.parseInt(beiyong)+"";
                                bianbiC = ShiOrShiliu.hexToFloatWuBuhuan(bianbi);
                                zabiC = ShiOrShiliu.hexToFloatWuBuhuan(zabi);
                                Log.e(TAG,beiyong+","+jiaoduC);
                            }
                            jinru +=1;
                            Log.e("进入==1", jinru+"");
                            if(jinru==15){
                                lianjiefangshi = LianjieZubie.getYi2(gyjxlx)+LianjieZubie.getEr2(dyjxlx)+LianjieZubie.getSan2(zubie);
                                finish();
                                Intent intent = new Intent(SxBbCeshiActivity.this, SxBbEndActivity.class);
                                intent.putExtra("bbfenjie",fenjie);
                                intent.putExtra("bbbianbiA",bianbiA);
                                intent.putExtra("bbzabiA",zabiA);
                                intent.putExtra("bbjdA",jiaoduA);
                                intent.putExtra("bbbianbiB",bianbiB);
                                intent.putExtra("bbzabiB",zabiB);
                                intent.putExtra("bbjdB",jiaoduB);
                                intent.putExtra("bbbianbiC",bianbiC);
                                intent.putExtra("bbzabiC",zabiC);
                                intent.putExtra("bbjdC",jiaoduC);
                                intent.putExtra("bblianjiefangshi",lianjiefangshi);
                                intent.putExtra("bbzubie",zubie);
                                startActivity(intent);
                                //startActivity(new Intent(DxBbCeshiActivity.this,DxBbEndActivity.class));
                            } else {
                                newMsgStr = "";
                            }
                            //}
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
        setContentView(R.layout.activity_sx_bb_ceshi);
        ActivityCollector.addActivity(this);
        Config.ymType = "bianbiSxBbCeshi";
        initModel();
        initView();
        sendDataByBle(SendUtil.initSend("77"),"");
    }

    public void initView(){
        tvCsdlA = findViewById(R.id.tvSxBbCeshiCsDlA);
        tvCsdlB = findViewById(R.id.tvSxBbCeshiCsDlB);
        tvCsdlC = findViewById(R.id.tvSxBbCeshiCsDlC);
        tvYcdyA = findViewById(R.id.tvSxBbCeshiYcDyA);
        tvYcdyB = findViewById(R.id.tvSxBbCeshiYcDyB);
        tvYcdyC = findViewById(R.id.tvSxBbCeshiYcDyC);
        tvEcdyA = findViewById(R.id.tvSxBbCeshiEcDyA);
        tvEcdyB = findViewById(R.id.tvSxBbCeshiEcDyB);
        tvEcdyC = findViewById(R.id.tvSxBbCeshiEcDyC);
        tvFanhui = findViewById(R.id.tvSxCszhongFanhui);
        tvFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataByBle(SendUtil.initSend("82"),"");
                finish();
                //startActivity(new Intent(SxBbCeshiActivity.this,SxBbEndActivity.class));
            }
        });
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(SxBbCeshiActivity.this);
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