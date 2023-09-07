package com.yc.allbluetooth.youzai.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dlzk.entity.Shujuchuli;
import com.yc.allbluetooth.dtd10c.entity.JlCx;
import com.yc.allbluetooth.std.adapter.SjglNewAdapter;
import com.yc.allbluetooth.std.entity.SjglNew;
import com.yc.allbluetooth.std.util.Zhiling;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.BytesToHexString;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SPUtils;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.youzai.adapter.SjcyAdapter;
import com.yc.allbluetooth.youzai.entity.Shujuchayue;
import com.yc.allbluetooth.youzai.util.CrcAll;
import com.yc.allbluetooth.youzai.util.ZhilingToHuanyuan;

import org.apache.poi.ss.formula.functions.T;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class YzSjcyActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private TextView tvGongTs;
    private TextView tvDiJy;
    private TextView tvGongJy;
    private TextView tvCk;
    private TextView tvDc;
    private TextView tvQk;
    private TextView tvSyy;
    private TextView tvXyy;
    private TextView tvFanhui;
    private TextView tvTime;

    List<Shujuchayue> mDatas = new ArrayList<>();
    List<Shujuchayue> mDatas2 = new ArrayList<>();
    private SjcyAdapter mAdapter;

    private TextView tvQx;


    //public TextView tvAll;


    private String TAG = "YzSjcyActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    String csLongStr = "";//参数长度字符串
    int csLong = 0;//参数字节个数

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
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"yzSjcy")){
                        String msgStr = msg.obj.toString();
                        //Log.e(TAG, "Sjcy:"+msgStr);
                        if(IndexOfAndSubStr.isIndexOf(msgStr,"FEEF")){
                            newMsgStr = msgStr;
                        }else {
                            newMsgStr = newMsgStr + msgStr;
                            //Log.e(TAG, "lsjl:"+newMsgStr);
                        }
                        if(IndexOfAndSubStr.isIndexOf(newMsgStr,"FEEF")&&IndexOfAndSubStr.isIndexOf(newMsgStr,"FDDF")){
                            Log.e(TAG, "lsjl:"+newMsgStr);
                            csLongStr = StringUtils.subStrStartToEnd(newMsgStr, 8, 12);;//12-16取反
                            csLong = ShiOrShiliu.parseInt(HexUtil.reverseHex(csLongStr));//442
                            tvGongTs.setText(csLong/34+"");
                            for(int i=0;i<csLong/34;i++){
                                String csStr = StringUtils.subStrStartToEnd(newMsgStr,12+i*68,12+(i+1)*68);
                                Log.e(TAG,csStr);
                                String lcStr = StringUtils.subStrStartToEnd(csStr,0,4);
                                String scStr = StringUtils.subStrStartToEnd(csStr,4,8);
                                String lmdStr = StringUtils.subStrStartToEnd(csStr,8,12);
                                String fjzzStr = StringUtils.subStrStartToEnd(csStr,12,16);//分接自增
                                String fj1Str = StringUtils.subStrStartToEnd(csStr,16,20);//分接1
                                String fj2Str = StringUtils.subStrStartToEnd(csStr,20,24);//分接2
                                String spbhStr = StringUtils.subStrStartToEnd(csStr,24,48);//试品编号
                                String ljfsStr = StringUtils.subStrStartToEnd(csStr,48,52);
                                String csxwStr = StringUtils.subStrStartToEnd(csStr,52,56);
                                String nianStr = StringUtils.subStrStartToEnd(csStr,56,58);//时间
                                String yueStr = StringUtils.subStrStartToEnd(csStr,58,60);//时间
                                String riStr = StringUtils.subStrStartToEnd(csStr,60,62);//时间
                                String shiStr = StringUtils.subStrStartToEnd(csStr,62,64);//时间
                                String fenStr = StringUtils.subStrStartToEnd(csStr,64,66);//时间
                                String miaoStr = StringUtils.subStrStartToEnd(csStr,66,68);//时间
                                //Log.e(TAG,spbhStr);
                                String spbh = BytesToHexString.gbkQuLing(spbhStr);
                                //Log.e(TAG,spbh);
                                //String spbh = BytesToHexString.hexStr2Str(spbhStr);//BytesToHexString.gbkQuLing(spbhStr)
                                int nian = ShiOrShiliu.parseInt(nianStr);
                                int yue = ShiOrShiliu.parseInt(yueStr);
                                int ri = ShiOrShiliu.parseInt(riStr);
                                int shi = ShiOrShiliu.parseInt(shiStr);
                                int fen = ShiOrShiliu.parseInt(fenStr);
                                int miao = ShiOrShiliu.parseInt(miaoStr);
                                String fj1 = StringUtils.subStrStartToEnd(fj1Str,0,2);
                                String fj2 = StringUtils.subStrStartToEnd(fj2Str,0,2);

                                String lc = ZhilingToHuanyuan.lc(lcStr);
                                String sc = ZhilingToHuanyuan.sc(scStr);
                                String lmd = ShiOrShiliu.parseInt(HexUtil.reverseHex(lmdStr))+"";
                                String ljfs = ZhilingToHuanyuan.ljfs(ljfsStr);
                                String csxs = ZhilingToHuanyuan.csxs(csxwStr);


                                Shujuchayue shujuchayue = new Shujuchayue();
                                shujuchayue.setId(i);
                                shujuchayue.setSpbh(spbh);
                                //Log.e(TAG,spbh);
                                shujuchayue.setCheck(false);
                                shujuchayue.setFjwz(fj1+"->"+fj2);
                                shujuchayue.setCssj("20"+ShiOrShiliu.xiaoyushiBl(nian)+"-"+ShiOrShiliu.xiaoyushiBl(yue)+"-"+ShiOrShiliu.xiaoyushiBl(ri)+" "
                                        +ShiOrShiliu.xiaoyushiBl(shi)+":"+ShiOrShiliu.xiaoyushiBl(fen)+":"+ShiOrShiliu.xiaoyushiBl(miao));

                                shujuchayue.setLc(lc);
                                shujuchayue.setSc(sc);
                                shujuchayue.setLmd(lmd);
                                shujuchayue.setLjfs(ljfs);
                                shujuchayue.setCsxs(csxs);
                                mDatas.add(shujuchayue);
                            }
                            mAdapter = new SjcyAdapter(YzSjcyActivity.this,mDatas,tvQx);
                            lv.setAdapter(mAdapter);
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

        setContentView(R.layout.activity_yz_sjcy);
        Config.ymType = "yzSjcy";
        ActivityCollector.addActivity(this);
        initView();
        initModel();
        new TimeThread().start();
    }
    public void initView(){
        lv = findViewById(R.id.lvYzSjcy);
        tvGongTs = findViewById(R.id.tvYzSjcyGong);
        tvDiJy = findViewById(R.id.tvYzSjcyDijiye);
        tvGongJy = findViewById(R.id.tvYzSjcyGongJiye);
        tvCk = findViewById(R.id.tvYzSjcyCk);
        tvDc = findViewById(R.id.tvYzSjcyDc);
        tvQk = findViewById(R.id.tvYzSjcyQk);
//        tvSyy = findViewById(R.id.tvYzSjcySyy);
//        tvXyy = findViewById(R.id.tvYzSjcyXyy);
        tvFanhui = findViewById(R.id.tvYzSjcyFh);
        tvQx = findViewById(R.id.tvYzSjcyQx);
        tvTime = findViewById(R.id.tvYzSjcyShijian);
        tvCk.setOnClickListener(this);
        tvDc.setOnClickListener(this);
        tvQk.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        tvQx.setOnClickListener(this);

        //        for(int i=0;i<50;i++){
//            Shujuchayue shujuchayue = new Shujuchayue();
//            shujuchayue.setId(i);
//            shujuchayue.setSpbh(""+i);
//            shujuchayue.setCheck(false);
//            shujuchayue.setFjwz("01->02");
//            shujuchayue.setCssj(GetTime.getTime(1));
//            mDatas.add(shujuchayue);
//        }
        mAdapter = new SjcyAdapter(YzSjcyActivity.this,mDatas,tvQx);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shujuchayue shujuchayue = mDatas.get(position);
                Log.e("lv==Id",shujuchayue.getId()+"");
                //sendDataByBle("feef04a00100"+ ShiOrShiliu.toHexString(shujuchayue.getId()+1) +"00fddf","");
                Intent intent=new Intent();
                intent.setClass(YzSjcyActivity.this,YzSjcyInfoActivity.class);
                intent.putExtra("lvId",shujuchayue.getId());     //put传到另一个界面
                intent.putExtra("lc",shujuchayue.getLc());
                intent.putExtra("sc",shujuchayue.getSc());
                intent.putExtra("lmd",shujuchayue.getLmd());
                intent.putExtra("spbh",shujuchayue.getSpbh());
                intent.putExtra("csfj",shujuchayue.getFjwz());
                intent.putExtra("ljfs",shujuchayue.getLjfs());
                intent.putExtra("csxs",shujuchayue.getCsxs());
                intent.putExtra("cssj",shujuchayue.getCssj());
                //启动
                startActivity(intent);
                //startActivity(new Intent(YzSjcyActivity.this,YzSjcyInfoActivity.class));
            }
        });

    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(YzSjcyActivity.this);
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"f00000fddf",Config.yzCrcTYpe), "");
            }
        }, 1000);//1秒后执行Runnable中的run方法

    }
    /**
     * 屏幕左下角时间显示，每隔一秒执行一次
     */
    public class TimeThread extends Thread{
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvYzSjcyQk:
                List<Shujuchayue> ids = new ArrayList<>();
                String idsStr = "";
                if (mAdapter.flage) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).isCheck) {
                            ids.add(mDatas.get(i));
                            idsStr+=mDatas.get(i).getCssj()+",";
                        }
                    }

                    // 根据选中的条目进行全部移除
                    Log.e("gouwuche==delete", ids.toString());
                    Log.e("gouwuche==delete2", idsStr);
                    //gouwucheUserModel.deleteGouwuche(getActivity(),idsStr);
                    // 根据选中的条目进行全部移除
                    mDatas.removeAll(ids);
                    //删除之后，重新保存
                    Log.e("列表删除后==", mDatas.toString());
