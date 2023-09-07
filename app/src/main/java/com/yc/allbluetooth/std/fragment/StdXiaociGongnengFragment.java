package com.yc.allbluetooth.std.fragment;



import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.activity.ZhizuCeshiStdErActivity;
import com.yc.allbluetooth.std.util.XiaociXbList;
import com.yc.allbluetooth.std.util.Zhiling;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdXiaociGongnengFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdXiaociGongnengFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvXcxb;
    private LinearLayout llXcxbJt;
    private ProgressBar pgbXc;
    private LinearLayout llStartXc;
    private TextView tvStartXc;
    String TAG = "xiaoci";

    View viewFd;
    AlertDialog alertDialogFd;//消磁未完成，点击停止，等待放电
    View viewWc;
    AlertDialog alertDialogWc;//消磁完成，自动出现，消磁完成

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
    String a0orab;//A0或者ab测试数据，消磁数据，完成百分比
    String b0orbc;//B0或者bc测试数据
    String c0orca;//C0或者ac测试数据

    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;

    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals("stdXiaoci",Config.ymType)){
                        String msgStr = msg.obj.toString();
                        Log.i("xiaoci", "Home:"+msgStr);
                        if(msgStr.length()==20||msgStr.length()>26){
                            if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                                newMsgStr = msgStr;
                                Log.e("xiaociNew1=:",newMsgStr);
                            }else{
                                newMsgStr = newMsgStr+msgStr;
                                //可以
                                Log.e("xiaoci2=:",newMsgStr);
                            }
                            if(newMsgStr.length()==60){
                                //可以
                                Log.i("diaoyue", "new:"+newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
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
                                Log.e(TAG+"a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr,38,46);
                                Log.e(TAG+"b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr,46,54);
                                Log.e(TAG+"c", c0orca);
                                String a0orabHl = HexUtil.reverseHex(a0orab);
                                float a0orabF = 0;
                                try {
                                    a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl,16));
                                    XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
                                    int baifenbi = (int)xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshu(a0orabF+""),xiaoshuYunsuan.xiaoshu("100"));
                                    String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF+"");
                                    Log.e(TAG+"进度条百分比==2",a0orabStr2);
                                    Log.e(TAG+"进度条百分比==3",baifenbi+"");


                                    pgbXc.setProgress(baifenbi);
                                    if(baifenbi!=100){
                                        tvStartXc.setText(R.string.tingzhixiaoci);//开始消磁变为停止消磁
                                    }else{
                                        tvStartXc.setText(R.string.kaishixiaoci);//停止消磁变为开始消磁
                                        llXcxbJt.setEnabled(true);//消磁完成，相别切换箭头可以点击
                                        //alertDialogWc.show();
                                    }
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (StringUtils.isEquals("03", sjxz)) {//突发消息；
                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电
                                        //alertDialogFd.show();
                                        Toast.makeText(getActivity(),getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕==>0F
                                        //alertDialogFd.cancel();//关闭放电弹窗
                                        tvStartXc.setText(R.string.kaishixiaoci);//停止消磁变为开始消磁
                                        llXcxbJt.setEnabled(true);//放电完成，相别切换箭头可以点击
                                        //alertDialogFd.cancel();
                                        pgbXc.setProgress(0);//进度条归零
                                    }
                                }
                            }else if(newMsgStr.length()>60){
                                newMsgStr = StringUtils.subStrStart(newMsgStr,26);
                                //可以
                                Log.i("xiaoci", "new:"+newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
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
                                Log.e(TAG+"a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr,38,46);
                                Log.e(TAG+"b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr,46,54);
                                Log.e(TAG+"c", c0orca);

                                String a0orabHl = HexUtil.reverseHex(a0orab);
                                float a0orabF = 0;
                                try {
                                    a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl,16));
                                    XiaoshuYunsuan xiaoshuYunsuan = new XiaoshuYunsuan();
                                    int baifenbi = (int)xiaoshuYunsuan.xiaoshuCheng(xiaoshuYunsuan.xiaoshu(a0orabF+""),xiaoshuYunsuan.xiaoshu("100"));
                                    String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF+"");
                                    Log.e(TAG+"进度条百分比==2",a0orabStr2);
                                    Log.e(TAG+"进度条百分比==3",baifenbi+"");

                                    pgbXc.setProgress(baifenbi);
                                    if(baifenbi!=100){
                                        tvStartXc.setText(R.string.tingzhixiaoci);//开始消磁变为停止消磁
                                    }else{
                                        tvStartXc.setText(R.string.kaishixiaoci);//停止消磁变为开始消磁
                                        llXcxbJt.setEnabled(true);//消磁完成，相别切换箭头可以点击
                                    }
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (StringUtils.isEquals("03", sjxz)) {//突发消息；
                                    if (StringUtils.isEquals("02", tfxx)) {//正在放电
                                        //alertDialogFd.show();
                                        Toast.makeText(getActivity(),getString(R.string.fangdiantanchuang),Toast.LENGTH_SHORT).show();
                                    } else if (ShiOrShiliu.parseInt(tfxx) > 11) {//放电完毕==>0F
                                        //alertDialogFd.cancel();//关闭放电弹窗
                                        tvStartXc.setText(R.string.kaishixiaoci);//停止消磁变为开始消磁
                                        llXcxbJt.setEnabled(true);//放电完成，相别切换箭头可以点击
                                        //alertDialogFd.cancel();
                                        pgbXc.setProgress(0);//进度条归零
                                    }
                                }
                            }
                        }else{
                            Log.e("xiaoci","这是返回的第一条验证指令："+msgStr);
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

    public StdXiaociGongnengFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StdXiaociGongnengFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdXiaociGongnengFragment newInstance(String param1, String param2) {
        StdXiaociGongnengFragment fragment = new StdXiaociGongnengFragment();
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
        //return inflater.inflate(R.layout.fragment_xiaoci_gongneng, container, false);
        View mainView = inflater.inflate(R.layout.fragment_std_xiaoci_gongneng,null);
        initView(mainView);
        initModel();

        return mainView;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("xiaoci===", "onStop()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacks(checkConnetRunnable);
//        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("xiaoci===", "onResume()");
        Config.ymType = "stdXiaoci";
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("xiaoci=============","onHiddenChanged");


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
    public void initView(View view){
        tvXcxb = view.findViewById(R.id.tvXcGnXcXb);
        llXcxbJt = view.findViewById(R.id.llXcGnXcXbJiantou);
        pgbXc = view.findViewById(R.id.pgbXcGn);
        llStartXc = view.findViewById(R.id.llXcGnStartXc);
        tvStartXc = view.findViewById(R.id.tvXcGnStartXc);
        llXcxbJt.setOnClickListener(this);
        llStartXc.setOnClickListener(this);
        viewFd = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fangdian, null, false);
        alertDialogFd = new AlertDialog.Builder(getContext(),R.style.MyDialog).setView(viewFd).create();
        alertDialogFd.setCanceledOnTouchOutside(false);
        viewWc = LayoutInflater.from(getContext()).inflate(R.layout.dialog_xiaoci_wancheng, null, false);
        alertDialogWc = new AlertDialog.Builder(getContext(),R.style.MyDialog).setView(viewWc).create();
        alertDialogWc.setCanceledOnTouchOutside(false);
    }
    public void initModel(){
        Config.ymType = "stdXiaoci";
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
            case R.id.llXcGnXcXbJiantou:
                String xbStr = tvXcxb.getText().toString();
                XiaociXbList xiaociXbList = new XiaociXbList();
                xiaociXbList.Xb(xbStr,tvXcxb);
                break;
            case R.id.llXcGnStartXc:
                if(bleConnectUtil.isConnected()){
                    String startXcStr = tvStartXc.getText().toString();//开始消磁、停止消磁==》str

                    Zhiling zhiling = new Zhiling();
                    String xcXbStr = tvXcxb.getText().toString();
                    String xcXbZhiling = zhiling.ceshixiangwei(xcXbStr);
                    if(StringUtils.isEquals(startXcStr,"开始消磁")||StringUtils.isEquals(startXcStr,"Start Degaussing")){
                        sendDataByBle(SendUtil.xcSend("72",xcXbZhiling), "");
                        //handler.sendEmptyMessage(0);
                        tvStartXc.setText(R.string.tingzhixiaoci);
                        llXcxbJt.setEnabled(false);
                    }else{
                        sendDataByBle(SendUtil.xcSend("69",xcXbZhiling), "");
                        //alertDialogFd.show();
                        //tvStartXc.setText(R.string.kaishixiaoci);
                        pgbXc.setProgress(0);
                        //handler.removeCallbacks(runnable);
                        //alertDialogFd.cancel();
                        //handler.sendEmptyMessage(100);
                    }
                }else{
                    Toast.makeText(getActivity(),R.string.bluetooth_not_connected,Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            pgbXc.setProgress(pgbXc.getProgress()+10);
            //handler.sendEmptyMessage(0);
        }
    };
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
            Log.e("xiaoci", "onSuccessSend: ");

        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("xiaoci", "onDisconnect: ");
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