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

import java.util.Locale;

public class HlChanpinshouceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivCpsc;
    private LinearLayout llShang;
    private LinearLayout llXia;
    private LinearLayout llFanhui;
    private LinearLayout llJszb;//技术指标
    private LinearLayout llXntd;//性能特点
    private LinearLayout llJxt;//接线图
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

        llJszb = findViewById(R.id.llHlCpscJszb);
        llXntd = findViewById(R.id.llHlCpscXntd);
        llJxt = findViewById(R.id.llHlCpscCsyujx);

        llShang.setOnClickListener(this);
        llXia.setOnClickListener(this);
        llFanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llHlCpscShang){//上一页
//            if(type==1){
//                type=2;
//            }else{
//                type=1;
//            }
//            //Log.e("===",type+"");
//            ChanpinShouce chanpinShouce = new ChanpinShouce();
//            chanpinShouce.qiehuanHl(HlChanpinshouceActivity.this,type,ivCpsc);
            Chanpinshouce chanpinshouce = new Chanpinshouce();
            chanpinshouce.xianOryin(0,llJszb,llXntd,llJxt);
        } else if (v.getId() == R.id.llHlCpscXia) {//下一页
//            if(type==1){
//                type=2;
//            }else{
//                type=1;
//            }
//            //Log.e("===",type+"");
//            ChanpinShouce chanpinShouce = new ChanpinShouce();
//            chanpinShouce.qiehuanHl(HlChanpinshouceActivity.this,type,ivCpsc);
            Chanpinshouce chanpinshouce = new Chanpinshouce();
            chanpinshouce.xianOryin(1,llJszb,llXntd,llJxt);
        }else if (v.getId() == R.id.llHlCpscFanhui) {//返回
            finish();
        }
    }
}