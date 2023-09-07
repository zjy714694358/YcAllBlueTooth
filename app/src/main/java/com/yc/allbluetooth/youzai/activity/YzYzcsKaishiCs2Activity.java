package com.yc.allbluetooth.youzai.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.BitmapUtils;
import com.yc.allbluetooth.utils.BytesToHexString;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;
import com.yc.allbluetooth.youzai.util.CrcAll;

import org.apache.poi.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class YzYzcsKaishiCs2Activity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private TextView tvRa1;
    private TextView tvRa2;
    private TextView tvRb1;
    private TextView tvRb2;
    private TextView tvRc1;
    private TextView tvRc2;
    private TextView tvGdsj;
    private TextView tvBtqsj;
    private TextView tvMsDiv;
    private TextView tvZuoxia1;
    private TextView tvZuoxia2;
    private TextView tvGbqh;
    private ImageView ivZuo;
    private ImageView ivYou;
    private TextView tvTime;
    private TextView tvCs;
    private TextView tvBxsf;
    private TextView tvDy;
    private TextView tvBc;
    private TextView tvFh;
    private LineChartView lineChart;
    private LineChartView lineChart2;
    private LineChartView lineChart22;
    private TextView tvColor;
    private LinearLayout llYzCs2;//要截屏的部分
    int type = 0;
    int sfType = 0;
    int gb = 0;//光标

//    String[] date = {"0","0.5","1","1.5","2","2.5","3","3.5","4","4.5","5",
//            "5.5","6","6.5","7","7.5","8","8.5","9","9.5","10","10.5","11",
//            "11.5","12","12.5","13","13.5"};//X轴的标注
    String[] date = {};//X轴的标注
    //public static long[] score={50,42,0,33,10,74,22,18,79,20,30,50}; //{50,42,0,33,10,74,22,18,79,20,30,50};//图表的数据点
    //private int[] score= {0,5,5,5,5,5,20,20,20,20,20,20,20,20,10,10,10,10,10,20,20,20,20,20,28,20,20,20};//图表的数据
    private int[] score= {};//图表的数据
    //private int[] score= {84,22,18,79,20,74,20,74,42,90,74,42,90,50,42,90,33,10,74,22,18,79,20,74,22,18,79,30};//图表的数据
    List<String>dateList1 = new ArrayList<>();
    List<String>scoreList1 = new ArrayList<>();
    List<String>scoreList2 = new ArrayList<>();
    List<String>scoreList3 = new ArrayList<>();
    int typeX = 0;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<PointValue> mPointValues2 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues3 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues5 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues6 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();


    LineChartData data = new LineChartData();
    LineChartData data2 = new LineChartData();
    LineChartData data3 = new LineChartData();

    private String TAG = "YzYzcsKsCs2Activity";

    BleConnectUtil bleConnectUtil;
    String newMsgStr = "";

    int regainBleDataCount = 0;
    String currentRevice, currentSendOrder;
    byte[] sData = null;
    /**
     * 跟ble通信的标志位,检测数据是否在指定时间内返回
     */
    private boolean bleFlag = false;

    String lcStr = "";//12-16取反
    String scStr = "";//16-20
    String lmdStr = "";//20-24
    String fjzzStr = "";//24-28 分接自增
    String fj1Str = "";//28-32
    String fj2Str = "";//32-36
    String spbhStr = "";//36-60
    String ljfsStr = "";//60-64
    String csxStr = "";//64-68
    //String sjStr = "";//68-80
    String nian;//68-70
    String yue;//70-72
    String ri;//72-74
    String shi;//74-76
    String fen;//76-78
    String miao;//78-80
    String bxStr = "";//80-28880
    String bxLongStr = "";//波形参数长度（十六进制截取字符串）
    int bxLong = 0;//波形参数长度（字节）

    //两个相邻的数作比较
    float fa1 = 0;
    float fa2 = 0;
    float fb1 = 0;
    float fb2 = 0;
    float fc1 = 0;
    float fc2 = 0;

    int startTypeA = 0;//判断是否出现过，出现一次后改为1
    int startTypeB = 0;//判断是否出现过，出现一次后改为1
    int startTypeC = 0;//判断是否出现过，出现一次后改为1
    float fa11 = 0;
    float fa21 = 0;
    int endType = 0;
    String startTimeA;//A相开始时间
    String startTimeB;//B相开始时间
    String startTimeC;//C相开始时间

    int startGddzTypeA = 0;//过渡电阻起点A;0:未找到起点；1:找到起点
    int gddzTypeA = 0;//过渡电阻起点A
    int startGddzTypeB = 0;//过渡电阻起点A;0:未找到起点；1:找到起点
    int gddzTypeB = 0;//过渡电阻起点A
    int startGddzTypeC = 0;//过渡电阻起点A;0:未找到起点；1:找到起点
    int gddzTypeC = 0;//过渡电阻起点A

    float fa3 = 0;
    float fb3 = 0;
    float fc3 = 0;
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
                    //Log.e(TAG,"=======start==========");
                    if(StringUtils.isEquals(Config.ymType,"yzYzcsKaishiCs2")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "CsBoxing:"+msgStr);
                        if(StringUtils.isEquals("FEEF04E0010001FDDF",msgStr)){

                        }else{
                            if(IndexOfAndSubStr.isIndexOf(msgStr,"FEEF")&&StringUtils.isEmpty(newMsgStr)){
                                bxLongStr = StringUtils.subStrStartToEnd(msgStr, 8, 12);;//12-16取反
                                bxLong = ShiOrShiliu.parseInt(HexUtil.reverseHex(bxLongStr));
                                //Log.e(TAG+"bxlong",bxLongStr);
                                newMsgStr = msgStr;
                            }else {
                                newMsgStr = newMsgStr + msgStr;
                                Log.e(TAG, "CsBoxing:"+newMsgStr);
                            }
                            Log.e(TAG, "CsBoxing:"+newMsgStr.length());
                            Log.e(TAG+"allLong",(bxLong*2+16)+"");
                            if(newMsgStr.length()>=(bxLong*2+16)&&newMsgStr.length()<(bxLong*2+16+128)){
                                Log.e(TAG, "进...:"+"zhiling=======1");
                                lcStr = StringUtils.subStrStartToEnd(newMsgStr, 12, 16);;//12-16取反
                                scStr = StringUtils.subStrStartToEnd(newMsgStr, 16, 20);;//16-20
                                lmdStr = StringUtils.subStrStartToEnd(newMsgStr, 20, 24);;//20-24
                                fjzzStr = StringUtils.subStrStartToEnd(newMsgStr, 24, 28);;//24-28
                                fj1Str = StringUtils.subStrStartToEnd(newMsgStr, 28, 32);;//28-32
                                fj2Str = StringUtils.subStrStartToEnd(newMsgStr, 32, 36);;//32-36
                                spbhStr = StringUtils.subStrStartToEnd(newMsgStr, 36, 60);;//36-60
                                ljfsStr = StringUtils.subStrStartToEnd(newMsgStr, 60, 64);;//60-64
                                csxStr = StringUtils.subStrStartToEnd(newMsgStr, 64, 68);;//64-68
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 68, 70);;//68-70
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 70, 72);;//70-72
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 72, 74);;//72-74
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 74, 76);;//74-76
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 76, 78);;//76-78
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 78, 80);;//78-80
                                bxStr = StringUtils.subStrStartToEnd(newMsgStr, 80, (bxLong*2)+12);//80-28880

                                Log.e(TAG, "bxStr"+bxStr.length());
                                int type2 = 0;
                                XiaoshuYunsuan xsys = new XiaoshuYunsuan();
