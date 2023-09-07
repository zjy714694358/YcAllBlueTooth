package com.yc.allbluetooth.std.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2023/7/26 9:29
 * author:jingyu zheng
 */
public class CsFf {
    /**
     *
     * @param fragment  所在Fragment
     * @param str   当前显示内容
     * @param tvCsfs    测试方法控件
     * @param tvDl  电流控件
     * @param tvDz  电流下方电阻控件
     * @param tvCsxb    测试相别控件
     * @param llLxdz    零线电阻整体LinearLayout
     * @param tvLxdz    零线电阻控件
     * @param llZc  助磁整体LinearLayout
     * @param tvzc  助磁控件
     * @param ivCsxb    测试相别箭头ImageView(三通道隐藏)
     */
    public void csfs(Fragment fragment, String str, TextView tvCsfs, TextView tvDl, TextView tvDz, TextView tvCsxb,
                     LinearLayout llLxdz, TextView tvLxdz, LinearLayout llZc, TextView tvzc, ImageView ivCsxb){
        if(StringUtils.isEquals(fragment.getString(R.string.zzcs_santongdao_ceshi),str)){
            tvCsfs.setText(fragment.getString(R.string.zzcs_yn_ceshi));
            init(1,tvDl,tvDz);
            tvCsxb.setText("A0");
            llLxdz.setVisibility(View.GONE);
            tvLxdz.setText("OFF");
            llZc.setVisibility(View.GONE);
            tvzc.setText("OFF");
            ivCsxb.setVisibility(View.VISIBLE);
        }else if(StringUtils.isEquals(fragment.getString(R.string.zzcs_yn_ceshi),str)){
            tvCsfs.setText(fragment.getString(R.string.zzcs_dy_ceshi));
            init(1,tvDl,tvDz);
            tvCsxb.setText("ab");
            llLxdz.setVisibility(View.GONE);
            tvLxdz.setText("OFF");
            llZc.setVisibility(View.VISIBLE);
            ivCsxb.setVisibility(View.VISIBLE);
        }else if(StringUtils.isEquals(fragment.getString(R.string.zzcs_dy_ceshi),str)){
            tvCsfs.setText(fragment.getString(R.string.zzcs_santongdao_ceshi));
            init(0,tvDl,tvDz);
            tvCsxb.setText("ABC0");
            llLxdz.setVisibility(View.VISIBLE);
            llZc.setVisibility(View.GONE);
            tvzc.setText("OFF");
            ivCsxb.setVisibility(View.INVISIBLE);
        }
    }
    public void init(int type,TextView tvCsDl,TextView tvCsDz){
        if(type==0){
            if(StringUtils.isEquals("31", Config.yqlx)){
                tvCsDl.setText("5A+5A");
                tvCsDz.setText("（2mΩ~1.2Ω）");
            }else if(StringUtils.isEquals("34", Config.yqlx)){
                tvCsDl.setText("10A+10A");
                tvCsDz.setText("（1mΩ~0.6Ω）");
            }else if(StringUtils.isEquals("35", Config.yqlx)){
                tvCsDl.setText("20A+20A");
                tvCsDz.setText("（0.5mΩ~0.3Ω）");
            }else if(StringUtils.isEquals("36", Config.yqlx)){
                tvCsDl.setText("25A+25A");
                tvCsDz.setText("（0.4mΩ~0.1Ω）");
            }
        }else if(type==1){
            if(StringUtils.isEquals("31", Config.yqlx)){
                tvCsDl.setText("10A");
                tvCsDz.setText("（1mΩ~2Ω）");
            }else if(StringUtils.isEquals("34", Config.yqlx)){
                tvCsDl.setText("20A");
                tvCsDz.setText("（0.5mΩ~1Ω）");
            }else if(StringUtils.isEquals("35", Config.yqlx)){
                tvCsDl.setText("40A");
                tvCsDz.setText("（0.25mΩ~0.5Ω）");
            }else if(StringUtils.isEquals("36", Config.yqlx)){
                tvCsDl.setText("50A");
                tvCsDz.setText("（0.2mΩ~0.2Ω）");
            }
        }

    }
}
