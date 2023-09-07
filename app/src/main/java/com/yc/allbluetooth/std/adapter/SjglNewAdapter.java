package com.yc.allbluetooth.std.adapter;

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
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.poi.PoiUtils;
import com.yc.allbluetooth.std.entity.SjglNew;
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
public class SjglNewAdapter extends BaseAdapter {
    private Context mContext;

    private List<SjglNew> mDatas;

    private LayoutInflater mInflater;

    public boolean flage = true;

    private CheckBox ckbAll;


    public SjglNewAdapter(Context mContext, List<SjglNew> mDatas, CheckBox ckbAll) {
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
            convertView = mInflater.inflate(R.layout.std_shujuguanli_listview_item_new, null);

            holder = new ViewHolder();

            holder.ckbItem =  convertView.findViewById(R.id.ckbSjglListItem);
            holder.tvShijian = convertView.findViewById(R.id.tvSjglListItemShijian);
            holder.btnDaochu = convertView.findViewById(R.id.btnSjglListItemDaochu);
            holder.tvFenjie = convertView.findViewById(R.id.tvSjglListItemFenjie);
            holder.tvWendu = convertView.findViewById(R.id.tvSjglListItemWendu);
            holder.tvA0DzName = convertView.findViewById(R.id.tvSjglListItemA0DzName);
            holder.tvA0DzZhi = convertView.findViewById(R.id.tvSjglListItemA0DzZhi);
            holder.tvA0DzDw = convertView.findViewById(R.id.tvSjglListItemA0DzDw);
            holder.tvA0ZsDzName = convertView.findViewById(R.id.tvSjglListItemA0ZsDzName);
            holder.tvA0ZsDzZhi = convertView.findViewById(R.id.tvSjglListItemA0ZsDzZhi);
            holder.tvA0ZsDzDw = convertView.findViewById(R.id.tvSjglListItemA0ZsDzDw);

            holder.tvB0DzName = convertView.findViewById(R.id.tvSjglListItemB0DzName);
            holder.tvB0DzZhi = convertView.findViewById(R.id.tvSjglListItemB0DzZhi);
            holder.tvB0DzDw = convertView.findViewById(R.id.tvSjglListItemB0DzDw);
            holder.tvB0ZsDzName = convertView.findViewById(R.id.tvSjglListItemB0ZsDzName);
            holder.tvB0ZsDzZhi = convertView.findViewById(R.id.tvSjglListItemB0ZsDzZhi);
            holder.tvB0ZsDzDw = convertView.findViewById(R.id.tvSjglListItemB0ZsDzDw);

            holder.tvC0DzName = convertView.findViewById(R.id.tvSjglListItemC0DzName);
            holder.tvC0DzZhi = convertView.findViewById(R.id.tvSjglListItemC0DzZhi);
            holder.tvC0DzDw = convertView.findViewById(R.id.tvSjglListItemC0DzDw);
            holder.tvC0ZsDzName = convertView.findViewById(R.id.tvSjglListItemC0ZsDzName);
            holder.tvC0ZsDzZhi = convertView.findViewById(R.id.tvSjglListItemC0ZsDzZhi);
            holder.tvC0ZsDzDw = convertView.findViewById(R.id.tvSjglListItemC0ZsDzDw);

            holder.tvBphl = convertView.findViewById(R.id.tvSjglListItemBphl);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final SjglNew dataBean = mDatas.get(position);
        if (dataBean != null) {
            XiaoshuYunsuan xsys = new XiaoshuYunsuan();
            BigDecimal kt = xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.zswd)),
                    xsys.xiaoshuJia(xsys.xiaoshu(Config.rzczInt + ""), xsys.xiaoshu(Config.cswd)));//少测试温度
            holder.tvShijian.setText(dataBean.getShijian());
            holder.tvFenjie.setText(dataBean.getFenjie());
            holder.tvWendu.setText(ShiOrShiliu.parseInt(dataBean.getCeliangwendu())+"");

            //十六进制先转二进制，比如12==》0001（相位） 0010（有效值）
            //前四位仍和0~5左比较，判断是哪一项（主要是区分A0或者Ab），
            //后四位看后三位（010）有几个1，代表A、B、C哪相值有效
            //三通道的话，前四位都是0，最后三个全是1
            String csxwEjz = ShiOrShiliu.hexString2binaryString(dataBean.getCsxw(),1);//测试相位十六转二进制
            Log.e("std===sj==csxwEjz",csxwEjz);
            String csxwQianStr = StringUtils.subStrStartToEnd(csxwEjz,0,4);//前四位
            String csxwHouStrA = StringUtils.subStrStartToEnd(csxwEjz,5,6);//后二位
            String csxwHouStrB = StringUtils.subStrStartToEnd(csxwEjz,6,7);//后三位
            String csxwHouStrC = StringUtils.subStrStartToEnd(csxwEjz,7,8);//后四位
            if(StringUtils.isEquals(csxwQianStr,"0000")||StringUtils.isEquals(csxwQianStr,"0001")||StringUtils.isEquals(csxwQianStr,"0010")){//0、1、2
                holder.tvA0DzName.setText(R.string.a0dianzu);
                holder.tvB0DzName.setText(R.string.b0dianzu);
                holder.tvC0DzName.setText(R.string.c0dianzu);
                holder.tvA0ZsDzName.setText(R.string.a0zhesuandianzu);
                holder.tvB0ZsDzName.setText(R.string.b0zhesuandianzu);
                holder.tvC0ZsDzName.setText(R.string.c0zhesuandianzu);
            }else{//3、4、5(ab\bc\ca)
                holder.tvA0DzName.setText(R.string.abdianzu);
                holder.tvB0DzName.setText(R.string.bcdianzu);
                holder.tvC0DzName.setText(R.string.cadianzu);
                holder.tvA0ZsDzName.setText(R.string.abzhesuandianzu);
                holder.tvB0ZsDzName.setText(R.string.bczhesuandianzu);
                holder.tvC0ZsDzName.setText(R.string.cazhesuandianzu);
            }