//                    if(mDatas==null||mDatas.size()==0){
//                        Log.e("列表删除后==归零", mDatas.toString());
//                        //Config.getSjglListType = 0;
//                        SjglListType = 0;
//                        //保存删除后的最新列表数据
//                        //Gson gson = new Gson();
//                        //String jsonStr=gson.toJson(mDatas); //将List转换成Json
//                        SharedPreferences.Editor editor = sp.edit() ;
//                        editor.putString("jiluchaxunList", "") ; //存入json串
//                        editor.commit() ;  //提交
//                        SPUtils.put(this,"abc","");
//                    }else{
//                        //保存删除后的最新列表数据
//                        Gson gson = new Gson();
//                        String jsonStr=gson.toJson(mDatas); //将List转换成Json
//                        SharedPreferences.Editor editor = sp.edit() ;
//                        editor.putString("jiluchaxunList", jsonStr) ; //存入json串
//                        editor.commit() ;  //提交
//                        SPUtils.put(this,"abc",jsonStr);
//                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tvYzSjcyFh:
                finish();
                break;
            case R.id.tvYzSjcyQx:
                String qxStr = tvQx.getText().toString();
                if(StringUtils.isEquals(qxStr,getString(R.string.quanxuan))){//全选中
                    tvQx.setText(getString(R.string.quanqing));
                    tvQx.setBackgroundResource(R.color.result_minor_text);
                    if (mAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            Shujuchayue shujuchuli =mDatas.get(i);
                            shujuchuli.isCheck = true;
                            if (shujuchuli.isCheck) {

                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }else{//全部未选中
                    tvQx.setText(getString(R.string.quanxuan));
                    tvQx.setBackgroundResource(R.color.index2);
                    if (mAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            mDatas.get(i).isCheck = false;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }
}