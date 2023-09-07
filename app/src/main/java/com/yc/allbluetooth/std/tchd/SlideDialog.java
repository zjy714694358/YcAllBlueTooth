package com.yc.allbluetooth.std.tchd;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.allbluetooth.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部滑动选择弹跳框
 */
public class SlideDialog extends Dialog {
    private boolean isCancelable = false;
    private boolean isBackCancelable = false;
    private Context mContext;   //上下文
    private List<String> list = new ArrayList<>(0); //数据
    private int selectPos; //默认选中位置
    private OnSelectListener mSelectListener; //监听

    public SlideDialog(@NonNull Context context, List<String> list, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.SlideDialog);
        this.mContext = context;
        this.isCancelable = isCancelable;
        this.isBackCancelable = isBackCancelable;
        this.list = list;
        this.selectPos = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置View
        setContentView(R.layout.select_slide_template);
        //设置点击物理返回键是否可关闭弹框
        setCancelable(isCancelable);
        //设置点击弹框外是否可关闭弹框
        setCanceledOnTouchOutside(isBackCancelable);
        //设置view显示位置
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        //初始化控件
        TextView tv_cancel = findViewById(R.id.tv_cancel);
        TextView tv_agree = findViewById(R.id.tv_agree);
        com.yc.allbluetooth.std.tchd.EasyPickerView pickerView = findViewById(R.id.pickerView);
        //取消点击事件
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                mSelectListener.onCancel();
                dismiss();
            }
        });
        //确认点击事件
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认
                mSelectListener.onAgree(list.get(selectPos));
                dismiss();
            }
        });
        //设置数据
        pickerView.setDataList(list);
        //监听数据
        pickerView.setOnScrollChangedListener(new com.yc.allbluetooth.std.tchd.EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                //滚动时选中项发生变化

            }

            @Override
            public void onScrollFinished(int curIndex) {
                //滚动结束
                selectPos = curIndex;

            }
        });

    }

    public interface OnSelectListener {
        //取消
        void onCancel();
        //确认
        void onAgree(String txt);
    }

    public void setOnSelectClickListener(OnSelectListener listener) {
        this.mSelectListener = listener;
    }
}
