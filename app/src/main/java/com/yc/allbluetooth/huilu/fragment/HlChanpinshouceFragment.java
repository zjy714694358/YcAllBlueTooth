package com.yc.allbluetooth.huilu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.huilu.util.Chanpinshouce;
import com.yc.allbluetooth.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HlChanpinshouceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HlChanpinshouceFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView ivCpsc;
    private LinearLayout llShang;
    private LinearLayout llXia;
    private LinearLayout llFanhui;
    private LinearLayout llJszb100;//技术指标
    private LinearLayout llXntd100;//性能特点
    private LinearLayout llJxt100;//接线图
    private LinearLayout llJszb200;//技术指标
    private LinearLayout llXntd200;//性能特点
    private LinearLayout llJxt200;//接线图
    int type = 1;

    public HlChanpinshouceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HlChanpinshouceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HlChanpinshouceFragment newInstance(String param1, String param2) {
        HlChanpinshouceFragment fragment = new HlChanpinshouceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_hl_chanpinshouce, container, false);
        View mainView = inflater.inflate(R.layout.fragment_hl_chanpinshouce,null);
        initView(mainView);

        return mainView;
    }
    public void initView(View view){
        ivCpsc = view.findViewById(R.id.ivHlCpsc);
        llShang = view.findViewById(R.id.llHlCpscShang);
        llXia = view.findViewById(R.id.llHlCpscXia);
        llFanhui = view.findViewById(R.id.llHlCpscFanhui);

        llJszb100 = view.findViewById(R.id.llHlCpscJszb100);
        llXntd100 = view.findViewById(R.id.llHlCpscXntd100);
        llJxt100 = view.findViewById(R.id.llHlCpscCsyujx100);
        llJszb200 = view.findViewById(R.id.llHlCpscJszb200);
        llXntd200 = view.findViewById(R.id.llHlCpscXntd200);
        llJxt200 = view.findViewById(R.id.llHlCpscCsyujx200);

        llShang.setOnClickListener(this);
        llXia.setOnClickListener(this);
        llFanhui.setOnClickListener(this);
        if(StringUtils.isEquals(Config.yqlx,"39")){
            llJszb100.setVisibility(View.VISIBLE);
            llXntd100.setVisibility(View.GONE);
            llJxt100.setVisibility(View.GONE);
            llJszb200.setVisibility(View.GONE);
            llXntd200.setVisibility(View.GONE);
            llJxt200.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llHlCpscShang){//上一页
            Chanpinshouce chanpinshouce = new Chanpinshouce();
            chanpinshouce.xianOryin(0,llJszb100,llXntd100,llJxt100,llJszb200,llXntd200,llJxt200);
        } else if (v.getId() == R.id.llHlCpscXia) {//下一页
            Chanpinshouce chanpinshouce = new Chanpinshouce();
            chanpinshouce.xianOryin(1,llJszb100,llXntd100,llJxt100,llJszb200,llXntd200,llJxt200);
        }else if (v.getId() == R.id.llHlCpscFanhui) {//返回
            getActivity().finish();
        }
    }
}