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
import com.yc.allbluetooth.std.util.LxDzlList;
import com.yc.allbluetooth.std.util.StdCsDlList;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdZzCsStdCsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdZzCsStdCsFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvDl;
    private TextView tvDz;
    private TextView tvCsXb;
    private LinearLayout llCsDl;
    private TextView tvDzType;
    private LinearLayout llLxDz;

    public StdZzCsStdCsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StdZzCsStdCsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdZzCsStdCsFragment newInstance(String param1, String param2) {
        StdZzCsStdCsFragment fragment = new StdZzCsStdCsFragment();
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
        View mainView = inflater.inflate(R.layout.fragment_std_zz_cs_std_cs,null);
        initView(mainView);
        return mainView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_std_zz_cs_std_cs, container, false);
    }
    public void initView(View view){
        tvDl = view.findViewById(R.id.tvZzCsStdCsCsDl);
        tvDz = view.findViewById(R.id.tvZzCsStdCsCsDz);
        llCsDl = view.findViewById(R.id.llZzCsStdCsCsDlJiantou);
        tvCsXb = view.findViewById(R.id.tvZzCsStdCsCsXb);
        tvDzType = view.findViewById(R.id.tvZzCsStdCsLxDz);
        llLxDz = view.findViewById(R.id.llZzCsStdCsLxDzJiantou);
        llCsDl.setOnClickListener(this);
        llLxDz.setOnClickListener(this);
        if(StringUtils.isEquals("31", Config.yqlx)){
            tvDl.setText("5A+5A");
            tvDz.setText("（2mΩ~1.2Ω）");
        }else if(StringUtils.isEquals("34", Config.yqlx)){
            tvDl.setText("10A+10A");
            tvDz.setText("（1mΩ~0.6Ω）");
        }else if(StringUtils.isEquals("35", Config.yqlx)){
            tvDl.setText("20A+20A");
            tvDz.setText("（0.5mΩ~0.3Ω）");
        }else if(StringUtils.isEquals("36", Config.yqlx)){
            tvDl.setText("25A+25A");
            tvDz.setText("（0.4mΩ~0.1Ω）");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llZzCsStdCsCsDlJiantou:
                String dlValStr = tvDl.getText().toString();
                StdCsDlList stdCsDlList = new StdCsDlList();
                Log.e("===",Config.yqlx);
                if(StringUtils.isEquals("31", Config.yqlx)){
                    stdCsDlList.DlAndDz_10(dlValStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("34", Config.yqlx)){
                    stdCsDlList.DlAndDz_20(dlValStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("35", Config.yqlx)){
                    stdCsDlList.DlAndDz_40(dlValStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("36", Config.yqlx)){
                    stdCsDlList.DlAndDz_50(dlValStr,tvDl,tvDz);
                }
                break;
            case R.id.llZzCsStdCsLxDzJiantou://零线电阻====》=8中性点电阻测试
                String dzTypeValStr = tvDzType.getText().toString();
                LxDzlList lxDzlList = new LxDzlList();
                lxDzlList.DzType(dzTypeValStr,tvDzType);
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
        String csdlStr = tvDl.getText().toString();
        String csxbStr = tvCsXb.getText().toString();
        String lxdzStr = tvDzType.getText().toString();
        Log.e("三通道测试",csdlStr+csxbStr+lxdzStr);

        TestSet testSet = new TestSet();
        testSet.setDl(csdlStr);
        testSet.setXw(csxbStr);
        testSet.setDz(lxdzStr);
        testSet.setZc("OFF");
        list.add(testSet);
        //return csdlStr+","+csxbStr+","+lxdzStr;
        return list;
    }
}