//                            type+=1;
//                            typeX+=2;
//                            mAxisXValues.clear();
//                            mPointValues.clear();

                                for(int i=1;i<=(bxLong-40)/6;i++){

                                    String strI = StringUtils.subStrStartToEnd(bxStr,type2,i*12);
                                    type2 = i*12;
                                    Log.e(TAG, "strI:"+strI);
                                    String aStr = StringUtils.subStrStartToEnd(strI,0,4);
                                    String bStr = StringUtils.subStrStartToEnd(strI,4,8);
                                    String cStr = StringUtils.subStrStartToEnd(strI,8,12);
                                    Log.e(TAG, "CsBoxing2:"+aStr+","+bStr+","+cStr);
                                    type+=1;
                                    fa1 = (float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(aStr))+""),xsys.xiaoshu("0.01"));
                                    fb1 = (float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(bStr))+""),xsys.xiaoshu("0.01"));
                                    fc1 = (float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(cStr))+""),xsys.xiaoshu("0.01"));
                                    //Log.e(TAG+"C值",(-(float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(cStr))+""),xsys.xiaoshu("0.01")))+"");

//                                if(startTypeA==0){
//                                    if(xsys.bijiao(xsys.xiaoshu(fa1+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fa1+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fa2+""),xsys.xiaoshu("2"))+""))){
//                                        //Log.e("qishidian==1",fa1+"");
//                                        startTypeA = 1;
//                                        mPointValues6.remove(0);
//                                        mPointValues6.add(new PointValue(i-1,0));
//                                        //tvRa2.setText(fa1+"");
//                                        startTimeA = String.format("%.2f",(i-1)*0.05);
//                                        //Log.e("========",startTimeA);
//                                    }
//                                    fa2 = fa1;
//                                }
//                                if(startTypeB==0){
//                                    if(xsys.bijiao(xsys.xiaoshu(fb1+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fb1+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fb2+""),xsys.xiaoshu("2"))+""))){
//                                        //Log.e("qishidian==1",fb1+"");
//                                        startTypeB = 1;
//                                        mPointValues6.remove(0);
//                                        mPointValues6.add(new PointValue(i-1,0));
//                                        //tvRa2.setText(fa1+"");
//                                        startTimeB = String.format("%.2f",(i-1)*0.05);
//                                        //Log.e("========",startTimeB);
//                                    }
//                                    fb2 = fb1;
//                                }
//                                if(startTypeC==0){
//                                    if(xsys.bijiao(xsys.xiaoshu(fc1+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fc1+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fc2+""),xsys.xiaoshu("2"))+""))){
//                                        //Log.e("qishidian==1",fc1+"");
//                                        startTypeC = 1;
//                                        mPointValues6.remove(0);
//                                        mPointValues6.add(new PointValue(i-1,0));
//                                        //tvRa2.setText(fa1+"");
//                                        startTimeC = String.format("%.2f",(i-1)*0.05);
//                                        //Log.e("========",startTimeC);
//                                    }
//                                    fc2 = fc1;
//                                }
//                                if(endType==0){
//                                    if(startGddzTypeA==0){//未找到过渡起点A
//                                        if(xsys.bijiao(xsys.xiaoshu(fa3+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshuJian(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa3+"")),xsys.xiaoshu("0.1"))==false){//先转小数，求差，再和“0.1”作比较
//                                            //bigDecimalA = xsys.xiaoshuJia(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa1+""));
//                                            //Log.e("guodudianzu==",fa1+","+fa3);
//                                            startGddzTypeA = 1;//找到过渡起点
//                                            gddzTypeA = i;
//                                            //bigDecimalA = xsys.xiaoshu(fa1+"");
//                                        }
//                                        fa3 = fa1;
//                                    }
//
//                                    if(i==gddzTypeA+5){//&&i<gddzTypeA+15
//                                        //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
//                                        //gddzTypeA=gddzTypeA+1;
//                                        //Log.e("guodudianzu=a=all",fa1+"");
//                                        tvRa1.setText(String.format("%.2f",fa1));
//                                    }
//                                    if(startGddzTypeB==0){//未找到过渡起点B
//                                        if(xsys.bijiao(xsys.xiaoshu(fb3+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshuJian(xsys.xiaoshu(fb1+""),xsys.xiaoshu(fb3+"")),xsys.xiaoshu("0.1"))==false){//先转小数，求差，再和“0.1”作比较
//                                            //bigDecimalA = xsys.xiaoshuJia(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa1+""));
//                                            //Log.e("guodudianzu==b",fb1+","+fb3);
//                                            startGddzTypeB = 1;//找到过渡起点
//                                            gddzTypeB = i;
//                                            //bigDecimalA = xsys.xiaoshu(fa1+"");
//                                        }
//                                        fb3 = fb1;
//                                    }
//
//                                    if(i==gddzTypeB+5){//&&i<gddzTypeA+15
//                                        //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
//                                        //gddzTypeA=gddzTypeA+1;
//                                        //Log.e("guodudianzu=b=all",fb1+"");
//                                        tvRb1.setText(String.format("%.2f",fb1));
//                                    }
//                                    if(startGddzTypeC==0){//未找到过渡起点C
//                                        if(xsys.bijiao(xsys.xiaoshu(fc3+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshuJian(xsys.xiaoshu(fc1+""),xsys.xiaoshu(fc3+"")),xsys.xiaoshu("0.1"))==false){//先转小数，求差，再和“0.1”作比较
//                                            //bigDecimalA = xsys.xiaoshuJia(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa1+""));
//                                            //Log.e("guodudianzu=c=",fc1+","+fc3);
//                                            startGddzTypeC = 1;//找到过渡起点
//                                            gddzTypeC = i;
//                                            //bigDecimalA = xsys.xiaoshu(fa1+"");
//                                        }
//                                        fc3 = fc1;
//                                    }
//
//                                    if(i==gddzTypeC+5){//&&i<gddzTypeA+15
//                                        //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
//                                        //gddzTypeA=gddzTypeA+1;
//                                        //Log.e("guodudianzu=c=all",fc1+"");
//                                        tvRc1.setText(String.format("%.2f",fc1));
//                                    }
//                                    if(xsys.bijiao(xsys.xiaoshu(fa2+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fa2+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fa1+""),xsys.xiaoshu("2"))+""))){
//                                        Log.e("qishidian==2",fa2+"");
//                                        //endType = 1;
//                                        mPointValues5.remove(0);
//                                        mPointValues5.add(new PointValue(i-1,0));
//                                        tvRa2.setText(fa2+"");
//                                        tvRb2.setText(fb2+"");
//                                        tvRc2.setText(fc2+"");
//                                    }
//                                    if(xsys.bijiao(xsys.xiaoshu(fb2+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fb2+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fb1+""),xsys.xiaoshu("2"))+""))){
//                                        Log.e("qishidian==2",fb2+"");
//                                        //endType = 1;
//                                        mPointValues5.remove(0);
//                                        mPointValues5.add(new PointValue(i-1,0));
//                                        tvRa2.setText(fa2+"");
//                                        tvRb2.setText(fb2+"");
//                                        tvRc2.setText(fc2+"");
//                                    }
//                                    if(xsys.bijiao(xsys.xiaoshu(fc2+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fc2+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fc1+""),xsys.xiaoshu("2"))+""))){
//                                        Log.e("qishidian==2",fc2+"");
//                                        //endType = 1;
//                                        mPointValues5.remove(0);
//                                        mPointValues5.add(new PointValue(i-1,0));
//                                        tvRa2.setText(fa2+"");
//                                        tvRb2.setText(fb2+"");
//                                        tvRc2.setText(fc2+"");
//                                    }
//                                    fa2 = fa1;
//                                    fb2 = fb1;
//                                    fc2 = fc1;
//                                }
                                /*int x11 = (int)mPointValues5.get(0).getX();
                                int x21 = (int)mPointValues6.get(0).getX();
                                int cha1 = x11-x21;
                                int cha2;
                                if(cha1<0){
                                    cha2 = cha1*(-1);
                                }else{
                                    cha2 = cha1;
                                }
                                tvZuoxia1.setText(String.format("%.2f", cha2*0.05));
                                tvGdsj.setText(String.format("%.2f", cha2*0.05));
                                if(StringUtils.noEmpty(startTimeA)&&StringUtils.noEmpty(startTimeB)&&StringUtils.noEmpty(startTimeC)){
                                    tvBtqsj.setText(xsys.bijiaoDaxiaoCha(xsys.xiaoshu(startTimeA),xsys.xiaoshu(startTimeB),xsys.xiaoshu(startTimeC))+"");
                                }*/

