package wificontrol.lichuang.com.wificontrol.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import wificontrol.lichuang.com.wificontrol.Util.MyLog;

/**
 * Created by Administrator on 2014/12/19.
 */
public class WifiAdmin {
    private  WifiManager mWifiManager;
    private WifiInfo mWifiInfo;


    public WifiAdmin(Context context) {
        //获取系统Wifi服务   WIFI_SERVICE
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //获取连接信息
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    /**
     *
     * @return true:WIFI现在可用  false:wifi现在不可用
     */
    public boolean getWifiState(){
        return mWifiManager.isWifiEnabled();
    }

    public WifiInfo getWifiInfo(){
        return mWifiInfo;
    }



}
