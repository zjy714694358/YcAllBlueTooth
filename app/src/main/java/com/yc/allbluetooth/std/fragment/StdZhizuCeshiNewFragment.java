package com.yc.allbluetooth.std.fragment;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.activity.ZhizuCeshiDyYiActivity;
import com.yc.allbluetooth.std.activity.ZhizuCeshiStdLingxianYiActivity;
import com.yc.allbluetooth.std.activity.ZhizuCeshiStdYiActivity;
import com.yc.allbluetooth.std.activity.ZhizuCeshiYnYiActivity;
import com.yc.allbluetooth.std.entity.TestSet;
import com.yc.allbluetooth.std.tchd.SlideDialog;
import com.yc.allbluetooth.std.util.CsFf;
import com.yc.allbluetooth.std.util.DyCsDlList;
import com.yc.allbluetooth.std.util.DyXbList;
import com.yc.allbluetooth.std.util.DyZcList;
import com.yc.allbluetooth.std.util.LxDzlList;
import com.yc.allbluetooth.std.util.StdCsDlList;
import com.yc.allbluetooth.std.util.YnCsDlList;
import com.yc.allbluetooth.std.util.YnXbList;
import com.yc.allbluetooth.std.util.Zhiling;
import com.yc.allbluetooth.std.util.ZzCsStartCs;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdZhizuCeshiNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdZhizuCeshiNewFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout llCsFf;
    private TextView tvCsFf;
    private LinearLayout llCsDl;
    private TextView tvCsDl;
    private LinearLayout llCsXb;
    private TextView tvCsXb;
    private LinearLayout llLxDz;
    private TextView tvLxDz;
    private LinearLayout llZc;
    private TextView tvZc;
    private LinearLayout llLxDzAll;
    private LinearLayout llZcAll;
    private TextView tvCsDz;
    private LinearLayout llStart;
    private Button btnZzCsStartCsNew;
    private ImageView ivCsXb;

    List<String> lists=new ArrayList<>();

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

    String tfxxType;//突发信息状态码
    int btnType = 0;//点击保存、打印、停止按钮对应的状态；保存：1；打印：2；停止：3;换相：4


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
                    if(StringUtils.isEquals(Config.ymType,"zhizuceshiNew")){
                        String msgStr = msg.obj.toString();
                        Log.e("yemian==",Config.ymType);

                        Log.e("zhizu0", msgStr);
                        if (msgStr.length() == 20 || msgStr.length() > 26||msgStr.length()==6) {
                            if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                                newMsgStr = msgStr;
                                Log.e("zhizuNew1=:", newMsgStr);
                            } else {
                                newMsgStr = newMsgStr + msgStr;
                                //可以
                                Log.e("zhizuNew2=:", newMsgStr);
                            }
                            if (newMsgStr.length() == 60) {//> 40
                                //可以
                                Log.e("首页zhizu=60", "new:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 8, 10);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 30, 38);
                                Log.e( "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 38, 46);
                                Log.e( "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 46, 54);
                                Log.e( "c", c0orca);

                            }

                        } else {
                            Log.e("返回==", "这是返回的第一条验证指令：" + msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr, 4, 6);
                            //接收到返回指令后
                            String csff2 = tvCsFf.getText().toString();
                            String csdl2 = tvCsDl.getText().toString();
                            String csxb2 = tvCsXb.getText().toString();
                            String lxdz2 = tvLxDz.getText().toString();
                            String zc2 = tvZc.getText().toString();
                            Config.csdlStr = csdl2;//设置三通道(二)电阻页面中，电流的选择值，提前设置好，后续直接显示
                            Log.e("start===",csff2+","+csdl2+","+csxb2+","+lxdz2+","+zc2);
                            Zhiling zhiling = new Zhiling();
                            String ffZl = zhiling.ceshifangfa2(StdZhizuCeshiNewFragment.this,csff2);
                            String dlZl = zhiling.ceshidianliu(csdl2);
                            String xbZl = zhiling.ceshixiangwei(csxb2);
                            String lxdzZl = zhiling.lingxiandianzuqidong(lxdz2);
                            String zcZl = zhiling.zhuciqidong(zc2);
                            Log.e("start===",ffZl+","+dlZl+","+xbZl+","+lxdzZl+","+zcZl);

                            String strStdCsAll = "";
                            if(StringUtils.isEquals("08",lxdzZl)){//08
                                Config.isStdLingxian = 1;
                                strStdCsAll = "6886" + "6b" + ffZl + xbZl + lxdzZl + dlZl + "00" + "00" + "0000";
                            }else{
                                Config.isStdLingxian = 0;
                                strStdCsAll = "6886" + "6b" + ffZl + xbZl + zcZl + dlZl + "00" + "00" + "0000";
                            }
                            byte[] bytes = new BigInteger(strStdCsAll, 16).toByteArray();
                            String crc = CrcUtil.getTableCRC(bytes);
                            String sendAll = strStdCsAll + crc;
                            Log.e("fasong2:", sendAll);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /**
                                     *要执行的操作：延迟0.5秒发送跳转，不然有时候跳转不过去
                                     */
                                    if(StringUtils.isEquals("00",ffZl)){//三通道
                                        if(StringUtils.isEquals("08",lxdzZl)){//08
                                            sendDataByBle(sendAll, "");
                                            startActivity(new Intent(getActivity(), ZhizuCeshiStdLingxianYiActivity.class));//三通道08
                                        }else{
                                            sendDataByBle(sendAll, "");
                                            startActivity(new Intent(getActivity(), ZhizuCeshiStdYiActivity.class));
                                        }
                                    }else if(StringUtils.isEquals("01",ffZl)){//YN
                                        sendDataByBle(sendAll, "");
                                        startActivity(new Intent(getActivity(), ZhizuCeshiYnYiActivity.class));
                                    }else if(StringUtils.isEquals("02",ffZl)){//D(Y)
                                        sendDataByBle(sendAll, "");
                                        startActivity(new Intent(getActivity(), ZhizuCeshiDyYiActivity.class));
                                    }
                                }
                            }, 500);//3秒后执行Runnable中的run方法

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

    public StdZhizuCeshiNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StdZhizuCeshiNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdZhizuCeshiNewFragment newInstance(String param1, String param2) {
        StdZhizuCeshiNewFragment fragment = new StdZhizuCeshiNewFragment();
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
        View mainView  = inflater.inflate(R.layout.fragment_std_zhizu_ceshi_new,null);
        initView(mainView);
        initModel();
        return mainView;
        //return inflater.inflate(R.layout.fragment_std_zhizu_ceshi_new, container, false);
    }
    public void initView(View view){
        Config.ymType = "zhizuceshiNew";
        //Config.yqlx = "35";
        llCsFf = view.findViewById(R.id.llCsFf);
        tvCsFf = view.findViewById(R.id.tvCsFf);
        llCsFf.setOnClickListener(this);
        llCsDl = view.findViewById(R.id.llCsDl);
        tvCsDl = view.findViewById(R.id.tvCsDl);
        llCsDl.setOnClickListener(this);
        llCsXb = view.findViewById(R.id.llCsXb);
        tvCsXb = view.findViewById(R.id.tvCsXb);
        ivCsXb = view.findViewById(R.id.ivCsXb);
        llCsXb.setOnClickListener(this);
        llLxDz = view.findViewById(R.id.llLxDz);
        tvLxDz = view.findViewById(R.id.tvLxDz);
        llLxDz.setOnClickListener(this);
        llZc = view.findViewById(R.id.llZc);
        tvZc = view.findViewById(R.id.tvZc);
        llZc.setOnClickListener(this);
        llLxDzAll = view.findViewById(R.id.llLxDzAll);
        llZcAll = view.findViewById(R.id.llZcAll);
        tvCsDz = view.findViewById(R.id.tvCsDz);

        llStart = view.findViewById(R.id.llZzCsStartCsNew);
        llStart.setOnClickListener(this);

        btnZzCsStartCsNew = view.findViewById(R.id.btnZzCsStartCsNew);
        btnZzCsStartCsNew.setOnClickListener(this);

        init1();
    }
    public void init1(){
        if(StringUtils.isEquals("31", Config.yqlx)){//10A
            tvCsDl.setText("5A+5A");
            tvCsDz.setText("（2mΩ~1.2Ω）");
        }else if(StringUtils.isEquals("34", Config.yqlx)){//20A
            tvCsDl.setText("10A+10A");
            tvCsDz.setText("（1mΩ~0.6Ω）");
        }else if(StringUtils.isEquals("35", Config.yqlx)){//40A
            tvCsDl.setText("20A+20A");
            tvCsDz.setText("（0.5mΩ~0.3Ω）");
        }else if(StringUtils.isEquals("36", Config.yqlx)){//50A
            tvCsDl.setText("25A+25A");
            tvCsDz.setText("（0.4mΩ~0.1Ω）");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //bleConnectUtil.setCallback(null);
        Log.e("zhizunew--->","onHiddenChanged()");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
    }

    public void initModel() {
//        bleConnectUtil = new BleConnectUtil(getActivity());
//        if(StringUtils.noEmpty(Config.lyAddress)){
//            bleConnectUtil.connect(Config.lyAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
//        }else if(StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);
//        }
//        bleConnectUtil.setCallback(blecallback);
        bleConnectUtil = new BleConnectUtil(getActivity());
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llCsFf:
                //Log.e("==","000000000000");
                String tvFfStr = tvCsFf.getText().toString();
                CsFf csFf = new CsFf();
                csFf.csfs(StdZhizuCeshiNewFragment.this,tvFfStr,tvCsFf,tvCsDl,tvCsDz,tvCsXb,llLxDzAll,tvLxDz,llZcAll,tvZc,ivCsXb);
                break;
            case R.id.llCsDl:
                lists.clear();
                String csffStr = tvCsFf.getText().toString();
                if(StringUtils.isEquals(getString(R.string.zzcs_santongdao_ceshi),csffStr)){
                    String dlValStr = tvCsDl.getText().toString();
                    StdCsDlList stdCsDlList = new StdCsDlList();
                    Log.e("===",Config.yqlx);
                    if(StringUtils.isEquals("31", Config.yqlx)){
                        stdCsDlList.DlAndDz_10(dlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("34", Config.yqlx)){
                        stdCsDlList.DlAndDz_20(dlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("35", Config.yqlx)){
                        stdCsDlList.DlAndDz_40(dlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("36", Config.yqlx)){
                        stdCsDlList.DlAndDz_50(dlValStr,tvCsDl,tvCsDz);
                    }
                }else if(StringUtils.isEquals(getString(R.string.zzcs_yn_ceshi),csffStr)){
                    String tvDlValStr = tvCsDl.getText().toString();
                    YnCsDlList ynCsDlList = new YnCsDlList();
                    if(StringUtils.isEquals("31", Config.yqlx)){
                        ynCsDlList.DlAndDz_10(tvDlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("34", Config.yqlx)){
                        ynCsDlList.DlAndDz_20(tvDlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("35", Config.yqlx)){
                        ynCsDlList.DlAndDz_40(tvDlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("36", Config.yqlx)){
                        ynCsDlList.DlAndDz_50(tvDlValStr,tvCsDl,tvCsDz);
                    }
                }else if(StringUtils.isEquals(getString(R.string.zzcs_dy_ceshi),csffStr)){
                    String tvDlValStr = tvCsDl.getText().toString();
                    DyCsDlList dyCsDlList = new DyCsDlList();
                    if(StringUtils.isEquals("31", Config.yqlx)){
                        dyCsDlList.DlAndDz_10(tvDlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("34", Config.yqlx)){
                        dyCsDlList.DlAndDz_20(tvDlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("35", Config.yqlx)){
                        dyCsDlList.DlAndDz_40(tvDlValStr,tvCsDl,tvCsDz);
                    }else if(StringUtils.isEquals("36", Config.yqlx)){
                        dyCsDlList.DlAndDz_50(tvDlValStr,tvCsDl,tvCsDz);
                    }
                }
                break;
            case R.id.llCsXb:
                lists.clear();
                csffStr = tvCsFf.getText().toString();
                if(StringUtils.isEquals(getString(R.string.zzcs_santongdao_ceshi),csffStr)){//三相不做处理

                }else if(StringUtils.isEquals(getString(R.string.zzcs_yn_ceshi),csffStr)){
                    String tvXbValStr = tvCsXb.getText().toString();
                    YnXbList ynXbList = new YnXbList();
                    ynXbList.Xb(tvXbValStr,tvCsXb);
                }else if(StringUtils.isEquals(getString(R.string.zzcs_dy_ceshi),csffStr)){
                    String xbStr = tvCsXb.getText().toString();
                    DyXbList dyXbList = new DyXbList();
                    dyXbList.Xb(xbStr,tvCsXb);
                }
                break;
            case R.id.llLxDz:
                lists.clear();
                csffStr = tvCsFf.getText().toString();
                if(StringUtils.isEquals(getString(R.string.zzcs_santongdao_ceshi),csffStr)){
                    String dzTypeValStr = tvLxDz.getText().toString();
                    LxDzlList lxDzlList = new LxDzlList();
                    lxDzlList.DzType(dzTypeValStr,tvLxDz);
                }
                break;
            case R.id.llZc:
                lists.clear();
                csffStr = tvCsFf.getText().toString();
                if(StringUtils.isEquals(getString(R.string.zzcs_dy_ceshi),csffStr)){
                    String zcStr = tvZc.getText().toString();
                    DyZcList dyZcList = new DyZcList();
                    dyZcList.ZcType(zcStr,tvZc);
                }
                break;
            case R.id.btnZzCsStartCsNew:
                Log.e("","=============");
                sendDataByBle(SendUtil.initSend("73"),"");//先发送返回
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
        Config.ymType = "zhizuceshiNew";
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("zhizunew--->", "onStop()：" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("zhizunew--->", "onDestroy()：");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
    }

}