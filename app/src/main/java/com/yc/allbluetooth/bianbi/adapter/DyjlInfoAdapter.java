package com.yc.allbluetooth.bianbi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.entity.DyjlInfo;

import java.util.List;

/**
 * @Author ZJY
 * @Date 2024/4/10 13:46
 */
public class DyjlInfoAdapter extends BaseAdapter {
    private Context mContext;

    private List<DyjlInfo> mDatas;

    private LayoutInflater mInflater;


    public DyjlInfoAdapter(Context mContext, List<DyjlInfo> mDatas) {
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
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.bb_dyjl_list1_info_item_layout, null);

            holder = new ViewHolder();

            holder.tvFj = convertView.findViewById(R.id.tvBbDyjlList1InfoItemFenjie);
            holder.tvBbzA = convertView.findViewById(R.id.tvBbDyjlList1InfoItemBbzA);
            holder.tvBbzB = convertView.findViewById(R.id.tvBbDyjlList1InfoItemBbzB);
            holder.tvBbzC = convertView.findViewById(R.id.tvBbDyjlList1InfoItemBbzC);
            holder.tvWc = convertView.findViewById(R.id.tvBbDyjlList1InfoItemWucha);


            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final DyjlInfo dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.tvFj.setText(dataBean.getFenjie()+"");
            holder.tvBbzA.setText(dataBean.getBbzA());
            holder.tvBbzB.setText(dataBean.getBbzB());
            holder.tvBbzC.setText(dataBean.getBbzC());
            holder.tvWc.setText(dataBean.getWucha());

        }
        return convertView;
    }
    class ViewHolder {
        public TextView tvFj;
        public TextView tvBbzA;
        public TextView tvBbzB;
        public TextView tvBbzC;
        public TextView tvWc;

    }
}