//            if(dataBean.getA0orabType()<=2||dataBean.getA0orabType()==6){
//                holder.tvA0DzName.setText(R.string.a0dianzu);
//                holder.tvB0DzName.setText(R.string.b0dianzu);
//                holder.tvC0DzName.setText(R.string.c0dianzu);
//                holder.tvA0ZsDzName.setText(R.string.a0zhesuandianzu);
//                holder.tvB0ZsDzName.setText(R.string.b0zhesuandianzu);
//                holder.tvC0ZsDzName.setText(R.string.c0zhesuandianzu);
//            }else{
//                holder.tvA0DzName.setText(R.string.abdianzu);
//                holder.tvB0DzName.setText(R.string.bcdianzu);
//                holder.tvC0DzName.setText(R.string.cadianzu);
//                holder.tvA0ZsDzName.setText(R.string.abzhesuandianzu);
//                holder.tvB0ZsDzName.setText(R.string.bczhesuandianzu);
//                holder.tvC0ZsDzName.setText(R.string.cazhesuandianzu);
//            }
            if(StringUtils.noEmpty(dataBean.getA0orab())&&StringUtils.isEquals(csxwHouStrA,"1")){
                //holder.tvA0DzName.setText(R.string.a0dianzu);
                holder.tvA0DzZhi.setText(DianliuDianzuDw.dw("01",dataBean.getA0orab(),holder.tvA0DzDw));
                //holder.tvA0ZsDzName.setText(R.string.a0zhesuandianzu);
                holder.tvA0ZsDzZhi.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getA0orab(),holder.tvA0ZsDzDw)))+""));
            }else{
                holder.tvA0DzZhi.setText("0.000");
                holder.tvA0DzDw.setText("mΩ");
                holder.tvA0ZsDzZhi.setText("0.000");
                holder.tvA0ZsDzDw.setText("mΩ");
            }
            if(StringUtils.noEmpty(dataBean.getB0orbc())&&StringUtils.isEquals(csxwHouStrB,"1")){
                //holder.tvB0DzName.setText(R.string.b0dianzu);
                holder.tvB0DzZhi.setText(DianliuDianzuDw.dw("01",dataBean.getB0orbc(),holder.tvB0DzDw));
               // holder.tvB0ZsDzName.setText(R.string.b0zhesuandianzu);
                holder.tvB0ZsDzZhi.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getB0orbc(),holder.tvB0ZsDzDw)))+""));
            }else{
                holder.tvB0DzZhi.setText("0.000");
                holder.tvB0DzDw.setText("mΩ");
                holder.tvB0ZsDzZhi.setText("0.000");
                holder.tvB0ZsDzDw.setText("mΩ");
            }
            if(StringUtils.noEmpty(dataBean.getC0orca())&&StringUtils.isEquals(csxwHouStrC,"1")){
                //holder.tvC0DzName.setText(R.string.c0dianzu);
                holder.tvC0DzZhi.setText(DianliuDianzuDw.dw("01",dataBean.getC0orca(),holder.tvC0DzDw));
               // holder.tvC0ZsDzName.setText(R.string.c0zhesuandianzu);
                holder.tvC0ZsDzZhi.setText(StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01",dataBean.getC0orca(),holder.tvC0ZsDzDw)))+""));
            }else{
                holder.tvC0DzZhi.setText("0.000");
                holder.tvC0DzDw.setText("mΩ");
                holder.tvC0ZsDzZhi.setText("0.000");
                holder.tvC0ZsDzDw.setText("mΩ");
            }
            String tvA0Str = holder.tvA0DzZhi.getText().toString();
            String tvB0Str = holder.tvB0DzZhi.getText().toString();
            String tvC0Str = holder.tvC0DzZhi.getText().toString();

            if(!StringUtils.isEquals(tvA0Str,"0.000") && !StringUtils.isEquals(tvB0Str,"0.000") && !StringUtils.isEquals(tvC0Str,"0.000")){
                BigDecimal b1 = xsys.xiaoshu(tvA0Str);
                BigDecimal b2 = xsys.xiaoshu(tvB0Str);
                BigDecimal b3 = xsys.xiaoshu(tvC0Str);
                double b0 = xsys.xiaoshuCheng(xsys.xiaoshuChu(xsys.bijiaoDaxiaoCha(b1,b2,b3),xsys.xiaoshuChu(xsys.xiaoshuJia(xsys.xiaoshuJia(b1,b2),b3),xsys.xiaoshu("3"))),xsys.xiaoshu("100"));
                //Log.e("===double==",b0+"%");
                holder.tvBphl.setText(b0+"%");
            }else{
                holder.tvBphl.setText("");
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
                        SjglNew sjglNew = mDatas.get(i);
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
                    Log.e("",dataBean.getShijian());
                    //TextView textView = finalHolder.tvA0DzDw;

                    try {
                        String lujingStr = "";
//                         String targetDocPath = getContext().getExternalFilesDir("poi").getPath() + File.separator + "模板3"+".doc";//这个目录，不需要申请存储权限
                        //InputStream templetDocStream = mContext.getAssets().open("10kV变压器.doc");
                        InputStream templetDocStream = mContext.getAssets().open("三通道报告.doc");
                        String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                        Log.e("=====", targetDocPath);
                        lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);

                        Log.e("=====", lujingStr);
                        Map<String, String> dataMap = new HashMap<String, String>();
                        dataMap.put("$fenjieweizhi$", dataBean.getFenjie());
                        dataMap.put("$celiangwendu$", ShiOrShiliu.parseInt(dataBean.getCeliangwendu())+"");
                        dataMap.put("$ceshishijian$", dataBean.getShijian());
                        String csxwEjz = ShiOrShiliu.hexString2binaryString(dataBean.getCsxw(),1);//测试相位十六转二进制
                        Log.e("std===sj==csxwEjz",csxwEjz);
                        String csxwQianStr = StringUtils.subStrStartToEnd(csxwEjz,0,4);//前四位
//                        String csxwHouStrA = StringUtils.subStrStartToEnd(csxwEjz,5,6);//后二位
//                        String csxwHouStrB = StringUtils.subStrStartToEnd(csxwEjz,6,7);//后三位
//                        String csxwHouStrC = StringUtils.subStrStartToEnd(csxwEjz,7,8);//后四位
                        String tvA0Str = finalHolder1.tvA0DzZhi.getText().toString();
                        String tvB0Str = finalHolder1.tvB0DzZhi.getText().toString();
                        String tvC0Str = finalHolder1.tvC0DzZhi.getText().toString();
                        String tvA0ZsStr = finalHolder1.tvA0ZsDzZhi.getText().toString();
                        String tvB0ZsStr = finalHolder1.tvB0ZsDzZhi.getText().toString();
                        String tvC0ZsStr = finalHolder1.tvC0ZsDzZhi.getText().toString();
                        String tvA0Dw = finalHolder1.tvA0DzDw.getText().toString();
                        String tvB0Dw = finalHolder1.tvB0DzDw.getText().toString();
                        String tvC0Dw = finalHolder1.tvC0DzDw.getText().toString();
                        String tvA0ZsDw = finalHolder1.tvA0ZsDzDw.getText().toString();
                        String tvB0ZsDw = finalHolder1.tvB0ZsDzDw.getText().toString();
                        String tvC0ZsDw = finalHolder1.tvC0ZsDzDw.getText().toString();
                        // StringUtils.isEquals(csxwQianStr,"0000")||StringUtils.isEquals(csxwQianStr,"0001")||StringUtils.isEquals(csxwQianStr,"0010")
                        //if(dataBean.getA0orabType()<=2||dataBean.getA0orabType()==6){//A0--B0--C0
                        if(StringUtils.isEquals(csxwQianStr,"0000")||StringUtils.isEquals(csxwQianStr,"0001")||StringUtils.isEquals(csxwQianStr,"0010")){
                            dataMap.put("$A0orAb$","A0");
                            dataMap.put("$B0orBc$","B0");
                            dataMap.put("$C0orCa$","C0");
                        }else{//ab--bc--ca
                            dataMap.put("$A0orAb$","ab");
                            dataMap.put("$B0orBc$","bc");
                            dataMap.put("$C0orCa$","ca");
                        }
//                        dataMap.put("$A0dianzu$", DianliuDianzuDw.dw("01", dataBean.getA0orab(), finalHolder.tvA0DzDw) + finalHolder.tvA0DzDw.getText().toString());
//                        dataMap.put("$A0zhesuandianzu$", StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", dataBean.getA0orab(), finalHolder.tvA0ZsDzDw))) + "") + finalHolder.tvA0ZsDzDw.getText().toString());
//                        dataMap.put("$B0dianzu$", DianliuDianzuDw.dw("01", dataBean.getB0orbc(), finalHolder.tvB0DzDw) + finalHolder.tvB0DzDw.getText().toString());
//                        dataMap.put("$B0zhesuandianzu$", StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", dataBean.getB0orbc(), finalHolder.tvB0ZsDzDw))) + "") + finalHolder.tvB0ZsDzDw.getText().toString());
//                        dataMap.put("$C0dianzu$", DianliuDianzuDw.dw("01", dataBean.getC0orca(), finalHolder.tvC0DzDw) + finalHolder.tvC0DzDw.getText().toString());
//                        dataMap.put("$C0zhesuandianzu$", StringUtils.siweiYouxiaoStr(xsys.xiaoshuCheng(kt, xsys.xiaoshu(DianliuDianzuDw.dw("01", dataBean.getC0orca(), finalHolder.tvC0ZsDzDw))) + "") + finalHolder.tvC0ZsDzDw.getText().toString());
//                        dataMap.put("$zuidabupinghenglv$",finalHolder.tvBphl.getText().toString());

                        dataMap.put("$A0dianzu$", tvA0Str+tvA0Dw);
                        dataMap.put("$A0zhesuandianzu$", tvA0ZsStr+tvA0ZsDw);
                        dataMap.put("$B0dianzu$", tvB0Str+tvB0Dw);
                        dataMap.put("$B0zhesuandianzu$", tvB0ZsStr+tvB0ZsDw);
                        dataMap.put("$C0dianzu$", tvC0Str+tvC0Dw);
                        dataMap.put("$C0zhesuandianzu$", tvC0ZsStr+tvC0ZsDw);
                        dataMap.put("$zuidabupinghenglv$",finalHolder.tvBphl.getText().toString());

                        PoiUtils.writeToDoc(templetDocStream, targetDocPath, dataMap);

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
                        }}
            });
        }
        return convertView;
    }

    class ViewHolder {

        public CheckBox ckbItem;
        public TextView tvShijian;
        public Button btnDaochu;
        public TextView tvFenjie;
        public TextView tvWendu;
        public TextView tvA0DzName;
        public TextView tvA0DzZhi;
        public TextView tvA0DzDw;
        public TextView tvA0ZsDzName;
        public TextView tvA0ZsDzZhi;
        public TextView tvA0ZsDzDw;
        public TextView tvB0DzName;
        public TextView tvB0DzZhi;
        public TextView tvB0DzDw;
        public TextView tvB0ZsDzName;
        public TextView tvB0ZsDzZhi;
        public TextView tvB0ZsDzDw;
        public TextView tvC0DzName;
        public TextView tvC0DzZhi;
        public TextView tvC0DzDw;
        public TextView tvC0ZsDzName;
        public TextView tvC0ZsDzZhi;
        public TextView tvC0ZsDzDw;
        public TextView tvBphl;

    }

}
