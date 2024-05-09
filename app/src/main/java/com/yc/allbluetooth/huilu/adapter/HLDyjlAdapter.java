package com.yc.allbluetooth.huilu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.entity.DiaoyuejiluNew;
import com.yc.allbluetooth.huilu.entity.HlDiaoyuejilu;

import java.util.List;

/**
 * @Author ZJY
 * @Date 2024/5/6 9:27
 */
public class HLDyjlAdapter extends BaseAdapter {
    private Context mContext;

    private List<HlDiaoyuejilu> mDatas;

    private LayoutInflater mInflater;


    public HLDyjlAdapter(Context mContext, List<HlDiaoyuejilu> mDatas) {
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
        HLDyjlAdapter.ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.hl_dyjl_list_item_layout, null);

            holder = new HLDyjlAdapter.ViewHolder();

            holder.tvBianhao = convertView.findViewById(R.id.tvHlDyjlListItemBianhao);
            holder.tvJilushijian = convertView.findViewById(R.id.tvHlDyjlListItemJilusj);
            holder.tvDlz = convertView.findViewById(R.id.tvHlDyjlListItemDlz);
            holder.tvDzz = convertView.findViewById(R.id.tvHlDyjlListItemDzz);

            convertView.setTag(holder);

        } else {

            holder = (HLDyjlAdapter.ViewHolder) convertView.getTag();
        }

        final HlDiaoyuejilu dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.tvBianhao.setText(dataBean.getBianhao());
            holder.tvJilushijian.setText(dataBean.getJilushijian());
            holder.tvDlz.setText(dataBean.getDlz());
            holder.tvDzz.setText(dataBean.getDzz());

        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvBianhao;
        public TextView tvJilushijian;
        public TextView tvDlz;
        public TextView tvDzz;

    }
}
