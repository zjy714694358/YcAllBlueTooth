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
import android.widget.EditText;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

import java.util.Locale;

public class DxBbActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEdgy;
    private EditText etEddy;
    private EditText etFjzs;
    private EditText etFjjj;
    private EditText etRwbh;
    private TextView tvCeshi;
    private TextView tvFanhui;
    String TAG = "DxBbActivity";
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
        setContentView(R.layout.activity_dx_bb);
        ActivityCollector.addActivity(this);
        initView();
    }
    public void initView(){
        etEdgy = findViewById(R.id.etDxCsEdgy);
        etEddy = findViewById(R.id.etDxCsEddy);
        etFjzs = findViewById(R.id.etDxCsFjzs);
        etFjjj = findViewById(R.id.etDxCsFjjj);
        etRwbh = findViewById(R.id.etDxCsRwbh);
        tvCeshi = findViewById(R.id.tvDxCsCeshi);
        tvFanhui = findViewById(R.id.tvDxCsFanhui);

        tvCeshi.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvDxCsCeshi){
            Log.e("bianbi==","测试");
            startActivity(new Intent(DxBbActivity.this, DxBbCeshiActivity.class));
        }else if (v.getId() == R.id.tvDxCsFanhui ){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(DxBbActivity.this);
        super.onDestroy();
    }
}