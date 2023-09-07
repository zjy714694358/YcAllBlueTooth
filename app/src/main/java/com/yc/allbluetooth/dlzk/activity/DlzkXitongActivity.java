package com.yc.allbluetooth.dlzk.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.Locale;

public class DlzkXitongActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llSjsz;
    private LinearLayout llTssz;
    private LinearLayout llTxcs;
    private TextView tvFanhui;
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

        setContentView(R.layout.activity_dlzk_xitong);
        Config.ymType = "dlzkXt";
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        llSjsz = findViewById(R.id.llDlzkXitongYiSjsz);
        llTssz = findViewById(R.id.llDlzkXitongYiTssz);
        llTxcs = findViewById(R.id.llDlzkXitongYiTxcs);
        tvFanhui = findViewById(R.id.tvDlzkXitongYiFanhui);
        llSjsz.setOnClickListener(this);
        llTssz.setOnClickListener(this);
        llTxcs.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.ymType = "dlzkXt";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llDlzkXitongYiSjsz:
                startActivity(new Intent(DlzkXitongActivity.this,DlzkXitongShijianShezhiActivity.class));
                break;
            case R.id.llDlzkXitongYiTxcs:
                //startActivity(new Intent(DlzkXitongActivity.this,DlzkXitongShijianShezhiActivity.class));
                break;
            case R.id.llDlzkXitongYiTssz:
                //startActivity(new Intent(DlzkXitongActivity.this,DlzkXitongTiaoshiShezhiActivity.class));
                break;
            case R.id.tvDlzkXitongYiFanhui:
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}