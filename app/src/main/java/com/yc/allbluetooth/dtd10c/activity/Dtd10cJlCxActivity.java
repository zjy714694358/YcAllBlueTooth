package com.yc.allbluetooth.dtd10c.activity;



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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dtd10c.adapter.JlCxAdapter;
import com.yc.allbluetooth.dtd10c.entity.JlCx;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.DateUtil;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SPUtils;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;

import org.apache.poi.ss.formula.functions.T;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Dtd10cJlCxActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lvJlCxList;
    private List<JlCx> mDatas2 = new ArrayList<>();
    private JlCxAdapter mAdapter;

    List<JlCx> mDatas = new ArrayList<>();

    public CheckBox ckbAll;
    private TextView tvDelete;
    private TextView tvBack;
    SharedPreferences sp;
    String TAG = "jiluchaxun";

    int SjglListType = 0;

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

    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals(Config.ymType,"dtdZzCsJlcx")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG + "，msg0", msgStr);
                        if (msgStr.length() != 26) {//msgStr.length()==22||msgStr.length()>26
                            if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                                newMsgStr = msgStr;
                                Log.e(TAG + "6677", newMsgStr);
                            } else {
                                if(!IndexOfAndSubStr.isIndexOf(newMsgStr,msgStr)){
                                    newMsgStr = newMsgStr+msgStr;
                                    Log.e(TAG+2,newMsgStr);
                                }
//                                newMsgStr = newMsgStr + msgStr;
//                                //可以
//                                Log.e(TAG + "可以", newMsgStr);
                            }
                            if (newMsgStr.length() == 62) {//>40
                                //可以
                                Log.e("newMsgStr=62", "new1:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                                //0B00010101==>A0,B0,C0
                                //0B00101010==>ab,bc,ca
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 10);
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 30, 32);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 32, 40);
                                Log.e(TAG + "1" + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 40, 48);
                                Log.e(TAG + "1" + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 48, 56);
                                Log.e(TAG + "1" + "c", c0orca);
                                Log.e(TAG + "1", "数据序号：" + sjxh);
                                Log.e(TAG + "1", "测试相位：" + csxw);
                                if (!StringUtils.isEquals(sjxh, "ABAB")) {
                                    JlCx jlCx = new JlCx();
                                    Config.getSjglListId += 1;
                                    jlCx.setId(Config.getSjglListId);
                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    jlCx.setShijian("20" + nian + "-" + yue + "-" + ri + " " + shi + ":" + fen + ":" + miao);
                                    jlCx.setFenjie(fjwz);
                                    jlCx.setCeliangwendu(cswd);
                                    jlCx.setA0orab(a0orab);
                                    jlCx.setB0orbc(b0orbc);
                                    jlCx.setC0orca(c0orca);
                                    jlCx.setA0orabType(csxw);
                                    mDatas2.add(jlCx);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================2", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<JlCx>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(JlCx lhs, JlCx rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getShijian());
                                            Date date2 = DateUtil.stringToDate(rhs.getShijian());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                            //更新UI数据列表
                                    mAdapter = new JlCxAdapter(Dtd10cJlCxActivity.this, mDatas, ckbAll);
                                    lvJlCxList.setAdapter(mAdapter);
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
                            } else if (newMsgStr.length() == 70) {//newMsgStr.length()>62&&newMsgStr.length()!=80
                                //newMsgStr = StringUtils.subStrStart(newMsgStr,26);
                                //可以
                                //可以
                                Log.e("newMsgStr2=70", "new2:" + newMsgStr);
                                Log.i(TAG + "2", "new2:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                                //0B00010101==>A0,B0,C0
                                //0B00101010==>ab,bc,ca
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 10);
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 30, 32);

                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 32, 40);
                                Log.e(TAG + "2" + "a", a0orab);
                                float a0orabF = 0;
                                String a0orabHl = HexUtil.reverseHex(a0orab);
                                try {
                                    a0orabF = Float.intBitsToFloat((int) HexUtil.parseLong(a0orabHl, 16));
                                } catch (HexUtil.NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                String a0orabStr2 = StringUtils.siweiYouxiaoStr(a0orabF + "");
                                Log.e(TAG + "2" + "a转换后", a0orabStr2);


                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 40, 48);
                                Log.e(TAG + "2" + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 48, 56);
                                Log.e(TAG + "2" + "c", c0orca);
                                Log.e(TAG + "2", "数据序号2：" + sjxh);
                                Log.e(TAG + "2", "测试相位2：" + csxw);
                                if (!StringUtils.isEquals(sjxh, "ABAB")) {
                                    JlCx jlCx = new JlCx();
                                    Config.getSjglListId += 1;
                                    jlCx.setId(Config.getSjglListId);

                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    jlCx.setShijian("20" + nian + "-" + yue + "-" + ri + " " + shi + ":" + fen + ":" + miao);
                                    jlCx.setFenjie(fjwz);
                                    jlCx.setCeliangwendu(cswd);
                                    jlCx.setA0orab(a0orab);
                                    jlCx.setB0orbc(b0orbc);
                                    jlCx.setC0orca(c0orca);
                                    jlCx.setA0orabType(csxw);


                                    mDatas2.add(jlCx);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================3", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<JlCx>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(JlCx lhs, JlCx rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getShijian());
                                            Date date2 = DateUtil.stringToDate(rhs.getShijian());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
//                            //更新UI数据列表
                                    mAdapter = new JlCxAdapter(Dtd10cJlCxActivity.this, mDatas, ckbAll);
                                    lvJlCxList.setAdapter(mAdapter);


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
                            } else if (newMsgStr.length() == 88) {
                                //可以
                                Log.e("newMsgStr=88-1", "new3:" + newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr, 26);
                                Log.e("newMsgStr=88-2", "new3:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                                //0B00010101==>A0,B0,C0
                                //0B00101010==>ab,bc,ca
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 10);
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 30, 32);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 32, 40);
                                Log.e(TAG + "3" + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 40, 48);
                                Log.e(TAG + "3" + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 48, 56);
                                Log.e(TAG + "3" + "c", c0orca);
                                Log.e(TAG + "3", "数据序号：" + sjxh);
                                Log.e(TAG + "3", "测试相位：" + csxw);
                                if (!StringUtils.isEquals(sjxh, "ABAB")) {
                                    JlCx jlCx = new JlCx();
                                    Config.getSjglListId += 1;
                                    jlCx.setId(Config.getSjglListId);
                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    jlCx.setShijian("20" + nian + "-" + yue + "-" + ri + " " + shi + ":" + fen + ":" + miao);
                                    jlCx.setFenjie(fjwz);
                                    jlCx.setCeliangwendu(cswd);
                                    jlCx.setA0orab(a0orab);
                                    jlCx.setB0orbc(b0orbc);
                                    jlCx.setC0orca(c0orca);
                                    jlCx.setA0orabType(csxw);

                                    mDatas2.add(jlCx);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================4", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<JlCx>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(JlCx lhs, JlCx rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getShijian());
                                            Date date2 = DateUtil.stringToDate(rhs.getShijian());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                            //更新UI数据列表
                                    mAdapter = new JlCxAdapter(Dtd10cJlCxActivity.this, mDatas, ckbAll);
                                    lvJlCxList.setAdapter(mAdapter);
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
                            } else if (newMsgStr.length() == 90) {
                                Log.e("大于80好多2", newMsgStr);
                                //可以
                                Log.e("newMsgStr=88-1", "new3:" + newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr, 28);
                                Log.e("newMsgStr=88-2", "new3:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                                //0B00010101==>A0,B0,C0
                                //0B00101010==>ab,bc,ca
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 10);
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 30, 32);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 32, 40);
                                Log.e(TAG + "3" + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 40, 48);
                                Log.e(TAG + "3" + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 48, 56);
                                Log.e(TAG + "3" + "c", c0orca);
                                Log.e(TAG + "3", "数据序号：" + sjxh);
                                Log.e(TAG + "3", "测试相位：" + csxw);
                                if (!StringUtils.isEquals(sjxh, "ABAB")) {
                                    JlCx jlCx = new JlCx();
                                    Config.getSjglListId += 1;
                                    jlCx.setId(Config.getSjglListId);
                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    jlCx.setShijian("20" + nian + "-" + yue + "-" + ri + " " + shi + ":" + fen + ":" + miao);
                                    jlCx.setFenjie(fjwz);
                                    jlCx.setCeliangwendu(cswd);
                                    jlCx.setA0orab(a0orab);
                                    jlCx.setB0orbc(b0orbc);
                                    jlCx.setC0orca(c0orca);
                                    jlCx.setA0orabType(csxw);

                                    mDatas2.add(jlCx);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================5", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<JlCx>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(JlCx lhs, JlCx rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getShijian());
                                            Date date2 = DateUtil.stringToDate(rhs.getShijian());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                            //更新UI数据列表
                                    mAdapter = new JlCxAdapter(Dtd10cJlCxActivity.this, mDatas, ckbAll);
                                    lvJlCxList.setAdapter(mAdapter);
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
                            } else if (newMsgStr.length() == 96) {
                                Log.e("大于80好多2", newMsgStr);
                                //可以
                                Log.e("newMsgStr=88-1", "new3:" + newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr, 34);
                                Log.e("newMsgStr=88-2", "new3:" + newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr, 4, 6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                //sjxhH = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                                //0B00010101==>A0,B0,C0
                                //0B00101010==>ab,bc,ca
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr, 6, 10);
                                csxw = StringUtils.subStrStartToEnd(newMsgStr, 10, 12);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr, 12, 14);
                                cswd = StringUtils.subStrStartToEnd(newMsgStr, 14, 16);
                                fjwz = StringUtils.subStrStartToEnd(newMsgStr, 16, 18);
                                csdl = StringUtils.subStrStartToEnd(newMsgStr, 18, 20);
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 20, 22);
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 22, 24);
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 24, 26);
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 26, 28);
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 28, 30);
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 30, 32);
                                a0orab = StringUtils.subStrStartToEnd(newMsgStr, 32, 40);
                                Log.e(TAG + "3" + "a", a0orab);
                                b0orbc = StringUtils.subStrStartToEnd(newMsgStr, 40, 48);
                                Log.e(TAG + "3" + "b", b0orbc);
                                c0orca = StringUtils.subStrStartToEnd(newMsgStr, 48, 56);
                                Log.e(TAG + "3" + "c", c0orca);
                                Log.e(TAG + "3", "数据序号：" + sjxh);
                                Log.e(TAG + "3", "测试相位：" + csxw);
                                if (!StringUtils.isEquals(sjxh, "ABAB")) {
                                    JlCx jlCx = new JlCx();
                                    Config.getSjglListId += 1;
                                    jlCx.setId(Config.getSjglListId);
                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    jlCx.setShijian("20" + nian + "-" + yue + "-" + ri + " " + shi + ":" + fen + ":" + miao);
                                    jlCx.setFenjie(fjwz);
                                    jlCx.setCeliangwendu(cswd);
                                    jlCx.setA0orab(a0orab);
                                    jlCx.setB0orbc(b0orbc);
                                    jlCx.setC0orca(c0orca);
                                    jlCx.setA0orabType(csxw);

                                    mDatas2.add(jlCx);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================6", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<JlCx>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(JlCx lhs, JlCx rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getShijian());
                                            Date date2 = DateUtil.stringToDate(rhs.getShijian());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                            //更新UI数据列表
                                    mAdapter = new JlCxAdapter(Dtd10cJlCxActivity.this, mDatas, ckbAll);
                                    lvJlCxList.setAdapter(mAdapter);
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
                        } else if (msgStr.length() != 8) {
                            Log.e(TAG + 0, "这是返回的第一条验证指令JLCX：" + msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr,4,6);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        /*Language language = new Language();
        if(Config.CN_US_TYPE==2){
            language.translateText(JlCxActivity.this,"",1);
        }else{
            language.translateText(JlCxActivity.this,"",2);
        }*/
        Resources resources = this.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if ("zh".equals(Config.zyType)) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_dtd10c_jl_cx);
        Config.ymType = "dtdZzCsJlcx";
        ActivityCollector.addActivity(this);

        initModel();
        initView();

        Log.e("记录查询", "onCreat()");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initView() {
        lvJlCxList = findViewById(R.id.lvJlCx);
        ckbAll = findViewById(R.id.ckbJlCxAll);
        tvDelete = findViewById(R.id.tvJlCxDelete);
        tvBack = findViewById(R.id.tvJlCxBack);
        sp = this.getSharedPreferences("jiluchaxun", Activity.MODE_PRIVATE);//创建sp对象

        //读取数据
        //SharedPreferences sp = this.getSharedPreferences("SP_NewUserModel_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String listJson = sp.getString("jiluchaxunList", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        Log.e("spStr===", listJson);
        Log.e("spAbcStr===", SPUtils.get(this, "abc", ""));
        if (StringUtils.noEmpty(listJson) && !"[]".equals(listJson))  //防空判断
        {
            Gson gson = new Gson();
            mDatas2 = gson.fromJson(listJson, new TypeToken<List<JlCx>>() {
            }.getType()); //将json字符串转换成List集合
        }
        //根据时间属性去重
        mDatas = mDatas2.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
        Log.e("=================1", mDatas.toString());
        //对mList数组的数据按data字段升序
        Collections.sort(mDatas, new Comparator<JlCx>() {
            /**
             *时间排序
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(JlCx lhs, JlCx rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getShijian());
                Date date2 = DateUtil.stringToDate(rhs.getShijian());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });

        mAdapter = new JlCxAdapter(Dtd10cJlCxActivity.this, mDatas, ckbAll);
        lvJlCxList.setAdapter(mAdapter);
        lvJlCxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JlCx jlCx = mDatas.get(position);
                Log.e("lv==Id", jlCx.getId() + "");
            }
        });


        Gson gson = new Gson();
        String jsonStr = gson.toJson(mDatas); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("jiluchaxunList", jsonStr); //存入json串
        editor.commit();  //提交
        SPUtils.put(this, "abc", jsonStr);

        tvBack.setOnClickListener(this);
        ckbAll.setOnClickListener(this);
        tvDelete.setOnClickListener(this);

    }

    public void initModel() {
        Log.e("记录查询", "onCreat()==>initModel()");
        Log.e("","onCreat()==>initModel()");
        SjglListType = 0;
        bleConnectUtil = new BleConnectUtil(Dtd10cJlCxActivity.this);
        if (!bleConnectUtil.isConnected() && StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)) {
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     *要执行的操作
                     */
                    sendDataByBle(SendUtil.jiluchaxunSend("6b", 0), "");
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

            Message message = new Message();
            message.obj = str;
            message.what = Config.BLUETOOTH_GETDATA;
            mHandler.sendMessage(message);
        }

        @Override
        public void onSuccessSend() {
            //数据发送成功
            Log.e("JlCx", "onSuccessSend: ");

        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("JlCx", "onDisconnect: ");
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
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
        //bleConnectUtil.disConnect();
        //mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        //bleConnectUtil.disConnect();
        //mHandler.removeCallbacksAndMessages(null);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//
//        }
        bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("记录查询","onResume()");
        Config.ymType = "dtdZzCsJlcx";
        //SjglListType = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvJlCxBack:
                sendDataByBle(SendUtil.initSend("6e"),"");
                finish();
                break;
            case R.id.ckbJlCxAll:
                if(((CheckBox)v).isChecked()){//全选中
                    if (mAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            JlCx jlCx =mDatas.get(i);
                            jlCx.isCheck = true;
                            if (jlCx.isCheck) {

                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }else{//全部未选中
                    if (mAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            mDatas.get(i).isCheck = false;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.tvJlCxDelete:
                List<JlCx> ids = new ArrayList<>();
                String idsStr = "";
                if (mAdapter.flage) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).isCheck) {
                            ids.add(mDatas.get(i));
                            idsStr+=mDatas.get(i).getShijian()+",";
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
                    if(mDatas==null||mDatas.size()==0){
                        Log.e("列表删除后==归零", mDatas.toString());
                        //Config.getSjglListType = 0;
                        SjglListType = 0;
                        //保存删除后的最新列表数据
                        //Gson gson = new Gson();
                        //String jsonStr=gson.toJson(mDatas); //将List转换成Json
                        SharedPreferences.Editor editor = sp.edit() ;
                        editor.putString("jiluchaxunList", "") ; //存入json串
                        editor.commit() ;  //提交
                        SPUtils.put(this,"abc","");
                    }else{
                        //保存删除后的最新列表数据
                        Gson gson = new Gson();
                        String jsonStr=gson.toJson(mDatas); //将List转换成Json
                        SharedPreferences.Editor editor = sp.edit() ;
                        editor.putString("jiluchaxunList", jsonStr) ; //存入json串
                        editor.commit() ;  //提交
                        SPUtils.put(this,"abc",jsonStr);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}