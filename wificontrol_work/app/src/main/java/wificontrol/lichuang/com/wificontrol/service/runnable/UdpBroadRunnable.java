package wificontrol.lichuang.com.wificontrol.service.runnable;

import android.content.Context;

import java.net.MulticastSocket;

import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;


/**
 * Created by Administrator on 2014/12/19.
 */
public class UdpBroadRunnable implements BaseRunnable {

    private byte[] cmd;
    /*发送组播的socket*/
    private MulticastSocket ms;

    private boolean isrun_flag = true;

    private ChazuoManager chaZuoManager;

    public UdpBroadRunnable(Context context){

        chaZuoManager = SysApplication.getInstance().getCmInstance();
    }



    /**
     * 设置运行标志
     * @param b
     */
    public void SetFlag(boolean b){
        isrun_flag = b;
    }

    @Override
    public void run() {
        chaZuoManager.SendBroadCast();

    }

    @Override
    public void GetName() {

    }


}
