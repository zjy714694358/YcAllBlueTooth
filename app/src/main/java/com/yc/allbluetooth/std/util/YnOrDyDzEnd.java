package com.yc.allbluetooth.std.util;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.math.BigDecimal;

/**
 * Date:2023/2/9 14:24
 * author:jingyu zheng
 */
public class YnOrDyDzEnd {
    public void initYn( LinearLayout llCsEr,LinearLayout llCsYi,TextView tvCsErXb,String xbStr,
                     TextView tvA0Dz,TextView tvA0DzDw,TextView tvA0ZsDz,TextView tvA0ZsDzDw,TextView tvB0Dz,TextView tvB0DzDw,TextView tvB0ZsDz,TextView tvB0ZsDzDw,
                     TextView tvC0Dz,TextView tvC0DzDw,TextView tvC0ZsDz,TextView tvC0ZsDzDw,TextView tvBphl){
        if(StringUtils.noEmpty(Config.ynA0Csdz)&&StringUtils.noEmpty(Config.ynB0Csdz)&&StringUtils.noEmpty(Config.ynC0Csdz)){
            llCsEr.setVisibility(View.VISIBLE);
            llCsYi.setVisibility(View.GONE);
            tvCsErXb.setText(xbStr);
            tvA0Dz.setText(Config.ynA0Csdz);
            tvA0DzDw.setText(Config.ynA0CsdzDw);
            tvA0ZsDz.setText(Config.ynA0CsZsdz);
            tvA0ZsDzDw.setText(Config.ynA0CsZsdzDw);
            tvB0Dz.setText(Config.ynB0Csdz);
            tvB0DzDw.setText(Config.ynB0CsdzDw);
            tvB0ZsDz.setText(Config.ynB0CsZsdz);
            tvB0ZsDzDw.setText(Config.ynB0CsZsdzDw);
            tvC0Dz.setText(Config.ynC0Csdz);
            tvC0DzDw.setText(Config.ynC0CsdzDw);
            tvC0ZsDz.setText(Config.ynC0CsZsdz);
            tvC0ZsDzDw.setText(Config.ynC0CsZsdzDw);
            if(!StringUtils.isEquals(Config.ynA0Csdz,"0.000") && !StringUtils.isEquals(Config.ynB0Csdz,"0.000") && !StringUtils.isEquals(Config.ynC0Csdz,"0.000")){
                XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                BigDecimal b1 = xsys.xiaoshu(Config.ynA0Csdz);
                BigDecimal b2 = xsys.xiaoshu(Config.ynB0Csdz);
                BigDecimal b3 = xsys.xiaoshu(Config.ynC0Csdz);
                double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(xsys.bijiaoDaxiaoCha(b1,b2,b3),xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshuJia(b1,b2),b3),xsys.xiaoshu("3"))),xsys.xiaoshu("100"));
                tvBphl.setText(b0+"%");
            }else{
                tvBphl.setText("");
            }
        }
    }
    public void initDy( LinearLayout llCsEr,LinearLayout llCsYi,TextView tvCsErXb,String xbStr,
                        TextView tvA0Dz,TextView tvA0DzDw,TextView tvA0ZsDz,TextView tvA0ZsDzDw,TextView tvB0Dz,TextView tvB0DzDw,TextView tvB0ZsDz,TextView tvB0ZsDzDw,
                        TextView tvC0Dz,TextView tvC0DzDw,TextView tvC0ZsDz,TextView tvC0ZsDzDw,TextView tvBphl){
        if(StringUtils.noEmpty(Config.dyAbCsdz)&&StringUtils.noEmpty(Config.dyBcCsdz)&&StringUtils.noEmpty(Config.dyCaCsdz)){
            llCsEr.setVisibility(View.VISIBLE);
            llCsYi.setVisibility(View.GONE);
            tvCsErXb.setText(xbStr);
            tvA0Dz.setText(Config.dyAbCsdz);
            tvA0DzDw.setText(Config.dyAbCsdzDw);
            tvA0ZsDz.setText(Config.dyAbCsZsdz);
            tvA0ZsDzDw.setText(Config.dyAbCsZsdzDw);
            tvB0Dz.setText(Config.dyBcCsdz);
            tvB0DzDw.setText(Config.dyBcCsdzDw);
            tvB0ZsDz.setText(Config.dyBcCsZsdz);
            tvB0ZsDzDw.setText(Config.dyBcCsZsdzDw);
            tvC0Dz.setText(Config.dyCaCsdz);
            tvC0DzDw.setText(Config.dyCaCsdzDw);
            tvC0ZsDz.setText(Config.dyCaCsZsdz);
            tvC0ZsDzDw.setText(Config.dyCaCsZsdzDw);
            if(!StringUtils.isEquals(Config.dyAbCsdz,"0.000") && !StringUtils.isEquals(Config.dyBcCsdz,"0.000") && !StringUtils.isEquals(Config.dyCaCsdz,"0.000")){
                XiaoshuYunsuan xsys = new XiaoshuYunsuan();
                BigDecimal b1 = xsys.xiaoshu(Config.dyAbCsdz);
                BigDecimal b2 = xsys.xiaoshu(Config.dyBcCsdz);
                BigDecimal b3 = xsys.xiaoshu(Config.dyCaCsdz);
                double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(xsys.bijiaoDaxiaoCha(b1,b2,b3),xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshuJia(b1,b2),b3),xsys.xiaoshu("3"))),xsys.xiaoshu("100"));
                tvBphl.setText(b0+"%");
            }else{
                tvBphl.setText("");
            }
        }
    }
}
