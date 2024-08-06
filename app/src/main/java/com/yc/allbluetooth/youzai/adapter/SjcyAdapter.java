package com.yc.allbluetooth.youzai.adapter;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.poi.PoiUtils;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.youzai.entity.Shujuchayue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2023/6/14 16:58
 * author:jingyu zheng
 */
public class SjcyAdapter extends BaseAdapter {
    private Context mContext;

    private List<Shujuchayue> mDatas;

    private LayoutInflater mInflater;

    public boolean flage = true;

    private TextView tvAll;


    public SjcyAdapter(Context mContext, List<Shujuchayue> mDatas, TextView tvAll) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.tvAll = tvAll;
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
      SjcyAdapter.ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.yz_sjcy_shuju_list_item_layout, null);

            holder = new SjcyAdapter.ViewHolder();

            holder.ckbItem =  convertView.findViewById(R.id.ckbYzSjcyListItem);
            holder.tvSpbh = convertView.findViewById(R.id.tvYzSjcyListItemSpbh);
            holder.tvFjwz = convertView.findViewById(R.id.tvYzSjcyListItemFjwz);
            holder.tvCssj = convertView.findViewById(R.id.tvYzSjcyListItemCssj);
            holder.tvDc = convertView.findViewById(R.id.tvYzSjcyListItemDc);

            convertView.setTag(holder);

        } else {

            holder = (SjcyAdapter.ViewHolder) convertView.getTag();
        }

        final Shujuchayue dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.tvSpbh.setText(dataBean.getSpbh());
            holder.tvFjwz.setText(dataBean.getFjwz());
            holder.tvCssj.setText(dataBean.getCssj());

            holder.ckbItem.setChecked(dataBean.isCheck);
            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            ViewHolder finalHolder1 = holder;
            holder.ckbItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//禁止删除某一条，和原有列表对不上
                    finalHolder1.ckbItem.setChecked(false);//永远不能勾选
                    /*if (dataBean.isCheck) {
                        dataBean.isCheck = false;
                    } else {
                        dataBean.isCheck = true;
                    }
                    int typeNum=0;
                    for (int i = 0; i < mDatas.size(); i++) {
                        Shujuchayue shujuchayue = mDatas.get(i);
                        if (shujuchayue.isCheck) {
                            typeNum = typeNum+1;
                            //Log.e("===","check");
                        }
                        if(typeNum==mDatas.size()){//监听列表选中状态，如果选中个数和列表条数相等，就是全选中
                            Log.e("===","全选");
                            tvAll.setText(mContext.getString(R.string.quanqing));
                            tvAll.setBackgroundResource(R.color.result_minor_text);
                        }else{
                            tvAll.setText(mContext.getString(R.string.quanxuan));
                            tvAll.setBackgroundResource(R.color.index2);
                        }
                    }
                    Log.e("===","check"+typeNum);*/
                }
            });
            holder.ckbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
            ViewHolder finalHolder = holder;
            holder.tvDc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("====","导出："+dataBean.getId());
                    Log.e("==","导出："+dataBean.getId()+","+dataBean.getLc()+","+dataBean.getSc()+","+dataBean.getLmd()+","
                            +dataBean.getSpbh()+","+dataBean.getFjwz()+","+dataBean.getLjfs()+","+dataBean.getCsxs()+","+dataBean.getCssj());
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
                                InputStream templetDocStream = mContext.getAssets().open("有载1.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                                Log.e("=====", targetDocPath);
                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);

                                Log.e("=====", lujingStr);
                                Map<String, String> dataMap = new HashMap<String, String>();

                                dataMap.put("$name$",etStr);
                                dataMap.put("$liangcheng$", dataBean.getLc());
                                dataMap.put("$shichang$", dataBean.getSc());
                                dataMap.put("$lingmindu$", dataBean.getLmd());
                                dataMap.put("$shipinbianhao$", dataBean.getSpbh());//dataBean.getSpbh()
                                dataMap.put("$ceshifenjie$", dataBean.getFjwz());
                                dataMap.put("$lianjiefangshi$", dataBean.getLjfs());
                                dataMap.put("$ceshixiangshu$", dataBean.getCsxs());
                                dataMap.put("$ceshishijian$", dataBean.getCssj());
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

            //十六进制先转二进制，比如12==》0001（相位） 0010（有效值）
            //前四位仍和0~5左比较，判断是哪一项（主要是区分A0或者Ab），
            //后四位看后三位（010）有几个1，代表A、B、C哪相值有效
            //三通道的话，前四位都是0，最后三个全是1
        }
        return convertView;
    }

    class ViewHolder {

        public CheckBox ckbItem;
        public TextView tvSpbh;
        public TextView tvFjwz;
        public TextView tvCssj;
        public TextView tvDc;

    }
}
