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
import com.yc.allbluetooth.std.util.YnCsDlList;
import com.yc.allbluetooth.std.util.YnXbList;
import com.yc.allbluetooth.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdZzCsYnCsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdZzCsYnCsFragment extends Fragment implements View.OnClickListener{

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

    public StdZzCsYnCsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StdZzCsYnCsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdZzCsYnCsFragment newInstance(String param1, String param2) {
        StdZzCsYnCsFragment fragment = new StdZzCsYnCsFragment();
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
        View mainView = inflater.inflate(R.layout.fragment_std_zz_cs_yn_cs,null);
        initView(mainView);
        return mainView;
        //return inflater.inflate(R.layout.fragment_std_zz_cs_yn_cs, container, false);
    }
    public void initView(View view){
        tvDl = view.findViewById(R.id.tvZzCsYnCsCsDl);
        tvDz = view.findViewById(R.id.tvZzCsYnCsCsDz);
        llDl = view.findViewById(R.id.llZzCsYnCsCsDlJiantou);
        tvXb = view.findViewById(R.id.tvZzCsYnCsXb);
        llXb = view.findViewById(R.id.llZzCsYnCsCsXbJiantou);
        llDl.setOnClickListener(this);
        llXb.setOnClickListener(this);

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
            case R.id.llZzCsYnCsCsDlJiantou:
                String tvDlValStr = tvDl.getText().toString();
                YnCsDlList ynCsDlList = new YnCsDlList();
                if(StringUtils.isEquals("31", Config.yqlx)){
                    ynCsDlList.DlAndDz_10(tvDlValStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("34", Config.yqlx)){
                    ynCsDlList.DlAndDz_20(tvDlValStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("35", Config.yqlx)){
                    ynCsDlList.DlAndDz_40(tvDlValStr,tvDl,tvDz);
                }else if(StringUtils.isEquals("36", Config.yqlx)){
                    ynCsDlList.DlAndDz_50(tvDlValStr,tvDl,tvDz);
                }
                break;
            case R.id.llZzCsYnCsCsXbJiantou:
                String tvXbValStr = tvXb.getText().toString();
                YnXbList ynXbList = new YnXbList();
                ynXbList.Xb(tvXbValStr,tvXb);
                break;
        }
    }
    /**
     * 为了给父Fragment回传数据
     * @return
     */
    public List<TestSet> getData(){
        String dlStr = tvDl.getText().toString();
        String xbStr = tvXb.getText().toString();
        List<TestSet>list = new ArrayList<>();
        list.clear();
        Log.e("Yn测试",dlStr+xbStr);
        TestSet testSet = new TestSet();
        testSet.setDl(dlStr);
        testSet.setXw(xbStr);
        testSet.setZc("OFF");
        testSet.setDz("OFF");
        list.add(testSet);
        return list;
    }
}