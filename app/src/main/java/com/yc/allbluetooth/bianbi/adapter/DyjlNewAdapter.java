package com.yc.allbluetooth.bianbi.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.entity.DiaoyuejiluNew;
import com.yc.allbluetooth.poi.PoiUtils;
import com.yc.allbluetooth.utils.BytesToHexString;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.HexUtil;
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
            holder.tvDaochu = convertView.findViewById(R.id.tvBbDyjlListNewItemDaochu);

            convertView.setTag(holder);

        } else {

            holder = (DyjlNewAdapter.ViewHolder) convertView.getTag();
        }

        final DiaoyuejiluNew dataBean = mDatas.get(position);
        if (dataBean != null) {

            String bbzA = "";
            String bbzB = "";
            String bbzC = "";
            if(StringUtils.noEmpty(dataBean.getBbzA())){
                bbzA = ShiOrShiliu.hexToFloatWuBuhuan(dataBean.getBbzA());
            }

            String bianhao = "";
            if(IndexOfAndSubStr.isIndexOf(dataBean.getBianhao(),"FFFF")==false){
                bianhao = BytesToHexString.hexStr2Str2(HexUtil.reverseHex(dataBean.getBianhao()));
            }
            holder.tvBianhao.setText(bianhao);
            holder.tvFenjie.setText(dataBean.getFenjie());

            if(StringUtils.isEquals(dataBean.getDanOrSan(),"00")){
                holder.tvBbzA.setText(bbzA);
            }else if (StringUtils.isEquals(dataBean.getDanOrSan(),"01")){
                if(StringUtils.noEmpty(dataBean.getBbzB())){
                    bbzB = ShiOrShiliu.hexToFloatWuBuhuan(dataBean.getBbzB());
                }
                if(StringUtils.noEmpty(dataBean.getBbzC())){
                    bbzC = ShiOrShiliu.hexToFloatWuBuhuan(dataBean.getBbzC());
                }
                holder.tvBbzA.setText(bbzA);
                holder.tvBbzB.setText(bbzB);
                holder.tvBbzC.setText(bbzC);
            }
            holder.tvCeshishijian.setText(dataBean.getCeshishijian());
            ViewHolder finalHolder = holder;
            holder.tvDaochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("", dataBean.getCeshishijian());
                    Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.layout_daochu_tanchu);
                    TextView tvQd = dialog.findViewById(R.id.tvQd);
                    TextView tvQx = dialog.findViewById(R.id.tvQx);
                    EditText et = dialog.findViewById(R.id.et);
                    tvQd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 自定义弹窗中按钮的点击事件回调
                            String etStr = et.getText().toString();
                            Log.e("=====",etStr);
                            dialog.dismiss();
                            try {
                                String lujingStr = "";
//                         String targetDocPath = getContext().getExternalFilesDir("poi").getPath() + File.separator + "模板3"+".doc";//这个目录，不需要申请存储权限
                                //InputStream templetDocStream = mContext.getAssets().open("10kV变压器.doc");
                                InputStream templetDocStream = mContext.getAssets().open("变比.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                                Log.e("=====", targetDocPath);
                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);
                                Log.e("=====", lujingStr);

                                Map<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("$name$",etStr);
                                dataMap.put("$bianhao$", finalHolder.tvBianhao.getText().toString());
                                dataMap.put("$fenjieweizhi$", dataBean.getFenjie());
                                dataMap.put("$bianbiA$", finalHolder.tvBbzA.getText().toString());
                                dataMap.put("$bianbiB$",finalHolder.tvBbzB.getText().toString());
                                dataMap.put("$bianbiC$",finalHolder.tvBbzC.getText().toString());
                                dataMap.put("$ceshishijian$", dataBean.getCeshishijian());

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
                    tvQx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 自定义弹窗中按钮的点击事件回调
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });

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
        public TextView tvDaochu;
    }
}
