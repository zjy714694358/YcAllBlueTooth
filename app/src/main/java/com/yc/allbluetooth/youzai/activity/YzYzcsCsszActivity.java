package com.yc.allbluetooth.youzai.activity;

import static com.yc.allbluetooth.ble.BleConnectUtil.mBluetoothGattCharacteristic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.ble.BleConnectUtil;
import com.yc.allbluetooth.callback.BleConnectionCallBack;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dlzk.activity.DlzkHomeActivity;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;
import com.yc.allbluetooth.utils.EditTextTextChanged;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.SendUtil;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.youzai.util.CrcAll;
import com.yc.allbluetooth.youzai.util.CsszQiehuan;
import com.yc.allbluetooth.youzai.util.YzSendUtil;

import java.util.Locale;

public class YzYzcsCsszActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvLc;
    private ImageView ivLcYou;
    private TextView tvSc;
    private ImageView ivScYou;
    private ImageView ivLmdZuo;
    private TextView tvLmd;
    private ImageView ivLmdYou;
    private EditText etSpbh;
    private ImageView ivCsfjZuo;
    private TextView tvCsfjZuo;
    private TextView tvCsfjYou;
    private ImageView ivCsfjYou;
    private ImageView ivLjfsZuo;
    private TextView tvLjfs;
    private ImageView ivLjfsYou;
    private ImageView ivCsxsZuo;
    private TextView tvCsxs;
    private ImageView ivCsxsYou;
    private TextView tvKscd;
    private TextView tvFanhui;
    private TextView tvTime;
    int jlType = 0;

    private String TAG = "YzYzcsCsszActivity";

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
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
                    break;
                case Config.BLUETOOTH_GETDATA:
                    if (StringUtils.isEquals(Config.ymType, "yzYzcsCssz")) {
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "Cssz:" + msgStr);

