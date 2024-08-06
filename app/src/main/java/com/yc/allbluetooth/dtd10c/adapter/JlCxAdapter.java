package com.yc.allbluetooth.dtd10c.adapter;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.dtd10c.entity.JlCx;
import com.yc.allbluetooth.poi.PoiUtils;
import com.yc.allbluetooth.std.util.DianliuDianzuDw;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.IndexOfAndSubStr;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;
import com.yc.allbluetooth.utils.XiaoshuYunsuan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2023/1/4 9:14
 * author:jingyu zheng
 */
public class JlCxAdapter extends BaseAdapter {
    private Context mContext;

    private List<JlCx> mDatas;

    private LayoutInflater mInflater;

    public boolean flage = true;

    private CheckBox ckbAll;


    public JlCxAdapter(Context mContext, List<JlCx> mDatas, CheckBox ckbAll) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.ckbAll = ckbAll;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.dtd10c_jiluchaxun_listview_item, null);

            holder = new ViewHolder();

            holder.ckbItem =  convertView.findViewById(R.id.ckbJlCxLvItem);
            holder.tvShijian = convertView.findViewById(R.id.tvJlCxLvItemCsShijian);
            holder.btnDaochu = convertView.findViewById(R.id.btnJlCxLvItemDaochu);
            holder.tvFenjie = convertView.findViewById(R.id.tvJlCxLvItemFenjie);
            holder.tvDzA = convertView.findViewById(R.id.tvJlCxLvItemCsDzA);
            holder.tvDzADw = convertView.findViewById(R.id.tvJlCxLvItemCsDzADw);
            holder.tvDzB = convertView.findViewById(R.id.tvJlCxLvItemCsDzB);
            holder.tvDzBDw = convertView.findViewById(R.id.tvJlCxLvItemCsDzBDw);
            holder.tvDzC = convertView.findViewById(R.id.tvJlCxLvItemCsDzC);
            holder.tvDzCDw = convertView.findViewById(R.id.tvJlCxLvItemCsDzCDw);

            holder.tvZsDzA = convertView.findViewById(R.id.tvJlCxLvItemZsDzA);
            holder.tvZsDzADw = convertView.findViewById(R.id.tvJlCxLvItemZsDzADw);
            holder.tvZsDzB = convertView.findViewById(R.id.tvJlCxLvItemZsDzB);
            holder.tvZsDzBDw = convertView.findViewById(R.id.tvJlCxLvItemZsDzBDw);
            holder.tvZsDzC = convertView.findViewById(R.id.tvJlCxLvItemZsDzC);
            holder.tvZsDzCDw = convertView.findViewById(R.id.tvJlCxLvItemZsDzCDw);

            holder.tvCsA0orAB = convertView.findViewById(R.id.tvJlCxLvItemCsDzA0OrAB);
            holder.tvCsB0orBC = convertView.findViewById(R.id.tvJlCxLvItemCsDzB0OrBC);
            holder.tvCsC0orCA = convertView.findViewById(R.id.tvJlCxLvItemCsDzC0OrCA);
            holder.tvZsA0orAB = convertView.findViewById(R.id.tvJlCxLvItemZsDzA0OrAB);
            holder.tvZsB0orBC = convertView.findViewById(R.id.tvJlCxLvItemZsDzB0OrBC);
            holder.tvZsC0orCA = convertView.findViewById(R.id.tvJlCxLvItemZsDzC0OrCA);



            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final JlCx dataBean = mDatas.get(position);

        if (dataBean != null) {
            String csxwBin = ShiOrShiliu.hexString2binaryString(dataBean.getA0orabType()+"",1);
            //Log.e("二进制转换","十六进制："+dataBean.getA0orabType()+",二进制："+csxwBin);
            String c1 = StringUtils.subStrStartToEnd(csxwBin,2,4);
            String b1 = StringUtils.subStrStartToEnd(csxwBin,4,6);
            String a1 = StringUtils.subStrStartToEnd(csxwBin,6,8);


            XiaoshuYunsuan xsys = new XiaoshuYunsuan();
            BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                    xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd2)));//少测试温度


            holder.tvShijian.setText(dataBean.getShijian());
            holder.tvFenjie.setText(dataBean.getFenjie());
            //dataBean.getA0orabType();//转成二进制，判断哪项有值

            //if(StringUtils.isEquals("01",a1)||StringUtils.isEquals("01",b1)||StringUtils.isEquals("01",c1)){//A0\B0\C0
            if(StringUtils.isEquals("01",a1)){
                holder.tvCsA0orAB.setText(mContext.getString(R.string.ceshidianzua0));
                holder.tvZsA0orAB.setText(mContext.getString(R.string.zhesuandianzua0));
                holder.tvCsB0orBC.setText(mContext.getString(R.string.ceshidianzub0));
                holder.tvZsB0orBC.setText(mContext.getString(R.string.zhesuandianzub0));
                holder.tvCsC0orCA.setText(mContext.getString(R.string.ceshidianzuc0));
                holder.tvZsC0orCA.setText(mContext.getString(R.string.zhesuandianzuc0));
                holder.tvDzA.setText(DianliuDianzuDw.dw("01",dataBean.getA0orab(),holder.tvDzADw));
                holder.tvZsDzA.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getA0orab(),holder.tvZsDzADw)))+""));
            }else if(StringUtils.isEquals("10",a1)){
                holder.tvCsA0orAB.setText(mContext.getString(R.string.ceshidianzuab));
                holder.tvZsA0orAB.setText(mContext.getString(R.string.zhesuandianzuab));
                holder.tvCsB0orBC.setText(mContext.getString(R.string.ceshidianzubc));
                holder.tvZsB0orBC.setText(mContext.getString(R.string.zhesuandianzubc));
                holder.tvCsC0orCA.setText(mContext.getString(R.string.ceshidianzuca));
                holder.tvZsC0orCA.setText(mContext.getString(R.string.zhesuandianzuca));
                holder.tvDzA.setText(DianliuDianzuDw.dw("01",dataBean.getA0orab(),holder.tvDzADw));
                holder.tvZsDzA.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getA0orab(),holder.tvZsDzADw)))+""));
            }else{
                holder.tvDzA.setText("0.000");
                holder.tvDzADw.setText("Ω");
                holder.tvZsDzA.setText("0.000");
                holder.tvZsDzADw.setText("Ω");
            }
            if(StringUtils.isEquals("01",b1)){
                holder.tvCsA0orAB.setText(mContext.getString(R.string.ceshidianzua0));
                holder.tvZsA0orAB.setText(mContext.getString(R.string.zhesuandianzua0));
                holder.tvCsB0orBC.setText(mContext.getString(R.string.ceshidianzub0));
                holder.tvZsB0orBC.setText(mContext.getString(R.string.zhesuandianzub0));
                holder.tvCsC0orCA.setText(mContext.getString(R.string.ceshidianzuc0));
                holder.tvZsC0orCA.setText(mContext.getString(R.string.zhesuandianzuc0));
                holder.tvDzB.setText(DianliuDianzuDw.dw("01",dataBean.getB0orbc(),holder.tvDzBDw));
                holder.tvZsDzB.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getB0orbc(),holder.tvZsDzBDw)))+""));
            }else if(StringUtils.isEquals("10",b1)){
                holder.tvCsA0orAB.setText(mContext.getString(R.string.ceshidianzuab));
                holder.tvZsA0orAB.setText(mContext.getString(R.string.zhesuandianzuab));
                holder.tvCsB0orBC.setText(mContext.getString(R.string.ceshidianzubc));
                holder.tvZsB0orBC.setText(mContext.getString(R.string.zhesuandianzubc));
                holder.tvCsC0orCA.setText(mContext.getString(R.string.ceshidianzuca));
                holder.tvZsC0orCA.setText(mContext.getString(R.string.zhesuandianzuca));
                holder.tvDzB.setText(DianliuDianzuDw.dw("01",dataBean.getB0orbc(),holder.tvDzBDw));
                holder.tvZsDzB.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getB0orbc(),holder.tvZsDzBDw)))+""));
            }else{
                holder.tvDzB.setText("0.000");
                holder.tvDzBDw.setText("Ω");
                holder.tvZsDzB.setText("0.000");
                holder.tvZsDzBDw.setText("Ω");
            }
            if(StringUtils.isEquals("01",c1)){
                holder.tvCsA0orAB.setText(mContext.getString(R.string.ceshidianzua0));
                holder.tvZsA0orAB.setText(mContext.getString(R.string.zhesuandianzua0));
                holder.tvCsB0orBC.setText(mContext.getString(R.string.ceshidianzub0));
                holder.tvZsB0orBC.setText(mContext.getString(R.string.zhesuandianzub0));
                holder.tvCsC0orCA.setText(mContext.getString(R.string.ceshidianzuc0));
                holder.tvZsC0orCA.setText(mContext.getString(R.string.zhesuandianzuc0));
                holder.tvDzC.setText(DianliuDianzuDw.dw("01",dataBean.getC0orca(),holder.tvDzCDw));
                holder.tvZsDzC.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getC0orca(),holder.tvZsDzCDw)))+""));
            }else if(StringUtils.isEquals("10",c1)){
                holder.tvCsA0orAB.setText(mContext.getString(R.string.ceshidianzuab));
                holder.tvZsA0orAB.setText(mContext.getString(R.string.zhesuandianzuab));
                holder.tvCsB0orBC.setText(mContext.getString(R.string.ceshidianzubc));
                holder.tvZsB0orBC.setText(mContext.getString(R.string.zhesuandianzubc));
                holder.tvCsC0orCA.setText(mContext.getString(R.string.ceshidianzuca));
                holder.tvZsC0orCA.setText(mContext.getString(R.string.zhesuandianzuca));
                holder.tvDzC.setText(DianliuDianzuDw.dw("01",dataBean.getC0orca(),holder.tvDzCDw));
                holder.tvZsDzC.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getC0orca(),holder.tvZsDzCDw)))+""));
            }else{
                holder.tvDzC.setText("0.000");
                holder.tvDzCDw.setText("Ω");
                holder.tvZsDzC.setText("0.000");
                holder.tvZsDzCDw.setText("Ω");
            }



            holder.ckbItem.setChecked(dataBean.isCheck);
            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            holder.ckbItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataBean.isCheck) {
                        dataBean.isCheck = false;
                    } else {
                        dataBean.isCheck = true;
                    }
                    int typeNum=0;
                    for (int i = 0; i < mDatas.size(); i++) {
                        JlCx sjglNew = mDatas.get(i);
                        if (sjglNew.isCheck) {
                            typeNum = typeNum+1;
                            //Log.e("===","check");
                        }
                        if(typeNum==mDatas.size()){//监听列表选中状态，如果选中个数和列表条数相等，就是全选中
                            Log.e("===","全选");
                            ckbAll.setChecked(true);
                        }else{
                            ckbAll.setChecked(false);
                        }
                    }
                    Log.e("===","check"+typeNum);
                }
            });
            holder.ckbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
            ViewHolder finalHolder = holder;
            ViewHolder finalHolder1 = holder;
            holder.btnDaochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("", dataBean.getShijian());

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
                                InputStream templetDocStream = mContext.getAssets().open("10C报告.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                                Log.e("=====", targetDocPath);
                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);
                                Log.e("=====", lujingStr);

                                Map<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("$name$",etStr);//导出报告名称
                                dataMap.put("$fenjieweizhi$", dataBean.getFenjie());
                                //dataMap.put("$celiangwendu$", ShiOrShiliu.parseInt(dataBean.getCeliangwendu())+"");
                                dataMap.put("$ceshishijian$", dataBean.getShijian());
                                dataMap.put("$A0orAB$",finalHolder1.tvCsA0orAB.getText().toString());
                                dataMap.put("$B0orBC$",finalHolder1.tvCsB0orBC.getText().toString());
                                dataMap.put("$C0orCA$",finalHolder1.tvCsC0orCA.getText().toString());
                                dataMap.put("$A0dianzu$", finalHolder1.tvDzA.getText().toString()+ finalHolder1.tvDzADw.getText().toString());
                                dataMap.put("$B0dianzu$", finalHolder1.tvDzB.getText().toString()+ finalHolder1.tvDzBDw.getText().toString());
                                dataMap.put("$C0dianzu$", finalHolder1.tvDzC.getText().toString()+ finalHolder1.tvDzCDw.getText().toString());



                                //dataMap.put("$A0dianzu$", DianliuDianzuDw.dw("01", dataBean.getA0orab(), finalHolder.tvA0DzDw) + finalHolder.tvA0DzDw.getText().toString());
                                //dataMap.put("$A0zhesuandianzu$", StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", dataBean.getA0orab(), finalHolder.tvA0ZsDzDw))) + "") + finalHolder.tvA0ZsDzDw.getText().toString());
                                //dataMap.put("$B0dianzu$", DianliuDianzuDw.dw("01", dataBean.getB0orbc(), finalHolder.tvB0DzDw) + finalHolder.tvB0DzDw.getText().toString());
                                //dataMap.put("$B0zhesuandianzu$", StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", dataBean.getB0orbc(), finalHolder.tvB0ZsDzDw))) + "") + finalHolder.tvB0ZsDzDw.getText().toString());
                                //dataMap.put("$C0dianzu$", DianliuDianzuDw.dw("01", dataBean.getC0orca(), finalHolder.tvC0DzDw) + finalHolder.tvC0DzDw.getText().toString());
                                //dataMap.put("$C0zhesuandianzu$", StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", dataBean.getC0orca(), finalHolder.tvC0ZsDzDw))) + "") + finalHolder.tvC0ZsDzDw.getText().toString());
                                //dataMap.put("$zuidabupinghenglv$",finalHolder.tvBphl.getText().toString());

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

        public CheckBox ckbItem;
        public TextView tvShijian;
        public Button btnDaochu;
        public TextView tvFenjie;
        public TextView tvDzA;
        public TextView tvDzADw;
        public TextView tvDzB;
        public TextView tvDzBDw;
        public TextView tvDzC;
        public TextView tvDzCDw;

        public TextView tvZsDzA;
        public TextView tvZsDzADw;
        public TextView tvZsDzB;
        public TextView tvZsDzBDw;
        public TextView tvZsDzC;
        public TextView tvZsDzCDw;

        public TextView tvCsA0orAB;
        public TextView tvZsA0orAB;
        public TextView tvCsB0orBC;
        public TextView tvZsB0orBC;
        public TextView tvCsC0orCA;
        public TextView tvZsC0orCA;


    }

}
