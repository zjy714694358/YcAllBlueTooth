package com.yc.allbluetooth.huilu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yc.allbluetooth.R;

public class HlDianzuceshi2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBianhao;
    private TextView tvDianliu;
    private TextView tvI;
    private TextView tvR;
    private TextView tvChongce;
    private TextView tvBaocun;
    private TextView tvDayin;
    private TextView tvTingzhi;
    private TextView tvFanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hl_dianzuceshi2);
        initView();
    }
    public void  initView(){
        tvBianhao = findViewById(R.id.tvHlDzcs2Bianhao);
        tvDianliu = findViewById(R.id.tvHlDzcs2Dianliu);
        tvI = findViewById(R.id.tvHlDzcs2I);
        tvR = findViewById(R.id.tvHlDzcs2R);
        tvChongce = findViewById(R.id.tvHlDzcs2Chongce);
        tvBaocun = findViewById(R.id.tvHlDzcs2Baocun);
        tvDayin = findViewById(R.id.tvHlDzcs2Dayin);
        tvTingzhi = findViewById(R.id.tvHlDzcs2Tingzhi);
        tvFanhui = findViewById(R.id.tvHlDzcs2Fanhui);
        tvChongce.setOnClickListener(this);
        tvBaocun.setOnClickListener(this);
        tvDayin.setOnClickListener(this);
        tvTingzhi.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvHlDzcs2Chongce){//重测

        } else if (view.getId() == R.id.tvHlDzcs2Baocun) {//保存

        }else if (view.getId() == R.id.tvHlDzcs2Dayin) {//打印

        }else if (view.getId() == R.id.tvHlDzcs2Tingzhi) {//停止

        }else if (view.getId() == R.id.tvHlDzcs2Fanhui) {//返回
            finish();
        }
    }
}