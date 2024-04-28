package com.yc.allbluetooth.bianbi.activity.danxiang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.HomeActivity;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;
import com.yc.allbluetooth.utils.CheckUtils;

import java.util.Locale;

public class DxBbEndActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFj;
    private TextView tvBb;
    private TextView tvZb;
    private TextView tvJx;
    private TextView tvWc;
    private TextView tvCc;
    private TextView tvBc;
    private TextView tvDy;
    private TextView tvFh;
    String TAG = "DxBbEndActivity";
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
        setContentView(R.layout.activity_dx_bb_end);
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        tvFj = findViewById(R.id.tvDxBbEndFenjie);
        tvBb = findViewById(R.id.tvDxBbEndBianbi);
        tvZb = findViewById(R.id.tvDxBbEndZabi);
        tvJx = findViewById(R.id.tvDxBbEndJixing);
        tvWc = findViewById(R.id.tvDxBbEndWucha);
        tvCc = findViewById(R.id.tvDxBbEndChongce);
        tvBc = findViewById(R.id.tvDxBbEndBaocun);
        tvDy = findViewById(R.id.tvDxBbEndDayin);
        tvFh = findViewById(R.id.tvDxBbEndFanhui);
        tvCc.setOnClickListener(this);
        tvBc.setOnClickListener(this);
        tvDy.setOnClickListener(this);
        tvFh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDxBbEndChongce){//重测

        } else if (v.getId() == R.id.tvDxBbEndBaocun) {//保存

        } else if (v.getId() == R.id.tvDxBbEndDayin) {//打印

        } else if (v.getId() == R.id.tvDxBbEndFanhui) {//返回
            finish();
            startActivity(new Intent(DxBbEndActivity.this, HomeActivity.class));
        }
    }
    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(DxBbEndActivity.this);
        super.onDestroy();
    }
}