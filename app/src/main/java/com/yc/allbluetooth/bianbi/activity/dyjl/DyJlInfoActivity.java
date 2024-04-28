package com.yc.allbluetooth.bianbi.activity.dyjl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.adapter.DyjlInfoAdapter;
import com.yc.allbluetooth.bianbi.entity.DyjlInfo;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DyJlInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private TextView tvCsjl;
    private TextView tvDayin;
    private TextView tvDaochu;
    private TextView tvFanhui;
    List<DyjlInfo>mDatas = new ArrayList<>();
    DyjlInfoAdapter dyjlInfoAdapter;
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
        setContentView(R.layout.activity_dy_jl_info);
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        lv = findViewById(R.id.lvBbDyjlInfo);
        tvCsjl = findViewById(R.id.tvBbDyjlInfoCsjl);
        tvDayin = findViewById(R.id.tvBbDyjlInfoDayin);
        tvDaochu = findViewById(R.id.tvBbDyjlInfoDaochu);
        tvFanhui = findViewById(R.id.tvBbDyjlInfoFanhui);
        tvDayin.setOnClickListener(this);
        tvDaochu.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        for(int i=1;i<32;i++){
            DyjlInfo dyjlInfo = new DyjlInfo();
            dyjlInfo.setFenjie(i);
            dyjlInfo.setBbzA(i+".000");
            dyjlInfo.setBbzB(i+".001");
            dyjlInfo.setBbzC(i+".002");
            dyjlInfo.setWucha(i+".0001");
            mDatas.add(dyjlInfo);
        }
        dyjlInfoAdapter = new DyjlInfoAdapter(DyJlInfoActivity.this,mDatas);
        lv.setAdapter(dyjlInfoAdapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(DyJlInfoActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvBbDyjlInfoDayin){//打印

        } else if (v.getId() == R.id.tvBbDyjlInfoDaochu) {//导出

        } else if (v.getId() == R.id.tvBbDyjlInfoFanhui) {//返回
            finish();
        }
    }
}