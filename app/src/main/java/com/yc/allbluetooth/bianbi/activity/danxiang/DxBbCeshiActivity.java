package com.yc.allbluetooth.bianbi.activity.danxiang;

import androidx.appcompat.app.AppCompatActivity;

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

public class DxBbCeshiActivity extends AppCompatActivity {

    private TextView tvFj;
    private TextView tvCsdl;
    private TextView tvYcdy;
    private TextView tvEcdy;
    private TextView tvFanhui;
    String TAG = "DxBbCeshiActivity";

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
        setContentView(R.layout.activity_dx_bb_ceshi);
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        tvFj = findViewById(R.id.tvDxBbCeshiFenjie);
        tvCsdl = findViewById(R.id.tvDxBbCeshiCsDl);
        tvYcdy = findViewById(R.id.tvDxBbCeshiYcDy);
        tvEcdy = findViewById(R.id.tvDxBbCeshiEcDy);
        tvFanhui = findViewById(R.id.tvDxCszhongFanhui);
        tvFanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(DxBbCeshiActivity.this);
        super.onDestroy();
    }
}