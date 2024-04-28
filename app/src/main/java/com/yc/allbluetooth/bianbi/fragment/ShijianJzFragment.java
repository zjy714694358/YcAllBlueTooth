package com.yc.allbluetooth.bianbi.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yc.allbluetooth.R;
import com.yc.allbluetooth.utils.EditorAction;
import com.yc.allbluetooth.utils.GetTime;
import com.yc.allbluetooth.utils.StringUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShijianJzFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShijianJzFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etNian;
    private EditText etYue;
    private EditText etRi;
    private EditText etShi;
    private EditText etFen;
    private EditText etMiao;

    public ShijianJzFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShijianJzFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShijianJzFragment newInstance(String param1, String param2) {
        ShijianJzFragment fragment = new ShijianJzFragment();
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
        View mainView = inflater.inflate(R.layout.fragment_shijian_jz,null);
        initView(mainView);
        return mainView;
        //return inflater.inflate(R.layout.fragment_shijian_jz, container, false);
    }
    public void initView(View view){
        etNian = view.findViewById(R.id.etBbXtSjjzNian);
        etYue = view.findViewById(R.id.etBbXtSjjzYue);
        etRi = view.findViewById(R.id.etBbXtSjjzRi);
        etShi = view.findViewById(R.id.etBbXtSjjzShi);
        etFen = view.findViewById(R.id.etBbXtSjjzFen);
        etMiao = view.findViewById(R.id.etBbXtSjjzMiao);

        String timeStr = GetTime.getTime(3);
        String nian = StringUtils.subStrStartToEnd(timeStr,0,2);
        String yue = StringUtils.subStrStartToEnd(timeStr,2,4);
        String ri = StringUtils.subStrStartToEnd(timeStr,4,6);
        String shi = StringUtils.subStrStartToEnd(timeStr,8,10);
        String fen = StringUtils.subStrStartToEnd(timeStr,10,12);
        String miao = StringUtils.subStrStartToEnd(timeStr,12,14);
        EditorAction editorAction = new EditorAction();
        editorAction.nian(etNian,nian);
        editorAction.yue(etYue,yue);
        editorAction.ri(etRi,etNian,etYue,ri);
        editorAction.shi(etShi,shi);
        editorAction.fen(etFen,fen);
        editorAction.miao(etMiao,miao);
        //Log.e("年=：",nian+yue+ri+shi+fen+miao+",type="+type);
        etNian.setText(nian);
        etYue.setText(yue);
        etRi.setText(ri);
        etShi.setText(shi);
        etFen.setText(fen);
        etMiao.setText(miao);
    }
}