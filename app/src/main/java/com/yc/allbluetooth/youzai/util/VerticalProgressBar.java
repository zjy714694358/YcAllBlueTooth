package com.yc.allbluetooth.youzai.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Date:2023/6/14 8:20
 * author:jingyu zheng
 */
public class VerticalProgressBar extends View {
    private Paint paint;// 画笔
    private int progress;// 进度值
    private int width;// 宽度值
    private int height;// 高度值
    private int color;

    public VerticalProgressBar(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalProgressBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();   //初始化
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth() - 1;// 宽度值
        height = getMeasuredHeight() - 1;// 高度值
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        设置矩形颜色
//        if (progress>=0&&progress<=30){
//            paint.setColor(Color.rgb(255, 215, 0));// 数值小于30 展示一种颜色
//            paint.setColor();
//        }
//        else if  (progress>=30&&progress<=60){
//            paint.setColor(Color.rgb(127, 255, 0));// 设置30-60情况的画笔颜色
//        }else
//
//            paint.setColor(Color.rgb(0, 255, 154));
        paint.setColor(color);

        canvas.drawRect(0, height - progress / 100f * height, width, height,
                paint);// 画矩形

        canvas.drawLine(0, 0, width, 0, paint);// 画顶边
        canvas.drawLine(0, 0, 0, height, paint);// 画左边
        canvas.drawLine(width, 0, width, height, paint);// 画右边
        canvas.drawLine(0, height, width, height, paint);// 画底边

        super.onDraw(canvas);
    }

    /** 设置progressbar进度 */
    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }
    public void setColor(int color) {
        this.color = color;
        //postInvalidate();
    }
}
