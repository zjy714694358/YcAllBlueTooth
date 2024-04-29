package com.yc.allbluetooth.bianbi.activity.sanxiang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.Locale;

public class SxBbCeshiActivity extends AppCompatActivity {

    private TextView tvCsdlA;
    private TextView tvCsdlB;
    private TextView tvCsdlC;
    private TextView tvYcdyA;
    private TextView tvYcdyB;
    private TextView tvYcdyC;
    private TextView tvEcdyA;
    private TextView tvEcdyB;
    private TextView tvEcdyC;
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
        setContentView(R.layout.activity_sx_bb_ceshi);
        ActivityCollector.addActivity(this);
        initView();
    }

    public void initView(){
        tvCsdlA = findViewById(R.id.tvSxBbCeshiCsDlA);
        tvCsdlB = findViewById(R.id.tvSxBbCeshiCsDlB);
        tvCsdlC = findViewById(R.id.tvSxBbCeshiCsDlC);
        tvYcdyA = findViewById(R.id.tvSxBbCeshiYcDyA);
        tvYcdyB = findViewById(R.id.tvSxBbCeshiYcDyB);
        tvYcdyC = findViewById(R.id.tvSxBbCeshiYcDyC);
        tvEcdyA = findViewById(R.id.tvSxBbCeshiEcDyA);
        tvEcdyB = findViewById(R.id.tvSxBbCeshiEcDyB);
        tvEcdyC = findViewById(R.id.tvSxBbCeshiEcDyC);
        tvFanhui = findViewById(R.id.tvSxCszhongFanhui);
        tvFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SxBbCeshiActivity.this,SxBbEndActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(SxBbCeshiActivity.this);
        super.onDestroy();
    }
}