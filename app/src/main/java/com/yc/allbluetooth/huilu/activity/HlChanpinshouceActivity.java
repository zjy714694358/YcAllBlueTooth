package com.yc.allbluetooth.huilu.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.huilu.util.Chanpinshouce;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.Locale;

public class HlChanpinshouceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCpsc;
    private LinearLayout llShang;
    private LinearLayout llXia;
    private LinearLayout llFanhui;
    private LinearLayout llJszb100;//技术指标
    private LinearLayout llXntd100;//性能特点
    private LinearLayout llJxt100;//接线图
    private LinearLayout llJszb200;//技术指标
    private LinearLayout llXntd200;//性能特点
    private LinearLayout llJxt200;//接线图
    int type = 1;
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
        setContentView(R.layout.activity_hl_chanpinshouce);
        ActivityCollector.addActivity(this);
        Config.ymType = "huiluCpsc";
        initView();
    }
    public void initView(){
        ivCpsc = findViewById(R.id.ivHlCpsc);
        llShang = findViewById(R.id.llHlCpscShang);
        llXia = findViewById(R.id.llHlCpscXia);
        llFanhui = findViewById(R.id.llHlCpscFanhui);

        llJszb100 = findViewById(R.id.llHlCpscJszb100);
        llXntd100 = findViewById(R.id.llHlCpscXntd100);
        llJxt100 = findViewById(R.id.llHlCpscCsyujx100);
        llJszb200 = findViewById(R.id.llHlCpscJszb200);
        llXntd200 = findViewById(R.id.llHlCpscXntd200);
        llJxt200 = findViewById(R.id.llHlCpscCsyujx200);

        llShang.setOnClickListener(this);
        llXia.setOnClickListener(this);
        llFanhui.setOnClickListener(this);
        if(StringUtils.isEquals(Config.yqlx,"39")){
            llJszb100.setVisibility(View.VISIBLE);
            llXntd100.setVisibility(View.GONE);
            llJxt100.setVisibility(View.GONE);
            llJszb200.setVisibility(View.GONE);
            llXntd200.setVisibility(View.GONE);
            llJxt200.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llHlCpscShang){//上一页
            Chanpinshouce chanpinshouce = new Chanpinshouce();
            chanpinshouce.xianOryin(0,llJszb100,llXntd100,llJxt100,llJszb200,llXntd200,llJxt200);
        } else if (v.getId() == R.id.llHlCpscXia) {//下一页
            Chanpinshouce chanpinshouce = new Chanpinshouce();
            chanpinshouce.xianOryin(1,llJszb100,llXntd100,llJxt100,llJszb200,llXntd200,llJxt200);
        }else if (v.getId() == R.id.llHlCpscFanhui) {//返回
            finish();
        }
    }
}