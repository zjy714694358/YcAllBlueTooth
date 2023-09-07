package com.yc.allbluetooth.utils;

import android.text.Editable;
import android.widget.EditText;

/**
 * 作者：zjingyu on 2020-10-19 0019 09:53
 */
public class EditTextTextChanged {
    public void liangweixiaoshuInit(EditText editText,CharSequence s){
        //删除“.”后面超过2位后的数据
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 2+1);
                editText.setText(s);
                editText.setSelection(s.length()); //光标移到最后
            }
        }
        //如果"."在起始位置,则起始位置自动补0
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }
    }
    public void after(EditText editText, Editable s,CharSequence temp){
        int selectionStart = editText.getSelectionStart();
        //获取光标结束的位置
        int selectionEnd = editText.getSelectionEnd();
        //这里其实selectionStart  == selectionEnd
        // 大家可以把获取的位置放入beforeTextChanged 然后选择部分文字(选择部分位置用光标选择2个以上) 删除可以看到效果 我后面做实验
        IsChinese isChinese = new IsChinese();
        if (!isChinese.isAllchese(temp.toString())) {
            //Toast.makeText(DlzkSanxiangYiActivity.this, "只能输入3个字",Toast.LENGTH_SHORT).show();
            //删除部分字符串 为[x,y) 包含x位置不包含y
            //也就是说删除 位置x到y-1
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionEnd;
            //这里我修改了原作者 不需要这部
            //etCsry.setText(s);
            //如果你setText 传入s 的话会将编辑框的光标移到文本框最前面 所以这里我也注释了原作者
            //etCsry.setSelection(tempSelection);
        }
    }
    //使用如下
//    etDianpuGoodsInfoGuanliHeaderJiage.addTextChangedListener(new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            EditTextTextChanged editTextTextChanged = new EditTextTextChanged();
//            editTextTextChanged.liangweixiaoshuInit(etDianpuGoodsInfoGuanliHeaderJiage,s);
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    });

}