//                                mPointValues.add(new PointValue((float)type, -fa1));
//                                mPointValues2.add(new PointValue((float)type,-fb1));
//                                mPointValues3.add(new PointValue((float)type,-fc1));
//                                //添加X轴数据
//                                mAxisXValues.add(new AxisValue(type).setLabel(String.valueOf(type/2)));
//                                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
//                                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
//                                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原qqqqqqqqq
                                    dateList1.add(i+"");
                                    scoreList1.add(fa1+"");
                                    scoreList2.add(fb1+"");
                                    scoreList3.add(fc1+"");
                                }
                                getAxisXLables();//获取x轴的标注
                                getAxisPoints(0);//获取坐标点
                                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
                                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
                                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
                            }else if((newMsgStr.length()==(bxLong*2+16)*2)){//第二次发送数据，需要穿插的
                                Log.e(TAG, "进...:"+"zhiling=======2");
                                newMsgStr = StringUtils.subStrStartToEnd(newMsgStr, (bxLong*2+16), (bxLong*2+16)*2);
                                lcStr = StringUtils.subStrStartToEnd(newMsgStr, 12, 16);;//12-16取反
                                scStr = StringUtils.subStrStartToEnd(newMsgStr, 16, 20);;//16-20
                                lmdStr = StringUtils.subStrStartToEnd(newMsgStr, 20, 24);;//20-24
                                fjzzStr = StringUtils.subStrStartToEnd(newMsgStr, 24, 28);;//24-28
                                fj1Str = StringUtils.subStrStartToEnd(newMsgStr, 28, 32);;//28-32
                                fj2Str = StringUtils.subStrStartToEnd(newMsgStr, 32, 36);;//32-36
                                spbhStr = StringUtils.subStrStartToEnd(newMsgStr, 36, 60);;//36-60
                                ljfsStr = StringUtils.subStrStartToEnd(newMsgStr, 60, 64);;//60-64
                                csxStr = StringUtils.subStrStartToEnd(newMsgStr, 64, 68);;//64-68
                                nian = StringUtils.subStrStartToEnd(newMsgStr, 68, 70);;//68-70
                                yue = StringUtils.subStrStartToEnd(newMsgStr, 70, 72);;//70-72
                                ri = StringUtils.subStrStartToEnd(newMsgStr, 72, 74);;//72-74
                                shi = StringUtils.subStrStartToEnd(newMsgStr, 74, 76);;//74-76
                                fen = StringUtils.subStrStartToEnd(newMsgStr, 76, 78);;//76-78
                                miao = StringUtils.subStrStartToEnd(newMsgStr, 78, 80);;//78-80
                                bxStr = StringUtils.subStrStartToEnd(newMsgStr, 80, (bxLong*2)+16-4);//80-76880
                                int type2 = 0;
//                        List<String>listA = new ArrayList<>();
//                        List<String>listB = new ArrayList<>();
//                        List<String>listC = new ArrayList<>();
                                type+=1;
                                typeX+=2;

                                XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                                for(int i=1;i<=(bxLong-40)/6;i++){
                                    String strI = StringUtils.subStrStartToEnd(bxStr,type2,i*12);
                                    type2 = i*12;

                                    String aStr = StringUtils.subStrStartToEnd(strI,0,4);
                                    String bStr = StringUtils.subStrStartToEnd(strI,4,8);
                                    String cStr = StringUtils.subStrStartToEnd(strI,8,12);

                                    type+=1;
                                    fa1 = (float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(aStr))+""),xsys.xiaoshu("0.01"));
                                    fb1 = (float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(bStr))+""),xsys.xiaoshu("0.01"));
                                    fc1 = (float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(cStr))+""),xsys.xiaoshu("0.01"));
                                    //Log.e(TAG+"C值",(-(float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex(cStr))+""),xsys.xiaoshu("0.01")))+"");

