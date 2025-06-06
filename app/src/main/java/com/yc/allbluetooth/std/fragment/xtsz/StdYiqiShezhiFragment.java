package com.yc.allbluetooth.std.fragment.xtsz;


import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.util.RzCzList;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.EditorAction;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdYiqiShezhiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdYiqiShezhiFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etSpbh;
    private TextView tvRzcz;
    private EditText etZswd;
    private LinearLayout llRzczJt;


    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    String TAG = "yiqishezhi";
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
    String mlType;//命令类型：70：下发仪器编号；
    String crcJy;




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
                    Log.i("yiqishezhi", msgStr);
                    if(msgStr.length()==22||msgStr.length()>28){
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                            newMsgStr = msgStr;
                            Log.e("yiqishezhi1=:",newMsgStr);
                        }else{
                            newMsgStr = newMsgStr+msgStr;
                            //可以
                            Log.e("yiqishezhi2=:",newMsgStr);
                        }
                        if(newMsgStr.length()>40){
                            //可以
                            Log.i("yiqishezhi", "new:"+newMsgStr);
                            //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
//                            sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
//                            sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,8);
//                            //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
//                            csxw = StringUtils.subStrStartToEnd(newMsgStr,8,10);
//                            tfxx = StringUtils.subStrStartToEnd(newMsgStr,10,12);
//                            cswd = StringUtils.subStrStartToEnd(newMsgStr,12,14);
//                            fjwz = StringUtils.subStrStartToEnd(newMsgStr,14,16);
//                            csdl = StringUtils.subStrStartToEnd(newMsgStr,16,18);
//                            nian = StringUtils.subStrStartToEnd(newMsgStr,18,20);
//                            yue = StringUtils.subStrStartToEnd(newMsgStr,20,22);
//                            ri = StringUtils.subStrStartToEnd(newMsgStr,22,24);
//                            shi = StringUtils.subStrStartToEnd(newMsgStr,24,26);
//                            fen = StringUtils.subStrStartToEnd(newMsgStr,26,28);
//                            miao = StringUtils.subStrStartToEnd(newMsgStr,28,30);
//                            a0orab = StringUtils.subStrStartToEnd(newMsgStr,30,38);
//                            b0orbc = StringUtils.subStrStartToEnd(newMsgStr,38,46);
//                            c0orca = StringUtils.subStrStartToEnd(newMsgStr,46,54);
                        }

                    }else{
                        Log.e(TAG,"这是返回的第一条验证指令："+msgStr);
                        mlType = StringUtils.subStrStartToEnd(msgStr,4,6);
                        if(StringUtils.isEquals("70",mlType)){
                            Config.spbh = etSpbh.getText().toString();
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

    public StdYiqiShezhiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YiqiShezhiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdYiqiShezhiFragment newInstance(String param1, String param2) {
        StdYiqiShezhiFragment fragment = new StdYiqiShezhiFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_yiqi_shezhi, container, false);
        View mainView  = inflater.inflate(R.layout.fragment_std_yiqi_shezhi,null);
        initView(mainView);
        initModel();
        return mainView;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("xitongYiqi===", "onStop()");

        String strStdCsAllStdSave = "6886"+"73"+"00000000"+"00"+"00"+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        //sendDataByBle(sendAllYnSave,"");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }

//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacks(checkConnetRunnable);
//        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("xitongYiqi===", "onHiddenChanged()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("xitongYiqi===", "onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("xitongYiqi===", "onDestroy()");
    }

    public void initView(View view) {
        etSpbh = view.findViewById(R.id.etStdXtSzYqSzSpBh);
        tvRzcz = view.findViewById(R.id.tvXtSzYqSzRzCz);
        llRzczJt = view.findViewById(R.id.llXtSzYqSzRzCzJt);
        etZswd = view.findViewById(R.id.etXtSzYqSzZswd);
        llRzczJt.setOnClickListener(this);
        EditorAction editorAction = new EditorAction();
        editorAction.bulingWdXtszWd(etZswd);//调用小于10补零方法
        etSpbh.setText(Config.spbh);

        etSpbh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE://输入框，完成按钮点击事件
                        String spbh = etSpbh.getText().toString();
//                        if(spbh.length()<4||spbh.length()>8){
//                            Toast.makeText(getContext(),"请输入4到8位数之间",Toast.LENGTH_SHORT).show();
//                        }else{
//                            Log.e(TAG,spbh);
//                            String strStdCsAllStdSave2 = "6886"+"70"+BytesToHexString.str2HexStr(spbh);//字符串转成十六进制
//                            Log.e(TAG,strStdCsAllStdSave2);
//                            byte[] bytesStdSave2 = new BigInteger(strStdCsAllStdSave2, 16).toByteArray();
//                            //Log.e(TAG, BytesToHexString.bytesToHexString(bytesStdSave2));
//                            String crcStdSave2 = CrcUtil.getTableCRC(bytesStdSave2);
//                            String sendAllYnSave2 = strStdCsAllStdSave2 + crcStdSave2;
//                            sendDataByBle(sendAllYnSave2,"");
//                        }
                        sendDataByBle(SendUtil.yiqibianhaoSend_std("70", spbh),"");
                        break;
                }
                return false;
            }
        });

        etSpbh.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }

            @NonNull
            @Override
            protected char[] getAcceptedChars() {
                return "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                //return "0123456789abcdefABCDEF".toCharArray();
            }
        });
        etSpbh.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        /**
         * 系统设置折算温度设置
         * @param editText  折算温度输入框Et
         */
        etZswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String etStr = etZswd.getText().toString();
                        if(StringUtils.isEquals("00",etStr)){
                            etZswd.setText("00");
                            sendDataByBle(SendUtil.initSendStdNew("75","00"),"");
                        }else if(StringUtils.isEquals("01",etStr)){
                            etZswd.setText("01");
                            sendDataByBle(SendUtil.initSendStdNew("75","01"),"");
                        }else if(StringUtils.isEquals("02",etStr)){
                            etZswd.setText("02");
                            sendDataByBle(SendUtil.initSendStdNew("75","02"),"");
                        }else if(StringUtils.isEquals("03",etStr)){
                            etZswd.setText("03");
                            sendDataByBle(SendUtil.initSendStdNew("75","03"),"");
                        }else if(StringUtils.isEquals("04",etStr)){
                            etZswd.setText("04");
                            sendDataByBle(SendUtil.initSendStdNew("75","04"),"");
                        }else if(StringUtils.isEquals("05",etStr)){
                            etZswd.setText("05");
                            sendDataByBle(SendUtil.initSendStdNew("75","05"),"");
                        }else if(StringUtils.isEquals("06",etStr)){
                            etZswd.setText("06");
                            sendDataByBle(SendUtil.initSendStdNew("75","06"),"");
                        }else if(StringUtils.isEquals("07",etStr)){
                            etZswd.setText("07");
                            sendDataByBle(SendUtil.initSendStdNew("75","07"),"");
                        }else if(StringUtils.isEquals("08",etStr)){
                            etZswd.setText("08");
                            sendDataByBle(SendUtil.initSendStdNew("75","08"),"");
                        }else if(StringUtils.isEquals("09",etStr)){
                            etZswd.setText("09");
                            sendDataByBle(SendUtil.initSendStdNew("75","09"),"");
                        }else if(StringUtils.noEmpty(etStr) && StringUtils.strToInt(etZswd.getText().toString())<10){
                            etZswd.setText("0"+etStr);
                            sendDataByBle(SendUtil.initSendStdNew("75",etZswd.getText().toString()),"");
                        }else if(StringUtils.isEmpty(etStr)){
                            etZswd.setText("75");
                            sendDataByBle(SendUtil.initSendStdNew("74","4b"),"");
                        }else if(StringUtils.strToInt(etZswd.getText().toString())>=10&&StringUtils.strToInt(etZswd.getText().toString())<=15){
                            sendDataByBle(SendUtil.initSendStdNew("75","0"+ ShiOrShiliu.toHexString(StringUtils.strToInt(etZswd.getText().toString()))),"");
                        }else if(StringUtils.strToInt(etZswd.getText().toString())>=16){
                            sendDataByBle(SendUtil.initSendStdNew("75",ShiOrShiliu.toHexString(StringUtils.strToInt(etZswd.getText().toString()))),"");
                        }
                        String zswd = etZswd.getText().toString();
                        Config.zswd = zswd;

                        break;
                }
                return false;
            }
        });
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(getActivity());
//        if(!bleConnectUtil.isConnected()&&StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
//        }
        bleConnectUtil.connect(Config.lyAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
        bleConnectUtil.setCallback(blecallback);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llXtSzYqSzRzCzJt:
                String rzczStr = tvRzcz.getText().toString();
                if(StringUtils.isEquals("铜",rzczStr)||StringUtils.isEquals("copper",rzczStr)){
                    Config.rzczInt = 225;
                    Config.rzczStr = "09";
                    sendDataByBle(SendUtil.initSendStdNew("76","09"),"");
                }else{
                    Config.rzczStr = "08";
                    Config.rzczInt = 235;
                    sendDataByBle(SendUtil.initSendStdNew("76","08"),"");
                }
                RzCzList rzCzList = new RzCzList();
                rzCzList.Rzcz(rzczStr,tvRzcz);
                break;
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
}