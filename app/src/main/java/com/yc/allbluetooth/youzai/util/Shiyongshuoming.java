package com.yc.allbluetooth.youzai.util;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Date:2023/6/14 15:21
 * author:jingyu zheng
 */
public class Shiyongshuoming {
    public void Xianshiyincang(LinearLayout ll1,LinearLayout ll2,LinearLayout ll3,LinearLayout ll4,LinearLayout ll5,int type){
        int ll10 = ll1.getVisibility();
        int ll20 = ll2.getVisibility();
        int ll30 = ll3.getVisibility();
        int ll40 = ll4.getVisibility();
        int ll50 = ll5.getVisibility();
        if(type == 0){//0：下一页
            if(ll10==0){//0：目前显示的
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }else if(ll20==0){
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
            }else if(ll30==0){
                ll3.setVisibility(View.GONE);
                ll4.setVisibility(View.VISIBLE);
            }else if(ll40==0){
                ll4.setVisibility(View.GONE);
                ll5.setVisibility(View.VISIBLE);
            }else if(ll50==0){
                ll5.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }
        }else{
            if(ll10==0){//0：目前显示的
                ll1.setVisibility(View.GONE);
                ll5.setVisibility(View.VISIBLE);
            }else if(ll20==0){
                ll2.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
            }else if(ll30==0){
                ll3.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
            }else if(ll40==0){
                ll4.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
            }else if(ll50==0){
                ll5.setVisibility(View.GONE);
                ll4.setVisibility(View.VISIBLE);
            }
        }
    }
}
