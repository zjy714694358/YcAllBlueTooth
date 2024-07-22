package com.yc.allbluetooth.huilu.util;

import android.view.View;
import android.widget.LinearLayout;

/**
 * @Date 2024/5/23 9:01
 * @Author ZJY
 * @email 714694358@qq.com
 */
public class Chanpinshouce {
    /**
     * 因为是只有两个，所以简写了上页下页业务逻辑
     * @param ll1 产品手册
     * @param ll2 性能特点
     */
    public void xianOryin(int type,LinearLayout ll1,LinearLayout ll2,LinearLayout ll3){
        int ll10 = ll1.getVisibility();
        int ll20 = ll2.getVisibility();
        int ll30 = ll3.getVisibility();
        if(type==0){
            if(ll10==0){//0：目前显示的
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
            }else if(ll20==0){
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }else if(ll30==0){
                ll3.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }
        } else if (type==1) {
            if(ll10==0){//0：目前显示的
                ll1.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }else if(ll20==0){
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
            }else if(ll30==0){
                ll3.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }
        }

    }
}
