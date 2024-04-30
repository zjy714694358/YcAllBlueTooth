package com.yc.allbluetooth.bianbi.activity.dyjl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.adapter.DyjlAdapter;
import com.yc.allbluetooth.bianbi.entity.Diaoyuejilu;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DyJlActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    private TextView tvQingchu;
    private TextView tvFanhui;
    List<Diaoyuejilu> mDatas = new ArrayList<>();
    private DyjlAdapter mAdapter;
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
        setContentView(R.layout.activity_dy_jl);
        ActivityCollector.addActivity(this);
        initView();
    }

    public void initView(){
        lv = findViewById(R.id.lvBbDyjl);
        tvQingchu = findViewById(R.id.tvBbDyjlQingchu);
        tvFanhui = findViewById(R.id.tvBbDyjlFanhui);
        tvQingchu.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        for(int i=0;i<10;i++){
            Diaoyuejilu diaoyuejilu = new Diaoyuejilu();
            diaoyuejilu.setId(i);
            diaoyuejilu.setCsjl("csjl:"+i);
            diaoyuejilu.setRwbh("rwbh:"+"123456789123456789");
            mDatas.add(diaoyuejilu);
        }

        mAdapter = new DyjlAdapter(DyJlActivity.this,mDatas);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diaoyuejilu diaoyuejilu = mDatas.get(position);
                Log.e("lv==Id",diaoyuejilu.getId()+","+diaoyuejilu.getCsjl()+","+diaoyuejilu.getRwbh());
                //startActivity(new Intent(YzSjcyActivity.this,YzSjcyInfoActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(DyJlActivity.this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvBbDyjlQingchu){//清除
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.tvBbDyjlFanhui) {//返回
            finish();
        }
    }
}