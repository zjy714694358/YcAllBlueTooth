package com.yc.allbluetooth.bianbi.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.bianbi.util.ChanpinShouce;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChanpinScFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChanpinScFragment extends Fragment implements View.OnClickListener {

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
    int type = 1;

    public ChanpinScFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChanpinScFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChanpinScFragment newInstance(String param1, String param2) {
        ChanpinScFragment fragment = new ChanpinScFragment();
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
        View mainView = inflater.inflate(R.layout.fragment_chanpin_sc,null);
        initView(mainView);
        return mainView;
        //return inflater.inflate(R.layout.fragment_chanpin_sc, container, false);
    }
    public void initView(View view){
        ivCpsc = view.findViewById(R.id.ivBbXtCpsc);
        llShang = view.findViewById(R.id.llBbXtCpscShang);
        llXia = view.findViewById(R.id.llBbXtCpscXia);
        llShang.setOnClickListener(this);
        llXia.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llBbXtCpscShang){//上一页
            //ivCpsc.setImageAlpha(R.drawable.cpsc06);
            if(type > 1){
                type-=1;
            }else{
                type = 6;
            }
        } else if (v.getId() == R.id.llBbXtCpscXia) {//下一页
            if(type < 6){
                type+=1;
            } else {
                type = 1;
            }
        }
        ChanpinShouce chanpinShouce = new ChanpinShouce();
        chanpinShouce.qiehuan(getActivity(),type,ivCpsc);
    }
}