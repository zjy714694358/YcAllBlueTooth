package com.yc.allbluetooth.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2023/2/27 13:47
 * author:jingyu zheng
 * 管理所有进过的Activity，任何页面调用finishAll()退出程序
 * ... onCreate(...){
 * 		...
 * 		ActivityCollector.addActivity(this);
 *        }
 *
 * 	...onDestroy(...){
 * 		...
 * 		ActivityCollector.remove(this);
 *    }
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
        activity.finish();
    }

    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
