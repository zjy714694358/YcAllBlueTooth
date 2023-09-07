package com.yc.allbluetooth.std.util;

import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.std.entity.TestSet;
import com.yc.allbluetooth.std.fragment.zzcs.StdZzCsDyCsFragment;
import com.yc.allbluetooth.std.fragment.zzcs.StdZzCsStdCsFragment;
import com.yc.allbluetooth.std.fragment.zzcs.StdZzCsYnCsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2022/11/7 15:18
 * author:jingyu zheng
 * 直阻测试==》开始测试
 */
public class ZzCsStartCs {
    public static List<TestSet> getChildData(Fragment fragment, int type){
        String str = "";
        List<TestSet>list = new ArrayList<>();
        if(type == 1){
            StdZzCsStdCsFragment zzCsStdCsFragment = (StdZzCsStdCsFragment)fragment.getChildFragmentManager().findFragmentByTag("zzCsStdCsFragment");
            //zzCsDyCsFragment1.initData2(false);    //调用子Fragment UsingFragment中的initData2()
            list = zzCsStdCsFragment.getData();
            //str = zzCsStdCsFragment.getData();
        }else if(type == 2){
            StdZzCsYnCsFragment zzCsYnCsFragment = (StdZzCsYnCsFragment)fragment.getChildFragmentManager().findFragmentByTag("zzCsYnCsFragment");
            list = zzCsYnCsFragment.getData();
        }else if(type == 3){
            StdZzCsDyCsFragment zzCsDyCsFragment = (StdZzCsDyCsFragment)fragment.getChildFragmentManager().findFragmentByTag("zzCsDyCsFragment");
            list = zzCsDyCsFragment.getData();
        }
        return list;
    }
}
