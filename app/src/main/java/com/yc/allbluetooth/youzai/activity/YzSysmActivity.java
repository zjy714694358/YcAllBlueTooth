package com.yc.allbluetooth.youzai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.youzai.util.Shiyongshuoming;

import java.util.Locale;

public class YzSysmActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llXntd;
    private LinearLayout llJscs;
    private LinearLayout llWtJiJj;
    private LinearLayout llJxtYn;
    private LinearLayout llJxtSj;
    private TextView tvSyy;
    private TextView tvXyy;
    private TextView tvFanhui;
    private TextView tvTime;

    private String TAG = "YzSysmActivity";
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
                    if(StringUtils.isEquals(Config.ymType,"yzSysm")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "Sysm:"+msgStr);
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

        setContentView(R.layout.activity_yz_sysm);
        Config.ymType = "yzSysm";
        ActivityCollector.addActivity(this);

        initView();
        new TimeThread().start();
    }
    public void initView(){
        llXntd = findViewById(R.id.llYzSysmXntd);
        llJscs = findViewById(R.id.llYzSysmJscs);
        llWtJiJj = findViewById(R.id.llYzSysmCjgzAndJjff);
        llJxtYn = findViewById(R.id.llYzSysmJxtYn);
        llJxtSj = findViewById(R.id.llYzSysmJxtSanjiao);
        tvSyy = findViewById(R.id.tvYzSysmSyy);
        tvXyy = findViewById(R.id.tvYzSysmXyy);
        tvFanhui = findViewById(R.id.tvYzSysmFanhui);
        tvTime = findViewById(R.id.tvYzSysmShijian);
        tvSyy.setOnClickListener(this);
        tvXyy.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
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
        Shiyongshuoming shiyongshuoming = new Shiyongshuoming();
        switch (v.getId()){
            case R.id.tvYzSysmSyy://1
                shiyongshuoming.Xianshiyincang(llXntd,llJscs,llWtJiJj,llJxtYn,llJxtSj,1);
                Log.e("====",llXntd.getVisibility()+","+llJscs.getVisibility());
                break;
            case R.id.tvYzSysmXyy://0
//                llXntd.setVisibility(View.GONE);
//                llJscs.setVisibility(View.VISIBLE);
                shiyongshuoming.Xianshiyincang(llXntd,llJscs,llWtJiJj,llJxtYn,llJxtSj,0);
                Log.e("====",llXntd.getVisibility()+","+llJscs.getVisibility());
                break;
            case R.id.tvYzSysmFanhui:
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //bleConnectUtil.setCallback(null);
        ActivityCollector.removeActivity(this);
    }
}