//                                if(startTypeA==0){
//                                    if(xsys.bijiao(xsys.xiaoshu(fa1+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fa1+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fa2+""),xsys.xiaoshu("2"))+""))){
//                                        //Log.e("qishidian==1",fa1+"");
//                                        startTypeA = 1;
//                                        mPointValues6.remove(0);
//                                        mPointValues6.add(new PointValue(i-1,0));
//                                        //tvRa2.setText(fa1+"");
//                                        startTimeA = String.format("%.2f",(i-1)*0.05);
//                                        //Log.e("========",startTimeA);
//                                    }
//                                    fa2 = fa1;
//                                }
//                                if(startTypeB==0){
//                                    if(xsys.bijiao(xsys.xiaoshu(fb1+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fb1+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fb2+""),xsys.xiaoshu("2"))+""))){
//                                        //Log.e("qishidian==1",fb1+"");
//                                        startTypeB = 1;
//                                        mPointValues6.remove(0);
//                                        mPointValues6.add(new PointValue(i-1,0));
//                                        //tvRa2.setText(fa1+"");
//                                        startTimeB = String.format("%.2f",(i-1)*0.05);
//                                        //Log.e("========",startTimeB);
//                                    }
//                                    fb2 = fb1;
//                                }
//                                if(startTypeC==0){
//                                    if(xsys.bijiao(xsys.xiaoshu(fc1+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fc1+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fc2+""),xsys.xiaoshu("2"))+""))){
//                                        //Log.e("qishidian==1",fc1+"");
//                                        startTypeC = 1;
//                                        mPointValues6.remove(0);
//                                        mPointValues6.add(new PointValue(i-1,0));
//                                        //tvRa2.setText(fa1+"");
//                                        startTimeC = String.format("%.2f",(i-1)*0.05);
//                                        //Log.e("========",startTimeC);
//                                    }
//                                    fc2 = fc1;
//                                }
//                                if(endType==0){
//                                    if(startGddzTypeA==0){//未找到过渡起点A
//                                        if(xsys.bijiao(xsys.xiaoshu(fa3+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshuJian(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa3+"")),xsys.xiaoshu("0.1"))==false){//先转小数，求差，再和“0.1”作比较
//                                            //bigDecimalA = xsys.xiaoshuJia(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa1+""));
//                                            //Log.e("guodudianzu==",fa1+","+fa3);
//                                            startGddzTypeA = 1;//找到过渡起点
//                                            gddzTypeA = i;
//                                            //bigDecimalA = xsys.xiaoshu(fa1+"");
//                                        }
//                                        fa3 = fa1;
//                                    }
//
//                                    if(i==gddzTypeA+5){//&&i<gddzTypeA+15
//                                        //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
//                                        //gddzTypeA=gddzTypeA+1;
//                                        //Log.e("guodudianzu=a=all",fa1+"");
//                                        tvRa1.setText(String.format("%.2f",fa1));
//                                    }
//                                    if(startGddzTypeB==0){//未找到过渡起点B
//                                        if(xsys.bijiao(xsys.xiaoshu(fb3+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshuJian(xsys.xiaoshu(fb1+""),xsys.xiaoshu(fb3+"")),xsys.xiaoshu("0.1"))==false){//先转小数，求差，再和“0.1”作比较
//                                            //bigDecimalA = xsys.xiaoshuJia(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa1+""));
//                                            //Log.e("guodudianzu==b",fb1+","+fb3);
//                                            startGddzTypeB = 1;//找到过渡起点
//                                            gddzTypeB = i;
//                                            //bigDecimalA = xsys.xiaoshu(fa1+"");
//                                        }
//                                        fb3 = fb1;
//                                    }
//
//                                    if(i==gddzTypeB+5){//&&i<gddzTypeA+15
//                                        //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
//                                        //gddzTypeA=gddzTypeA+1;
//                                        //Log.e("guodudianzu=b=all",fb1+"");
//                                        tvRb1.setText(String.format("%.2f",fb1));
//                                    }
//                                    if(startGddzTypeC==0){//未找到过渡起点C
//                                        if(xsys.bijiao(xsys.xiaoshu(fc3+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshuJian(xsys.xiaoshu(fc1+""),xsys.xiaoshu(fc3+"")),xsys.xiaoshu("0.1"))==false){//先转小数，求差，再和“0.1”作比较
//                                            //bigDecimalA = xsys.xiaoshuJia(xsys.xiaoshu(fa1+""),xsys.xiaoshu(fa1+""));
//                                            //Log.e("guodudianzu=c=",fc1+","+fc3);
//                                            startGddzTypeC = 1;//找到过渡起点
//                                            gddzTypeC = i;
//                                            //bigDecimalA = xsys.xiaoshu(fa1+"");
//                                        }
//                                        fc3 = fc1;
//                                    }
//
//                                    if(i==gddzTypeC+5){//&&i<gddzTypeA+15
//                                        //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
//                                        //gddzTypeA=gddzTypeA+1;
//                                        //Log.e("guodudianzu=c=all",fc1+"");
//                                        tvRc1.setText(String.format("%.2f",fc1));
//                                    }
//                                    if(xsys.bijiao(xsys.xiaoshu(fa2+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fa2+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fa1+""),xsys.xiaoshu("2"))+""))){
//                                        Log.e("qishidian==2",fa2+"");
//                                        //endType = 1;
//                                        mPointValues5.remove(0);
//                                        mPointValues5.add(new PointValue(i-1,0));
//                                        tvRa2.setText(fa2+"");
//                                        tvRb2.setText(fb2+"");
//                                        tvRc2.setText(fc2+"");
//                                    }
//                                    if(xsys.bijiao(xsys.xiaoshu(fb2+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fb2+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fb1+""),xsys.xiaoshu("2"))+""))){
//                                        Log.e("qishidian==2",fb2+"");
//                                        //endType = 1;
//                                        mPointValues5.remove(0);
//                                        mPointValues5.add(new PointValue(i-1,0));
//                                        tvRa2.setText(fa2+"");
//                                        tvRb2.setText(fb2+"");
//                                        tvRc2.setText(fc2+"");
//                                    }
//                                    if(xsys.bijiao(xsys.xiaoshu(fc2+""),xsys.xiaoshu("0.1"))&&xsys.bijiao(xsys.xiaoshu(fc2+""),xsys.xiaoshu(xsys.xiaoshuCheng(xsys.xiaoshu(fc1+""),xsys.xiaoshu("2"))+""))){
//                                        Log.e("qishidian==2",fc2+"");
//                                        //endType = 1;
//                                        mPointValues5.remove(0);
//                                        mPointValues5.add(new PointValue(i-1,0));
//                                        tvRa2.setText(fa2+"");
//                                        tvRb2.setText(fb2+"");
//                                        tvRc2.setText(fc2+"");
//                                    }
//                                    fa2 = fa1;
//                                    fb2 = fb1;
//                                    fc2 = fc1;
//                                }
                                /*int x11 = (int)mPointValues5.get(0).getX();
                                int x21 = (int)mPointValues6.get(0).getX();
                                int cha1 = x11-x21;
                                int cha2;
                                if(cha1<0){
                                    cha2 = cha1*(-1);
                                }else{
                                    cha2 = cha1;
                                }
                                tvZuoxia1.setText(String.format("%.2f", cha2*0.05));
                                tvGdsj.setText(String.format("%.2f", cha2*0.05));
                                if(StringUtils.noEmpty(startTimeA)&&StringUtils.noEmpty(startTimeB)&&StringUtils.noEmpty(startTimeC)){
                                    tvBtqsj.setText(xsys.bijiaoDaxiaoCha(xsys.xiaoshu(startTimeA),xsys.xiaoshu(startTimeB),xsys.xiaoshu(startTimeC))+"");
                                }*/

