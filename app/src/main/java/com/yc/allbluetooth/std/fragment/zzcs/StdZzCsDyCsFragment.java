package com.yc.allbluetooth.std.fragment.zzcs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.std.entity.TestSet;
import com.yc.allbluetooth.std.util.DyCsDlList;
import com.yc.allbluetooth.std.util.DyXbList;
import com.yc.allbluetooth.std.util.DyZcList;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdZzCsDyCsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdZzCsDyCsFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvDl;
    private TextView tvDz;
    private LinearLayout llDl;
    private TextView tvXb;
    private LinearLayout llXb;
    private TextView tvZc;
    private LinearLayout llZc;

    public StdZzCsDyCsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StdZzCsDyCsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdZzCsDyCsFragment newInstance(String param1, String param2) {
        StdZzCsDyCsFragment fragment = new StdZzCsDyCsFragment();
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

        View mainView = inflater.inflate(R.layout.fragment_std_zz_cs_dy_cs,null);
        //return inflater.inflate(R.layout.fragment_zz_cs_dy_cs, container, false);
        initView(mainView);
        return mainView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_std_zz_cs_dy_cs, container, false);
    }
    public void initView(View view){
        tvDl = view.findViewById(R.id.tvZzCsDyCsDl);
        tvDz = view.findViewById(R.id.tvZzCsDyCsDz);
        llDl = view.findViewById(R.id.llZzCsDyCsDlJiantou);
        tvXb = view.findViewById(R.id.tvZzCsDyCsXb);
        llXb = view.findViewById(R.id.llZzCsDyCsXbJiantou);
        tvZc = view.findViewById(R.id.tvZzCsDyCsZc);
        llZc = view.findViewById(R.id.llZzCsDyCsZcJiantou);

        llDl.setOnClickListener(this);
        llXb.setOnClickListener(this);
        llZc.setOnClickListener(this);

        if(StringUtils.isEquals("31", Config.yqlx)){
            tvDl.setText("10A");
            tvDz.setText("（1mΩ~2Ω）");
        }else if(StringUtils.isEquals("34", Config.yqlx)){
            tvDl.setText("20A");
            tvDz.setText("（0.5mΩ~1Ω）");
        }else if(StringUtils.isEquals("35", Config.yqlx)){
            tvDl.setText("40A");
            tvDz.setText("（0.25mΩ~0.5Ω）");
        }else if(StringUtils.isEquals("36", Config.yqlx)){
            tvDl.setText("50A");
            tvDz.setText("（0.2mΩ~0.2Ω）");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llZzCsDyCsDlJiantou:
                String dlStr = tvDl.getText().toString();
                DyCsDlList dyCsDlList = new DyCsDlList();
                if(StringUtils.isEquals("31", Config.yqlx)){
                    dyCsDlList.DlAndDz_10(dlStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("34", Config.yqlx)){
                    dyCsDlList.DlAndDz_20(dlStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("35", Config.yqlx)){
                    dyCsDlList.DlAndDz_40(dlStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("36", Config.yqlx)){
                    dyCsDlList.DlAndDz_50(dlStr,tvDl,tvDz);
                }
                break;
            case R.id.llZzCsDyCsXbJiantou:
                String xbStr = tvXb.getText().toString();
                DyXbList dyXbList = new DyXbList();
                dyXbList.Xb(xbStr,tvXb);
                break;
            case R.id.llZzCsDyCsZcJiantou:
                String zcStr = tvZc.getText().toString();
                DyZcList dyZcList = new DyZcList();
                dyZcList.ZcType(zcStr,tvZc);
                break;
        }
    }

    /**
     * 为了给父Fragment回传数据
     * @return
     */
    public List<TestSet> getData(){
        List<TestSet>list = new ArrayList<>();
        list.clear();
        String dlStr = tvDl.getText().toString();
        String xbStr = tvXb.getText().toString();
        String zcStr = tvZc.getText().toString();
        Log.e("Dy测试",dlStr+xbStr+zcStr);
        TestSet testSet = new TestSet();
        testSet.setDl(dlStr);
        testSet.setXw(xbStr);
        testSet.setZc(zcStr);
        testSet.setDz("OFF");
        list.add(testSet);
        return list;
    }
}