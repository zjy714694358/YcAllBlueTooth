package com.yc.allbluetooth.bianbi.util;

import android.widget.ImageView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * @Author ZJY
 * @Date 2024/4/29 14:17
 * 三相结尾页面图
 */
public class SanxiangEndImg {
    public void getImg(ImageView imageView,int num){
        if(num == 0){
            imageView.setImageResource(R.drawable.slt00);
        }else if (num == 1) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 2) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 3) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 4) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 5) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 6) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 7) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 8) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 9) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 10) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (num == 11) {
            imageView.setImageResource(R.drawable.slt01);
        }
    }
    public void getImg2(ImageView imageView,String str){
        if(StringUtils.isEquals(str,"00")){
            imageView.setImageResource(R.drawable.slt00);
        }else if (StringUtils.isEquals(str,"01")) {
            imageView.setImageResource(R.drawable.slt01);
        }else if (StringUtils.isEquals(str,"02")) {
            imageView.setImageResource(R.drawable.slt02);
        }else if (StringUtils.isEquals(str,"03")) {
            imageView.setImageResource(R.drawable.slt03);
        }else if (StringUtils.isEquals(str,"04")) {
            imageView.setImageResource(R.drawable.slt04);
        }else if (StringUtils.isEquals(str,"05")) {
            imageView.setImageResource(R.drawable.slt05);
        }else if (StringUtils.isEquals(str,"06")) {
            imageView.setImageResource(R.drawable.slt06);
        }else if (StringUtils.isEquals(str,"07")) {
            imageView.setImageResource(R.drawable.slt07);
        }else if (StringUtils.isEquals(str,"08")) {
            imageView.setImageResource(R.drawable.slt08);
        }else if (StringUtils.isEquals(str,"09")) {
            imageView.setImageResource(R.drawable.slt09);
        }else if (StringUtils.isEquals(str,"0A")) {
            imageView.setImageResource(R.drawable.slt10);
        }else if (StringUtils.isEquals(str,"0B")) {
            imageView.setImageResource(R.drawable.slt11);
        }
    }
}
