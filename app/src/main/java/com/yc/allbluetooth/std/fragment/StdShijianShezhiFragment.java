package com.yc.allbluetooth.std.fragment;



import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.EditorAction;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdShijianShezhiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdShijianShezhiFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String TAG = "shijian";

    private EditText etNian;
    private EditText etYue;
    private EditText etRi;
    private EditText etShi;
    private EditText etFen;
    private EditText etMiao;
    private LinearLayout llQueren;

    int type = 0;//记录是否进过时间设置页面。0.未进过；1.进过，未点击确定；2.进过，点击完确定

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";

    String sjxz;//数据性质
    String sjxh;//数据序号
    String csxw;//测试相位
    String tfxx;//突发信息
    String cswd;//测试温度
    String fjwz;//分接位置
    String csdl;//测试电流
    String nian;//年
    String yue;//月
    String ri;//日
    String shi;//时
    String fen;//分
    String miao;//秒
    String a0orab;//A0或者ab测试数据，单精度浮点型
    String b0orbc;//B0或者bc测试数据，单精度浮点型
    String c0orca;//C0或者ac测试数据，单精度浮点型

    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;
    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.BLUETOOTH_GETDATA:
                    String msgStr = msg.obj.toString();
                    Log.i("shijian", msgStr);
                    if(StringUtils.isEquals("stdShijian",Config.ymType)){
                        if(msgStr.length()==20||msgStr.length()>26){
                            if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                                newMsgStr = msgStr;
                                Log.e("diaoyueNew1=:",newMsgStr);
                            }else{
                                newMsgStr = newMsgStr+msgStr;
                                //可以
                                Log.e("diaoyueNew2=:",newMsgStr);
                            }
                            if(newMsgStr.length()>40){
                                //可以
                                Log.i("diaoyue", "new:"+newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
                                csxw = StringUtils.subStrStartToEnd(newMsgStr,8,10);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr,12,14);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr,14,16);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr,16,18);
                                nian = StringUtils.subStrStartToEnd(newMsgStr,18,20);
                                yue = StringUtils.subStrStartToEnd(newMsgStr,20,22);
                                ri = StringUtils.subStrStartToEnd(newMsgStr,22,24);
                                shi = StringUtils.subStrStartToEnd(newMsgStr,24,26);
                                fen = StringUtils.subStrStartToEnd(newMsgStr,26,28);
                                miao = StringUtils.subStrStartToEnd(newMsgStr,28,30);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr,30,38);
                            }

                        }else{
                            Log.e(TAG,"这是返回的第一条验证指令："+msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr,4,6);
                        }
                    }

                    break;

                case Config.BLUETOOTH_LIANJIE_DUANKAI:
                    bleConnectUtil.disConnect();
                    break;
                default:
                    break;
            }
        }
    };

    public StdShijianShezhiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShijianShezhiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdShijianShezhiFragment newInstance(String param1, String param2) {
        StdShijianShezhiFragment fragment = new StdShijianShezhiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("shijian===", "stop====");

//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacks(checkConnetRunnable);
//        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("shijianshezhi==","onHiddenChanged");

        String strStdCsAllStdSave = "6886"+"73"+"00000000"+"00"+"00"+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        //sendDataByBle(sendAllYnSave,"");

//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.disConnect();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_shijian_shezhi, container, false);
        View mainView = inflater.inflate(R.layout.fragment_std_shijian_shezhi,null);
        initView(mainView);
        initModel();
        return mainView;
    }
    public void initView(View view){
        etNian = view.findViewById(R.id.etSjSzNian);
        etYue = view.findViewById(R.id.etSjSzYue);
        etRi = view.findViewById(R.id.etSjSzRi);
        etShi = view.findViewById(R.id.etSjSzShi);
        etFen = view.findViewById(R.id.etSjSzFen);
        etMiao = view.findViewById(R.id.etSjSzMiao);
        llQueren = view.findViewById(R.id.llSjSzQueren);
        llQueren.setOnClickListener(this);
    }
    public void initModel(){
        Config.ymType = "stdShijian";
        bleConnectUtil = new BleConnectUtil(getActivity());
//        if(!bleConnectUtil.isConnected()&&StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
//        }
        if(StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
        }else if(StringUtils.noEmpty(Config.lyAddress)){
            bleConnectUtil.connect(Config.lyAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
        }
        bleConnectUtil.setCallback(blecallback);
        type = 1;//进过
        String timeStr = GetTime.getTime(3);
        String nian = StringUtils.subStrStartToEnd(timeStr,0,2);
        String yue = StringUtils.subStrStartToEnd(timeStr,2,4);
        String ri = StringUtils.subStrStartToEnd(timeStr,4,6);
        String shi = StringUtils.subStrStartToEnd(timeStr,8,10);
        String fen = StringUtils.subStrStartToEnd(timeStr,10,12);
        String miao = StringUtils.subStrStartToEnd(timeStr,12,14);
        EditorAction editorAction = new EditorAction();
        editorAction.nian(etNian,nian);
        editorAction.yue(etYue,yue);
        editorAction.ri(etRi,etNian,etYue,ri);
        editorAction.shi(etShi,shi);
        editorAction.fen(etFen,fen);
        editorAction.miao(etMiao,miao);
        Log.e("年=：",nian+yue+ri+shi+fen+miao+",type="+type);
        etNian.setText(nian);
        etYue.setText(yue);
        etRi.setText(ri);
        etShi.setText(shi);
        etFen.setText(fen);
        etMiao.setText(miao);
    }
    @Override
    public void onClick(View v) {
        type = 2;
        String nianStr = etNian.getText().toString();
        String yueStr = etYue.getText().toString();
        String riStr = etRi.getText().toString();
        String shiStr = etShi.getText().toString();
        String fenStr = etFen.getText().toString();
        String miaoStr = etMiao.getText().toString();
        Log.e("时间===：",nianStr+yueStr+riStr+shiStr+fenStr+miaoStr+",type=:"+type);

//        String nianHexStr = ShiOrShiliu.toHexString(StringUtils.strToInt(nianStr));
//        String yueHexStr = ShiOrShiliu.toHexString(StringUtils.strToInt(yueStr));
//        String riHexStr = ShiOrShiliu.toHexString(StringUtils.strToInt(riStr));
//        String shiHexStr = ShiOrShiliu.toHexString(StringUtils.strToInt(shiStr));
//        String fenHexStr = ShiOrShiliu.toHexString(StringUtils.strToInt(fenStr));
//        String miaoHexStr = ShiOrShiliu.toHexString(StringUtils.strToInt(miaoStr));

        String strStdCsAllStdSave = "6886"+"71"+nianStr+yueStr+riStr+shiStr+fenStr+miaoStr+"0000";//下发时间0x71
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        sendDataByBle(sendAllYnSave,"");
    }
    public int getType(){
        return type;
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
            Log.e("zhizu", "onSuccessSend: ");

        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("zhizu", "onDisconnect: ");
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
    public void onResume() {
        super.onResume();

        Config.ymType = "stdShijian";
        Log.e(TAG,"sjsz---onResume()");
    }
}