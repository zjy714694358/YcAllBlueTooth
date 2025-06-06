package com.yc.allbluetooth.std.fragment;


import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.adapter.SjglNewAdapter;
import com.yc.allbluetooth.std.entity.SjglNew;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.DateUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdDyJlNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdDyJlNewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView lvSjglList;
    private List<SjglNew> mDatas2 = new ArrayList<>();
    private SjglNewAdapter mAdapter;

    List<SjglNew> mDatas;
    Context context;

    public CheckBox ckbAll;
    private TextView tvDelete;

    String TAG = "diaoyueNew";

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
    String crcJy;//crc校验

    String tfxxType;//突发信息状态码
    int a0orabType = 0;//0代表A0，B0，C0；1代表ab,bc,ca

    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;
    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.BLUETOOTH_GETDATA:
                    if(StringUtils.isEquals("stdDyjl",Config.ymType)){
                        String msgStr = msg.obj.toString();
                        Log.i("diaoyueNewStart", msgStr);
                        if(msgStr.length()==22||msgStr.length()>28||msgStr.length()==6){
                            if(IndexOfAndSubStr.isIndexOf(msgStr,"6677")){
                                newMsgStr = msgStr;
                                Log.e("diaoyueNew1=:",newMsgStr);
                            }else{
                                newMsgStr = newMsgStr+msgStr;
                                //可以
                                Log.e("diaoyueNew2=:",newMsgStr);
                            }
                            if(newMsgStr.length()==62){//>40
                                Log.e("diaoyueNew2=60:",newMsgStr);
                                crcJy = StringUtils.subStrStartToEnd(newMsgStr,54,58);
                                Log.e("crcJy1", crcJy);
                                byte[] bytesJieshou = new BigInteger(StringUtils.subStrStartToEnd(newMsgStr,0,54), 16).toByteArray();
                                Log.e("crcUtil1", CrcUtil.getTableCRC(bytesJieshou));
                                if(StringUtils.isEquals(CrcUtil.getTableCRC(bytesJieshou),crcJy)){
                                    Log.e("crcUtiltrue1", "true");
                                }else{
                                    Log.e("crcUtilfalse1", "false");
                                }
                                //可以
                                Log.i("diaoyueNew2=60", "new:"+newMsgStr);
                                //数据性质：=0测试电流，单位（A），=1 测试电阻值，单位（mΩ），=2放电电流，单位（A）,=3下位机突发信息
                                sjxz = StringUtils.subStrStartToEnd(newMsgStr,4,6);
                                //数据序号，=0xaa 当前数据，=0xab 无此序号数据，
                                sjxh = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                //测试相位：=0  AO，=1 BO，=2 CO，=3  ab，=4  bc ， =5 ca，=6三相(FF:无此数据)
                                //0B00010101==>A0,B0,C0
                                //0B00101010==>ab,bc,ca
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
                                Log.e(TAG,"测试相位："+csxw);

                                //mHandler.sendEmptyMessage(3);
                                //XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                //BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                //       xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
                                //========================================二进制====================================
                                //========================================二进制====================================
                                //========================================二进制====================================
/*                            String csxwBin = ShiOrShiliu.hexString2binaryString(csxw,1);
                            Log.e("测试相位1",csxw+",二进制："+csxwBin);
                            String a0OrAbJq = StringUtils.subStrStartToEnd(csxwBin,2,4);
                            String b0OrBcJq = StringUtils.subStrStartToEnd(csxwBin,4,6);
                            String c0OrCaJq = StringUtils.subStrStartToEnd(csxwBin,6,8);
                            Log.e("测试相位2",a0OrAbJq+","+b0OrBcJq+","+c0OrCaJq);
                            if(StringUtils.isEquals("01",a0OrAbJq)||StringUtils.isEquals("01",b0OrBcJq)||StringUtils.isEquals("01",c0OrCaJq)){
                                a0orabType = 0;
                            }else if(StringUtils.isEquals("10",a0OrAbJq)||StringUtils.isEquals("10",b0OrBcJq)||StringUtils.isEquals("10",c0OrCaJq)){
                                a0orabType = 1;
                            }*/
                                //========================================二进制====================================
                                //========================================二进制====================================
                                //========================================二进制====================================
                                if(!StringUtils.isEquals(csxw,"FF")){
                                    SjglNew sjglNew = new SjglNew();
                                    Config.getSjglListId+=1;
                                    sjglNew.setId(Config.getSjglListId);
                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    sjglNew.setShijian("20"+StringUtils.buling(ShiOrShiliu.parseInt(nian))+"-"+StringUtils.buling(ShiOrShiliu.parseInt(yue))+"-" +
                                            StringUtils.buling(ShiOrShiliu.parseInt(ri))+" "+StringUtils.buling(ShiOrShiliu.parseInt(shi))+":"+
                                            StringUtils.buling(ShiOrShiliu.parseInt(fen))+":"+StringUtils.buling(ShiOrShiliu.parseInt(miao)));
                                    sjglNew.setFenjie(fjwz);
                                    sjglNew.setCeliangwendu(cswd);
//                                if(StringUtils.isEquals(csxw,"00")){
//                                    sjglNew.setA0orab(a0orab);
//                                }else if(StringUtils.isEquals(csxw,"01")){
//                                    sjglNew.setB0orbc(b0orbc);
//                                }else if(StringUtils.isEquals(csxw,"02")){
//                                    sjglNew.setC0orca(c0orca);
//                                }else if(StringUtils.isEquals(csxw,"03")){
//                                    sjglNew.setA0orab(a0orab);
//                                }else if(StringUtils.isEquals(csxw,"04")){
//                                    sjglNew.setB0orbc(b0orbc);
//                                }else if(StringUtils.isEquals(csxw,"05")){
//                                    sjglNew.setC0orca(c0orca);
//                                }else if(StringUtils.isEquals(csxw,"06")){
                                    sjglNew.setA0orab(a0orab);
                                    sjglNew.setB0orbc(b0orbc);
                                    sjglNew.setC0orca(c0orca);
                                    // }
                                    sjglNew.setCsxw(csxw);
                                    sjglNew.setA0orabType(ShiOrShiliu.parseInt(csxw));
                                    mDatas2.add(sjglNew);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================",mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<SjglNew>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(SjglNew lhs, SjglNew rhs) {
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
                                    mAdapter = new SjglNewAdapter(getContext(),mDatas,ckbAll);
                                    lvSjglList.setAdapter(mAdapter);


                                    Config.getSjglListType+=1;
                                    String strStdCsAllStdSave = "";
                                    if(Config.getSjglListType<10){
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"0"+Config.getSjglListType+"0000";
                                    }else if(Config.getSjglListType>=10&&Config.getSjglListType<16){
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"0"+ShiOrShiliu.toHexString(Config.getSjglListType)+"0000";
                                    }else{
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+ShiOrShiliu.toHexString(Config.getSjglListType)+"0000";
                                    }
                                    byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave+"00", 16).toByteArray();//+00+
                                    String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
                                    String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
                                    sendDataByBle(sendAllYnSave,"");
                                }else{
                                    Config.getSjglListType-=1;
                                }

                                SharedPreferences sp = getContext().getSharedPreferences("shujuguanli", Activity.MODE_PRIVATE);//创建sp对象
                                Gson gson = new Gson();
                                String jsonStr=gson.toJson(mDatas); //将List转换成Json
                                SharedPreferences.Editor editor = sp.edit() ;
                                editor.putString("shujuguanliList", jsonStr) ; //存入json串
                                editor.commit() ;  //提交
                            }else if(newMsgStr.length()==88){//86(newMsgStr.length()>60&&newMsgStr.length()!=80)+00+
                                Log.e("diaoyueNew2=86:",newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr,26);

                                crcJy = StringUtils.subStrStartToEnd(newMsgStr,54,58);
                                Log.e("crcJy2", crcJy);
                                byte[] bytesJieshou = new BigInteger(StringUtils.subStrStartToEnd(newMsgStr,0,54), 16).toByteArray();
                                Log.e("crcUtil2", CrcUtil.getTableCRC(bytesJieshou));
                                if(StringUtils.isEquals(CrcUtil.getTableCRC(bytesJieshou),crcJy)){
                                    Log.e("crcUtiltrue2", "true");
                                }else{
                                    Log.e("crcUtilfalse2", "false");
                                }

                                //可以
                                Log.i("diaoyue=86", "new:"+newMsgStr);
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
                                Log.e(TAG,"测试相位2："+csxw);
                                //mHandler.sendEmptyMessage(3);
                                //XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                //BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                //       xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度

                            /*String csxwBin = ShiOrShiliu.hexString2binaryString(csxw,1);
                            Log.e("测试相位1",csxw+",二进制："+csxwBin);
                            String a0OrAbJq = StringUtils.subStrStartToEnd(csxwBin,2,4);
                            String b0OrBcJq = StringUtils.subStrStartToEnd(csxwBin,4,6);
                            String c0OrCaJq = StringUtils.subStrStartToEnd(csxwBin,6,8);
                            Log.e("测试相位2",a0OrAbJq+","+b0OrBcJq+","+c0OrCaJq);
                            if(StringUtils.isEquals("01",a0OrAbJq)||StringUtils.isEquals("01",b0OrBcJq)||StringUtils.isEquals("01",c0OrCaJq)){
                                a0orabType = 0;
                            }else if(StringUtils.isEquals("10",a0OrAbJq)||StringUtils.isEquals("10",b0OrBcJq)||StringUtils.isEquals("10",c0OrCaJq)){
                                a0orabType = 1;
                            }*/
                                if(!StringUtils.isEquals(csxw,"FF")){
                                    SjglNew sjglNew = new SjglNew();
                                    Config.getSjglListId+=1;
                                    sjglNew.setId(Config.getSjglListId);

                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    sjglNew.setShijian("20"+StringUtils.buling(ShiOrShiliu.parseInt(nian))+"-"+StringUtils.buling(ShiOrShiliu.parseInt(yue))+"-" +
                                            StringUtils.buling(ShiOrShiliu.parseInt(ri))+" "+StringUtils.buling(ShiOrShiliu.parseInt(shi))+":"+
                                            StringUtils.buling(ShiOrShiliu.parseInt(fen))+":"+StringUtils.buling(ShiOrShiliu.parseInt(miao)));
                                    sjglNew.setFenjie(fjwz);
                                    sjglNew.setCeliangwendu(cswd);
//                                if(StringUtils.isEquals(csxw,"00")){
//                                    sjglNew.setA0orab(a0orab);
//                                }else if(StringUtils.isEquals(csxw,"01")){
//                                    sjglNew.setB0orbc(b0orbc);
//                                }else if(StringUtils.isEquals(csxw,"02")){
//                                    sjglNew.setC0orca(c0orca);
//                                }else if(StringUtils.isEquals(csxw,"03")){
//                                    sjglNew.setA0orab(a0orab);
//                                }else if(StringUtils.isEquals(csxw,"04")){
//                                    sjglNew.setB0orbc(b0orbc);
//                                }else if(StringUtils.isEquals(csxw,"05")){
//                                    sjglNew.setC0orca(c0orca);
//                                }else if(StringUtils.isEquals(csxw,"06")){
                                    sjglNew.setA0orab(a0orab);
                                    sjglNew.setB0orbc(b0orbc);
                                    sjglNew.setC0orca(c0orca);
                                    //}
                                    sjglNew.setCsxw(csxw);
                                    sjglNew.setA0orabType(ShiOrShiliu.parseInt(csxw));
                                    mDatas2.add(sjglNew);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================",mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<SjglNew>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(SjglNew lhs, SjglNew rhs) {
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

//                                Log.e("=============2",getActivity().toString());
                                    //updateUi();
                                    //mAdapter.notifyDataSetChanged();//线程中无效
                                    //Log.e("this.mContext==线程",getContext()+"");
//                            //更新UI数据列表
                                    mAdapter = new SjglNewAdapter(getContext(),mDatas,ckbAll);
                                    lvSjglList.setAdapter(mAdapter);


                                    Config.getSjglListType+=1;
                                    String strStdCsAllStdSave = "";
                                    if(Config.getSjglListType<10){
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"0"+Config.getSjglListType+"0000";
                                    }else if(Config.getSjglListType>=10&&Config.getSjglListType<16){
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"0"+ShiOrShiliu.toHexString(Config.getSjglListType)+"0000";
                                    }else{
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+ShiOrShiliu.toHexString(Config.getSjglListType)+"0000";
                                    }
                                    byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
                                    String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
                                    String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
                                    sendDataByBle(sendAllYnSave,"");
                                }else{
                                    Config.getSjglListType-=1;
                                }

                                SharedPreferences sp = getContext().getSharedPreferences("shujuguanli", Activity.MODE_PRIVATE);//创建sp对象
                                Gson gson = new Gson();
                                String jsonStr=gson.toJson(mDatas); //将List转换成Json
                                SharedPreferences.Editor editor = sp.edit() ;
                                editor.putString("shujuguanliList", jsonStr) ; //存入json串
                                editor.commit() ;  //提交
                            }else if(newMsgStr.length()==82&&msgStr.length()==6){//+00+
                                newMsgStr = newMsgStr + msgStr;
                                Log.e(TAG,newMsgStr);
                                Log.e("diaoyueNew2=80&&6:",newMsgStr);
                                newMsgStr = StringUtils.subStrStart(newMsgStr,26);

                                crcJy = StringUtils.subStrStartToEnd(newMsgStr,54,58);
                                Log.e("crcJy2", crcJy);
                                byte[] bytesJieshou = new BigInteger(StringUtils.subStrStartToEnd(newMsgStr,0,54), 16).toByteArray();
                                Log.e("crcUtil2", CrcUtil.getTableCRC(bytesJieshou));
                                if(StringUtils.isEquals(CrcUtil.getTableCRC(bytesJieshou),crcJy)){
                                    Log.e("crcUtiltrue2", "true");
                                }else{
                                    Log.e("crcUtilfalse2", "false");
                                }

                                //可以
                                Log.i("diaoyue=86", "new:"+newMsgStr);
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
                                Log.e(TAG,"测试相位2："+csxw);
                                //mHandler.sendEmptyMessage(3);
                                //XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                //BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                                //       xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度

                            /*String csxwBin = ShiOrShiliu.hexString2binaryString(csxw,1);
                            Log.e("测试相位1",csxw+",二进制："+csxwBin);
                            String a0OrAbJq = StringUtils.subStrStartToEnd(csxwBin,2,4);
                            String b0OrBcJq = StringUtils.subStrStartToEnd(csxwBin,4,6);
                            String c0OrCaJq = StringUtils.subStrStartToEnd(csxwBin,6,8);
                            Log.e("测试相位2",a0OrAbJq+","+b0OrBcJq+","+c0OrCaJq);
                            if(StringUtils.isEquals("01",a0OrAbJq)||StringUtils.isEquals("01",b0OrBcJq)||StringUtils.isEquals("01",c0OrCaJq)){
                                a0orabType = 0;
                            }else if(StringUtils.isEquals("10",a0OrAbJq)||StringUtils.isEquals("10",b0OrBcJq)||StringUtils.isEquals("10",c0OrCaJq)){
                                a0orabType = 1;
                            }*/
                                if(!StringUtils.isEquals(csxw,"FF")){
                                    SjglNew sjglNew = new SjglNew();
                                    Config.getSjglListId+=1;
                                    sjglNew.setId(Config.getSjglListId);

                                    //if(!(ShiOrShiliu.parseInt(nian)<2021)){
                                    sjglNew.setShijian("20"+StringUtils.buling(ShiOrShiliu.parseInt(nian))+"-"+StringUtils.buling(ShiOrShiliu.parseInt(yue))+"-" +
                                            StringUtils.buling(ShiOrShiliu.parseInt(ri))+" "+StringUtils.buling(ShiOrShiliu.parseInt(shi))+":"+
                                            StringUtils.buling(ShiOrShiliu.parseInt(fen))+":"+StringUtils.buling(ShiOrShiliu.parseInt(miao)));
                                    sjglNew.setFenjie(fjwz);
                                    sjglNew.setCeliangwendu(cswd);
//                                if(StringUtils.isEquals(csxw,"00")){
//                                    sjglNew.setA0orab(a0orab);
//                                }else if(StringUtils.isEquals(csxw,"01")){
//                                    sjglNew.setB0orbc(b0orbc);
//                                }else if(StringUtils.isEquals(csxw,"02")){
//                                    sjglNew.setC0orca(c0orca);
//                                }else if(StringUtils.isEquals(csxw,"03")){
//                                    sjglNew.setA0orab(a0orab);
//                                }else if(StringUtils.isEquals(csxw,"04")){
//                                    sjglNew.setB0orbc(b0orbc);
//                                }else if(StringUtils.isEquals(csxw,"05")){
//                                    sjglNew.setC0orca(c0orca);
//                                }else if(StringUtils.isEquals(csxw,"06")){
                                    sjglNew.setA0orab(a0orab);
                                    sjglNew.setB0orbc(b0orbc);
                                    sjglNew.setC0orca(c0orca);
                                    //}
                                    sjglNew.setCsxw(csxw);
                                    sjglNew.setA0orabType(ShiOrShiliu.parseInt(csxw));
                                    mDatas2.add(sjglNew);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
                                    Log.e("=================",mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<SjglNew>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(SjglNew lhs, SjglNew rhs) {
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

//                                Log.e("=============2",getActivity().toString());
                                    //updateUi();
                                    //mAdapter.notifyDataSetChanged();//线程中无效
                                    //Log.e("this.mContext==线程",getContext()+"");
//                            //更新UI数据列表
                                    mAdapter = new SjglNewAdapter(getContext(),mDatas,ckbAll);
                                    lvSjglList.setAdapter(mAdapter);


                                    Config.getSjglListType+=1;
                                    String strStdCsAllStdSave = "";
                                    if(Config.getSjglListType<10){
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"0"+Config.getSjglListType+"0000";
                                    }else if(Config.getSjglListType>=10&&Config.getSjglListType<16){
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"0"+ShiOrShiliu.toHexString(Config.getSjglListType)+"0000";
                                    }else{
                                        strStdCsAllStdSave = "6886"+"6e00000000"+"00"+ShiOrShiliu.toHexString(Config.getSjglListType)+"0000";
                                    }
                                    byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
                                    String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
                                    String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
                                    sendDataByBle(sendAllYnSave,"");
                                }else{
                                    Config.getSjglListType-=1;
                                }

                                SharedPreferences sp = getContext().getSharedPreferences("shujuguanli", Activity.MODE_PRIVATE);//创建sp对象
                                Gson gson = new Gson();
                                String jsonStr=gson.toJson(mDatas); //将List转换成Json
                                SharedPreferences.Editor editor = sp.edit() ;
                                editor.putString("shujuguanliList", jsonStr) ; //存入json串
                                editor.commit() ;  //提交
                            }
                        }else{
                            Log.e(TAG,"这是返回的第一条验证指令DyJlNew："+msgStr);
                            //tfxxType = StringUtils.subStrStartToEnd(msgStr,4,6);
                        }
                    }

                    break;

                case Config.BLUETOOTH_LIANJIE_DUANKAI:
                    bleConnectUtil.disConnect();
                    break;
                case 3:
                    Log.e("case=3","进入...");

                    break;
                default:
                    break;
            }
        }
    };


    public StdDyJlNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DyJlNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdDyJlNewFragment newInstance(String param1, String param2) {
        StdDyJlNewFragment fragment = new StdDyJlNewFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dy_jl_new, container, false);
        View mainView = inflater.inflate(R.layout.fragment_std_dy_jl_new,null);
        initView(mainView);

        return mainView;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initView(View view){
        Config.ymType = "stdDyjl";
        lvSjglList = view.findViewById(R.id.lvSjgl);
        ckbAll = view.findViewById(R.id.ckbSjglAll);
        tvDelete = view.findViewById(R.id.tvSjglDelete);

        bleConnectUtil = new BleConnectUtil(getActivity());
        Log.e(TAG,"进入");
//        if(!bleConnectUtil.isConnected()&&StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            Log.e(TAG,"进入1");
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
        if(StringUtils.noEmpty(Config.lyAddress)){
            bleConnectUtil.connect(Config.lyAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }else if(StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress, 10, 10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */

                Config.getSjglListType=0;
                String strStdCsAllStdSave = "6886"+"6e00000000"+"00"+"00"+"0000"+"00";//+00+
                byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
                String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
                String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
                sendDataByBle(sendAllYnSave,"");
            }
        }, 1000);//3秒后执行Runnable中的run方法
        //}
        SharedPreferences sp = getContext().getSharedPreferences("shujuguanli", Activity.MODE_PRIVATE);//创建sp对象

        //读取数据
        //SharedPreferences sp = mBaseActivity.getSharedPreferences("SP_NewUserModel_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String listJson = sp.getString("shujuguanliList","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        Log.e("spStr===",listJson);
        if(StringUtils.noEmpty(listJson)&&!"[]".equals(listJson))  //防空判断
        {
            Gson gson = new Gson();
            mDatas2 = gson.fromJson(listJson, new TypeToken<List<SjglNew>>() {}.getType()); //将json字符串转换成List集合
        }
        //根据时间属性去重
        mDatas = mDatas2.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getShijian() + ";" + o.getShijian()))), ArrayList::new));
        Log.e("=================",mDatas.toString());
        //对mList数组的数据按data字段升序
        Collections.sort(mDatas, new Comparator<SjglNew>() {
            /**
             *时间排序
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(SjglNew lhs, SjglNew rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getShijian());
                Date date2 = DateUtil.stringToDate(rhs.getShijian());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });

        Log.e("=============1",getContext().toString());
        mAdapter = new SjglNewAdapter(getContext(),mDatas,ckbAll);
        lvSjglList.setAdapter(mAdapter);
        lvSjglList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SjglNew sjglNew = mDatas.get(position);
                Log.e("lv==Id",sjglNew.getId()+"");
            }
        });

        Gson gson = new Gson();
        String jsonStr=gson.toJson(mDatas); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putString("shujuguanliList", jsonStr) ; //存入json串
        editor.commit() ;  //提交

        ckbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        ckbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){//全选中
                    if (mAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            SjglNew sjglNew =mDatas.get(i);
                            sjglNew.isCheck = true;
                            if (sjglNew.isCheck) {

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
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SjglNew> ids = new ArrayList<>();
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
                        Config.getSjglListType = 0;
                    }

                    //保存删除后的最新列表数据
                    Gson gson = new Gson();
                    String jsonStr=gson.toJson(mDatas); //将List转换成Json
                    SharedPreferences.Editor editor = sp.edit() ;
                    editor.putString("shujuguanliList", jsonStr) ; //存入json串
                    editor.commit() ;  //提交


                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("dyjlNew===", "onStop()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacks(checkConnetRunnable);
//        mHandler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("dyjlNew=============","onHiddenChanged");
        String strStdCsAllStdSave = "6886"+"73"+"00000000"+"00"+"00"+"0000"+"00";//+00+
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        //sendDataByBle(sendAllYnSave,"");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacks(checkConnetRunnable);
//        mHandler.removeCallbacksAndMessages(null);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
    }
    public void updateUi(){
        //更新UI数据列表
        Log.e("=============2",getContext().toString());
        mAdapter = new SjglNewAdapter(getContext(),mDatas,ckbAll);
        lvSjglList.setAdapter(mAdapter);
        SharedPreferences sp = getContext().getSharedPreferences("shujuguanli", Activity.MODE_PRIVATE);//创建sp对象
        Gson gson = new Gson();
        String jsonStr=gson.toJson(mDatas); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putString("shujuguanliList", jsonStr) ; //存入json串
        editor.commit() ;  //提交
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
            Log.e("diaoyuNew", "onSuccessSend: ");

        }

        @Override
        public void onDisconnect() {
            //设备断开连接
            Log.e("diaoyuNew", "onDisconnect: ");
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
        Log.e(TAG,"dyjl---onResume()");
        Config.ymType = "stdDyjl";
    }
}