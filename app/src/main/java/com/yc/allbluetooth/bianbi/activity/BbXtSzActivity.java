package com.yc.allbluetooth.bianbi.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.fragment.ChangjiaTsFragment;
import com.yc.allbluetooth.bianbi.fragment.ChanpinScFragment;
import com.yc.allbluetooth.bianbi.fragment.ShijianJzFragment;
import com.yc.allbluetooth.bianbi.fragment.TongxunCsFragment;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.Locale;

public class BbXtSzActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llCpsc;
    private LinearLayout llSjjz;
    private LinearLayout llTxcs;
    private LinearLayout llCjts;
    private FrameLayout frameLayout;
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
        setContentView(R.layout.activity_bb_xt_sz);
        ActivityCollector.addActivity(this);
        Config.ymType = "bianbiXtsz";
        initView();
    }

    public void initView(){
        ChanpinScFragment chanpinScFragment = ChanpinScFragment.newInstance("", "");
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        //getSupportFragmentManager().beginTransaction().add(R.id.frameHome, zhizuCeshiFragment).commit();
        addFragemntToShow(0, chanpinScFragment);
        llCpsc = findViewById(R.id.llBbXtCpsc);
        llSjjz = findViewById(R.id.llBbXtSjjz);
        llTxcs = findViewById(R.id.llBbXtTxcs);
        llCjts = findViewById(R.id.llBbXtCjts);
        frameLayout = findViewById(R.id.frameBbXtSz);
        tvFanhui = findViewById(R.id.tvBbXtFanhui);
        llCpsc.setOnClickListener(this);
        llSjjz.setOnClickListener(this);
        llTxcs.setOnClickListener(this);
        llCjts.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(BbXtSzActivity.this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llBbXtCpsc){//产品手册
            llCpsc.setBackgroundResource(R.drawable.yuanjiao_bac_bacg_biankuang_jin);
            llSjjz.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llTxcs.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llCjts.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            ChanpinScFragment chanpinScFragment = ChanpinScFragment.newInstance("", "");
            addFragemntToShow(1, chanpinScFragment);
        } else if (v.getId() == R.id.llBbXtSjjz) {//时间校正
            llCpsc.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llSjjz.setBackgroundResource(R.drawable.yuanjiao_bac_bacg_biankuang_jin);
            llTxcs.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llCjts.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            ShijianJzFragment shijianJzFragment = ShijianJzFragment.newInstance("", "");
            addFragemntToShow(1, shijianJzFragment);
        } else if (v.getId() == R.id.llBbXtTxcs) {//通讯参数
            llCpsc.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llSjjz.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llTxcs.setBackgroundResource(R.drawable.yuanjiao_bac_bacg_biankuang_jin);
            llCjts.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            TongxunCsFragment tongxunCsFragment = TongxunCsFragment.newInstance("","");
            addFragemntToShow(1,tongxunCsFragment);
        } else if (v.getId() == R.id.llBbXtCjts) {//厂家调试
            llCpsc.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llSjjz.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llTxcs.setBackgroundResource(R.drawable.yuanjiao_bac_lv_biankuang_lv2);
            llCjts.setBackgroundResource(R.drawable.yuanjiao_bac_bacg_biankuang_jin);
            ChangjiaTsFragment changjiaTsFragment = ChangjiaTsFragment.newInstance("","");
            addFragemntToShow(1,changjiaTsFragment);
        } else if (v.getId() == R.id.tvBbXtFanhui) {//返回
            finish();
        }
    }
    /**
     * 隐藏所有已加入的fragment
     */
    private void setAllFragmentToHideen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < Config.fragmentList.size(); i++) {
            Fragment fragment = Config.fragmentList.get(i);
            if (fragment.isAdded()) {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 替换中间的fragment
     *
     * @param type 0是第一个，默认显示的；1代表不是首次进入程序，点击切换过
     * @param addedFragment
     */
    public void addFragemntToShow(int type, Fragment addedFragment) {
        if (null == addedFragment) {
            return;
        }
        if (type != 0) {
            // 隐藏所有fragment
            setAllFragmentToHideen();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 已经添加fragment
        if (addedFragment.isAdded()) {
            transaction.show(addedFragment);
        } else { // 新加入的fragment
            transaction.add(R.id.frameBbXtSz, addedFragment);
            Config.fragmentList.add(addedFragment);
        }
        transaction.commit();
    }
}