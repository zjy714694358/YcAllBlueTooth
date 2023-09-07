package com.yc.allbluetooth.std.fragment;


import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


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
import com.yc.allbluetooth.std.fragment.zzcs.StdZzCsDyCsFragment;
import com.yc.allbluetooth.std.fragment.zzcs.StdZzCsStdCsFragment;
import com.yc.allbluetooth.std.fragment.zzcs.StdZzCsYnCsFragment;
import com.yc.allbluetooth.std.util.Zhiling;
import com.yc.allbluetooth.std.util.ZzCsStartCs;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdZhizuCeshiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdZhizuCeshiFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String TAG = "zhizuceshi";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";


    //第一部分，未点击开始测试之前
    private LinearLayout llYi;//第一个总ll
    private LinearLayout llStd;
    private LinearLayout llYn;
    private LinearLayout llDy;
    private LinearLayout llStart;
    private Button btnStart;
    private int zzcsType = 1;//1.三通道测试；2.YN测试；3.DY测试；Config.homeQiehuan代替

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
                    if(StringUtils.isEquals(Config.ymType,"zhizuceshi")){
                        String msgStr = msg.obj.toString();
                        Log.e("yemian==",Config.ymType);

                        Log.i("zhizu0", msgStr);
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
                                Log.e(TAG + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 38, 46);
                                Log.e(TAG + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 46, 54);
                                Log.e(TAG + "c", c0orca);

                            }

                        } else {
                            Log.e(TAG, "这是返回的第一条验证指令：" + msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr, 4, 6);
                            //接收到返回指令后
                            if(Config.zzcsBtnType == 1){
                                Config.zzcsBtnType = 0;
                                Zhiling zhiling = new Zhiling();
                                String csfsZl = zhiling.ceshifangfa(Config.homeQiehuan + "");

                                List<TestSet> list = ZzCsStartCs.getChildData(StdZhizuCeshiFragment.this, Config.homeQiehuan);

                                String xw = list.get(0).getXw();
                                String zc = list.get(0).getZc();
                                String dl = list.get(0).getDl();
                                String dz = list.get(0).getDz();

                                String xwZl = zhiling.ceshixiangwei(xw);
                                String zcZl = zhiling.zhuciqidong(zc);//
                                String dlZl = zhiling.ceshidianliu(dl);
                                //String dzZl = zhiling.lingxiandianzuqidong(dz);//零线电阻，08
                                String dzZl = zhiling.lingxiandianzuqidong2(Config.isStdLingxian);//零线电阻，08
                                Log.e("dyDl==", list.toString());
                                Log.e("isStdLingxian==", Config.isStdLingxian+"");
                                Log.e("dyDl==", csfsZl + "," + dl + "," + xw + "," + dz + "," + zc);
                                Log.e("dyDl==1", xwZl);

                                Config.csdlStr = dl;//设置三通道(二)电阻页面中，电流的选择值，提前设置好，后续直接显示

                                //tvStdDzDl.setText(dl);//设置三通道(二)电阻页面中，电流的选择值，提前设置好，后续直接显示
                                String strStdCsAll = "";
                                if(Config.homeQiehuan==1&&StringUtils.isEquals("08",dzZl)){
                                    strStdCsAll = "6886" + "6b" + csfsZl + xwZl + dzZl + dlZl + "00" + "00" + "0000";
                                }else{
                                    strStdCsAll = "6886" + "6b" + csfsZl + xwZl + zcZl + dlZl + "00" + "00" + "0000";
                                }
                                byte[] bytes = new BigInteger(strStdCsAll, 16).toByteArray();
                                String crc = CrcUtil.getTableCRC(bytes);
                                //Log.e("fasong:", CrcUtil.getTableCRC(bytes));
                                String sendAll = strStdCsAll + crc;
                                Log.e("fasong2:", sendAll);
                                sendDataByBle(sendAll, "");

                                if(Config.homeQiehuan==1){
                                    if(StringUtils.isEquals(dzZl,"08")){
                                        Config.isStdLingxian = 1;
                                        Config.lxType = 0;//每次勾选零线测试时，都默认是第一次进入，去除BC相值
                                        startActivity(new Intent(getActivity(), ZhizuCeshiStdLingxianYiActivity.class));//三通道08
                                    }else{
                                        startActivity(new Intent(getActivity(), ZhizuCeshiStdYiActivity.class));
                                    }
                                }else if(Config.homeQiehuan==2){
                                    startActivity(new Intent(getActivity(), ZhizuCeshiYnYiActivity.class));
                                }else if(Config.homeQiehuan==3){
                                    startActivity(new Intent(getActivity(), ZhizuCeshiDyYiActivity.class));
                                }
                            }

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


    public StdZhizuCeshiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZhizuCeshiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdZhizuCeshiFragment newInstance(String param1, String param2) {
        StdZhizuCeshiFragment fragment = new StdZhizuCeshiFragment();
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
        Log.e("zhizu===", "stop====");
        //bleConnectUtil.disConnect();
        //mHandler.removeCallbacks(checkConnetRunnable);
       // mHandler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.e("zhizu=============","likai");

//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.disConnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.ymType = "zhizuceshi";
        Config.yemianType2=0;

//        bleConnectUtil = new BleConnectUtil(getActivity());
//        if (!bleConnectUtil.isConnected()&&StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)) {
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
//            //bleConnectUtil.connect("94:A9:A8:32:76:49",10,10);//HC-08：94:A9:A8:32:76:49
//            bleConnectUtil.setCallback(blecallback);
//        }
        //initModel();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("zhizu=============","onHiddenChanged");

        String strStdCsAllStdSave = "6886"+"73"+"00000000"+"00"+"00"+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        //sendDataByBle(sendAllYnSave,"");

        Config.isStdLingxian = 0;
        Config.homeQiehuan = 1;
        StdZzCsStdCsFragment zzCsStdCsFragment = StdZzCsStdCsFragment.newInstance("", "");
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        getChildFragmentManager().beginTransaction().replace(R.id.frameZhizuCeshi, zzCsStdCsFragment, "zzCsStdCsFragment").commit();


        //mHandler.removeCallbacksAndMessages(null);
        //bleConnectUtil.disConnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mainView = inflater.inflate(R.layout.fragment_std_zhizu_ceshi, null);
        initView(mainView);
        initModel();
//        initAdapter();

        return mainView;
        //return inflater.inflate(R.layout.fragment_zhizu_ceshi, container, false);
    }

    public void initModel() {
        bleConnectUtil = new BleConnectUtil(getActivity());
//        if (!bleConnectUtil.isConnected()&&StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)) {
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
//            //bleConnectUtil.connect("94:A9:A8:32:76:49",10,10);//HC-08：94:A9:A8:32:76:49
//            bleConnectUtil.setCallback(blecallback);
//        }
//        bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
        if(StringUtils.noEmpty(Config.lyAddress)){
            bleConnectUtil.connect(Config.lyAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
        }else if(StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);
        }
        //bleConnectUtil.connect(Config.lyAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
        bleConnectUtil.setCallback(blecallback);
        //任何页面只要点击一次直阻测试，就执行一条返回（73）指令，保证在第一个页面进行其他操作.
//        String strStdCsAllStdSave = "6886"+"73"+"00000000"+"00"+"00"+"0000";
//        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
//        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
//        Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
//        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
//        sendDataByBle(sendAllYnSave,"");
        //sendDataByBle("68866e0100060a0509","");
    }

    public void initView(View view) {

         StdZzCsStdCsFragment zzCsStdCsFragment= StdZzCsStdCsFragment.newInstance("", "");
         //Config.homeQiehuan = 1;
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        getChildFragmentManager().beginTransaction().replace(R.id.frameZhizuCeshi, zzCsStdCsFragment, "zzCsStdCsFragment").commit();
        //一
        llYi = view.findViewById(R.id.llZhizuCeshiYi);
        llStd = view.findViewById(R.id.llZzCsStdCs);
        llYn = view.findViewById(R.id.llZzCsYnCs);
        llDy = view.findViewById(R.id.llZzCsDyCs);
        llStart = view.findViewById(R.id.llZzCsStartCs);
        btnStart = view.findViewById(R.id.btnZzCsStartCs);

        llStd.setOnClickListener(this);
        llYn.setOnClickListener(this);
        llDy.setOnClickListener(this);
        llStart.setOnClickListener(this);
        btnStart.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llZzCsStdCs:
                //三通道测试
                llStd.setBackgroundResource(R.drawable.zhizu_ceshi_ll_bac);
                llYn.setBackgroundResource(R.drawable.zhizu_ceshi_ll_1_bac);
                llDy.setBackgroundResource(R.drawable.zhizu_ceshi_ll_1_bac);
                Config.isStdLingxian = 0;//点击三通道测试后还原零线未启动
                Config.homeQiehuan = 1;
                StdZzCsStdCsFragment zzCsStdCsFragment = StdZzCsStdCsFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                getChildFragmentManager().beginTransaction().replace(R.id.frameZhizuCeshi, zzCsStdCsFragment, "zzCsStdCsFragment").commit();
                break;

            case R.id.llZzCsYnCs:
                //YN测试
                llStd.setBackgroundResource(R.drawable.zhizu_ceshi_ll_1_bac);
                llYn.setBackgroundResource(R.drawable.zhizu_ceshi_ll_bac);
                llDy.setBackgroundResource(R.drawable.zhizu_ceshi_ll_1_bac);
                Config.homeQiehuan = 2;
                StdZzCsYnCsFragment zzCsYnCsFragment = StdZzCsYnCsFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                getChildFragmentManager().beginTransaction().replace(R.id.frameZhizuCeshi, zzCsYnCsFragment, "zzCsYnCsFragment").commit();
                break;

            case R.id.llZzCsDyCs:
                //DY测试
                llStd.setBackgroundResource(R.drawable.zhizu_ceshi_ll_1_bac);
                llYn.setBackgroundResource(R.drawable.zhizu_ceshi_ll_1_bac);
                llDy.setBackgroundResource(R.drawable.zhizu_ceshi_ll_bac);
                Config.homeQiehuan = 3;
                StdZzCsDyCsFragment zzCsDyCsFragment = StdZzCsDyCsFragment.newInstance("", "");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                getChildFragmentManager().beginTransaction().replace(R.id.frameZhizuCeshi, zzCsDyCsFragment, "zzCsDyCsFragment").commit();

                break;

            //case R.id.llZzCsStartCs:
            case R.id.btnZzCsStartCs:
                //开始测试
                Config.zzcsBtnType = 1;
                sendDataByBle(SendUtil.initSend("73"),"");//先发送返回
                //timer.schedule(timerTask, 2000); // 1秒后执行一
                Log.e("csType==", Config.homeQiehuan + "");
                //if(bleConnectUtil.isConnected()){
//                Zhiling zhiling = new Zhiling();
//                String csfsZl = zhiling.ceshifangfa(Config.homeQiehuan + "");
//
//                List<TestSet> list = ZzCsStartCs.getChildData(StdZhizuCeshiFragment.this, Config.homeQiehuan);
//
//                String xw = list.get(0).getXw();
//                String zc = list.get(0).getZc();
//                String dl = list.get(0).getDl();
//                String dz = list.get(0).getDz();
//
//                String xwZl = zhiling.ceshixiangwei(xw);
//                String zcZl = zhiling.zhuciqidong(zc);
//                String dlZl = zhiling.ceshidianliu(dl);
//                String dzZl = zhiling.zhuciqidong(dz);//零线电阻，暂时没用
//                Log.e("dyDl==", list.toString());
//                Log.e("dyDl==", csfsZl + "," + dl + "," + xw + "," + dz + "," + zc);
//                Log.e("dyDl==1", xwZl);
//
//                Config.csdlStr = dl;
//
//                //tvStdDzDl.setText(dl);//设置三通道(二)电阻页面中，电流的选择值，提前设置好，后续直接显示
//                String strStdCsAll = "6886" + "6b" + csfsZl + xwZl + zcZl + dlZl + "00" + "00" + "0000";
//                byte[] bytes = new BigInteger(strStdCsAll, 16).toByteArray();
//                String crc = CrcUtil.getTableCRC(bytes);
//                //Log.e("fasong:", CrcUtil.getTableCRC(bytes));
//                String sendAll = strStdCsAll + crc;
//                Log.e("fasong2:", sendAll);
//                sendDataByBle(sendAll, "");

                //***************************************如果拆分后需要如下三个判断*********************************************************
//                if(Config.homeQiehuan==1){
//                    startActivity(new Intent(getActivity(), ZhizuCeshiStdYiActivity.class));
//                }else if(Config.homeQiehuan==2){
//                    startActivity(new Intent(getActivity(), ZhizuCeshiYnYiActivity.class));
//                }else if(Config.homeQiehuan==3){
//                    startActivity(new Intent(getActivity(), ZhizuCeshiDyYiActivity.class));
//                }
                //************************************************************************************************
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
    // 创建定时器
    Timer timer = new Timer();
    // 创建定时器任务
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println("Hello world!");
            //YN：68 86 6B 01 00 06 06 05 55 B5 89
            Log.e("csType==", Config.homeQiehuan + "");
            //if(bleConnectUtil.isConnected()){
            Zhiling zhiling = new Zhiling();
            String csfsZl = zhiling.ceshifangfa(Config.homeQiehuan + "");

            List<TestSet> list = ZzCsStartCs.getChildData(StdZhizuCeshiFragment.this, Config.homeQiehuan);

            String xw = list.get(0).getXw();
            String zc = list.get(0).getZc();
            String dl = list.get(0).getDl();
            String dz = list.get(0).getDz();

            String xwZl = zhiling.ceshixiangwei(xw);
            String zcZl = zhiling.zhuciqidong(zc);
            String dlZl = zhiling.ceshidianliu(dl);
            String dzZl = zhiling.zhuciqidong(dz);//零线电阻，暂时没用
            Log.e("dyDl==", list.toString());
            Log.e("dyDl==", csfsZl + "," + dl + "," + xw + "," + dz + "," + zc);
            Log.e("dyDl==1", xwZl);

            Config.csdlStr = dl;

            //tvStdDzDl.setText(dl);//设置三通道(二)电阻页面中，电流的选择值，提前设置好，后续直接显示
            String strStdCsAll = "6886" + "6b" + csfsZl + xwZl + zcZl + dlZl + "00" + "00" + "0000";
            byte[] bytes = new BigInteger(strStdCsAll, 16).toByteArray();
            String crc = CrcUtil.getTableCRC(bytes);
            //Log.e("fasong:", CrcUtil.getTableCRC(bytes));
            String sendAll = strStdCsAll + crc;
            Log.e("fasong2:", sendAll);
            sendDataByBle(sendAll, "");

            //***************************************如果拆分后需要如下三个判断*********************************************************
            if(Config.homeQiehuan==1){
                startActivity(new Intent(getActivity(), ZhizuCeshiStdYiActivity.class));
            }else if(Config.homeQiehuan==2){
                startActivity(new Intent(getActivity(), ZhizuCeshiYnYiActivity.class));
            }else if(Config.homeQiehuan==3){
                startActivity(new Intent(getActivity(), ZhizuCeshiDyYiActivity.class));
            }
            //************************************************************************************************
        }
    };
}