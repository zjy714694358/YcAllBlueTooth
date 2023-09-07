package com.yc.allbluetooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.entity.BlueTooth;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.List;

/**
 * Date:2022/11/3 14:19
 * author:jingyu zheng
 */
public class BlueToothListBaseAdapter extends BaseAdapter {
    private Context context;

    private List<BlueTooth> blueTooths;

    // 布局加载器
    private LayoutInflater layoutInflater;
    public BlueToothListBaseAdapter(Context context, List<BlueTooth> blueTooths) {
        this.context = context;
        this.blueTooths = blueTooths;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return blueTooths.size();
    }

    @Override
    public Object getItem(int position) {
        return blueTooths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.bluetooth_listview_item, null);

        // 取出布局加载器 加载等布局文件 控件ID
        TextView name = view.findViewById(R.id.tvBlueToothName);
        TextView type = view.findViewById(R.id.tvBlueToothType);

        // 布局加载器 加载等布局文件 控件和数据绑定
        if(StringUtils.noEmpty(blueTooths.get(position).getName())){
            name.setText(blueTooths.get(position).getName());
        }else{
            name.setText(blueTooths.get(position).getAddress());
        }
        type.setText(blueTooths.get(position).getType());

        //address.setText(blueTooths.get(position).getAddress());

        return view;
    }
}
