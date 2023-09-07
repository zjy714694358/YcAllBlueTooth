package com.yc.allbluetooth.std.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.ActivityCollector;

public class TestActivity extends AppCompatActivity {

    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        Config.ymType = "test";
        ActivityCollector.addActivity(this);
        Config.tiaozhuan = 1;//不再跳转
        if(Config.yemianType2==0){
            Config.yemianType2 = 1;
        }else{
            finish();
        }
        Log.e("test","进入test");
        Config.tiaozhuan = 1;//不再跳转
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });
    }
}