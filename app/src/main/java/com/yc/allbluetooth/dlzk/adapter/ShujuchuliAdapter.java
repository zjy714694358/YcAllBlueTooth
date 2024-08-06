package com.yc.allbluetooth.dlzk.adapter;

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
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.dlzk.entity.Shujuchuli;
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
 * Date:2023/4/14 10:37
 * author:jingyu zheng
 */
public class ShujuchuliAdapter extends BaseAdapter {
    private Context mContext;

    private List<Shujuchuli> mDatas;

    private LayoutInflater mInflater;

    public boolean flage = true;

    private CheckBox ckbAll;


    public ShujuchuliAdapter(Context mContext, List<Shujuchuli> mDatas, CheckBox ckbAll) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.ckbAll = ckbAll;
        //Log.e("this.mContext==",this.mContext+"");
        mInflater = LayoutInflater.from(this.mContext);

    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return mDatas.get(position).getViewType();
        //return 1;
    }

    @Override
    public int getViewTypeCount() {
        //return super.getViewTypeCount();
        return 4;
    }

    @Override
    public int getCount() {
        Log.e("====m.size()====",mDatas.size()+"");
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
        ViewHolderSx holdersx = null;
        ViewHolderDx holderdx = null;
        ViewHolderLx holderlx = null;
        ViewHolderSxAbc0 holderSxAbc0 = null;

        if (convertView == null) {
            switch (getItemViewType(position)){
                case 0:
                    convertView = mInflater.inflate(R.layout.dlzk_sanxiang_shuju_list_item, null);
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.dlzk_danxiang_shuju_list_item, null);
                    break;
                case 2:
                    convertView = mInflater.inflate(R.layout.dlzk_lingxu_shuju_list_item, null);
                    break;
                case 3:
                    convertView = mInflater.inflate(R.layout.dlzk_sanxiang_abc0_shuju_list_item, null);
                    break;
            }
            // 下拉项布局
            //convertView = mInflater.inflate(R.layout.dlzk_sanxiang_shuju_list_item, null);
            //convertView = mInflater.inflate(R.layout.dlzk_danxiang_shuju_list_item, null);
            //convertView = mInflater.inflate(R.layout.dlzk_lingxu_shuju_list_item, null);

            if(getItemViewType(position)==0){//三相
                holdersx = new ViewHolderSx();
                holdersx.ckbItem =convertView.findViewById(R.id.ckbDlzkShujuDanxuan);
                holdersx.tvSpbh = convertView.findViewById(R.id.tvDlzkShujuSpbh);
                holdersx.tvEdrl = convertView.findViewById(R.id.tvDlzkShujuEdrl);
                holdersx.tvFjdy = convertView.findViewById(R.id.tvDlzkShujuFjdy);
                holdersx.tvMpzk = convertView.findViewById(R.id.tvDlzkShujuMpzk);
                holdersx.tvFjwz = convertView.findViewById(R.id.tvDlzkShujuFjwz);
                holdersx.tvClwz = convertView.findViewById(R.id.tvDlzkShujuClwz);
                holdersx.tvClwd = convertView.findViewById(R.id.tvDlzkShujuClwd);
                holdersx.tvJzwd = convertView.findViewById(R.id.tvDlzkShujuJzwd);
                holdersx.tvCsry = convertView.findViewById(R.id.tvDlzkShujuCsry);
                holdersx.tvCljx = convertView.findViewById(R.id.tvDlzkShujuCljx);
                holdersx.tvCssj = convertView.findViewById(R.id.tvDlzkShujuCssj);

                holdersx.tvSjdyAb = convertView.findViewById(R.id.tvDlzkShujuSjdyAb);
                holdersx.tvSjdyBc = convertView.findViewById(R.id.tvDlzkShujuSjdyBc);
                holdersx.tvSjdyCa = convertView.findViewById(R.id.tvDlzkShujuSjdyCa);
                holdersx.tvSjdlAb = convertView.findViewById(R.id.tvDlzkShujuSjdlAb);
                holdersx.tvSjdlBc = convertView.findViewById(R.id.tvDlzkShujuSjdlBc);
                holdersx.tvSjdlCa = convertView.findViewById(R.id.tvDlzkShujuSjdlCa);
                holdersx.tvClxjAb = convertView.findViewById(R.id.tvDlzkShujuClxjAb);
                holdersx.tvClxjBc = convertView.findViewById(R.id.tvDlzkShujuClxjBc);
                holdersx.tvClxjCa = convertView.findViewById(R.id.tvDlzkShujuClxjCa);
                holdersx.tvYgglAb = convertView.findViewById(R.id.tvDlzkShujuYgglAb);
                holdersx.tvYgglBc = convertView.findViewById(R.id.tvDlzkShujuYgglBc);
                holdersx.tvYgglCa = convertView.findViewById(R.id.tvDlzkShujuYgglCa);
                holdersx.tvDlzkAb = convertView.findViewById(R.id.tvDlzkShujuDlzkAb);
                holdersx.tvDlzkBc = convertView.findViewById(R.id.tvDlzkShujuDlzkBc);
                holdersx.tvDlzkCa = convertView.findViewById(R.id.tvDlzkShujuDlzkCa);
                holdersx.tvDlgkAb = convertView.findViewById(R.id.tvDlzkShujuDlgkAb);
                holdersx.tvDlgkBc = convertView.findViewById(R.id.tvDlzkShujuDlgkBc);
                holdersx.tvDlgkCa = convertView.findViewById(R.id.tvDlzkShujuDlgkCa);
                holdersx.tvRzdgAb = convertView.findViewById(R.id.tvDlzkShujuRzdgAb);
                holdersx.tvRzdgBc = convertView.findViewById(R.id.tvDlzkShujuRzdgBc);
                holdersx.tvRzdgCa = convertView.findViewById(R.id.tvDlzkShujuRzdgCa);
                holdersx.tvZkdyAb = convertView.findViewById(R.id.tvDlzkShujuZkdyAb);
                holdersx.tvZkdyBc = convertView.findViewById(R.id.tvDlzkShujuZkdyBc);
                holdersx.tvZkdyCa = convertView.findViewById(R.id.tvDlzkShujuZkdyCa);
                holdersx.tvZkdyZkBfh = convertView.findViewById(R.id.tvDlzkShujuZkdyZkBfh);
                holdersx.tvZkwcDZkBfh = convertView.findViewById(R.id.tvDlzkShujuZkwcDZkBfh);
                holdersx.tvDaochu = convertView.findViewById(R.id.tvDlzkShujuDaochu);

                convertView.setTag(holdersx);

            }else if(getItemViewType(position)==1){//单相
                holderdx = new ViewHolderDx();
                holderdx.ckbItem =convertView.findViewById(R.id.ckbDlzkShujuDxDanxuan);
                holderdx.tvEdrl = convertView.findViewById(R.id.tvDlzkShujuDxEdrl);
                holderdx.tvFjdy = convertView.findViewById(R.id.tvDlzkShujuDxFjdy);
                holderdx.tvMpzk = convertView.findViewById(R.id.tvDlzkShujuDxMpzk);
                holderdx.tvClwd = convertView.findViewById(R.id.tvDlzkShujuDxClwd);
                holderdx.tvFjwz = convertView.findViewById(R.id.tvDlzkShujuDxFjwz);
                holderdx.tvClwz = convertView.findViewById(R.id.tvDlzkShujuDxClwz);
                holderdx.tvCssj = convertView.findViewById(R.id.tvDlzkShujuDxCssj);
                holderdx.tvCsbh = convertView.findViewById(R.id.tvDlzkShujuDxCsbh);

                holderdx.tvSjdy = convertView.findViewById(R.id.tvDlzkShujuDxSjdy);
                holderdx.tvSjdl = convertView.findViewById(R.id.tvDlzkShujuDxSjdl);
                holderdx.tvClpl = convertView.findViewById(R.id.tvDlzkShujuDxClpl);
                holderdx.tvClxj = convertView.findViewById(R.id.tvDlzkShujuDxClxj);
                holderdx.tvYggl = convertView.findViewById(R.id.tvDlzkShujuDxYggl);
                holderdx.tvDlzk = convertView.findViewById(R.id.tvDlzkShujuDxDlzk);
                holderdx.tvDlgk = convertView.findViewById(R.id.tvDlzkShujuDxDlgk);
                holderdx.tvDldz = convertView.findViewById(R.id.tvDlzkShujuDxDldz);
                holderdx.tvRzdg = convertView.findViewById(R.id.tvDlzkShujuDxRzdg);
                holderdx.tvGlys = convertView.findViewById(R.id.tvDlzkShujuDxGlys);
                holderdx.tvZkdyZkBfh = convertView.findViewById(R.id.tvDlzkShujuDxZkdyZkBfh);
                holderdx.tvZkwcDZkBfh = convertView.findViewById(R.id.tvDlzkShujuDxZkwcDZkBfh);
                holderdx.tvCsry = convertView.findViewById(R.id.tvDlzkShujuDxCsry);
                holderdx.tvDaochu = convertView.findViewById(R.id.tvDlzkShujuDxDaochu);

                convertView.setTag(holderdx);
            }else if(getItemViewType(position)==2){//零序
                holderlx = new ViewHolderLx();
                holderlx.ckbItem =convertView.findViewById(R.id.ckbDlzkShujuLxDanxuan);
                holderlx.tvEdrl = convertView.findViewById(R.id.tvDlzkShujuLxEdrl);
                holderlx.tvFjdy = convertView.findViewById(R.id.tvDlzkShujuLxFjdy);
                holderlx.tvMpzk = convertView.findViewById(R.id.tvDlzkShujuLxMpzk);
                holderlx.tvClwd = convertView.findViewById(R.id.tvDlzkShujuLxClwd);
                holderlx.tvCsry = convertView.findViewById(R.id.tvDlzkShujuLxCsry);
                holderlx.tvCssj = convertView.findViewById(R.id.tvDlzkShujuLxCssj);
                holderlx.tvCsbh = convertView.findViewById(R.id.tvDlzkShujuLxCsbh);

                holderlx.tvSjdy = convertView.findViewById(R.id.tvDlzkShujuLxSjdy);
                holderlx.tvSjdl = convertView.findViewById(R.id.tvDlzkShujuLxSjdl);
                holderlx.tvClpl = convertView.findViewById(R.id.tvDlzkShujuLxClpl);
                holderlx.tvClxj = convertView.findViewById(R.id.tvDlzkShujuLxClxj);
                holderlx.tvYggl = convertView.findViewById(R.id.tvDlzkShujuLxYggl);
                holderlx.tvLxzk = convertView.findViewById(R.id.tvDlzkShujuLxLxzk);
                holderlx.tvLxgk = convertView.findViewById(R.id.tvDlzkShujuLxLxgk);
                holderlx.tvLxdz = convertView.findViewById(R.id.tvDlzkShujuLxLxdz);
                holderlx.tvLxdg = convertView.findViewById(R.id.tvDlzkShujuLxLxdg);
                holderlx.tvGlys = convertView.findViewById(R.id.tvDlzkShujuLxGlys);

                holderlx.tvDaochu = convertView.findViewById(R.id.tvDlzkShujuLxDaochu);

                convertView.setTag(holderlx);
            }else if(getItemViewType(position)==3){//三相Abc0
                holderSxAbc0 = new ViewHolderSxAbc0();
                holderSxAbc0.ckbAbc0Item =convertView.findViewById(R.id.ckbDlzkShujuAbc0Danxuan);
                holderSxAbc0.tvAbc0Spbh = convertView.findViewById(R.id.tvDlzkShujuAbc0Spbh);
                holderSxAbc0.tvAbc0Edrl = convertView.findViewById(R.id.tvDlzkShujuAbc0Edrl);
                holderSxAbc0.tvAbc0Fjdy = convertView.findViewById(R.id.tvDlzkShujuAbc0Fjdy);
                holderSxAbc0.tvAbc0Mpzk = convertView.findViewById(R.id.tvDlzkShujuAbc0Mpzk);
                holderSxAbc0.tvAbc0Fjwz = convertView.findViewById(R.id.tvDlzkShujuAbc0Fjwz);
                holderSxAbc0.tvAbc0Clwz = convertView.findViewById(R.id.tvDlzkShujuAbc0Clwz);
                holderSxAbc0.tvAbc0Clwd = convertView.findViewById(R.id.tvDlzkShujuAbc0Clwd);
                holderSxAbc0.tvAbc0Jzwd = convertView.findViewById(R.id.tvDlzkShujuAbc0Jzwd);
                holderSxAbc0.tvAbc0Csry = convertView.findViewById(R.id.tvDlzkShujuAbc0Csry);
                holderSxAbc0.tvAbc0Cljx = convertView.findViewById(R.id.tvDlzkShujuAbc0Cljx);
                holderSxAbc0.tvAbc0Cssj = convertView.findViewById(R.id.tvDlzkShujuAbc0Cssj);

                holderSxAbc0.tvAbc0SjdyAb = convertView.findViewById(R.id.tvDlzkShujuAbc0SjdyAb);
                holderSxAbc0.tvAbc0SjdyBc = convertView.findViewById(R.id.tvDlzkShujuAbc0SjdyBc);
                holderSxAbc0.tvAbc0SjdyCa = convertView.findViewById(R.id.tvDlzkShujuAbc0SjdyCa);
                holderSxAbc0.tvAbc0SjdlAb = convertView.findViewById(R.id.tvDlzkShujuAbc0SjdlAb);
                holderSxAbc0.tvAbc0SjdlBc = convertView.findViewById(R.id.tvDlzkShujuAbc0SjdlBc);
                holderSxAbc0.tvAbc0SjdlCa = convertView.findViewById(R.id.tvDlzkShujuAbc0SjdlCa);
                holderSxAbc0.tvAbc0ClxjAb = convertView.findViewById(R.id.tvDlzkShujuAbc0ClxjAb);
                holderSxAbc0.tvAbc0ClxjBc = convertView.findViewById(R.id.tvDlzkShujuAbc0ClxjBc);
                holderSxAbc0.tvAbc0ClxjCa = convertView.findViewById(R.id.tvDlzkShujuAbc0ClxjCa);
                holderSxAbc0.tvAbc0YgglAb = convertView.findViewById(R.id.tvDlzkShujuAbc0YgglAb);
                holderSxAbc0.tvAbc0YgglBc = convertView.findViewById(R.id.tvDlzkShujuAbc0YgglBc);
                holderSxAbc0.tvAbc0YgglCa = convertView.findViewById(R.id.tvDlzkShujuAbc0YgglCa);
                holderSxAbc0.tvAbc0DlzkAb = convertView.findViewById(R.id.tvDlzkShujuAbc0DlzkAb);
                holderSxAbc0.tvAbc0DlzkBc = convertView.findViewById(R.id.tvDlzkShujuAbc0DlzkBc);
                holderSxAbc0.tvAbc0DlzkCa = convertView.findViewById(R.id.tvDlzkShujuAbc0DlzkCa);
                holderSxAbc0.tvAbc0DlgkAb = convertView.findViewById(R.id.tvDlzkShujuAbc0DlgkAb);
                holderSxAbc0.tvAbc0DlgkBc = convertView.findViewById(R.id.tvDlzkShujuAbc0DlgkBc);
                holderSxAbc0.tvAbc0DlgkCa = convertView.findViewById(R.id.tvDlzkShujuAbc0DlgkCa);
                holderSxAbc0.tvAbc0RzdgAb = convertView.findViewById(R.id.tvDlzkShujuAbc0RzdgAb);
                holderSxAbc0.tvAbc0RzdgBc = convertView.findViewById(R.id.tvDlzkShujuAbc0RzdgBc);
                holderSxAbc0.tvAbc0RzdgCa = convertView.findViewById(R.id.tvDlzkShujuAbc0RzdgCa);
                holderSxAbc0.tvAbc0ZkdyAb = convertView.findViewById(R.id.tvDlzkShujuAbc0ZkdyAb);
                holderSxAbc0.tvAbc0ZkdyBc = convertView.findViewById(R.id.tvDlzkShujuAbc0ZkdyBc);
                holderSxAbc0.tvAbc0ZkdyCa = convertView.findViewById(R.id.tvDlzkShujuAbc0ZkdyCa);
                holderSxAbc0.tvAbc0ZkdyZkBfh = convertView.findViewById(R.id.tvDlzkShujuAbc0ZkdyZkBfh);
                holderSxAbc0.tvAbc0ZkwcDZkBfh = convertView.findViewById(R.id.tvDlzkShujuAbc0ZkwcDZkBfh);
                holderSxAbc0.tvAbc0Daochu = convertView.findViewById(R.id.tvDlzkShujuAbc0Daochu);

                convertView.setTag(holderSxAbc0);

            }
        } else {
            switch (getItemViewType(position)){
                case 0:
                    holdersx = (ViewHolderSx) convertView.getTag();
                    break;
                case 1:
                    holderdx = (ViewHolderDx) convertView.getTag();
                    break;
                case 2:
                    holderlx = (ViewHolderLx) convertView.getTag();
                    break;
                case 3:
                    holderSxAbc0 = (ViewHolderSxAbc0) convertView.getTag();
                    break;
            }
        }
        Log.e("====!!==",getItemViewType(position)+"");
        if(getItemViewType(position)==0){//三相
            final Shujuchuli dataBean = mDatas.get(position);
            if (dataBean != null) {

                holdersx.tvSpbh.setText(BytesToHexString.hexStr2Str(dataBean.getSpbh().replace("FF","")));
                holdersx.tvEdrl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getEdrl()));
                holdersx.tvFjdy.setText(ShiOrShiliu.hexToFloatWu(dataBean.getFjdy()));
                holdersx.tvMpzk.setText(ShiOrShiliu.hexToFloatSi(dataBean.getMpzk()));
                holdersx.tvFjwz.setText(ShiOrShiliu.parseInt(dataBean.getFjwz())+"");

                String clwzStr = "";
                if(StringUtils.isEquals("04",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.gaodi);
                }else if(StringUtils.isEquals("05",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.gaozhong);
                }else if(StringUtils.isEquals("06",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.zhongdi);
                }
                holdersx.tvClwz.setText(clwzStr);

                holdersx.tvClwd.setText(ShiOrShiliu.hexToFloatSi(dataBean.getClwd()));
                holdersx.tvJzwd.setText(ShiOrShiliu.hexToFloatSi(dataBean.getJzwd()));
                holdersx.tvCsry.setText(BytesToHexString.hexStr2Str(dataBean.getCsry().replace("FF","")));

                String cljxStr = "";
                if(StringUtils.isEquals("07",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.yylianjie);
                }else if(StringUtils.isEquals("08",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.ydlianjie);
                }else if(StringUtils.isEquals("09",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.dylianjieaz);
                }else if(StringUtils.isEquals("0A",dataBean.getCljx())||StringUtils.isEquals("0a",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.dylianjieay);
                }
                holdersx.tvCljx.setText(cljxStr);
                holdersx.tvCssj.setText(dataBean.getCssj());

                holdersx.tvSjdyAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdyAb()));
                holdersx.tvSjdyBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdyBc()));
                holdersx.tvSjdyCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdyCa()));
                holdersx.tvSjdlAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdlAb()));
                holdersx.tvSjdlBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdlBc()));
                holdersx.tvSjdlCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdlCa()));
                holdersx.tvClxjAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxjAb()));
                holdersx.tvClxjBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxjBc()));
                holdersx.tvClxjCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxjCa()));
                holdersx.tvYgglAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYgglAb()));
                holdersx.tvYgglBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYgglBc()));
                holdersx.tvYgglCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYgglCa()));
                holdersx.tvDlzkAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzkAb()));
                holdersx.tvDlzkBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzkBc()));
                holdersx.tvDlzkCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzkCa()));
                holdersx.tvDlgkAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgkAb()));
                holdersx.tvDlgkBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgkBc()));
                holdersx.tvDlgkCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgkCa()));
                holdersx.tvRzdgAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdgAb()));
                holdersx.tvRzdgBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdgBc()));
                holdersx.tvRzdgCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdgCa()));
                holdersx.tvZkdyAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyAb()));
                holdersx.tvZkdyBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyBc()));
                holdersx.tvZkdyCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyCa()));
                holdersx.tvZkdyZkBfh.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyZkBfh()));
                holdersx.tvZkwcDZkBfh.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkwcDZkBfh()));


                holdersx.ckbItem.setChecked(dataBean.isCheck);
                //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
                holdersx.ckbItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataBean.isCheck) {
                            dataBean.isCheck = false;
                        } else {
                            dataBean.isCheck = true;
                        }
                        int typeNum=0;
                        for (int i = 0; i < mDatas.size(); i++) {
                            Shujuchuli shujuchuli = mDatas.get(i);
                            if (shujuchuli.isCheck) {
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
                        Log.e("","");
                    }
                });
            }
            ViewHolderSx finalHoldersx = holdersx;
            holdersx.tvDaochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("====","导出"+dataBean.getId());
                    Log.e("==","导出==");
                    Log.e("", dataBean.getCssj());

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
                                InputStream templetDocStream = mContext.getAssets().open("短路阻抗三相AB.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                                Log.e("=====", targetDocPath);

                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);
                                Log.e("=====", lujingStr);

                                Map<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("$name$",etStr);
                                dataMap.put("$shipinbianhao$", finalHoldersx.tvSpbh.getText().toString());
                                dataMap.put("$edingrongliang$", finalHoldersx.tvEdrl.getText().toString());
                                dataMap.put("$fenjiedianya$", finalHoldersx.tvFjdy.getText().toString());
                                dataMap.put("$mingpaizukang$", finalHoldersx.tvMpzk.getText().toString());
                                dataMap.put("$fenjieweizhi$", finalHoldersx.tvFjwz.getText().toString());
                                dataMap.put("$celiangweizhi$", finalHoldersx.tvClwz.getText().toString());
                                dataMap.put("$celiangwendu$", finalHoldersx.tvClwd.getText().toString());
                                dataMap.put("$jiaozhengwendu$", finalHoldersx.tvJzwd.getText().toString());
                                dataMap.put("$ceshirenyuan$", finalHoldersx.tvCsry.getText().toString());
                                dataMap.put("$celiangjiexian$", finalHoldersx.tvCljx.getText().toString());
                                dataMap.put("$ceshishijian$", finalHoldersx.tvCssj.getText().toString());
                                dataMap.put("$shijiadianyaab$", finalHoldersx.tvSjdyAb.getText().toString());
                                dataMap.put("$shijiadianyabc$", finalHoldersx.tvSjdyBc.getText().toString());
                                dataMap.put("$shijiadianyaca$", finalHoldersx.tvSjdyCa.getText().toString());
                                dataMap.put("$shijiadianliuab$", finalHoldersx.tvSjdlAb.getText().toString());
                                dataMap.put("$shijiadianliubc$", finalHoldersx.tvSjdlBc.getText().toString());
                                dataMap.put("$shijiadianliuca$", finalHoldersx.tvSjdlCa.getText().toString());
                                dataMap.put("$celiangxiangjiaoab$", finalHoldersx.tvClxjAb.getText().toString());
                                dataMap.put("$celiangxiangjiaobc$", finalHoldersx.tvClxjBc.getText().toString());
                                dataMap.put("$celiangxiangjiaoca$", finalHoldersx.tvClxjCa.getText().toString());
                                dataMap.put("$yougonggonglvab$", finalHoldersx.tvYgglAb.getText().toString());
                                dataMap.put("$yougonggonglvbc$", finalHoldersx.tvYgglBc.getText().toString());
                                dataMap.put("$yougonggonglvca$", finalHoldersx.tvYgglCa.getText().toString());
                                dataMap.put("$duanluzukangab$", finalHoldersx.tvDlzkAb.getText().toString());
                                dataMap.put("$duanluzukangbc$", finalHoldersx.tvDlzkBc.getText().toString());
                                dataMap.put("$duanluzukangca$", finalHoldersx.tvDlzkCa.getText().toString());
                                dataMap.put("$duanlugankangab$", finalHoldersx.tvDlgkAb.getText().toString());
                                dataMap.put("$duanlugankangbc$", finalHoldersx.tvDlgkBc.getText().toString());
                                dataMap.put("$duanlugankangca$", finalHoldersx.tvDlgkCa.getText().toString());
                                dataMap.put("$raozudianganab$", finalHoldersx.tvRzdgAb.getText().toString());
                                dataMap.put("$raozudianganbc$", finalHoldersx.tvRzdgBc.getText().toString());
                                dataMap.put("$raozudianganca$", finalHoldersx.tvRzdgCa.getText().toString());
                                dataMap.put("$zukangdianyaab$", finalHoldersx.tvZkdyAb.getText().toString());
                                dataMap.put("$zukangdianyabc$", finalHoldersx.tvZkdyBc.getText().toString());
                                dataMap.put("$zukangdianyaca$", finalHoldersx.tvZkdyCa.getText().toString());
                                dataMap.put("$zukangdianyazkbfh$", finalHoldersx.tvZkdyZkBfh.getText().toString());
                                dataMap.put("$zukangwuchadzkbfh$", finalHoldersx.tvZkwcDZkBfh.getText().toString());

                                PoiUtils.writeToDoc(templetDocStream, targetDocPath, dataMap);
                                Log.e("TTTT==", "写入...");
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
                                    Log.e("======","开始分享");
                                    mContext.startActivity(Intent.createChooser(share, "Share"));
                                } catch (Exception e) {
                                    Log.e("======","分享异常");
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
        }else if(getItemViewType(position)==1){//单相(完成)
            final Shujuchuli dataBean = mDatas.get(position);
            if (dataBean != null) {

                holderdx.tvEdrl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getEdrl()));
                holderdx.tvFjdy.setText(ShiOrShiliu.hexToFloatWu(dataBean.getFjdy()));
                holderdx.tvMpzk.setText(ShiOrShiliu.hexToFloatSi(dataBean.getMpzk()));
                holderdx.tvClwd.setText(ShiOrShiliu.hexToFloatSi(dataBean.getClwd()));
                holderdx.tvFjwz.setText(ShiOrShiliu.parseInt(dataBean.getFjwz())+"");
                String clwzStr = "";
                if(StringUtils.isEquals("04",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.gaodi);
                }else if(StringUtils.isEquals("05",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.gaozhong);
                }else if(StringUtils.isEquals("06",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.zhongdi);
                }
                holderdx.tvClwz.setText(clwzStr);
                holderdx.tvCssj.setText(dataBean.getCssj());

                holderdx.tvCsbh.setText(BytesToHexString.hexStr2Str(dataBean.getCsbh().replace("FF","")));

                holderdx.tvSjdy.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdy()));
                holderdx.tvSjdl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdl()));
                holderdx.tvClpl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClpl()));
                holderdx.tvClxj.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxj()));
                holderdx.tvYggl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYggl()));
                holderdx.tvDlzk.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzk()));
                holderdx.tvDlgk.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgk()));
                holderdx.tvDldz.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDldz()));
                holderdx.tvRzdg.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdg()));
                holderdx.tvGlys.setText(ShiOrShiliu.hexToFloatWu(dataBean.getGlys()));
                holderdx.tvZkdyZkBfh.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyZkBfh()));
                holderdx.tvZkwcDZkBfh.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkwcDZkBfh()));
                holderdx.tvCsry.setText(BytesToHexString.hexStr2Str(dataBean.getCsry().replace("FF","")));


                holderdx.ckbItem.setChecked(dataBean.isCheck);
                //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
                holderdx.ckbItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataBean.isCheck) {
                            dataBean.isCheck = false;
                        } else {
                            dataBean.isCheck = true;
                        }
                        int typeNum=0;
                        for (int i = 0; i < mDatas.size(); i++) {
                            Shujuchuli shujuchuli = mDatas.get(i);
                            if (shujuchuli.isCheck) {
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
                        Log.e("","");
                    }
                });
            }
            ViewHolderDx finalHolderdx = holderdx;
            holderdx.tvDaochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("====","导出"+dataBean.getId());
                    Log.e("type==","导出=="+dataBean.getViewType());
                    Log.e("", dataBean.getCssj());

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
                                InputStream templetDocStream = mContext.getAssets().open("短路阻抗单相.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                                Log.e("=====", targetDocPath);
                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);

                                Log.e("=====", lujingStr);
                                Map<String, String> dataMap = new HashMap<String, String>();

                                dataMap.put("$name$",etStr);
                                dataMap.put("$edingrongliang$", finalHolderdx.tvEdrl.getText().toString());
                                dataMap.put("$fenjiedianya$", finalHolderdx.tvFjdy.getText().toString());
                                dataMap.put("$mingpaizukang$", finalHolderdx.tvMpzk.getText().toString());
                                dataMap.put("$celiangwendu$", finalHolderdx.tvClwd.getText().toString());
                                dataMap.put("$fenjieweizhi$", finalHolderdx.tvFjwz.getText().toString());
                                dataMap.put("$celiangweizhi$", finalHolderdx.tvClwz.getText().toString());
                                dataMap.put("$ceshishijian$", finalHolderdx.tvCssj.getText().toString());
                                dataMap.put("$ceshibianhao$", finalHolderdx.tvCsbh.getText().toString());

                                dataMap.put("$ceshirenyuan$", finalHolderdx.tvCsry.getText().toString());

                                dataMap.put("$shijiadianya$", finalHolderdx.tvSjdy.getText().toString());
                                dataMap.put("$shijiadianliu$", finalHolderdx.tvSjdl.getText().toString());
                                dataMap.put("$celiangpinlv$", finalHolderdx.tvClpl.getText().toString());
                                dataMap.put("$celiangxiangjiao$", finalHolderdx.tvClxj.getText().toString());
                                dataMap.put("$yougonggonglv$", finalHolderdx.tvYggl.getText().toString());

                                dataMap.put("$duanluzukang$", finalHolderdx.tvDlzk.getText().toString());
                                dataMap.put("$duanlugankang$", finalHolderdx.tvDlgk.getText().toString());
                                dataMap.put("$duanludianzu$", finalHolderdx.tvDldz.getText().toString());
                                dataMap.put("$raozudiangan$", finalHolderdx.tvRzdg.getText().toString());
                                dataMap.put("$gonglvyinshu$", finalHolderdx.tvGlys.getText().toString());

                                dataMap.put("$zukangdianyazkbfh$", finalHolderdx.tvZkdyZkBfh.getText().toString());
                                dataMap.put("$zukangwuchadzkbfh$", finalHolderdx.tvZkwcDZkBfh.getText().toString());


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
        }else if(getItemViewType(position)==2){//零序
            final Shujuchuli dataBean = mDatas.get(position);
            Log.e("lingxu==",dataBean.getCssj());
            if (dataBean != null) {

                holderlx.tvEdrl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getEdrl()));
                holderlx.tvFjdy.setText(ShiOrShiliu.hexToFloatWu(dataBean.getFjdy()));
                holderlx.tvMpzk.setText(ShiOrShiliu.hexToFloatSi(dataBean.getMpzk()));
                holderlx.tvClwd.setText(ShiOrShiliu.hexToFloatSi(dataBean.getClwd()));
                holderlx.tvCsry.setText(BytesToHexString.hexStr2Str(dataBean.getCsry().replace("FF","")));
                holderlx.tvCssj.setText(dataBean.getCssj());

                holderlx.tvSjdy.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdy()));
                holderlx.tvSjdl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdl()));
                holderlx.tvClpl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClpl()));
                holderlx.tvClxj.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxj()));
                holderlx.tvYggl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYggl()));
                holderlx.tvLxzk.setText(ShiOrShiliu.hexToFloatWu(dataBean.getLxzk()));
                holderlx.tvLxgk.setText(ShiOrShiliu.hexToFloatWu(dataBean.getLxgk()));
                holderlx.tvLxdz.setText(ShiOrShiliu.hexToFloatWu(dataBean.getLxdz()));
                holderlx.tvLxdg.setText(ShiOrShiliu.hexToFloatWu(dataBean.getLxdg()));
                holderlx.tvGlys.setText(ShiOrShiliu.hexToFloatWu(dataBean.getGlys()));
                holderlx.tvCsbh.setText(BytesToHexString.hexStr2Str(dataBean.getCsbh().replace("FF","")));

                holderlx.ckbItem.setChecked(dataBean.isCheck);
                //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
                holderlx.ckbItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataBean.isCheck) {
                            dataBean.isCheck = false;
                        } else {
                            dataBean.isCheck = true;
                        }
                        int typeNum=0;
                        for (int i = 0; i < mDatas.size(); i++) {
                            Shujuchuli shujuchuli = mDatas.get(i);
                            if (shujuchuli.isCheck) {
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
                        Log.e("","");

                    }
                });
            }
            ViewHolderLx finalHolderlx = holderlx;
            holderlx.tvDaochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("====","导出"+dataBean.getId());
                    Log.e("==","导出==");
                    Log.e("", dataBean.getCssj());

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

                                InputStream templetDocStream = mContext.getAssets().open("短路阻抗零序.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"

                                Log.e("=====", targetDocPath);
                                Map<String, String> dataMap = new HashMap<String, String>();
                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);
                                dataMap.put("$name$",etStr);
                                dataMap.put("$edingrongliang$", finalHolderlx.tvEdrl.getText().toString());
                                dataMap.put("$fenjiedianya$", finalHolderlx.tvFjdy.getText().toString());
                                dataMap.put("$mingpaizukang$", finalHolderlx.tvMpzk.getText().toString());
                                dataMap.put("$celiangwendu$", finalHolderlx.tvClwd.getText().toString());
                                dataMap.put("$ceshirenyuan$", finalHolderlx.tvCsry.getText().toString());
                                dataMap.put("$ceshishijian$", finalHolderlx.tvCssj.getText().toString());

                                dataMap.put("$ceshibianhao$", finalHolderlx.tvCsbh.getText().toString());

                                dataMap.put("$shijiadianya$", finalHolderlx.tvSjdy.getText().toString());
                                dataMap.put("$shijiadianliu$", finalHolderlx.tvSjdl.getText().toString());
                                dataMap.put("$celiangpinlv$", finalHolderlx.tvClpl.getText().toString());
                                dataMap.put("$celiangxiangjiao$", finalHolderlx.tvClxj.getText().toString());
                                dataMap.put("$yougonggonglv$", finalHolderlx.tvYggl.getText().toString());

                                dataMap.put("$lingxuzukang$", finalHolderlx.tvLxzk.getText().toString());
                                dataMap.put("$lingxugankang$", finalHolderlx.tvLxgk.getText().toString());
                                dataMap.put("$lingxudianzu$", finalHolderlx.tvLxdz.getText().toString());
                                dataMap.put("$lingxudiangan$", finalHolderlx.tvLxdg.getText().toString());
                                dataMap.put("$gonglvyinshu$", finalHolderlx.tvGlys.getText().toString());

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
        }else if(getItemViewType(position)==3){//三相Abc0
            final Shujuchuli dataBean = mDatas.get(position);
            if (dataBean != null) {

                holderSxAbc0.tvAbc0Spbh.setText(BytesToHexString.hexStr2Str(dataBean.getSpbh().replace("FF","")));
                holderSxAbc0.tvAbc0Edrl.setText(ShiOrShiliu.hexToFloatWu(dataBean.getEdrl()));
                holderSxAbc0.tvAbc0Fjdy.setText(ShiOrShiliu.hexToFloatWu(dataBean.getFjdy()));
                holderSxAbc0.tvAbc0Mpzk.setText(ShiOrShiliu.hexToFloatSi(dataBean.getMpzk()));
                holderSxAbc0.tvAbc0Fjwz.setText(ShiOrShiliu.parseInt(dataBean.getFjwz())+"");

                String clwzStr = "";
                if(StringUtils.isEquals("04",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.gaodi);
                }else if(StringUtils.isEquals("05",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.gaozhong);
                }else if(StringUtils.isEquals("06",dataBean.getClwz())){
                    clwzStr = mContext.getString(R.string.zhongdi);
                }
                holderSxAbc0.tvAbc0Clwz.setText(clwzStr);
                holderSxAbc0.tvAbc0Clwd.setText(ShiOrShiliu.hexToFloatSi(dataBean.getClwd()));
                holderSxAbc0.tvAbc0Jzwd.setText(ShiOrShiliu.hexToFloatSi(dataBean.getJzwd()));
                holderSxAbc0.tvAbc0Csry.setText(BytesToHexString.hexStr2Str(dataBean.getCsry().replace("FF","")));

                String cljxStr = "";
                if(StringUtils.isEquals("07",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.yylianjie);
                }else if(StringUtils.isEquals("08",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.ydlianjie);
                }else if(StringUtils.isEquals("09",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.dylianjieaz);
                }else if(StringUtils.isEquals("0A",dataBean.getCljx())||StringUtils.isEquals("0a",dataBean.getCljx())){
                    cljxStr = mContext.getString(R.string.dylianjieay);
                }
                holderSxAbc0.tvAbc0Cljx.setText(cljxStr);
                holderSxAbc0.tvAbc0Cssj.setText(dataBean.getCssj());

                holderSxAbc0.tvAbc0SjdyAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdyA0()));
                holderSxAbc0.tvAbc0SjdyBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdyB0()));
                holderSxAbc0.tvAbc0SjdyCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdyC0()));
                holderSxAbc0.tvAbc0SjdlAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdlA0()));
                holderSxAbc0.tvAbc0SjdlBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdlB0()));
                holderSxAbc0.tvAbc0SjdlCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getSjdlC0()));
                holderSxAbc0.tvAbc0ClxjAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxjA0()));
                holderSxAbc0.tvAbc0ClxjBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxjB0()));
                holderSxAbc0.tvAbc0ClxjCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getClxjC0()));
                holderSxAbc0.tvAbc0YgglAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYgglA0()));
                holderSxAbc0.tvAbc0YgglBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYgglB0()));
                holderSxAbc0.tvAbc0YgglCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getYgglC0()));
                holderSxAbc0.tvAbc0DlzkAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzkA0()));
                holderSxAbc0.tvAbc0DlzkBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzkB0()));
                holderSxAbc0.tvAbc0DlzkCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlzkC0()));
                holderSxAbc0.tvAbc0DlgkAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgkA0()));
                holderSxAbc0.tvAbc0DlgkBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgkB0()));
                holderSxAbc0.tvAbc0DlgkCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getDlgkC0()));
                holderSxAbc0.tvAbc0RzdgAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdgA0()));
                holderSxAbc0.tvAbc0RzdgBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdgB0()));
                holderSxAbc0.tvAbc0RzdgCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getRzdgC0()));
                holderSxAbc0.tvAbc0ZkdyAb.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyA0()));
                holderSxAbc0.tvAbc0ZkdyBc.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyB0()));
                holderSxAbc0.tvAbc0ZkdyCa.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyC0()));
                holderSxAbc0.tvAbc0ZkdyZkBfh.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkdyZkBfh()));
                holderSxAbc0.tvAbc0ZkwcDZkBfh.setText(ShiOrShiliu.hexToFloatWu(dataBean.getZkwcDZkBfh()));


                holderSxAbc0.ckbAbc0Item.setChecked(dataBean.isCheck);
                //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
                holderSxAbc0.ckbAbc0Item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataBean.isCheck) {
                            dataBean.isCheck = false;
                        } else {
                            dataBean.isCheck = true;
                        }
                        int typeNum=0;
                        for (int i = 0; i < mDatas.size(); i++) {
                            Shujuchuli shujuchuli = mDatas.get(i);
                            if (shujuchuli.isCheck) {
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
                        Log.e("","");
                    }
                });
            }
            ViewHolderSxAbc0 finalHoldersx = holderSxAbc0;
            holderSxAbc0.tvAbc0Daochu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("====","导出"+dataBean.getId());
                    Log.e("==","导出==");
                    Log.e("", dataBean.getCssj());

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
                                InputStream templetDocStream = mContext.getAssets().open("短路阻抗三相A0.doc");
                                String targetDocPath = mContext.getExternalFilesDir("poi").getPath() + File.separator + GetTime.getTime(3)+ ".doc";//这个目录，不需要申请存储权限//"10kV干式变压器报告5"
                                lujingStr = IndexOfAndSubStr.subStrStart(targetDocPath,targetDocPath.length()-18);
                                Log.e("=====", targetDocPath);
                                Map<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("$name$",etStr);
                                dataMap.put("$shipinbianhao$", finalHoldersx.tvAbc0Spbh.getText().toString());
                                dataMap.put("$edingrongliang$", finalHoldersx.tvAbc0Edrl.getText().toString());
                                dataMap.put("$fenjiedianya$", finalHoldersx.tvAbc0Fjdy.getText().toString());
                                dataMap.put("$mingpaizukang$", finalHoldersx.tvAbc0Mpzk.getText().toString());
                                dataMap.put("$fenjieweizhi$", finalHoldersx.tvAbc0Fjwz.getText().toString());
                                dataMap.put("$celiangweizhi$", finalHoldersx.tvAbc0Clwz.getText().toString());
                                dataMap.put("$celiangwendu$", finalHoldersx.tvAbc0Clwd.getText().toString());
                                dataMap.put("$jiaozhengwendu$", finalHoldersx.tvAbc0Jzwd.getText().toString());
                                dataMap.put("$ceshirenyuan$", finalHoldersx.tvAbc0Csry.getText().toString());
                                dataMap.put("$celiangjiexian$", finalHoldersx.tvAbc0Cljx.getText().toString());
                                dataMap.put("$ceshishijian$", finalHoldersx.tvAbc0Cssj.getText().toString());
                                dataMap.put("$shijiadianyaab$", finalHoldersx.tvAbc0SjdyAb.getText().toString());
                                dataMap.put("$shijiadianyabc$", finalHoldersx.tvAbc0SjdyBc.getText().toString());
                                dataMap.put("$shijiadianyaca$", finalHoldersx.tvAbc0SjdyCa.getText().toString());
                                dataMap.put("$shijiadianliuab$", finalHoldersx.tvAbc0SjdlAb.getText().toString());
                                dataMap.put("$shijiadianliubc$", finalHoldersx.tvAbc0SjdlBc.getText().toString());
                                dataMap.put("$shijiadianliuca$", finalHoldersx.tvAbc0SjdlCa.getText().toString());
                                dataMap.put("$celiangxiangjiaoab$", finalHoldersx.tvAbc0ClxjAb.getText().toString());
                                dataMap.put("$celiangxiangjiaobc$", finalHoldersx.tvAbc0ClxjBc.getText().toString());
                                dataMap.put("$celiangxiangjiaoca$", finalHoldersx.tvAbc0ClxjCa.getText().toString());
                                dataMap.put("$yougonggonglvab$", finalHoldersx.tvAbc0YgglAb.getText().toString());
                                dataMap.put("$yougonggonglvbc$", finalHoldersx.tvAbc0YgglBc.getText().toString());
                                dataMap.put("$yougonggonglvca$", finalHoldersx.tvAbc0YgglCa.getText().toString());
                                dataMap.put("$duanluzukangab$", finalHoldersx.tvAbc0DlzkAb.getText().toString());
                                dataMap.put("$duanluzukangbc$", finalHoldersx.tvAbc0DlzkBc.getText().toString());
                                dataMap.put("$duanluzukangca$", finalHoldersx.tvAbc0DlzkCa.getText().toString());
                                dataMap.put("$duanlugankangab$", finalHoldersx.tvAbc0DlgkAb.getText().toString());
                                dataMap.put("$duanlugankangbc$", finalHoldersx.tvAbc0DlgkBc.getText().toString());
                                dataMap.put("$duanlugankangca$", finalHoldersx.tvAbc0DlgkCa.getText().toString());
                                dataMap.put("$raozudianganab$", finalHoldersx.tvAbc0RzdgAb.getText().toString());
                                dataMap.put("$raozudianganbc$", finalHoldersx.tvAbc0RzdgBc.getText().toString());
                                dataMap.put("$raozudianganca$", finalHoldersx.tvAbc0RzdgCa.getText().toString());
                                dataMap.put("$zukangdianyaab$", finalHoldersx.tvAbc0ZkdyAb.getText().toString());
                                dataMap.put("$zukangdianyabc$", finalHoldersx.tvAbc0ZkdyBc.getText().toString());
                                dataMap.put("$zukangdianyaca$", finalHoldersx.tvAbc0ZkdyCa.getText().toString());
                                dataMap.put("$zukangdianyazkbfh$", finalHoldersx.tvAbc0ZkdyZkBfh.getText().toString());
                                dataMap.put("$zukangwuchadzkbfh$", finalHoldersx.tvAbc0ZkwcDZkBfh.getText().toString());

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
    class ViewHolderSx {
        public CheckBox ckbItem;
        public TextView tvSpbh;
        public TextView tvEdrl;
        public TextView tvFjdy;
        public TextView tvMpzk;
        public TextView tvFjwz;
        public TextView tvClwz;
        public TextView tvClwd;
        public TextView tvJzwd;
        public TextView tvCsry;
        public TextView tvCljx;
        public TextView tvCssj;
        public TextView tvSjdyAb;
        public TextView tvSjdyBc;
        public TextView tvSjdyCa;
        public TextView tvSjdlAb;
        public TextView tvSjdlBc;
        public TextView tvSjdlCa;
        public TextView tvClxjAb;
        public TextView tvClxjBc;
        public TextView tvClxjCa;
        public TextView tvYgglAb;
        public TextView tvYgglBc;
        public TextView tvYgglCa;
        public TextView tvDlzkAb;
        public TextView tvDlzkBc;
        public TextView tvDlzkCa;
        public TextView tvDlgkAb;
        public TextView tvDlgkBc;
        public TextView tvDlgkCa;
        public TextView tvRzdgAb;
        public TextView tvRzdgBc;
        public TextView tvRzdgCa;
        public TextView tvZkdyAb;
        public TextView tvZkdyBc;
        public TextView tvZkdyCa;
        public TextView tvZkdyZkBfh;
        public TextView tvZkwcDZkBfh;
        public TextView tvDaochu;

    }
    class ViewHolderDx {
        public CheckBox ckbItem;
        public TextView tvEdrl;
        public TextView tvFjdy;
        public TextView tvMpzk;
        public TextView tvClwd;
        public TextView tvFjwz;
        public TextView tvClwz;
        public TextView tvCssj;
        public TextView tvCsbh;
        public TextView tvSjdy;
        public TextView tvSjdl;
        public TextView tvClpl;
        public TextView tvClxj;
        public TextView tvYggl;
        public TextView tvDlzk;
        public TextView tvDlgk;
        public TextView tvDldz;
        public TextView tvRzdg;
        public TextView tvGlys;
        public TextView tvZkdyZkBfh;
        public TextView tvZkwcDZkBfh;
        public TextView tvCsry;
        public TextView tvDaochu;
    }
    class ViewHolderLx {
        public CheckBox ckbItem;
        public TextView tvEdrl;
        public TextView tvFjdy;
        public TextView tvMpzk;
        public TextView tvClwd;
        public TextView tvCsry;
        public TextView tvCssj;
        public TextView tvSjdy;
        public TextView tvSjdl;
        public TextView tvClpl;
        public TextView tvClxj;
        public TextView tvYggl;
        public TextView tvLxzk;
        public TextView tvLxgk;
        public TextView tvLxdz;
        public TextView tvLxdg;
        public TextView tvGlys;
        public TextView tvCsbh;
        public TextView tvDaochu;
    }
    class ViewHolderSxAbc0 {//三相Abc0
        public CheckBox ckbAbc0Item;
        public TextView tvAbc0Spbh;
        public TextView tvAbc0Edrl;
        public TextView tvAbc0Fjdy;
        public TextView tvAbc0Mpzk;
        public TextView tvAbc0Fjwz;
        public TextView tvAbc0Clwz;
        public TextView tvAbc0Clwd;
        public TextView tvAbc0Jzwd;
        public TextView tvAbc0Csry;
        public TextView tvAbc0Cljx;
        public TextView tvAbc0Cssj;
        public TextView tvAbc0SjdyAb;
        public TextView tvAbc0SjdyBc;
        public TextView tvAbc0SjdyCa;
        public TextView tvAbc0SjdlAb;
        public TextView tvAbc0SjdlBc;
        public TextView tvAbc0SjdlCa;
        public TextView tvAbc0ClxjAb;
        public TextView tvAbc0ClxjBc;
        public TextView tvAbc0ClxjCa;
        public TextView tvAbc0YgglAb;
        public TextView tvAbc0YgglBc;
        public TextView tvAbc0YgglCa;
        public TextView tvAbc0DlzkAb;
        public TextView tvAbc0DlzkBc;
        public TextView tvAbc0DlzkCa;
        public TextView tvAbc0DlgkAb;
        public TextView tvAbc0DlgkBc;
        public TextView tvAbc0DlgkCa;
        public TextView tvAbc0RzdgAb;
        public TextView tvAbc0RzdgBc;
        public TextView tvAbc0RzdgCa;
        public TextView tvAbc0ZkdyAb;
        public TextView tvAbc0ZkdyBc;
        public TextView tvAbc0ZkdyCa;
        public TextView tvAbc0ZkdyZkBfh;
        public TextView tvAbc0ZkwcDZkBfh;
        public TextView tvAbc0Daochu;

    }
}
