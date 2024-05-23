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
    public void xianOryin(LinearLayout ll1,LinearLayout ll2){
        int ll10 = ll1.getVisibility();
        int ll20 = ll2.getVisibility();
        if(ll10==0){//0：目前显示的
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
        }else if(ll20==0){
            ll2.setVisibility(View.GONE);
            ll1.setVisibility(View.VISIBLE);
        }
    }
}
