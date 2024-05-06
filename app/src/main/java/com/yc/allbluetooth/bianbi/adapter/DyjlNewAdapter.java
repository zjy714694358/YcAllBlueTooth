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
import com.yc.allbluetooth.bianbi.entity.DiaoyuejiluNew;

import java.util.List;

/**
 * @Author ZJY
 * @Date 2024/5/6 9:27
 */
public class DyjlNewAdapter extends BaseAdapter {
    private Context mContext;

    private List<DiaoyuejiluNew> mDatas;

    private LayoutInflater mInflater;


    public DyjlNewAdapter(Context mContext, List<DiaoyuejiluNew> mDatas) {
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
        DyjlNewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.bb_dyjlnew_list_item_layout, null);

            holder = new DyjlNewAdapter.ViewHolder();

            holder.tvBianhao = convertView.findViewById(R.id.tvBbDyjlListNewItemBianhao);
            holder.tvFenjie = convertView.findViewById(R.id.tvBbDyjlListNewItemFenjie);
            holder.tvBbzA = convertView.findViewById(R.id.tvBbDyjlListNewItemBbzA);
            holder.tvBbzB = convertView.findViewById(R.id.tvBbDyjlListNewItemBbzB);
            holder.tvBbzC = convertView.findViewById(R.id.tvBbDyjlListNewItemBbzC);
            holder.tvCeshishijian = convertView.findViewById(R.id.tvBbDyjlListNewItemCcsj);

            convertView.setTag(holder);

        } else {

            holder = (DyjlNewAdapter.ViewHolder) convertView.getTag();
        }

        final DiaoyuejiluNew dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.tvBianhao.setText(dataBean.getBianhao());
            holder.tvFenjie.setText(dataBean.getFenjie());
            holder.tvBbzA.setText(dataBean.getBbzA());
            holder.tvBbzB.setText(dataBean.getBbzB());
            holder.tvBbzC.setText(dataBean.getBbzC());
            holder.tvCeshishijian.setText(dataBean.getCeshishijian());

        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvBianhao;
        public TextView tvFenjie;
        public TextView tvBbzA;
        public TextView tvBbzB;
        public TextView tvBbzC;
        public TextView tvCeshishijian;
    }
}
