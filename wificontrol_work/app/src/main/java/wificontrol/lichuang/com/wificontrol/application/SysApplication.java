package wificontrol.lichuang.com.wificontrol.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;

/**
 * Created by Administrator on 2014/12/19.
 */
public class SysApplication extends Application {

    //创建一个队列装载打开的activity
    private List<Activity> mList = new LinkedList<Activity>();
    //创建一个application 的单例
    private static SysApplication instance;
    private static ChazuoManager myChazuoManager;

    private static Context context;

    //默认构造函数
    public SysApplication(){}
    //返回application单例
    public synchronized static SysApplication getInstance(){
        if (null == instance) {
            instance = new SysApplication();

        }
        return instance;
    }

    public static ChazuoManager getCmInstance(){
        if(null == myChazuoManager){
            myChazuoManager = ChazuoManager.GetInstance(context);
        }
        return myChazuoManager;
    }


    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //退出程序时调用，并关闭所有打开的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
            myChazuoManager.ReleaseSource();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            System.exit(0);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.unregisterOnProvideAssistDataListener(callback);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
