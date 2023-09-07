package com.yc.allbluetooth.dlzk.activity;


import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.dlzk.adapter.ShujuchuliAdapter;
import com.yc.allbluetooth.dlzk.entity.Shujuchuli;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.DateUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SPUtils;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DlzkShujuActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    private CheckBox ckbQx;
    private TextView tvFanhui;
    private TextView tvShanchu;
    SharedPreferences sp;
    List<Shujuchuli> mDatas = new ArrayList<>();
    List<Shujuchuli> mDatas2 = new ArrayList<>();
    ShujuchuliAdapter shujuchuliAdapter;

    int SjglListType = 0;
    int jinruPeizhi = 0;

    private String TAG = "DlzkShujuActivity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";
    //String newMsgStr2 = "";
    //配置文件
    String pzSjcd;//数据长度
    String pzSjxz;//数据性质
    String pzSjxh;//数据序号
    String pzEdrl;//额定容量
    String pzFjdy;//分接电压
    String pzMpzk;//铭牌阻抗
    String pzClwd;//测量温度
    String pzJzwd;//校正温度
    String pzSpbh;//试品编号
    String pzCsry;//测试人员
    String pzNian;//年
    String pzYue;//月
    String pzRi;//日
    String pzShi;//时
    String pzFen;//分
    String pzMiao;//秒
    String pzFjwz;//分接位置
    String pzClwz;//测量位置
    String pzCljx;//测量接线
    String pzZkdyPjz;//阻抗电压平均值
    String pzZkwc;//阻抗误差
    String pzCrcJy;//Crc校验
    //相数据
    String xiangSjcd;//数据长度
    String xiangSjxz;//数据性质
    String xiangSjxh;//数据序号
    String xiangDy;//电压
    String xiangDl;//电流
    String xiangJd;//角度
    String xiangGl;//功率
    String xiangPl;//频率
    String xiangGlys;//功率因数
    String xiangZk;//相阻抗
    String xiangGk;//相感抗
    String xiangDg;//相电感
    String xiangZkdy;//相阻抗电压
    String xiangCrcJy;//Crc校验
    //突发信息
    String tfSjxz;//数据性质
    String tfxx;//突发信息
    String tfCrcJy;//CRC校验

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
                    if(StringUtils.isEquals(Config.ymType,"dlzkSj")){
                        String msgStr = msg.obj.toString();
                        //Log.e(TAG,msgStr);
                        //jinruI=jinruI+1;
                        //if(jinruI%2==0) {
                        if (IndexOfAndSubStr.isIndexOf(msgStr, "6677")) {
                            //newMsgStr2 = "";
                            newMsgStr = msgStr;
                            Log.e(TAG, newMsgStr);
                        } else {
                            if(IndexOfAndSubStr.isIndexOf(newMsgStr, msgStr)==false){
                                newMsgStr = newMsgStr + msgStr;
                            }
                            //可以
                            Log.e(TAG , newMsgStr);
                        }
                        //}
                        if (newMsgStr.length() == 124) {//优先配置文件
                            //可以
                            Log.e("newMsgStr=124", "配置1:" + newMsgStr);
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,120);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            pzCrcJy = StringUtils.subStrStartToEnd(newMsgStr,120,124);
                            if(CrcUtil.CrcIsOk(bytesSx,pzCrcJy)){
                                pzSjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);//0:AB相实时数据，1:BC相实时数据，2:CA相实时数据，3:单相实时数据，4:零序实时数据，5:配置文件
                                pzSjxh = StringUtils.subStrStartToEnd(newMsgStr,8,10);
                                pzEdrl = StringUtils.subStrStartToEnd(newMsgStr,18,26);
                                pzFjdy = StringUtils.subStrStartToEnd(newMsgStr,26,34);
                                pzMpzk = StringUtils.subStrStartToEnd(newMsgStr,34,42);
                                pzClwd = StringUtils.subStrStartToEnd(newMsgStr,42,50);
                                pzJzwd = StringUtils.subStrStartToEnd(newMsgStr,50,58);
                                pzSpbh = StringUtils.subStrStartToEnd(newMsgStr,58,74);
                                pzCsry = StringUtils.subStrStartToEnd(newMsgStr,74,86);
                                pzNian = StringUtils.subStrStartToEnd(newMsgStr,86,88);
                                pzYue = StringUtils.subStrStartToEnd(newMsgStr,88,90);
                                pzRi = StringUtils.subStrStartToEnd(newMsgStr,90,92);
                                pzShi = StringUtils.subStrStartToEnd(newMsgStr,92,94);
                                pzFen = StringUtils.subStrStartToEnd(newMsgStr,94,96);
                                pzMiao = StringUtils.subStrStartToEnd(newMsgStr,96,98);
                                pzFjwz = StringUtils.subStrStartToEnd(newMsgStr,98,100);
                                pzClwz = StringUtils.subStrStartToEnd(newMsgStr,100,102);
                                pzCljx = StringUtils.subStrStartToEnd(newMsgStr,102,104);
                                pzZkdyPjz = StringUtils.subStrStartToEnd(newMsgStr,104,112);
                                pzZkwc = StringUtils.subStrStartToEnd(newMsgStr,112,120);
                                jinruPeizhi = jinruPeizhi+1;

                                shujuchuli.setEdrl(pzEdrl);
                                shujuchuli.setFjdy(pzFjdy);
                                shujuchuli.setMpzk(pzMpzk);
                                shujuchuli.setFjwz(pzFjwz);
                                shujuchuli.setClwz(pzClwz);
                                shujuchuli.setClwd(pzClwd);
                                shujuchuli.setJzwd(pzJzwd);
                                shujuchuli.setCsry(pzCsry);
                                shujuchuli.setCljx(pzCljx);

                                shujuchuli.setCssj("20" + StringUtils.buling(ShiOrShiliu.parseInt(pzNian)) + "-" + StringUtils.buling(ShiOrShiliu.parseInt(pzYue)) +
                                        "-" + StringUtils.buling(ShiOrShiliu.parseInt(pzRi)) + " " + StringUtils.buling(ShiOrShiliu.parseInt(pzShi)) + ":" +
                                        StringUtils.buling(ShiOrShiliu.parseInt(pzFen)) + ":" + StringUtils.buling(ShiOrShiliu.parseInt(pzMiao)));
                                /***************************************试品编号、测试编号*****************************************************/
                                shujuchuli.setSpbh(pzSpbh);
                                shujuchuli.setCsbh(pzSpbh);
                                //页面缺少阻抗电压平均值
                                //shujuchuli.setzkdy

                                /*********************************百分数*************************************/
                                shujuchuli.setZkdyZkBfh(pzZkdyPjz);
                                shujuchuli.setZkwcDZkBfh(pzZkwc);
                            }
                        }
                        if (newMsgStr.length() == 94) {//相
                            //可以
                            Log.e("newMsgStr=94", "相1:" + newMsgStr);
                            String crcAll = StringUtils.subStrStartToEnd(newMsgStr,0,90);
                            byte[] bytesSx = new BigInteger(crcAll, 16).toByteArray();
                            xiangCrcJy = StringUtils.subStrStartToEnd(newMsgStr,90,94);
                            Log.e("====",CrcUtil.getTableCRC(bytesSx)+","+xiangCrcJy);
                            //xiangCrcJy.equals(CrcUtil.getTableCRC(bytesSx));
                            if(CrcUtil.CrcIsOk(bytesSx,xiangCrcJy)){
                                xiangSjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);//0:AB相实时数据，1:BC相实时数据，2:CA相实时数据，3:单相实时数据，4:零序实时数据，5:配置文件
                                xiangSjxh = StringUtils.subStrStartToEnd(newMsgStr,8,10);
                                xiangDy = StringUtils.subStrStartToEnd(newMsgStr,10,18);
                                xiangDl = StringUtils.subStrStartToEnd(newMsgStr,18,26);
                                xiangJd = StringUtils.subStrStartToEnd(newMsgStr,26,34);
                                xiangGl = StringUtils.subStrStartToEnd(newMsgStr,34,42);
                                xiangPl = StringUtils.subStrStartToEnd(newMsgStr,42,50);
                                xiangGlys = StringUtils.subStrStartToEnd(newMsgStr,50,58);
                                xiangZk = StringUtils.subStrStartToEnd(newMsgStr,58,66);
                                xiangGk = StringUtils.subStrStartToEnd(newMsgStr,66,74);
                                xiangDg = StringUtils.subStrStartToEnd(newMsgStr,74,82);
                                xiangZkdy = StringUtils.subStrStartToEnd(newMsgStr,82,90);
                                if(StringUtils.isEquals("00",xiangSjxz)){//Ab(完整)
                                    shujuchuli.setSjdyAb(xiangDy);
                                    shujuchuli.setSjdlAb(xiangDl);
                                    shujuchuli.setClxjAb(xiangJd);
                                    shujuchuli.setYgglAb(xiangGl);
                                    shujuchuli.setDlzkAb(xiangZk);
                                    shujuchuli.setDlgkAb(xiangGk);
                                    shujuchuli.setRzdgAb(xiangDg);
                                    shujuchuli.setZkdyAb(xiangZkdy);
                                }else if(StringUtils.isEquals("01",xiangSjxz)){//Bc
                                    shujuchuli.setSjdyBc(xiangDy);
                                    shujuchuli.setSjdlBc(xiangDl);
                                    shujuchuli.setClxjBc(xiangJd);
                                    shujuchuli.setYgglBc(xiangGl);
                                    shujuchuli.setDlzkBc(xiangZk);
                                    shujuchuli.setDlgkBc(xiangGk);
                                    shujuchuli.setRzdgBc(xiangDg);
                                    shujuchuli.setZkdyBc(xiangZkdy);
                                }else if(StringUtils.isEquals("02",xiangSjxz)){//Ca:三相结尾
                                    shujuchuli.setSjdyCa(xiangDy);
                                    shujuchuli.setSjdlCa(xiangDl);
                                    shujuchuli.setClxjCa(xiangJd);
                                    shujuchuli.setYgglCa(xiangGl);
                                    shujuchuli.setDlzkCa(xiangZk);
                                    shujuchuli.setDlgkCa(xiangGk);
                                    shujuchuli.setRzdgCa(xiangDg);
                                    shujuchuli.setZkdyCa(xiangZkdy);
                                    shujuchuli.setViewType(0);//三相
                                    shujuchuli.setId(100+SjglListType);
                                    mDatas2.add(shujuchuli);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCssj() + ";" + o.getCssj()))), ArrayList::new));
                                    Log.e("=================2", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<Shujuchuli>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(Shujuchuli lhs, Shujuchuli rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getCssj());
                                            Date date2 = DateUtil.stringToDate(rhs.getCssj());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                                  //更新UI数据列表
                                    shujuchuliAdapter = new ShujuchuliAdapter(DlzkShujuActivity.this, mDatas, ckbQx);
                                    lv.setAdapter(shujuchuliAdapter);
                                    SjglListType += 1;
                                    newMsgStr="";
                                    shujuchuli = new Shujuchuli();
                                    sendDataByBle(SendUtil.dlzkShujuSend("89", SjglListType), "");

                                    Gson gson = new Gson();
                                    String jsonStr = gson.toJson(mDatas); //将List转换成Json
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("dlzkShujuList", jsonStr); //存入json串
                                    editor.commit();  //提交
                                }else if(StringUtils.isEquals("03",xiangSjxz)){//单相结尾
                                    shujuchuli.setSjdy(xiangDy);
                                    shujuchuli.setSjdl(xiangDl);
                                    shujuchuli.setClpl(xiangPl);
                                    shujuchuli.setClxj(xiangJd);
                                    shujuchuli.setYggl(xiangGl);
                                    shujuchuli.setDlzk(xiangZk);
                                    shujuchuli.setDlgk(xiangGk);
                                    shujuchuli.setDldz(xiangZkdy);//短路电阻(阻抗电压)
                                    shujuchuli.setRzdg(xiangDg);
                                    shujuchuli.setGlys(xiangGlys);
                                    shujuchuli.setViewType(1);//单相
                                    shujuchuli.setId(100+SjglListType);
                                    //if(StringUtils.noEmpty(pzNian)){
                                    mDatas2.add(shujuchuli);
                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCssj() + ";" + o.getCssj()))), ArrayList::new));
                                    Log.e("=================2", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<Shujuchuli>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(Shujuchuli lhs, Shujuchuli rhs) {
                                            //                                        Log.e("时间对比1",lhs.getCssj()+"");
                                            //                                        Log.e("时间对比2",rhs.getCssj()+"");
                                            //                                        Log.e("时间对比11",DateUtil.stringToDate(lhs.getCssj()+"")+"");
                                            //                                        Log.e("时间对比22",DateUtil.stringToDate(rhs.getCssj()+"")+"");
                                            Date date1 = DateUtil.stringToDate(lhs.getCssj());
                                            Date date2 = DateUtil.stringToDate(rhs.getCssj());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

                                    //                            //更新UI数据列表
                                    shujuchuliAdapter = new ShujuchuliAdapter(DlzkShujuActivity.this, mDatas, ckbQx);
                                    lv.setAdapter(shujuchuliAdapter);
                                    SjglListType += 1;
                                    newMsgStr="";
                                    shujuchuli = new Shujuchuli();
                                    Gson gson = new Gson();
                                    String jsonStr = gson.toJson(mDatas); //将List转换成Json
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("dlzkShujuList", jsonStr); //存入json串
                                    editor.commit();  //提交
                                    //if(SjglListType<mDatas.size()){
                                    sendDataByBle(SendUtil.dlzkShujuSend("89", SjglListType), "");
                                    //sendDataByBle(SendUtil.dlzkShujuSend("89", 1), "");
                                    //}
                                    //}

                                }else if(StringUtils.isEquals("04",xiangSjxz)){//零序结尾
                                    Log.e("lingxu=",xiangDy);
                                    shujuchuli.setSjdy(xiangDy);
                                    shujuchuli.setSjdl(xiangDl);
                                    shujuchuli.setClpl(xiangPl);
                                    shujuchuli.setClxj(xiangJd);
                                    shujuchuli.setYggl(xiangGl);
                                    shujuchuli.setLxzk(xiangZk);
                                    shujuchuli.setLxgk(xiangGk);
                                    shujuchuli.setLxdz(xiangZkdy);//零序电阻(阻抗电压)
                                    shujuchuli.setLxdg(xiangDg);
                                    shujuchuli.setGlys(xiangGlys);
                                    shujuchuli.setViewType(2);//零序
                                    shujuchuli.setId(100+SjglListType);
                                    mDatas2.add(shujuchuli);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCssj() + ";" + o.getCssj()))), ArrayList::new));
                                    Log.e("=================2", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<Shujuchuli>() {
                                        /**
                                         *时间排序
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(Shujuchuli lhs, Shujuchuli rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getCssj());
                                            Date date2 = DateUtil.stringToDate(rhs.getCssj());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                                  //更新UI数据列表
                                    shujuchuliAdapter = new ShujuchuliAdapter(DlzkShujuActivity.this, mDatas, ckbQx);
                                    lv.setAdapter(shujuchuliAdapter);
                                    SjglListType += 1;
                                    newMsgStr="";
                                    shujuchuli = new Shujuchuli();

                                    Gson gson = new Gson();
                                    String jsonStr = gson.toJson(mDatas); //将List转换成Json
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("dlzkShujuList", jsonStr); //存入json串
                                    editor.commit();  //提交

                                    sendDataByBle(SendUtil.dlzkShujuSend("89", SjglListType), "");
                                }else if(StringUtils.isEquals("05",xiangSjxz)){//A0(完整)
                                    shujuchuli.setSjdyA0(xiangDy);
                                    shujuchuli.setSjdlA0(xiangDl);
                                    shujuchuli.setClxjA0(xiangJd);
                                    shujuchuli.setYgglA0(xiangGl);
                                    shujuchuli.setDlzkA0(xiangZk);
                                    shujuchuli.setDlgkA0(xiangGk);
                                    shujuchuli.setRzdgA0(xiangDg);
                                    shujuchuli.setZkdyA0(xiangZkdy);
                                }else if(StringUtils.isEquals("06",xiangSjxz)){//B0
                                    shujuchuli.setSjdyB0(xiangDy);
                                    shujuchuli.setSjdlB0(xiangDl);
                                    shujuchuli.setClxjB0(xiangJd);
                                    shujuchuli.setYgglB0(xiangGl);
                                    shujuchuli.setDlzkB0(xiangZk);
                                    shujuchuli.setDlgkB0(xiangGk);
                                    shujuchuli.setRzdgB0(xiangDg);
                                    shujuchuli.setZkdyB0(xiangZkdy);
                                }else if(StringUtils.isEquals("07",xiangSjxz)) {//C0:三相结尾
                                    shujuchuli.setSjdyC0(xiangDy);
                                    shujuchuli.setSjdlC0(xiangDl);
                                    shujuchuli.setClxjC0(xiangJd);
                                    shujuchuli.setYgglC0(xiangGl);
                                    shujuchuli.setDlzkC0(xiangZk);
                                    shujuchuli.setDlgkC0(xiangGk);
                                    shujuchuli.setRzdgC0(xiangDg);
                                    shujuchuli.setZkdyC0(xiangZkdy);
                                    shujuchuli.setViewType(3);//A0三相
                                    shujuchuli.setId(100 + SjglListType);
                                    mDatas2.add(shujuchuli);

                                    //根据时间属性去重
                                    mDatas = mDatas2.stream()
                                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCssj() + ";" + o.getCssj()))), ArrayList::new));
                                    Log.e("=================2", mDatas.toString());
                                    //对mList数组的数据按data字段升序
                                    Collections.sort(mDatas, new Comparator<Shujuchuli>() {
                                        /**
                                         * 时间排序
                                         *
                                         * @param lhs
                                         * @param rhs
                                         * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                         * equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                         */
                                        @Override
                                        public int compare(Shujuchuli lhs, Shujuchuli rhs) {
                                            Date date1 = DateUtil.stringToDate(lhs.getCssj());
                                            Date date2 = DateUtil.stringToDate(rhs.getCssj());
                                            // 对日期字段进行升序，如果欲降序可采用after方法
                                            if (date1.before(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }
                                    });
                                    // }

//                                  //更新UI数据列表
                                    shujuchuliAdapter = new ShujuchuliAdapter(DlzkShujuActivity.this, mDatas, ckbQx);
                                    lv.setAdapter(shujuchuliAdapter);
                                    SjglListType += 1;
                                    newMsgStr = "";
                                    shujuchuli = new Shujuchuli();
                                    sendDataByBle(SendUtil.dlzkShujuSend("89", SjglListType), "");

                                    Gson gson = new Gson();
                                    String jsonStr = gson.toJson(mDatas); //将List转换成Json
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("dlzkShujuList", jsonStr); //存入json串
                                    editor.commit();  //提交
                                }
                            }
                        }
                        if (newMsgStr.length() == 16&&StringUtils.isEquals(StringUtils.subStrStartToEnd(newMsgStr,0,4),"6677")) {//突发信息
                            //可以
                            Log.e("newMsgStr=16", "突发信息1:" + newMsgStr);
                            String crcTfAll = StringUtils.subStrStartToEnd(newMsgStr,0,12);
                            byte[] bytesTf = new BigInteger(crcTfAll, 16).toByteArray();
                            tfCrcJy = StringUtils.subStrStartToEnd(newMsgStr,12,16);
                            if(CrcUtil.CrcIsOk(bytesTf,tfCrcJy)){
                                tfSjxz = StringUtils.subStrStartToEnd(newMsgStr,6,8);
                                tfxx = StringUtils.subStrStartToEnd(newMsgStr,10,12);
                                if(StringUtils.isEquals("AB",tfxx)){
                                    Log.e("newMsgStr=16", "突发信息2:" + SjglListType);
                                    SjglListType = 0;
                                }
                            }
                        }
                    }
                    break;
                case 1000:
                    regainBleDataCount = 0;
                    bleFlag = false;
                    mHandler.removeCallbacks(checkConnetRunnable);

                    Toast.makeText(DlzkShujuActivity.this, "超时请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case 1111:
                    bleConnectUtil.disConnect();
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

        Resources resources = this.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if("zh".equals(Config.zyType)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);

        setContentView(R.layout.activity_dlzk_shuju);
        ActivityCollector.addActivity(this);
        initModel();
        initView();
    }
    public void initModel(){

        bleConnectUtil = new BleConnectUtil(DlzkShujuActivity.this);
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
                    sendDataByBle(SendUtil.dlzkShujuSend("89", 0), "");
                }
            }, 1000);//1秒后执行Runnable中的run方法
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initView(){
        lv = findViewById(R.id.lvDlzkShuju);
        ckbQx = findViewById(R.id.ckbDlzkShujuQuanxuan);
        tvFanhui = findViewById(R.id.tvDlzkShujuFanhui);
        tvShanchu = findViewById(R.id.tvDlzkShujuShanchu);
        tvFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sp = this.getSharedPreferences("dlzkShujuchuli", Activity.MODE_PRIVATE);//创建sp对象

        //读取数据
        //SharedPreferences sp = this.getSharedPreferences("SP_NewUserModel_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String listJson = sp.getString("dlzkShujuList", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        Log.e("spStr===", listJson);
        //Log.e("spAbcStr===", SPUtils.get(this, "abc", ""));
        if (StringUtils.noEmpty(listJson) && !"[]".equals(listJson))  //防空判断
        {
            Gson gson = new Gson();
            mDatas2 = gson.fromJson(listJson, new TypeToken<List<Shujuchuli>>() {
            }.getType()); //将json字符串转换成List集合
        }

        //根据时间属性去重
        mDatas = mDatas2.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getCssj() + ";" + o.getCssj()))), ArrayList::new));
        Log.e("=================1", mDatas.toString());
        //对mList数组的数据按data字段升序
        Collections.sort(mDatas, new Comparator<Shujuchuli>() {
            /**
             *时间排序
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(Shujuchuli lhs, Shujuchuli rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getCssj());
                Date date2 = DateUtil.stringToDate(rhs.getCssj());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });



        shujuchuliAdapter = new ShujuchuliAdapter(DlzkShujuActivity.this, mDatas, ckbQx);
        lv.setAdapter(shujuchuliAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shujuchuli shujuchuli = mDatas.get(position);
                Log.e("lv==Id", shujuchuli.getId() + "");
                Log.e("lv==type", shujuchuli.getViewType() + "");
            }
        });

        Gson gson = new Gson();
        String jsonStr = gson.toJson(mDatas); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("dlzkShujuList", jsonStr); //存入json串
        editor.commit();  //提交

        tvFanhui.setOnClickListener(this);
        ckbQx.setOnClickListener(this);
        tvShanchu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDlzkShujuFanhui:
                sendDataByBle(SendUtil.initSend("6e"),"");
                finish();
                break;
            case R.id.ckbDlzkShujuQuanxuan:
                if(((CheckBox)v).isChecked()){//全选中
                    if (shujuchuliAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            Shujuchuli shujuchuli =mDatas.get(i);
                            shujuchuli.isCheck = true;
                            if (shujuchuli.isCheck) {

                            }
                        }
                        shujuchuliAdapter.notifyDataSetChanged();
                    }
                }else{//全部未选中
                    if (shujuchuliAdapter.flage) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            mDatas.get(i).isCheck = false;
                        }
                        shujuchuliAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.tvDlzkShujuShanchu:
                List<Shujuchuli> ids = new ArrayList<>();
                String idsStr = "";
                if (shujuchuliAdapter.flage) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).isCheck) {
                            ids.add(mDatas.get(i));
                            //idsStr+=mDatas.get(i).getShijian()+",";
                        }
                    }

                    // 根据选中的条目进行全部移除
                    Log.e("dlzk==delete", ids.toString());
                    Log.e("dlzk==delete2", idsStr);
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
                        editor.putString("dlzkShujuList", "") ; //存入json串
                        editor.commit() ;  //提交
                        SPUtils.put(this,"dlzk","");
                    }else{
                        //保存删除后的最新列表数据
                        Gson gson = new Gson();
                        String jsonStr=gson.toJson(mDatas); //将List转换成Json
                        SharedPreferences.Editor editor = sp.edit() ;
                        editor.putString("dlzkShujuList", jsonStr) ; //存入json串
                        editor.commit() ;  //提交
                        SPUtils.put(this,"dlzk",jsonStr);
                    }
                    shujuchuliAdapter.notifyDataSetChanged();
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
    protected void onResume() {
        super.onResume();
        Config.ymType = "dlzkSj";
//        bleConnectUtil = new BleConnectUtil(DlzkShujuActivity.this);
//        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
//            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
//            bleConnectUtil.setCallback(blecallback);
//        }
//        bleConnectUtil.setCallback(blecallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.setCallback(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onStop()");
//        bleConnectUtil.disConnect();
//        mHandler.removeCallbacksAndMessages(null);
//        bleConnectUtil.setCallback(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
//        bleConnectUtil.disConnect();
 //       mHandler.removeCallbacksAndMessages(null);
        ActivityCollector.removeActivity(this);
    }
}