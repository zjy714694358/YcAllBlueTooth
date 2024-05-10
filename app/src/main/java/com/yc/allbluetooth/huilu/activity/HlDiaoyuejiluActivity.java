package com.yc.allbluetooth.huilu.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.dyjl.DyJlNewActivity;
import com.yc.allbluetooth.bianbi.adapter.DyjlNewAdapter;
import com.yc.allbluetooth.bianbi.entity.DiaoyuejiluNew;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dlzk.adapter.ShujuchuliAdapter;
import com.yc.allbluetooth.dlzk.entity.Shujuchuli;
import com.yc.allbluetooth.huilu.adapter.HLDyjlAdapter;
import com.yc.allbluetooth.huilu.entity.HlDiaoyuejilu;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.DateUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SPUtils;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class HlDiaoyuejiluActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView lv;
    private TextView tvShanchu;
    private TextView tvDayin;
    private TextView tvDaochu;
    private TextView tvFanhui;
    List<HlDiaoyuejilu> mDatas = new ArrayList<>();
    private HLDyjlAdapter mAdapter;
    SharedPreferences sp;
    List<HlDiaoyuejilu> mDatas2 = new ArrayList<>();
    HLDyjlAdapter hlDyjlAdapter;

    int SjglListType = 0;
    int jinruPeizhi = 0;

    private String TAG = "DlzkShujuActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    //String newMsgStr2 = "";
    //配置文件
    String sjcd;//数据长度
    String yqly;//仪器类型
    String danorSan;//单相或三相
    String gyjxlx;//高压接线类型：0,1,2,3
    String dyjxlx;//低压接线类型：0,1,2,3
    String zubie;//组别
    String csfj;//测试分接
    String edfj;//额定分接
    String nian;//年
    String yue;//月
    String ri;//日
    String shi;//时
    String fen;//分
    String miao;//秒
    String rwbh;//任务编号
    String ab;//AB
    String bcOrJixing;//BC或者极性：1+；0-
    String ca;//CA
    String crcJy;//Crc校验
    String sjxh = "";

    int jinruI = 0;

    Shujuchuli shujuchuli = new Shujuchuli();

    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;
    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.BLUETOOTH_GETDATA:
                    if (StringUtils.isEquals(Config.ymType, "huiluDyjl")) {
                        String msgStr = msg.obj.toString();
                        Log.e(TAG + "，msg0", msgStr);
                        if (msgStr.length() != 26) {//msgStr.length()==22||msgStr.length()>26
                            if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                                newMsgStr = msgStr;
                                Log.e(TAG + "6677", newMsgStr);
                            } else {
                                if (!IndexOfAndSubStr.isIndexOf(newMsgStr, msgStr)) {
                                    newMsgStr = newMsgStr + msgStr;
                                    Log.e(TAG + 2, newMsgStr);
                                }
//                                newMsgStr = newMsgStr + msgStr;
//                                //可以
//                                Log.e(TAG + "可以", newMsgStr);
                            }
                            if (newMsgStr.length() == 62) {//>40
                                //可以
                                Log.e("newMsgStr=62", "new1:" + newMsgStr);
                                sjcd = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                yqly = StringUtils.subStrStartToEnd(newMsgStr, 6, 8);
                                danorSan = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                gyjxlx = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                dyjxlx = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                zubie = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                csfj = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                edfj = StringUtils.subStrStartToEnd(newMsgStr, 20, 28);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 30, 32);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 32, 34);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 34, 36);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 36, 38);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 38, 40);
                                rwbh = StringUtils.subStrStartToEnd(newMsgStr, 40, 56);
                                ab = StringUtils.subStrStartToEnd(newMsgStr, 56, 64);
                                Log.e(TAG + "1" + "a", ab);
                                bcOrJixing = StringUtils.subStrStartToEnd(newMsgStr, 64, 72);
                                Log.e(TAG + "1" + "b", bcOrJixing);
                                ca = StringUtils.subStrStartToEnd(newMsgStr, 72, 80);
                                Log.e(TAG + "1" + "c", ca);
                                crcJy = StringUtils.subStrStartToEnd(newMsgStr,80,84);

                                if (!StringUtils.isEquals(sjxh, "ABAB")) {
                                    HlDiaoyuejilu jlCx = new HlDiaoyuejilu();
                                    Config.getSjglListId += 1;
                                    jlCx.setId(Config.getSjglListId);
                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    jlCx.setJilushijian("20" + nian + "-" + yue + "-" + ri + " " + shi + ":" + fen + ":" + miao);
                                    jlCx.setBianhao(rwbh);
                                    jlCx.setDlz(csfj);
                                    jlCx.setDzz(ab);
                                    mDatas2.add(jlCx);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getJilushijian() + ";" + o.getJilushijian()))), ArrayList::new));
                                    Log.e("=================2", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<HlDiaoyuejilu>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(HlDiaoyuejilu lhs, HlDiaoyuejilu rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getJilushijian());
                                            Date date2 = DateUtil.stringToDate(rhs.getJilushijian());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                            //更新UI数据列表
                                    mAdapter = new HLDyjlAdapter(HlDiaoyuejiluActivity.this, mDatas);
                                    lv.setAdapter(mAdapter);
                                    //Config.getSjglListType+=1;
                                    SjglListType += 1;
                                    sendDataByBle(SendUtil.jiluchaxunSend("6b", SjglListType), "");
                                } else {
                                    SjglListType -= 1;
                                }
                                //SharedPreferences sp = getContext().getSharedPreferences("shujuguanli", Activity.MODE_PRIVATE);//创建sp对象
                                Gson gson = new Gson();
                                String jsonStr = gson.toJson(mDatas); //将List转换成Json
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("jiluchaxunList", jsonStr); //存入json串
                                editor.commit();  //提交

                            }
                        }
                    }
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
        setContentView(R.layout.activity_hl_diaoyuejilu);
        ActivityCollector.addActivity(this);
        Config.ymType = "huiluDyjl";
        initView();
    }
    public void initView(){
        lv = findViewById(R.id.lvHlDyjl);
        tvShanchu = findViewById(R.id.tvHlDyjlShanchu);
        tvDayin = findViewById(R.id.tvHlDyjlDayin);
        tvDaochu = findViewById(R.id.tvHlDyjlDaochu);
        tvFanhui = findViewById(R.id.tvHlDyjlFanhui);
        tvShanchu.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvDaochu.setOnClickListener(this);
        for(int i=0;i<10;i++){
            HlDiaoyuejilu diaoyuejilu = new HlDiaoyuejilu();
            diaoyuejilu.setId(i);
            diaoyuejilu.setBianhao("12345678");
            diaoyuejilu.setJilushijian("2024-05-06 12:12:1"+i);
            diaoyuejilu.setDlz(i+".000");
            diaoyuejilu.setDzz(i+".000");
            mDatas.add(diaoyuejilu);
        }
        sp = this.getSharedPreferences("jiluchaxun", Activity.MODE_PRIVATE);//创建sp对象

        //读取数据
        //SharedPreferences sp = this.getSharedPreferences("SP_NewUserModel_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String listJson = sp.getString("hljiluchaxunList", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        Log.e("spStr===", listJson);
        Log.e("spAbcStr===", SPUtils.get(this, "hlabc", ""));
        if (StringUtils.noEmpty(listJson) && !"[]".equals(listJson))  //防空判断
        {
            Gson gson = new Gson();
            mDatas2 = gson.fromJson(listJson, new TypeToken<List<DiaoyuejiluNew>>() {
            }.getType()); //将json字符串转换成List集合
        }
        //根据时间属性去重
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDatas = mDatas2.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getJilushijian() + ";" + o.getJilushijian()))), ArrayList::new));
        }
        Log.e("=================1", mDatas.toString());
        //对mList数组的数据按data字段升序
        Collections.sort(mDatas, new Comparator<HlDiaoyuejilu>() {
            /**
             *时间排序
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(HlDiaoyuejilu lhs, HlDiaoyuejilu rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getJilushijian());
                Date date2 = DateUtil.stringToDate(rhs.getJilushijian());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });
        mAdapter = new HLDyjlAdapter(HlDiaoyuejiluActivity.this,mDatas);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HlDiaoyuejilu diaoyuejilu = mDatas.get(position);
                Log.e("lv==Id",diaoyuejilu.getId()+","+diaoyuejilu.bianhao+diaoyuejilu.getJilushijian()+diaoyuejilu.getDlz()+diaoyuejilu.getDzz());
                //startActivity(new Intent(YzSjcyActivity.this,YzSjcyInfoActivity.class));
            }
        });
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mDatas); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("hljiluchaxunList", jsonStr); //存入json串
        editor.commit();  //提交
        SPUtils.put(this, "hlabc", jsonStr);
    }
    public void initModel(){
        SjglListType = 0;
        bleConnectUtil = new BleConnectUtil(HlDiaoyuejiluActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     *要执行的操作
                     */
                    sendDataByBle(SendUtil.jiluchaxunSend("6c", 0), "");
                }
            }, 1000);//3秒后执行Runnable中的run方法
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
    public void onClick(View view) {
        if (view.getId() == R.id.tvHlDyjlShanchu){//清除
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        } else if (view.getId() == R.id.tvHlDyjlDayin) {//打印
            //finish();
        }else if (view.getId() == R.id.tvHlDyjlDaochu) {//导出
            //finish();
        }else if (view.getId() == R.id.tvHlDyjlFanhui) {//返回
            finish();
        }
    }
}