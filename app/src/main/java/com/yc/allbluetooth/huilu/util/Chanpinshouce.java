package com.yc.allbluetooth.huilu.util;

import android.view.View;
import android.widget.LinearLayout;

import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * @Date 2024/5/23 9:01
 * @Author ZJY
 * @email 714694358@qq.com
 */
public class Chanpinshouce {
    /**
     * 因为是只有两个，所以简写了上页下页业务逻辑
     * @param type 上一页：0；下一页：1
     * @param ll1 100产品手册
     * @param ll2 100性能特点
     * @param ll3 100接线图
     * @param ll4 200产品手册
     * @param ll5 200性能特点
     * @param ll6 200接线图
     */
    public void xianOryin(int type,LinearLayout ll1,LinearLayout ll2,LinearLayout ll3,LinearLayout ll4,LinearLayout ll5,LinearLayout ll6){

        if(StringUtils.isEquals(Config.yqlx,"39")){
            int ll10 = ll1.getVisibility();
            int ll20 = ll2.getVisibility();
            int ll30 = ll3.getVisibility();

            if(type==0){//上一页
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
            } else if (type==1) {//下一页
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
        } else if(StringUtils.isEquals(Config.yqlx,"3A")){
            int ll10 = ll4.getVisibility();
            int ll20 = ll5.getVisibility();
            int ll30 = ll6.getVisibility();

            if(type==0){//上一页
                if(ll10==0){//0：目前显示的
                    ll4.setVisibility(View.GONE);
                    ll5.setVisibility(View.GONE);
                    ll6.setVisibility(View.VISIBLE);
                }else if(ll20==0){
                    ll5.setVisibility(View.GONE);
                    ll6.setVisibility(View.GONE);
                    ll4.setVisibility(View.VISIBLE);
                }else if(ll30==0){
                    ll6.setVisibility(View.GONE);
                    ll4.setVisibility(View.GONE);
                    ll5.setVisibility(View.VISIBLE);
                }
            } else if (type==1) {//下一页
                if(ll10==0){//0：目前显示的
                    ll4.setVisibility(View.GONE);
                    ll6.setVisibility(View.GONE);
                    ll5.setVisibility(View.VISIBLE);
                }else if(ll20==0){
                    ll5.setVisibility(View.GONE);
                    ll4.setVisibility(View.GONE);
                    ll6.setVisibility(View.VISIBLE);
                }else if(ll30==0){
                    ll6.setVisibility(View.GONE);
                    ll5.setVisibility(View.GONE);
                    ll4.setVisibility(View.VISIBLE);
                }
            }
        }


    }
}
