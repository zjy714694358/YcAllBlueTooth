package com.yc.allbluetooth.bianbi.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.activity.dyjl.DyJlInfoActivity;
import com.yc.allbluetooth.bianbi.entity.Diaoyuejilu;

import java.util.List;

/**
 * @Author ZJY
 * @Date 2024/4/10 9:27
 */
public class DyjlAdapter extends BaseAdapter {
    private Context mContext;

    private List<Diaoyuejilu> mDatas;

    private LayoutInflater mInflater;


    public DyjlAdapter(Context mContext, List<Diaoyuejilu> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        //Log.e("this.mContext==",this.mContext+"");
        mInflater = LayoutInflater.from(this.mContext);

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.bb_dyjl_list1_item_layout, null);

            holder = new ViewHolder();

            holder.tvId = convertView.findViewById(R.id.tvBbDyjlList1ItemId);
            holder.tvCsjl = convertView.findViewById(R.id.tvBbDyjlList1ItemCsjl);
            holder.tvRwbh = convertView.findViewById(R.id.tvBbDyjlList1ItemRwbh);
            holder.tvCk = convertView.findViewById(R.id.tvBbDyjlList1ItemChakan);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final Diaoyuejilu dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.tvId.setText(dataBean.getId()+"");
            holder.tvCsjl.setText(dataBean.getCsjl());
            holder.tvRwbh.setText(dataBean.getRwbh());


            ViewHolder finalHolder = holder;
            holder.tvCk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//查看
                    //String str = dataBean.getRwbh();
                    Log.e("---",dataBean.getId()+","+dataBean.getCsjl()+","+dataBean.getRwbh());
                    mContext.startActivity(new Intent(mContext, DyJlInfoActivity.class));
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvId;
        public TextView tvCsjl;
        public TextView tvRwbh;
        public TextView tvCk;

    }
}
