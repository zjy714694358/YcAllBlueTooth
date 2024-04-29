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
import android.widget.ImageView;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.HomeActivity;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.Locale;

public class SxBbEndActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBbA;
    private TextView tvZbA;
    private TextView tvJdA;
    private TextView tvWcA;
    private TextView tvBbB;
    private TextView tvZbB;
    private TextView tvJdB;
    private TextView tvWcB;
    private TextView tvBbC;
    private TextView tvZbC;
    private TextView tvJdC;
    private TextView tvWcC;
    private TextView tvFj;
    private TextView tvFjzhi;
    private TextView tvLjfs;
    private TextView tvChongce;
    private TextView tvBaocun;
    private TextView tvDayin;
    private TextView tvFanhui;
    private ImageView ivZbsl;//组别矢量图
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
        setContentView(R.layout.activity_sx_bb_end);
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        tvBbA = findViewById(R.id.tvSxBbEndBianbiA);
        tvZbA = findViewById(R.id.tvSxBbEndZabiA);
        tvJdA = findViewById(R.id.tvSxBbEndJiaoduA);
        tvWcA = findViewById(R.id.tvSxBbEndWuchaA);
        tvBbB = findViewById(R.id.tvSxBbEndBianbiB);
        tvZbB = findViewById(R.id.tvSxBbEndZabiB);
        tvJdB = findViewById(R.id.tvSxBbEndJiaoduB);
        tvWcB = findViewById(R.id.tvSxBbEndWuchaB);
        tvBbC = findViewById(R.id.tvSxBbEndBianbiC);
        tvZbC = findViewById(R.id.tvSxBbEndZabiC);
        tvJdC = findViewById(R.id.tvSxBbEndJiaoduC);
        tvWcC = findViewById(R.id.tvSxBbEndWuchaC);
        tvFj = findViewById(R.id.tvSxBbEndFenjie);
        tvFjzhi = findViewById(R.id.tvSxBbEndFenjiezhi);
        tvLjfs = findViewById(R.id.tvSxBbEndLjfs);
        tvChongce = findViewById(R.id.tvSxBbEndChongce);
        tvBaocun = findViewById(R.id.tvSxBbEndBaocun);
        tvDayin = findViewById(R.id.tvSxBbEndDayin);
        tvFanhui = findViewById(R.id.tvSxBbEndFanhui);
        ivZbsl = findViewById(R.id.ivSxBbEndZbsl);
        tvChongce.setOnClickListener(this);
        tvBaocun.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        //ivZbsl.setImageResource(R.drawable.slt01);
    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(SxBbEndActivity.this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvSxBbEndChongce){//重测

        } else if (v.getId() == R.id.tvSxBbEndBaocun) {//保存

        } else if (v.getId() == R.id.tvSxBbEndDayin) {//打印

        } else if (v.getId() == R.id.tvSxBbEndFanhui) {//返回
            finish();
            startActivity(new Intent(SxBbEndActivity.this, HomeActivity.class));
        }
    }
}