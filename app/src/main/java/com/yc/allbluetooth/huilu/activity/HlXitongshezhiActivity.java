package com.yc.allbluetooth.huilu.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.huilu.adapter.HLXtszAdapter;
import com.yc.allbluetooth.huilu.fragment.HlChanpinshouceFragment;
import com.yc.allbluetooth.huilu.fragment.HlShijianjiaozhengFragment;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HlXitongshezhiActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private List<String> mTitle;// fr标题集合
    private List<Fragment> fragments;
    private TabLayout tabLayout;
    private ViewPager viewPager;


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
        setContentView(R.layout.activity_hl_xitongshezhi);
        ActivityCollector.addActivity(this);
        Config.ymType = "huiluXtsz";

        initView();
        initData();
    }
    private void initView() {
        tabLayout = findViewById(R.id.tabHlXtsz);
        viewPager = findViewById(R.id.vpHlXtsz);
    }
    private void initData() {
        mTitle = new ArrayList<>();
        fragments = new ArrayList<>();
        mTitle.clear();
        fragments.clear();
        mTitle.add(getString(R.string.chanpinshouce));//产品手册
        mTitle.add(getString(R.string.shijianjiaozheng));//时间校正
        fragments.add(new HlChanpinshouceFragment());
        fragments.add(new HlShijianjiaozhengFragment());

        HLXtszAdapter hlXtszAdapter = new HLXtszAdapter(getSupportFragmentManager(),mTitle,fragments);
        viewPager.setAdapter(hlXtszAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();//默认选中时间校正，下标从0开始计数（0：产品手册；1：时间校正）
        //去掉tabLayout标签的长按点击事件（长按显示标签文字）
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setLongClickable(false);
                // 针对android 26及以上需要设置setTooltipText为null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 可以设置null也可以是""
                    tab.view.setTooltipText(null);
                    // tab.view.setTooltipText("");
                }
            }
        }
        tabLayout.setSelectedTabIndicator(R.drawable.set_indicator);
    }
    @Override
    protected void onDestroy() {
        //Log.e(TAG,"onDestroy()");


        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }
}