package com.yc.allbluetooth.bianbi.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.yc.allbluetooth.R;


/**
 * @Author ZJY
 * @Date 2024/4/9 11:12
 */
public class ChanpinShouce {
    /**
     *
     * @param activity
     * @param i
     * @param imageView
     */
    public void qiehuan(Activity activity, int i, ImageView imageView){
        if(i == 1){
            Drawable drawable = activity.getResources().getDrawable(R.drawable.cpsc01);
            imageView.setImageDrawable(drawable);
        } else if (i == 2) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.cpsc02);
            imageView.setImageDrawable(drawable);
        }else if (i == 3) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.cpsc03);
            imageView.setImageDrawable(drawable);
        }else if (i == 4) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.cpsc04);
            imageView.setImageDrawable(drawable);
        }else if (i == 5) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.cpsc05);
            imageView.setImageDrawable(drawable);
        }else if (i == 6) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.cpsc06);
            imageView.setImageDrawable(drawable);
        }
    }
}