//                                mPointValues.add(new PointValue((float)type, -fa1));
//                                mPointValues2.add(new PointValue((float)type,-fb1));
//                                mPointValues3.add(new PointValue((float)type,-fc1));
//                                //添加X轴数据
//                                mAxisXValues.add(new AxisValue(type).setLabel(String.valueOf(type/2)));
//                                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
//                                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
//                                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原qqqqqqqqq
                                    dateList1.add(2*i-1,typeX+"");
                                    scoreList1.add(2*i-1,fa1+"");
                                    scoreList2.add(2*i-1,fb1+"");
                                    scoreList3.add(2*i-1,fc1+"");

                                }
                                mAxisXValues.clear();
                                mPointValues.clear();
                                mPointValues2.clear();
                                mPointValues3.clear();
                                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
                                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
                                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
                                getAxisXLables();//获取x轴的标注
                                startTypeA=0;
                                startTypeB=0;
                                startTypeC=0;
                                getAxisPoints(1);//获取坐标点
                                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
                                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
                                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
                                Log.e(TAG,"=======end==========");
                            }
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

        setContentView(R.layout.activity_yz_yzcs_kaishi_cs2);
        Config.ymType = "yzYzcsKaishiCs2";
        ActivityCollector.addActivity(this);

        Log.e(TAG,"jin...........");

        initModel();
        initView();
        //getAxisXLables();//获取x轴的标注
        //getAxisPoints(0);//获取坐标点
        initLineChart();//初始化
        new TimeThread().start();
    }
    public void initModel(){
        bleConnectUtil = new BleConnectUtil(YzYzcsKaishiCs2Activity.this);
        if(!bleConnectUtil.isConnected()&& StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)){
            bleConnectUtil.connect(bleConnectUtil.wsDeviceAddress,10,10);//标签从机：34:14:B5:B6:D6:E1
            bleConnectUtil.setCallback(blecallback);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //要执行的操作--->获取测试状态
                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"a001000000fddf",Config.yzCrcTYpe), "");
            }
        }, 1000);//1秒后执行Runnable中的run方法
    }
    public void initView(){
        tvRa1 = findViewById(R.id.tvYzYzcsKsCs2RA1);
        tvRa2 = findViewById(R.id.tvYzYzcsKsCs2RA2);
        tvRb1 = findViewById(R.id.tvYzYzcsKsCs2RB1);
        tvRb2 = findViewById(R.id.tvYzYzcsKsCs2RB2);
        tvRc1 = findViewById(R.id.tvYzYzcsKsCs2RC1);
        tvRc2 = findViewById(R.id.tvYzYzcsKsCs2RC2);
        tvGdsj = findViewById(R.id.tvYzYzcsKsCs2Gdsj);
        tvBtqsj = findViewById(R.id.tvYzYzcsKsCs2Btqsj);
        tvMsDiv = findViewById(R.id.tvYzYzcsKsCs2MsDiv);
        tvZuoxia1 = findViewById(R.id.tvYzYzcsKsCs2Zuoxia1);
        tvZuoxia2 = findViewById(R.id.tvYzYzcsKsCs2Zuoxia2);
        tvGbqh = findViewById(R.id.tvYzYzcsKsCs2Gbqh);
        ivZuo = findViewById(R.id.ivYzYzcsKsCs2Zuo);
        ivYou = findViewById(R.id.ivYzYzcsKsCs2You);
        tvTime = findViewById(R.id.tvYzYzcsKsCs2Time);
        tvCs = findViewById(R.id.tvYzYzcsKsCs2Cs);
        tvBxsf = findViewById(R.id.tvYzYzcsKsCs2Bxsf);
        tvDy = findViewById(R.id.tvYzYzcsKsCs2Dy);
        tvBc = findViewById(R.id.tvYzYzcsKsCs2Bc);
        tvFh = findViewById(R.id.tvYzYzcsKsCs2Fh);
        lineChart = findViewById(R.id.lineCvYzYzcsKsCs2);
        lineChart2 = findViewById(R.id.lineCvYzYzcsKsCs22);
        lineChart22 = findViewById(R.id.lineCvYzYzcsKsCs222);
        tvColor = findViewById(R.id.tvYzYzcsKsCs2GbqhType);

        llYzCs2 = findViewById(R.id.llYzCs2);
        tvGbqh.setOnClickListener(this);
        ivZuo.setOnClickListener(this);
        ivYou.setOnClickListener(this);
        tvCs.setOnClickListener(this);
        tvBxsf.setOnClickListener(this);
        tvDy.setOnClickListener(this);
        tvBc.setOnClickListener(this);
        tvFh.setOnClickListener(this);

        ivZuo.setOnTouchListener(this);
        ivZuo.setOnLongClickListener(this);
        ivYou.setOnTouchListener(this);
        ivYou.setOnLongClickListener(this);

        tvRa1.setOnClickListener(this);
        tvRb1.setOnClickListener(this);
        tvRc1.setOnClickListener(this);
        tvGdsj.setOnClickListener(this);
        tvBtqsj.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Chart chart=lineChart;

                break;
            case MotionEvent.ACTION_UP:
                Log.e("===","抬起。。。");
                switch (v.getId()){
                    case R.id.ivYzYzcsKsCs2Zuo:
                        countDownTimerupZuo.cancel();
                        break;
                    case R.id.ivYzYzcsKsCs2You:
                        countDownTimerupYou.cancel();
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //mPointValues2.clear();

                break;

            //
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        Log.e("===","按着。。。");
        switch (v.getId()){
            case R.id.ivYzYzcsKsCs2Zuo:
                countDownTimerupZuo.start();
                break;
            case R.id.ivYzYzcsKsCs2You:
                countDownTimerupYou.start();
                break;
        }
        return false;
    }
    //每隔1000毫秒===》1s触发一次;长按一次最大时间：1000000==>1000s
    final CountDownTimer countDownTimerupZuo=new CountDownTimer(1000000,1) {
        @Override
        public void onTick(long millisUntilFinished) {
           gbZuo();
        }
        @Override
        public void onFinish() {

        }

    };
    //每隔1000毫秒===》1s触发一次;长按一次最大时间：1000000==>1000s
    final CountDownTimer countDownTimerupYou=new CountDownTimer(1000000,1) {
        @Override
        public void onTick(long millisUntilFinished) {
          gbYou();
        }
        @Override
        public void onFinish() {

        }

    };

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
        int x11 = (int)mPointValues5.get(0).getX();
        int x12 = (int)mPointValues6.get(0).getX();
        switch (v.getId()){
            case R.id.tvYzYzcsKsCs2RA1:
                String ra2Str = tvRa2.getText().toString();
                tvRa1.setText(ra2Str);
//                float y1 = mPointValues.get(x11).getY();
//                float y2 = mPointValues.get(x12).getY();
//                float v1 = (y1 + y2)/2*(-1);
//                tvRa1.setText(String.format("%.2f",v1)+"");
                break;
            case R.id.tvYzYzcsKsCs2RB1:
                String rb2Str = tvRb2.getText().toString();
                tvRb1.setText(rb2Str);
//                float y12 = mPointValues2.get(x11).getY();
//                float y22 = mPointValues2.get(x12).getY();
//                float v12 = (y12 + y22)/2*(-1);
//                tvRb1.setText(String.format("%.2f",v12)+"");
                break;
            case R.id.tvYzYzcsKsCs2RC1:
                String rc2Str = tvRc2.getText().toString();
                tvRc1.setText(rc2Str);
//                float y13 = mPointValues3.get(x11).getY();
//                float y23 = mPointValues3.get(x12).getY();
//                float v13 = (y13 + y23)/2*(-1);
//                tvRc1.setText(String.format("%.2f",v13)+"");
                break;
            case R.id.tvYzYzcsKsCs2Gdsj:
                String tvZuoxiaStr = tvZuoxia1.getText().toString();
                tvGdsj.setText(tvZuoxiaStr);
                break;
            case R.id.tvYzYzcsKsCs2Btqsj:
                String tvZuoxiaStr2 = tvZuoxia1.getText().toString();
                tvBtqsj.setText(tvZuoxiaStr2);
                break;
            case R.id.tvYzYzcsKsCs2Gbqh://光标切换
                if(gb==0){
                    gb=1;
                    tvColor.setText(getString(R.string.bai));
                    tvColor.setTextColor(Color.WHITE);
                    if(x12==mPointValues.size()){
                        x12=mPointValues.size()-1;
                    }
                    float ya1 = mPointValues.get(x12).getY();
                    tvRa2.setText(String.format("%.2f",ya1*(-1)));
                    float yb1 = mPointValues2.get(x12).getY();
                    tvRb2.setText(String.format("%.2f",yb1*(-1)));
                    float yc1 = mPointValues3.get(x12).getY();
                    tvRc2.setText(String.format("%.2f",yc1*(-1)));
                }else{
                    gb=0;
                    tvColor.setText(getString(R.string.hei));
                    tvColor.setTextColor(Color.BLACK);
                    if(x11==mPointValues.size()){
                        x11=mPointValues.size()-1;
                    }
                    float ya2 = mPointValues.get(x11).getY();
                    tvRa2.setText(String.format("%.2f",ya2*(-1)));
                    float yb2 = mPointValues2.get(x11).getY();
                    tvRb2.setText(String.format("%.2f",yb2*(-1)));
                    float yc2 = mPointValues3.get(x11).getY();
                    tvRc2.setText(String.format("%.2f",yc2*(-1)));
                }
                break;
            case R.id.ivYzYzcsKsCs2Zuo:
                gbZuo();
                break;
            case R.id.ivYzYzcsKsCs2You:
                gbYou();
                break;
            case R.id.tvYzYzcsKsCs2Cs:
//                type+=1;
//                mPointValues.add(new PointValue((float)type, (float)-5));
//                mPointValues2.add(new PointValue((float)type,(float)-10));
//                mPointValues3.add(new PointValue((float)type,(float)-15));
//                //添加X轴数据
//                mAxisXValues.add(new AxisValue(type).setLabel(String.valueOf(type/2)));
//                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
//                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
//                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
//                Log.e(TAG,type+"------1-----");
                Intent it2 = new Intent();
                it2.setClass(YzYzcsKaishiCs2Activity.this, YzYzcsCsszActivity.class);
                it2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                it2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                startActivity(it2);
                break;
            case R.id.tvYzYzcsKsCs2Bxsf:
                Log.e(TAG,type+"-----2------");
                if(sfType==0){
                    sfType=type;
                }
                Viewport v2 = new Viewport(lineChart.getMaximumViewport());
                //sfType -= 2;
                sfType -= type/5;
                if(sfType<=3){//最少展示三个数据
                    sfType=type;
                }
                v2.left = 0;
                v2.right= sfType;
                lineChart.setCurrentViewport(v2);
                lineChart2.setCurrentViewport(v2);
                lineChart22.setCurrentViewport(v2);
                break;
            case R.id.tvYzYzcsKsCs2Dy:
                String lujingStr = "";
                //图片保存本地
                String path = getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".png";
                /* /storage/emulated/0/Android/data/com.yc.allbluetooth/files/poi/23080500160649.doc*/
                BitmapUtils.bitmap2Path(viewShot(llYzCs2), path);
                lujingStr = IndexOfAndSubStr.subStrStart(path,path.length()-18);

                Log.e("=====", lujingStr);
                Intent share = new Intent(Intent.ACTION_SEND);
                File file = new File("/storage/emulated/0/Android/data/com.yc.allbluetooth/files/poi/"+lujingStr);
                Uri contentUri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    contentUri = FileProvider.getUriForFile(this, getPackageName()+".FileProvider" , file);

                    share.putExtra(Intent.EXTRA_STREAM, contentUri);
                    share.setType("image/png");// 此处可发送多种文件//{".png", "image/png"},//"application/msword"
                } else {
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    share.setType("image/png");// 此处可发送多种文件
                }
                try{
                    startActivity(Intent.createChooser(share, "Share"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvYzYzcsKsCs2Bc:
                break;
            case R.id.tvYzYzcsKsCs2Fh:
                //feef04e00000fddf
                sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"e00000fddf",Config.yzCrcTYpe),"");
                finish();
                Intent it = new Intent();
                it.setClass(YzYzcsKaishiCs2Activity.this, YzHomeActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//销毁前面的页面
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//Main页面不会重新创建
                startActivity(it);
                //finish();

                break;
        }
    }
    /**
     * 控件截屏
     * @param linearLayout
     * @return
     */
    private Bitmap viewShot(LinearLayout linearLayout) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            h += linearLayout.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(linearLayout.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }
    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables(){
//        for (int i = 0; i < date.length; i++) {
//            Log.e("x===",date[i]);
//            //Log.e("x===",new AxisValue(i).setLabel(date[i]).toString());
//            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
//        }
        for(int i=0;i<dateList1.size();i++){
            mAxisXValues.add(new AxisValue(i).setLabel(dateList1.get(i)));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(int type) {
//        for (int i = 0; i < score.length; i++) {
//            type = i;
//        }
//        Log.e(TAG+"=sSize()",scoreList1.size()+"");
//        Log.e(TAG+"=sSize()2",scoreList1.toString()+"");
        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
        for(int i=0;i<scoreList1.size();i++){
            fa1 = Float.parseFloat(scoreList1.get(i));
            //Log.e(TAG,fa1+","+fa2);
            if(startTypeA==0){
                if(fa1>=fa2){
                    if(xsys.bijiao(xsys.xiaoshu(fa1+""),xsys.xiaoshu("1"))&&xsys.bijiao(xsys.xiaoshu("1"),xsys.xiaoshu(fa2+""))){//xsys.xiaoshuCheng(xsys.xiaoshu(fa2+""),xsys.xiaoshu("2"))
                        Log.e("qishidian==1",fa1+"");
                        startTypeA = 1;
                        gddzTypeA = i;
                        mPointValues6.remove(0);
                        mPointValues6.add(new PointValue(i,0));
                        mPointValues5.remove(0);
                        mPointValues5.add(new PointValue(i,0));
                        tvRa2.setText(String.format("%.2f",fa1));
                        //tvRa2.setText(fa1+"");
                        startTimeA = String.format("%.2f",(i-1)*0.05);
                        Log.e("========a",startTimeA);
                    }
                }else{
                    startTypeA=0;
                }
                fa2 = fa1;
            }
            if(endType==0){
                if(xsys.bijiao(xsys.xiaoshu(fa3+""),xsys.xiaoshu("1"))&&xsys.bijiao(xsys.xiaoshu("1"),xsys.xiaoshu(fa1+""))){
                    Log.e("qishidian==2",fa1+","+i);
                    //endType = 1;
                    mPointValues5.remove(0);
                    if(type==0){
                        mPointValues5.add(new PointValue(i-1,0));
                    }else{
                        mPointValues5.add(new PointValue((i-1),0));
                    }
                    tvRa2.setText(String.format("%.2f",fa3));
                    //startTimeA = String.format("%.2f",(i-1)*0.05);
                    //Log.e("========",startTimeA);
                }
                fa3 = fa1;
            }
            if(i==gddzTypeA+50){//&&i<gddzTypeA+15
                //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
                //gddzTypeA=gddzTypeA+1;
                //Log.e("guodudianzu=b=all",fb1+"");
                tvRa1.setText(String.format("%.2f",fa1));
            }
            mPointValues.add(new PointValue(i,-Float.parseFloat(scoreList1.get(i))));
        }
        for(int i=0;i<scoreList2.size();i++){
            fb1 = Float.parseFloat(scoreList2.get(i));
            //Log.e(TAG,fb1+","+fb2);

            if(startTypeB==0){
                if(fb1>=fb2){
                    if(xsys.bijiao(xsys.xiaoshu(fb1+""),xsys.xiaoshu("1"))&&xsys.bijiao(xsys.xiaoshu("1"),xsys.xiaoshu(fb2+""))){
                        Log.e("qishidian==1",fb1+"");
                        startTypeB = 1;
                        gddzTypeB = i;
                        mPointValues6.remove(0);
                        mPointValues6.add(new PointValue(i,0));
                        mPointValues5.remove(0);
                        mPointValues5.add(new PointValue(i,0));
                        tvRb2.setText(String.format("%.2f",fb1));
                        startTimeB = String.format("%.2f",(i-1)*0.05);
                        Log.e("========b",startTimeB);
                    }
                }else{
                    startTypeB=0;
                }
                fb2 = fb1;
            }
            if(endType==0){
                if(xsys.bijiao(xsys.xiaoshu(fb3+""),xsys.xiaoshu("1"))&&xsys.bijiao(xsys.xiaoshu("1"),xsys.xiaoshu(fb1+""))){
                    Log.e("qishidian==2",fb1+","+i);
                    //endType = 1;
                    mPointValues5.remove(0);
                    if(type==0){
                        mPointValues5.add(new PointValue(i-1,0));
                    }else{
                        mPointValues5.add(new PointValue((i-1),0));
                    }
                    tvRb2.setText(String.format("%.2f",fb3));
                    //startTimeA = String.format("%.2f",(i-1)*0.05);
                    //Log.e("========",startTimeA);
                }
                fb3 = fb1;
            }
            if(i==gddzTypeB+50){//&&i<gddzTypeA+15
                //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
                //gddzTypeA=gddzTypeA+1;
                //Log.e("guodudianzu=b=all",fb1+"");
                tvRb1.setText(String.format("%.2f",fb1));
            }
            mPointValues2.add(new PointValue(i,-Float.parseFloat(scoreList2.get(i))));
        }
        for(int i=0;i<scoreList3.size();i++){
            fc1 = Float.parseFloat(scoreList3.get(i));
            //Log.e(TAG,fc1+","+fc2);
            if(startTypeC==0){
                if(fc1>=fc2){
                    if(xsys.bijiao(xsys.xiaoshu(fc1+""),xsys.xiaoshu("1"))&&xsys.bijiao(xsys.xiaoshu("1"),xsys.xiaoshu(fc2+""))){
                        Log.e("qishidian==1",fc1+"");
                        startTypeC = 1;
                        gddzTypeC = i;
                        mPointValues6.remove(0);
                        mPointValues6.add(new PointValue(i,0));
                        mPointValues5.remove(0);
                        mPointValues5.add(new PointValue(i,0));
                        //tvRa2.setText(fa1+"");
                        tvRc2.setText(String.format("%.2f",fc1));
                        startTimeC = String.format("%.2f",(i-1)*0.05);
                        Log.e("========c",startTimeC);
                    }
                }else{
                    startTypeC=0;
                }
                fc2 = fc1;
            }
            if(endType==0){
                if(xsys.bijiao(xsys.xiaoshu(fc3+""),xsys.xiaoshu("1"))&&xsys.bijiao(xsys.xiaoshu("1"),xsys.xiaoshu(fc1+""))){
                    Log.e("qishidian==2",fc1+","+i);
                    //endType = 1;
                    mPointValues5.remove(0);
                    mPointValues5.add(new PointValue(i-1,0));
                    tvRc2.setText(String.format("%.2f",fc3));
                    //startTimeA = String.format("%.2f",(i-1)*0.05);
                    //Log.e("========",startTimeA);
                }
                fc3 = fc1;
            }
            if(i==gddzTypeC+50){//&&i<gddzTypeA+15
                //bigDecimalA = xsys.xiaoshuJia(bigDecimalA,xsys.xiaoshu(fa1+""));
                //gddzTypeA=gddzTypeA+1;
                //Log.e("guodudianzu=b=all",fb1+"");
                tvRc1.setText(String.format("%.2f",fc1));
            }
            mPointValues3.add(new PointValue(i,-Float.parseFloat(scoreList3.get(i))));
        }
        //if (mPointValues5!=null&&mPointValues6!=null){
        int x11 = (int)mPointValues5.get(0).getX();
        int x21 = (int)mPointValues6.get(0).getX();
        int cha1 = x11-x21;
        int cha2;
        if(cha1<0){
            cha2 = cha1*(-1);
        }else{
            cha2 = cha1;
        }
        tvZuoxia1.setText(String.format("%.2f", cha2*0.05*2));
        tvGdsj.setText(String.format("%.2f", cha2*0.05*2));
        if(StringUtils.noEmpty(startTimeA)&&StringUtils.noEmpty(startTimeB)&&StringUtils.noEmpty(startTimeC)){
            Log.e(TAG+"===1",startTimeA+","+startTimeB+","+startTimeC);
            tvBtqsj.setText(String.format("%.2f",xsys.bijiaoDaxiaoCha(xsys.xiaoshu(startTimeA),xsys.xiaoshu(startTimeB),xsys.xiaoshu(startTimeC))));
        }else{
            Log.e(TAG+"===2",startTimeA+","+startTimeB+","+startTimeC);
            tvBtqsj.setText("0.00");
        }
        //}

    }
    /**
     * 初始化设置
     */
    private void initLineChart(){
        sfType = type;
        //RA:黄
        Line line = new Line(mPointValues).setColor(Color.YELLOW);  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setPointRadius(1);
        lines.add(line);
        //RB：绿
        Line line2 = new Line(mPointValues2).setColor(Color.GREEN);//线的颜色
        List<Line> lines2 = new ArrayList<Line>();
        line2.setShape(ValueShape.CIRCLE);                  //折线上每个数据点的形状，设置为圆形
        line2.setCubic(false);                               //曲线是否圆滑
        line2.setFilled(false);  //是否填充曲线的面积
        line2.setHasLabels(false);//曲线的数据坐标是否加上备注
        line2.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line2.setPointRadius(1);
        lines2.add(line2);//第二条折线，实现寻峰。

        //RC：红
        Line line3 = new Line(mPointValues3).setColor(Color.RED);//直线的颜色
        List<Line> lines3 = new ArrayList<Line>();
        line3.setShape(ValueShape.CIRCLE);                  //折线上每个数据点的形状，设置为圆形
        line3.setCubic(false);                               //曲线是否圆滑
        line3.setFilled(false);  //是否填充曲线的面积
        line3.setHasLabels(false);//曲线的数据坐标是否加上备注
        line3.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line3.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line3.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line3.setPointRadius(1);
        lines3.add(line3);//第二条折线，实现寻峰。

        mPointValues5.add(new PointValue(1,0));
        Line line5 = new Line(mPointValues5).setColor(Color.BLACK);//将数据点填充到线上，并设置线的颜色为蓝色
        line5.setShape(ValueShape.CIRCLE);                  //折线上每个数据点的形状，设置为圆形
        line5.setCubic(true);                               //曲线是否圆滑
        line5.setFilled(false);  //是否填充曲线的面积
        line5.setHasLabels(false);//曲线的数据坐标是否加上备注
        line5.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line5.setHasLines(false);//是否用线显示。如果为false 则没有曲线只有点显示
        line5.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line5.setPointRadius(5);
        lines.add(line5);//第二条折线，实现寻峰。
        lines2.add(line5);
        lines3.add(line5);

        mPointValues6.add(new PointValue(3,0));
        Line line6 = new Line(mPointValues6).setColor(Color.WHITE);//将数据点填充到线上，并设置线的颜色为蓝色
        line6.setShape(ValueShape.CIRCLE);                  //折线上每个数据点的形状，设置为圆形
        line6.setCubic(true);                               //曲线是否圆滑
        line6.setFilled(false);  //是否填充曲线的面积
        line6.setHasLabels(false);//曲线的数据坐标是否加上备注
        line6.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line6.setHasLines(false);//是否用线显示。如果为false 则没有曲线只有点显示
        line6.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line6.setPointRadius(5);
        lines.add(line6);//第二条折线，实现寻峰。
        lines2.add(line6);
        lines3.add(line6);


        data.setLines(lines);
        data2.setLines(lines2);
        data3.setLines(lines3);


        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(0);//设置字体大小
        axisX.setMaxLabelChars(10); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        data2.setAxisXBottom(axisX); //x 轴在底部
        data3.setAxisXBottom(axisX); //x 轴在底部

        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextColor(Color.BLACK);
        axisY.setTextSize(0);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        data2.setAxisYLeft(axisY);  //Y轴设置在左边
        data3.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        axisY.setHasLines(false);//Y轴分割线

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 10);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);


        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= sfType;
        lineChart.setCurrentViewport(v);

        //设置行为属性，支持缩放、滑动以及平移
        lineChart2.setInteractive(true);
        lineChart2.setZoomType(ZoomType.HORIZONTAL);
        lineChart2.setMaxZoom((float) 10);//最大方法比例
        lineChart2.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart2.setLineChartData(data2);
        lineChart2.setVisibility(View.VISIBLE);


        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v2 = new Viewport(lineChart2.getMaximumViewport());
        v2.left = 0;
        v2.right= sfType;
        lineChart2.setCurrentViewport(v2);


        //设置行为属性，支持缩放、滑动以及平移
        lineChart22.setInteractive(true);//横向缩放
        lineChart22.setZoomType(ZoomType.HORIZONTAL);
        lineChart22.setMaxZoom((float) 10);//最大方法比例
        lineChart22.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart22.setLineChartData(data3);
        lineChart22.setVisibility(View.VISIBLE);


        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v22 = new Viewport(lineChart22.getMaximumViewport());
        v22.left = 0;
        v22.right= sfType;
        lineChart22.setCurrentViewport(v22);
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
    public void gbZuo(){
        if(gb==0){//(1,0)
            int x1 = (int)mPointValues5.get(0).getX();
            Log.e(TAG,x1+"");
            if(x1-1>=0){
                mPointValues5.remove(0);
                mPointValues5.add(new PointValue(x1-1,0));
            }
            int x11 = (int)mPointValues5.get(0).getX();
            int x21 = (int)mPointValues6.get(0).getX();
            int cha1 = x11-x21;
            int cha2;
            if(cha1<0){
                cha2 = cha1*(-1);
            }else{
                cha2 = cha1;
            }
            tvZuoxia1.setText(String.format("%.2f", cha2*0.05*2));

//                        int y1 = (int)mPointValues.get(x11).getY();
//                        int y2 = (int)mPointValues2.get(x11).getY();
//                        int y3 = (int)mPointValues3.get(x11).getY();
//                        tvRa2.setText(y1*(-1)-1+"");
//                        tvRb2.setText(y2*(-1)-1+"");
//                        tvRc2.setText(y3*(-1)-1+"");
            if(mPointValues!=null&&mPointValues.size()!=0&&!mPointValues.toString().equals("[]")){
                float y1 = mPointValues.get(x11).getY();
                tvRa2.setText(String.format("%.2f",y1*(-1)));
            }
            if(mPointValues2!=null&&mPointValues2.size()!=0&&!mPointValues2.toString().equals("[]")){
                float y2 = mPointValues2.get(x11).getY();
                tvRb2.setText(String.format("%.2f",y2*(-1)));
            }
            if(mPointValues3!=null&&mPointValues3.size()!=0&&!mPointValues3.toString().equals("[]")){
                float y3 = mPointValues3.get(x11).getY();
                tvRc2.setText(String.format("%.2f",y3*(-1)));
            }
        }else{//(3,0)
            int x2 = (int)mPointValues6.get(0).getX();
            Log.e(TAG,""+x2);
            if(x2-1>=0){
                mPointValues6.remove(0);
                mPointValues6.add(new PointValue(x2-1,0));
                // lineChart.setLineChartData(data);//点击缩放后是否刷新还原
            }
            int x11 = (int)mPointValues5.get(0).getX();
            int x21 = (int)mPointValues6.get(0).getX();
            int cha1 = x11-x21;
            int cha2;
            if(cha1<0){
                cha2 = cha1*(-1);
            }else{
                cha2 = cha1;
            }
            tvZuoxia1.setText(String.format("%.2f", cha2*0.05*2));

//                        int y1 = (int)mPointValues.get(x21).getY();
//                        int y2 = (int)mPointValues2.get(x21).getY();
//                        int y3 = (int)mPointValues3.get(x21).getY();
//                        tvRa2.setText(y1*(-1)-1+"");
//                        tvRb2.setText(y2*(-1)-1+"");
//                        tvRc2.setText(y3*(-1)-1+"");
            if(mPointValues!=null&&mPointValues.size()!=0&&!mPointValues.toString().equals("[]")){
                float y1 = mPointValues.get(x21).getY();
                tvRa2.setText(String.format("%.2f",y1*(-1)));
            }
            if(mPointValues2!=null&&mPointValues2.size()!=0&&!mPointValues2.toString().equals("[]")){
                float y2 = mPointValues2.get(x21).getY();
                tvRb2.setText(String.format("%.2f",y2*(-1)));
            }
            if(mPointValues3!=null&&mPointValues3.size()!=0&&!mPointValues3.toString().equals("[]")){
                float y3 = mPointValues3.get(x21).getY();
                tvRc2.setText(String.format("%.2f",y3*(-1)));
            }
        }

        lineChart.setLineChartData(data);//点击缩放后是否刷新还原
        lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
        lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
    }
    public void gbYou(){
        if(gb==0){//(1,0)
            int x1 = (int)mPointValues5.get(0).getX();
            Log.e(TAG,x1+"");
            if(x1<type-2){
                mPointValues5.remove(0);
                mPointValues5.add(new PointValue(x1+1,0));
//                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
//                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
//                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
            }
            int x11 = (int)mPointValues5.get(0).getX();
            int x21 = (int)mPointValues6.get(0).getX();
            int cha1 = x11-x21;
            int cha2;
            if(cha1<0){
                cha2 = cha1*(-1);
            }else{
                cha2 = cha1;
            }
            tvZuoxia1.setText(String.format("%.2f", cha2*0.05*2));

//                    int y1 = (int)mPointValues.get(x11).getY();
//                    int y2 = (int)mPointValues2.get(x11).getY();
            //Log.e(TAG,mPointValues3+"");
            if(mPointValues!=null&&mPointValues.size()!=0&&!mPointValues.toString().equals("[]")){
                float y1 = mPointValues.get(x11).getY();
                tvRa2.setText(String.format("%.2f",y1*(-1)));
            }
            if(mPointValues2!=null&&mPointValues2.size()!=0&&!mPointValues2.toString().equals("[]")){
                float y2 = mPointValues2.get(x11).getY();
                tvRb2.setText(String.format("%.2f",y2*(-1)));
            }
            if(mPointValues3!=null&&mPointValues3.size()!=0&&!mPointValues3.toString().equals("[]")){
                float y3 = mPointValues3.get(x11).getY();
                tvRc2.setText(String.format("%.2f",y3*(-1)));
            }

//                    tvRa2.setText(y1*(-1)-1+"");
//                    tvRb2.setText(y2*(-1)-1+"");

        }else{//(3,0)
            int x2 = (int)mPointValues6.get(0).getX();
            Log.e(TAG,""+x2);
            if(x2<type-2){
                mPointValues6.remove(0);
                mPointValues6.add(new PointValue(x2+1,0));
//                lineChart.setLineChartData(data);//点击缩放后是否刷新还原
//                lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
//                lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
            }
            int x11 = (int)mPointValues5.get(0).getX();
            int x21 = (int)mPointValues6.get(0).getX();
            int cha1 = x11-x21;
            int cha2;
            if(cha1<0){
                cha2 = cha1*(-1);
            }else{
                cha2 = cha1;
            }
            tvZuoxia1.setText(String.format("%.2f", cha2*0.05*2));

//                    int y1 = (int)mPointValues.get(x21).getY();
//                    int y2 = (int)mPointValues2.get(x21).getY();
//                    int y3 = (int)mPointValues3.get(x21).getY();
//                    tvRa2.setText(y1*(-1)-1+"");
//                    tvRb2.setText(y2*(-1)-1+"");
//                    tvRc2.setText(y3*(-1)-1+"");
            if(mPointValues!=null&&mPointValues.size()!=0&&!mPointValues.toString().equals("[]")){
                float y1 = mPointValues.get(x21).getY();
                tvRa2.setText(String.format("%.2f",y1*(-1)));
            }
            if(mPointValues2!=null&&mPointValues2.size()!=0&&!mPointValues2.toString().equals("[]")){
                float y2 = mPointValues2.get(x21).getY();
                tvRb2.setText(String.format("%.2f",y2*(-1)));
            }
            if(mPointValues3!=null&&mPointValues3.size()!=0&&!mPointValues3.toString().equals("[]")){
                float y3 = mPointValues3.get(x21).getY();
                tvRc2.setText(String.format("%.2f",y3*(-1)));
            }
        }
//                int x111 = (int)mPointValues5.get(0).getX();
//                int x211 = (int)mPointValues6.get(0).getX();
//                int cha11 = x111-x211;
//                int cha21;
//                if(cha11<0){
//                    cha21 = cha11*(-1);
//                }else{
//                    cha21 = cha11;
//                }
//                tvGdsj.setText(cha21*0.5+"");
        lineChart.setLineChartData(data);//点击缩放后是否刷新还原
        lineChart2.setLineChartData(data2);//点击缩放后是否刷新还原
        lineChart22.setLineChartData(data3);//点击缩放后是否刷新还原
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