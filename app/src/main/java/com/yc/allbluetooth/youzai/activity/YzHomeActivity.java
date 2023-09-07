package com.yc.allbluetooth.youzai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dtd10c.activity.Dtd10cHomeActivity;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.HexUtil;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;
import com.yc.allbluetooth.youzai.util.CsszQiehuan;

import java.util.Locale;

public class YzHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout llYzCs;
    private LinearLayout llSjCy;
    private LinearLayout llSjSz;
    private LinearLayout llSySm;
    private TextView tvVer;
    private TextView tvTime;

    private String TAG = "YzHomeActivity";
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
                    if(StringUtils.isEquals(Config.ymType,"yzHome")){
                        String msgStr = msg.obj.toString();
                        Log.e(TAG, "YzHome:"+msgStr);
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

        setContentView(R.layout.activity_yz_home);
        Config.ymType = "yzHome";
        ActivityCollector.addActivity(this);

        //Log.e(TAG,CsszQiehuan.getSpbh("12305"));
//        XiaoshuYunsuan xsys = new XiaoshuYunsuan();
//        Log.e(TAG, HexUtil.reverseHex("6300"));
//        Log.e(TAG,ShiOrShiliu.parseInt(HexUtil.reverseHex("6300"))+"");
//        Log.e(TAG,(float)xsys.xiaoshuCheng(xsys.xiaoshu(ShiOrShiliu.parseInt(HexUtil.reverseHex("6300"))+""),xsys.xiaoshu("0.01"))+"");

        initView();
        new TimeThread().start();
    }
    public void initView(){
        llYzCs = findViewById(R.id.llYzHomeYzCs);
        llSjCy = findViewById(R.id.llYzHomeSjCy);
        llSjSz = findViewById(R.id.llYzHomeSjSz);
        llSySm = findViewById(R.id.llYzHomeSySm);
        tvVer = findViewById(R.id.tvYzHomeVer);
        tvTime = findViewById(R.id.tvYzHomeTime);
        llYzCs.setOnClickListener(this);
        llSjCy.setOnClickListener(this);
        llSjSz.setOnClickListener(this);
        llSySm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llYzHomeYzCs:
                startActivity(new Intent(YzHomeActivity.this,YzYzcsCsszActivity.class));
                break;
            case R.id.llYzHomeSjCy:
                startActivity(new Intent(YzHomeActivity.this,YzSjcyActivity.class));
                break;
            case R.id.llYzHomeSjSz:
                startActivity(new Intent(YzHomeActivity.this,YzSjszActivity.class));
                break;
            case R.id.llYzHomeSySm:
                startActivity(new Intent(YzHomeActivity.this,YzSysmActivity.class));
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}