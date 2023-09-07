package com.yc.allbluetooth.std.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yc.allbluetooth.R;
import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.std.fragment.xtsz.StdYiqiShezhiFragment;

import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StdXitongShezhiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StdXitongShezhiFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout llYqSz;
    private LinearLayout llTxCs;
    private LinearLayout llNbTs;

    int xtszType = 1;//1:仪器设置；2：通讯参数；3：内部调试

    public StdXitongShezhiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StdXitongShezhiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StdXitongShezhiFragment newInstance(String param1, String param2) {
        StdXitongShezhiFragment fragment = new StdXitongShezhiFragment();
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
        //return inflater.inflate(R.layout.fragment_xitong_shezhi, container, false);
        View mainView = inflater.inflate(R.layout.fragment_std_xitong_shezhi,null);
        initView(mainView);
        return mainView;
    }
    public void initView(View view){
        Config.ymType = "stdXtsz";
        StdYiqiShezhiFragment yiqiShezhiFragment = StdYiqiShezhiFragment.newInstance("","");
        //必需继承FragmentActivity,嵌套fragment只需要这行代码
        getChildFragmentManager().beginTransaction().replace(R.id.frameXitongShezhi, yiqiShezhiFragment,"yiqiShezhiFragment").commit();

        llYqSz = view.findViewById(R.id.llXtSzYqSz);
        llTxCs = view.findViewById(R.id.llXtSzTxCs);
        llNbTs = view.findViewById(R.id.llXtSzNbTs);
        llYqSz.setOnClickListener(this);
        llTxCs.setOnClickListener(this);
        llNbTs.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        //获取Fragment子管理器
        FragmentManager fragmentManager = getChildFragmentManager();
        //通过设置的tag获取到存在的子Fragment
        Fragment childFragment = fragmentManager.findFragmentByTag("yiqiShezhiFragment");
        childFragment.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llXtSzYqSz:
                //仪器设置
                StdYiqiShezhiFragment yiqiShezhiFragment = StdYiqiShezhiFragment.newInstance("","");
                //必需继承FragmentActivity,嵌套fragment只需要这行代码
                getChildFragmentManager().beginTransaction().replace(R.id.frameXitongShezhi, yiqiShezhiFragment,"yiqiShezhiFragment").commit();
                break;
//            case R.id.llXtSzTxCs:
//                //通讯参数
//                TongxunCanshuFragment tongxunCanshuFragment = TongxunCanshuFragment.newInstance("","");
//                //必需继承FragmentActivity,嵌套fragment只需要这行代码
//                getChildFragmentManager().beginTransaction().replace(R.id.frameXitongShezhi, tongxunCanshuFragment,"tongxunCanshuFragment").commit();
//                break;
//            case R.id.llXtSzNbTs:
//                //内部调试
//                NeibuTiaoshiFragment neibuTiaoshiFragment = NeibuTiaoshiFragment.newInstance("","");
//                //必需继承FragmentActivity,嵌套fragment只需要这行代码
//                getChildFragmentManager().beginTransaction().replace(R.id.frameXitongShezhi, neibuTiaoshiFragment,"neibuTiaoshiFragment").commit();
//                break;
        }
    }
}