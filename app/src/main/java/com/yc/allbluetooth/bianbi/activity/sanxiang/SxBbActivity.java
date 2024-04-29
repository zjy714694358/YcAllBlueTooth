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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.util.LianjieZubie;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.Locale;

public class SxBbActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEdgy;
    private EditText etEddy;
    private EditText etFjzs;
    private EditText etFjjj;
    private EditText etRwbh;
    private TextView tvLjzbYi;
    private TextView tvLjzbEr;
    private TextView tvLjzbSan;
    private LinearLayout llLjzbYi;
    private LinearLayout llLjzbEr;
    private LinearLayout llLjzbSan;
    private TextView tvCeshi;
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
        setContentView(R.layout.activity_sx_bb);
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        etEdgy = findViewById(R.id.etSxCsEdgy);
        etEddy = findViewById(R.id.etSxCsEddy);
        etFjzs = findViewById(R.id.etSxCsFjzs);
        etFjjj = findViewById(R.id.etSxCsFjjj);
        etRwbh = findViewById(R.id.etSxCsRwbh);
        tvLjzbYi = findViewById(R.id.tvSxCsLjzbYi);
        tvLjzbEr = findViewById(R.id.tvSxCsLjzbEr);
        tvLjzbSan = findViewById(R.id.tvSxCsLjzbSan);
        llLjzbYi = findViewById(R.id.llSxCsLjzbYiJiantou);
        llLjzbEr = findViewById(R.id.llSxCsLjzbErJiantou);
        llLjzbSan = findViewById(R.id.llSxCsLjzbSanJiantou);
        tvCeshi = findViewById(R.id.tvSxCsCeshi);
        tvFanhui = findViewById(R.id.tvSxCsFanhui);
        llLjzbYi.setOnClickListener(this);
        llLjzbEr.setOnClickListener(this);
        llLjzbSan.setOnClickListener(this);
        tvCeshi.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llSxCsLjzbYiJiantou){//联结组别一切换
            String tvzbYiStr = tvLjzbYi.getText().toString();
            LianjieZubie lianjieZubie = new LianjieZubie();
            lianjieZubie.getYi(SxBbActivity.this,tvLjzbYi,tvzbYiStr);
        } else if (v.getId() == R.id.llSxCsLjzbErJiantou) {//联结组别二切换
            String tvzbErStr = tvLjzbEr.getText().toString();
            LianjieZubie lianjieZubie = new LianjieZubie();
            lianjieZubie.getEr(SxBbActivity.this,tvLjzbEr,tvzbErStr);
        } else if (v.getId() == R.id.llSxCsLjzbSanJiantou) {//联结组别三切换
            String tvzbSanStr = tvLjzbSan.getText().toString();
            LianjieZubie lianjieZubie = new LianjieZubie();
            lianjieZubie.getSan(SxBbActivity.this,tvLjzbSan,tvzbSanStr);
        } else if (v.getId() == R.id.tvSxCsCeshi) {
            startActivity(new Intent(SxBbActivity.this, SxBbCeshiActivity.class));
        } else if (v.getId() == R.id.tvSxCsFanhui) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(SxBbActivity.this);
        super.onDestroy();
    }
}