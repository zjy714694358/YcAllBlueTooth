package com.yc.allbluetooth.bianbi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.danxiang.DxBbActivity;
import com.yc.allbluetooth.bianbi.activity.sanxiang.SxBbActivity;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.GetTime;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivDx;
    private ImageView ivSx;
    private ImageView ivDyjl;
    private ImageView ivXtsz;
    private TextView tvTime;

    private static final int msgKey1 = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    tvTime.setText(GetTime.getTime(4));//年-月-日 时：分：秒
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

        setContentView(R.layout.activity_home);
        //Config.ymType = "dlzkHome";
        ActivityCollector.addActivity(this);
        //initModel();
        initView();
        new TimeThread().start();
    }
    public void initView(){
        ivDx = findViewById(R.id.ivHomeDxBianbi);
        ivSx = findViewById(R.id.ivHomeSxBianbi);
        ivDyjl = findViewById(R.id.ivHomeDyjl);
        ivXtsz = findViewById(R.id.ivHomeXtsz);
        tvTime = findViewById(R.id.tvHomeTime);
        ivDx.setOnClickListener(this);
        ivSx.setOnClickListener(this);
        ivDyjl.setOnClickListener(this);
        ivXtsz.setOnClickListener(this);
    }

    /**
     * 屏幕右下角时间显示，每隔一秒执行一次
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
        ActivityCollector.removeActivity(HomeActivity.this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivHomeDxBianbi){
            startActivity(new Intent(HomeActivity.this, DxBbActivity.class));
        } else if (v.getId() == R.id.ivHomeSxBianbi){
            startActivity(new Intent(HomeActivity.this, SxBbActivity.class));
        } else if (v.getId() == R.id.ivHomeDyjl) {
            startActivity(new Intent(HomeActivity.this, com.yc.allbluetooth.bianbi.activity.dyjl.DyJlActivity.class));
        } else if (v.getId() == R.id.ivHomeXtsz) {
            startActivity(new Intent(HomeActivity.this, XtSzActivity.class));
        }
    }
}