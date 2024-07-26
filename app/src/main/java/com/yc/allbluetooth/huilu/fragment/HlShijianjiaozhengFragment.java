package com.yc.allbluetooth.huilu.fragment;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.EditorAction;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HlShijianjiaozhengFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HlShijianjiaozhengFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String TAG = "HlShijianjiaozhengFragment";
    private EditText etNian;
    private EditText etYue;
    private EditText etRi;
    private EditText etShi;
    private EditText etFen;
    private EditText etMiao;
    private TextView tvQueren;
    private TextView tvFanhui;

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

                    break;
                default:
                    break;
            }
        }
    };


    public HlShijianjiaozhengFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HlShijianjiaozhengFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HlShijianjiaozhengFragment newInstance(String param1, String param2) {
        HlShijianjiaozhengFragment fragment = new HlShijianjiaozhengFragment();
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
        //return inflater.inflate(R.layout.fragment_hl_shijianjiaozheng, container, false);
        View mainView = inflater.inflate(R.layout.fragment_hl_shijianjiaozheng,null);
        initView(mainView);
        Config.ymType = "huiluSjjz";
        initModel();
        return mainView;
    }
    public void initView(View view){
        etNian = view.findViewById(R.id.etHlSjjzNian);
        etYue = view.findViewById(R.id.etHlSjjzYue);
        etRi = view.findViewById(R.id.etHlSjjzRi);
        etShi = view.findViewById(R.id.etHlSjjzShi);
        etFen = view.findViewById(R.id.etHlSjjzFen);
        etMiao = view.findViewById(R.id.etHlSjjzMiao);
        tvQueren = view.findViewById(R.id.tvHlSjjzQueren);
        tvFanhui = view.findViewById(R.id.tvHlSjjzFanhui);
        tvQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nianStr = etNian.getText().toString();
                String yueStr = etYue.getText().toString();
                String riStr = etRi.getText().toString();
                String shiStr = etShi.getText().toString();
                String fenStr = etFen.getText().toString();
                String miaoStr = etMiao.getText().toString();
//                sendDataByBle(SendUtil.shijianSend("6e", ShiOrShiliu.xiaoyushiBl(StringUtils.strToInt(nianStr))
//                        +ShiOrShiliu.xiaoyushiBl(StringUtils.strToInt(yueStr)) +ShiOrShiliu.xiaoyushiBl(StringUtils.strToInt(riStr))
//                        +ShiOrShiliu.xiaoyushiBl(StringUtils.strToInt(shiStr))+ShiOrShiliu.xiaoyushiBl(StringUtils.strToInt(fenStr))
//                        +ShiOrShiliu.xiaoyushiBl(StringUtils.strToInt(miaoStr))),"");
                sendDataByBle(SendUtil.shijianSend("6e", StringUtils.bulingXiaoShiliu(nianStr)+StringUtils.bulingXiaoShiliu(yueStr)+
                        StringUtils.bulingXiaoShiliu(riStr)+StringUtils.bulingXiaoShiliu(shiStr)+StringUtils.bulingXiaoShiliu(fenStr)+
                        StringUtils.bulingXiaoShiliu(miaoStr)),"");
            }
        });
        tvFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
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
        //Log.e("年=：",nian+yue+ri+shi+fen+miao+",type="+type);
        etNian.setText(nian);
        etYue.setText(yue);
        etRi.setText(ri);
        etShi.setText(shi);
        etFen.setText(fen);
        etMiao.setText(miao);

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
    public void onDestroy() {
        Log.e(TAG,"destroy()");
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        super.onDestroy();
    }
}