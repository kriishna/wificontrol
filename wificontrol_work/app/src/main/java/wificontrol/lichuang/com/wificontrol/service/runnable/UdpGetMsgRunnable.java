package wificontrol.lichuang.com.wificontrol.service.runnable;

import android.content.Context;

import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;

/**
 * Created by Administrator on 2014/12/20.
 */
public class UdpGetMsgRunnable implements BaseRunnable {
    private ChazuoManager chazuoManager;

    public UdpGetMsgRunnable(Context context){
        chazuoManager = SysApplication.getInstance().getCmInstance();
    }
    @Override
    public void run() {
        chazuoManager.GetReply();
    }

    @Override
    public void GetName() {

    }
}
