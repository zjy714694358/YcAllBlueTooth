package com.yc.allbluetooth.huilu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.huilu.entity.HlDiaoyuejilu;
import com.yc.allbluetooth.huilu.util.HlDlOrDzDw;
import com.yc.allbluetooth.poi.PoiUtils;
import com.yc.allbluetooth.utils.BytesToHexString;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            holder.tvDaochu = convertView.findViewById(R.id.tvHlDyjlListItemDaochu);

            convertView.setTag(holder);

        } else {

            holder = (HLDyjlAdapter.ViewHolder) convertView.getTag();
        }

        final HlDiaoyuejilu dataBean = mDatas.get(position);
        if (dataBean != null) {

            String bianhao = "";
            if(IndexOfAndSubStr.isIndexOf(dataBean.getBianhao(),"FFFFFFFFFFFFFFFF")==false){
                bianhao = BytesToHexString.hexStr2Str2(dataBean.getBianhao());//不换位
            }
            holder.tvBianhao.setText(bianhao);
            holder.tvJilushijian.setText(dataBean.getJilushijian());

            String dianliuzhi = "";
            String dianzuzhi = "";
            if(StringUtils.noEmpty(dataBean.getDlz())){
                dianliuzhi = ShiOrShiliu.hexToFloatSiBuhuan(dataBean.getDlz());
            }
            if(StringUtils.noEmpty(dataBean.getDzz())){
                dianzuzhi = ShiOrShiliu.hexToFloatWuBuhuan(dataBean.getDzz());
            }
            holder.tvDlz.setText(dianliuzhi+"A");
            holder.tvDzz.setText(HlDlOrDzDw.getDzDw(dianzuzhi));
            HLDyjlAdapter.ViewHolder finalHolder = holder;
            holder.tvDaochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //导出分享
                    Log.e("", dataBean.getJilushijian());
                    try {
                        String lujingStr = "";
//                         String targetDocPath = getContext().getExternalFilesDir("poi").getPath() + File.separator + "模板3"+".doc";//这个目录，不需要申请存储权限
                        //InputStream templetDocStream = mContext.getAssets().open("10kV变压器.doc");
                        InputStream templetDocStream = mContext.getAssets().open("回路.doc");
                        String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                        Log.e("=====", targetDocPath);
                        lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);
                        Log.e("=====", lujingStr);

                        Map<String, String> dataMap = new HashMap<String, String>();
                        dataMap.put("$bianhao$", finalHolder.tvBianhao.getText().toString());
                        dataMap.put("$dianliuzhi$", finalHolder.tvDlz.getText().toString());
                        dataMap.put("$dianzuzhi$", finalHolder.tvDzz.getText().toString());
                        dataMap.put("$jilushijian$",dataBean.getJilushijian());
//                        dataMap.put("$hldccsbg$",mContext.getString(R.string.hl_daochu_scbg));
//                        dataMap.put("$hldcbh$", mContext.getString(R.string.hl_daochu_bh));
//                        dataMap.put("$hldcdlz$", mContext.getString(R.string.hl_daochu_dlz));
//                        dataMap.put("$hldcdzz$", mContext.getString(R.string.hl_daochu_dzz));
//                        dataMap.put("$hldcjlsj$",mContext.getString(R.string.hl_daochu_jlsj));

                        PoiUtils.writeToDoc(templetDocStream, targetDocPath, dataMap);
                        //Log.e("TTTT==", "写入...");

                        Intent share = new Intent(Intent.ACTION_SEND);
                        File file = new File("/storage/emulated/0/Android/data/com.yc.allbluetooth/files/poi/"+lujingStr);///storage/emulated/0/Android/data/com.yc.allbluetooth/files/poi/23051500090137.doc

                        Uri contentUri = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".FileProvider" , file);
                            share.putExtra(Intent.EXTRA_STREAM, contentUri);
                            share.setType("application/msword");// 此处可发送多种文件
                        } else {
                            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            share.setType("application/msword");// 此处可发送多种文件
                        }
                        try{
                            mContext.startActivity(Intent.createChooser(share, "Share"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });

        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvBianhao;
        public TextView tvJilushijian;
        public TextView tvDlz;
        public TextView tvDzz;
        public TextView tvDaochu;

    }
}
