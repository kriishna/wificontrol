package wificontrol.lichuang.com.wificontrol.service.runnable;

import android.content.Context;

import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;

/**
 * Created by Administrator on 2014/12/20.
 */
public class UdpSendMsgRunnable implements BaseRunnable{

    private ChazuoManager chazuoManager;

    public UdpSendMsgRunnable(Context context){
        chazuoManager = SysApplication.getInstance().getCmInstance();
    }

    @Override
    public void run() {
        try {
            chazuoManager.SendCmd();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void GetName() {

    }
}
