package wificontrol.lichuang.com.wificontrol.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Administrator on 2015/1/24.
 */
public class WifiStateReceiver extends BroadcastReceiver {
    private Handler mHandler;

    public WifiStateReceiver (Context context,Handler handler){
           mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        System.out.println(intent.getAction());
        if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION))   //wifi得打开与关闭
        {
//WIFI开关
            int wifistate=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_DISABLED);
            if(wifistate==WifiManager.WIFI_STATE_DISABLED)
            {//如果关闭

            }
            if(wifistate == WifiManager.WIFI_STATE_ENABLED){
                mHandler.sendEmptyMessage(600009);
            }
        }

    }

    public int getStrength(Context context)
    {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
// 链接速度
// int speed = info.getLinkSpeed();
// // 链接速度单位
// String units = WifiInfo.LINK_SPEED_UNITS;
// // Wifi源名称
// String ssid = info.getSSID();
            return strength;
        }
        return 0;
    }

}