//                        if(IndexOfAndSubStr.isIndexOf(msgStr,"FEEF")){
//                            newMsgStr = msgStr;
//                        }else {
//                            newMsgStr = newMsgStr + msgStr;
//                            Log.e(TAG, "Cssz00:"+newMsgStr);
//                        }
//                        Log.e(TAG, "Cssz0:"+newMsgStr.length());
//                        if(newMsgStr.length()==28884){
//                            Log.e(TAG, "Cssz1:"+newMsgStr);
//                        }else if(newMsgStr.length()==76884){
//                            Log.e(TAG, "Cssz2:"+newMsgStr);
//                        }
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
        if ("zh".equals(Config.zyType)) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);

        setContentView(R.layout.activity_yz_yzcs_cssz);
        Config.ymType = "yzYzcsCssz";
        ActivityCollector.addActivity(this);
        Log.e(TAG, "jin...........");
        initModel();
        initView();


        new TimeThread().start();
    }

    public void initModel() {

        bleConnectUtil = new BleConnectUtil(YzYzcsCsszActivity.this);
        if (!bleConnectUtil.isConnected() && StringUtils.noEmpty(bleConnectUtil.wsDeviceAddress)) {
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
                Log.e(TAG,Config.yqlx);
                if (StringUtils.isEquals("37", Config.yqlx)) {
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c0014006009070000000100020000000000000000000000000000000307fddf", Config.yzCrcTYpe), "");
                }else if(StringUtils.isEquals("38", Config.yqlx)){
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c000A006009070000000100020000000000000000000000000000000307fddf", Config.yzCrcTYpe), "");

                }

            }
        }, 1000);//1秒后执行Runnable中的run方法
    }

    public void initView() {
        tvLc = findViewById(R.id.tvYzYzcsCsszLiangcheng);
        if (StringUtils.isEquals("37", Config.yqlx)) {//04
            tvLc.setText("20Ω(1.0A)");
        } else if (StringUtils.isEquals("38", Config.yqlx)) {//05
            tvLc.setText("10Ω(2.0A)");
        }
        ivLcYou = findViewById(R.id.ivYzYzcsCsszLiangchengYou);
        tvSc = findViewById(R.id.tvYzYzcsCsszShichang);
        ivScYou = findViewById(R.id.ivYzYzcsCsszShichangYou);
        ivLmdZuo = findViewById(R.id.ivYzYzcsCsszLmdZuo);
        tvLmd = findViewById(R.id.tvYzYzcsCsszLmd);
        ivLmdYou = findViewById(R.id.ivYzYzcsCsszLmdYou);
        etSpbh = findViewById(R.id.etYzYzcsCsszSpbh);
        ivCsfjZuo = findViewById(R.id.ivYzYzcsCsszCsfjZuo);
        tvCsfjZuo = findViewById(R.id.tvYzYzcsCsszCsfjZuo);
        tvCsfjYou = findViewById(R.id.tvYzYzcsCsszCsfjYou);
        ivCsfjYou = findViewById(R.id.ivYzYzcsCsszCsfjYou);
        ivLjfsZuo = findViewById(R.id.ivYzYzcsCsszLjfsZuo);
        tvLjfs = findViewById(R.id.tvYzYzcsCsszLjfs);
        ivLjfsYou = findViewById(R.id.ivYzYzcsCsszLjfsYou);
        ivCsxsZuo = findViewById(R.id.ivYzYzcsCsszCsxsZuo);
        tvCsxs = findViewById(R.id.tvYzYzcsCsszCsxs);
        ivCsxsYou = findViewById(R.id.ivYzYzcsCsszCsxsYou);
        tvKscd = findViewById(R.id.tvYzYzcsCsszKscd);
        tvFanhui = findViewById(R.id.tvDlzkShujuFanhui);
        tvTime = findViewById(R.id.tvYzYzcsCsszTime);
        tvFanhui = findViewById(R.id.tvYzYzcsCsszFanhui);
        ivLcYou.setOnClickListener(this);
        ivScYou.setOnClickListener(this);
        ivLmdZuo.setOnClickListener(this);
        ivLmdYou.setOnClickListener(this);
        ivCsfjZuo.setOnClickListener(this);
        ivCsfjYou.setOnClickListener(this);
        ivLjfsZuo.setOnClickListener(this);
        ivLjfsYou.setOnClickListener(this);
        ivCsxsZuo.setOnClickListener(this);
        ivCsxsYou.setOnClickListener(this);
        tvKscd.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        etSpbh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE://输入框，完成按钮点击事件
                        String spbh = etSpbh.getText().toString();
                        String lcStr = tvLc.getText().toString();
                        String scStr = tvSc.getText().toString();
                        String lmdStr = tvLmd.getText().toString();
                        String fj1Str = tvCsfjZuo.getText().toString();
                        String fj2Str = tvCsfjYou.getText().toString();
                        //String spbhStr = etSpbh.getText().toString();
                        String csxsStr = tvCsxs.getText().toString();
                        String ljfsStr = tvLjfs.getText().toString();
                        Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj1Str) +
                                CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbh) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf");
                        sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj1Str) +
                                CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbh) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                        break;
                }
                return false;
            }
        });
        etSpbh.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }

            @NonNull
            @Override
            protected char[] getAcceptedChars() {
                return "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                //return "0123456789abcdefABCDEF".toCharArray();
            }
        });
        etSpbh.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
    }

    @Override
    public void onClick(View v) {
        String lcStr = tvLc.getText().toString();
        String scStr = tvSc.getText().toString();
        String lmdStr = tvLmd.getText().toString();
        String fj1Str = tvCsfjZuo.getText().toString();
        String fj2Str = tvCsfjYou.getText().toString();
        String spbhStr = etSpbh.getText().toString();
        String csxsStr = tvCsxs.getText().toString();
        String ljfsStr = tvLjfs.getText().toString();
        switch (v.getId()) {
            case R.id.ivYzYzcsCsszLiangchengYou://量程
                // CsszQiehuan.liangcheng1A(lcStr,tvLc);
                //Log.e(TAG,"feef"+Config.yzBenjiAddress+"211c00"+CsszQiehuan.liangcheng1A(lcStr,tvLc)+CsszQiehuan.getShichang(scStr)+CsszQiehuan.getLmd(lmdStr)+"0000"+CsszQiehuan.getCsfj(fj1Str)+
                // CsszQiehuan.getCsfj(fj2Str)+CsszQiehuan.getSpbh(spbhStr)+CsszQiehuan.getLjfs(ljfsStr)+CsszQiehuan.getCsxs(csxsStr)+"fddf");

                if (StringUtils.isEquals("37", Config.yqlx)) {//04
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.liangcheng1A(lcStr, tvLc) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj1Str) +
                            CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                } else if (StringUtils.isEquals("38", Config.yqlx)) {//05
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.liangcheng2A(lcStr, tvLc) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj1Str) +
                            CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                }

                break;
            case R.id.ivYzYzcsCsszShichangYou://时长
                // CsszQiehuan.shichang(scStr,tvSc);
                Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.shichang(scStr, tvSc) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj1Str) +
                        CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf");
                sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.shichang(scStr, tvSc) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj1Str) +
                        CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                break;
            case R.id.ivYzYzcsCsszLmdZuo://灵敏度左
                //CsszQiehuan.lingmindu(lmdStr,tvLmd,0);
                Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.lingmindu(lmdStr, tvLmd, 0) + "0000" +
                        CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf");
                sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.lingmindu(lmdStr, tvLmd, 0) + "0000" +
                        CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                break;
            case R.id.ivYzYzcsCsszLmdYou://灵敏度右
                //CsszQiehuan.lingmindu(lmdStr,tvLmd,1);
                Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.lingmindu(lmdStr, tvLmd, 1) + "0000" +
                        CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf");
                sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.lingmindu(lmdStr, tvLmd, 1) + "0000" +
                        CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                break;
            case R.id.ivYzYzcsCsszCsfjZuo://测试分接左
                if (jlType == 0) {//默认右，第一次点击左
                    tvCsfjZuo.setText(fj2Str);
                    tvCsfjYou.setText(fj1Str);
                    jlType = 1;//点一次
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj2Str) +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj2Str) +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                } else {//不是第一次点击左，大于1次
                    // CsszQiehuan.csfj(fj1Str,fj2Str,tvCsfjZuo,tvCsfjYou,0);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.csfj(fj1Str, fj2Str, tvCsfjZuo, tvCsfjYou, 0) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.csfj(fj1Str, fj2Str, tvCsfjZuo, tvCsfjYou, 0) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                }
                break;
            case R.id.ivYzYzcsCsszCsfjYou://测试分接右
                Log.e(TAG, fj1Str);
                if (StringUtils.isEquals(fj1Str, "01")) {
                    jlType = 0;
                    // CsszQiehuan.csfj(fj1Str,fj2Str,tvCsfjZuo,tvCsfjYou,1);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.csfj(fj1Str, fj2Str, tvCsfjZuo, tvCsfjYou, 1) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.csfj(fj1Str, fj2Str, tvCsfjZuo, tvCsfjYou, 1) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                } else {
                    if (jlType == 1) {//第一次点击右//&&StringUtils.isEquals("01",fj1Str)==false
                        tvCsfjZuo.setText(fj2Str);
                        tvCsfjYou.setText(fj1Str);
                        jlType = 0;//点一次
                        Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj2Str) +
                                CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf");
                        sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" + CsszQiehuan.getCsfj(fj2Str) +
                                CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) + CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                    } else {//不是第一次点击右，大于1次

                        // CsszQiehuan.csfj(fj1Str,fj2Str,tvCsfjZuo,tvCsfjYou,1);
                        Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                                CsszQiehuan.csfj(fj1Str, fj2Str, tvCsfjZuo, tvCsfjYou, 1) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                                CsszQiehuan.getCsxs(csxsStr) + "fddf");
                        sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                                CsszQiehuan.csfj(fj1Str, fj2Str, tvCsfjZuo, tvCsfjYou, 1) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                                CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                    }
                }

                break;
            case R.id.ivYzYzcsCsszLjfsZuo://连接方式左
                if (StringUtils.isEquals("ABC", csxsStr)) {
                    // CsszQiehuan.ljfs(ljfsStr,tvLjfs,0,tvCsxs,0);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 0, tvCsxs, 0) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 0, tvCsxs, 0) +
                            "0206" + "fddf", Config.yzCrcTYpe), "");
                } else {
                    //CsszQiehuan.ljfs(ljfsStr,tvLjfs,0,tvCsxs,1);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 0, tvCsxs, 1) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 0, tvCsxs, 1) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                }
                break;
            case R.id.ivYzYzcsCsszLjfsYou://连接方式右
                if (StringUtils.isEquals("ABC", csxsStr)) {
                    //CsszQiehuan.ljfs(ljfsStr,tvLjfs,1,tvCsxs,0);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 1, tvCsxs, 0) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 1, tvCsxs, 0) +
                            "0206" + "fddf", Config.yzCrcTYpe), "");
                } else {
                    //CsszQiehuan.ljfs(ljfsStr,tvLjfs,1,tvCsxs,1);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 1, tvCsxs, 1) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.ljfs(ljfsStr, tvLjfs, 1, tvCsxs, 1) +
                            CsszQiehuan.getCsxs(csxsStr) + "fddf", Config.yzCrcTYpe), "");
                }
                break;
            case R.id.ivYzYzcsCsszCsxsZuo://测试相数左
                if (StringUtils.isEquals(ljfsStr, "YN")) {
                    //CsszQiehuan.csxs(csxsStr,tvCsxs,0,0);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 0, 0) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 0, 0) + "fddf", Config.yzCrcTYpe), "");
                } else {
                    // CsszQiehuan.csxs(csxsStr,tvCsxs,1,0);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 1, 0) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 1, 0) + "fddf", Config.yzCrcTYpe), "");
                }
                break;
            case R.id.ivYzYzcsCsszCsxsYou://测试相数右
                if (StringUtils.isEquals(ljfsStr, "YN")) {
                    //CsszQiehuan.csxs(csxsStr,tvCsxs,0,1);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 0, 1) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 0, 1) + "fddf", Config.yzCrcTYpe), "");
                } else {
                    //CsszQiehuan.csxs(csxsStr,tvCsxs,1,1);
                    Log.e(TAG, "feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 1, 1) + "fddf");
                    sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "211c00" + CsszQiehuan.getLiangcheng(lcStr) + CsszQiehuan.getShichang(scStr) + CsszQiehuan.getLmd(lmdStr) + "0000" +
                            CsszQiehuan.getCsfj(fj1Str) + CsszQiehuan.getCsfj(fj2Str) + CsszQiehuan.getSpbh(spbhStr) + CsszQiehuan.getLjfs(ljfsStr) +
                            CsszQiehuan.csxs(csxsStr, tvCsxs, 1, 1) + "fddf", Config.yzCrcTYpe), "");
                }
                break;
            case R.id.tvYzYzcsCsszKscd://开始充电
                //fe ef 04 22 01 00 01 00 fd df
                sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "2201000100fddf", Config.yzCrcTYpe), "");
                Log.e(TAG, "feef" + Config.yzBenjiAddress + "2201000100fddf");
                finish();
                startActivity(new Intent(YzYzcsCsszActivity.this, YzYzcsChongdianActivity.class));
                //sendDataByBle("feef04a001000800fddf","");
                break;
            case R.id.tvYzYzcsCsszFanhui://返回
                sendDataByBle(CrcAll.crcAdd("feef" + Config.yzBenjiAddress + "e00000fddf", Config.yzCrcTYpe), "");
                finish();
                break;
        }
    }

    /**
     * 屏幕左下角时间显示，每隔一秒执行一次
     */
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
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
    protected void onResume() {
        super.onResume();
        //sendDataByBle(CrcAll.crcAdd("feef"+Config.yzBenjiAddress+"211c0014006009070000000100020000000000000000000000000000000307fddf",Config.yzCrcTYpe), "");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        //mHandler.removeCallbacksAndMessages(null);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        }
        if(bleConnectUtil.mBluetoothGatt!=null){
            bleConnectUtil.mBluetoothGatt.close();
        }
        bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }
//    private void getBatteryCharacteristic() {
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mBeaconBatteryStateReader = new BeaconBatteryStateReader(
//                        BeaconDetailsActivity.this,
//                        mBeacon.getMacAddress());
//                mBeaconBatteryStateReader.readBatteryState(BeaconDetailsActivity.this);
//            }
//        }, 100);
//    }
}