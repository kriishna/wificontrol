package wificontrol.lichuang.com.wificontrol.service;

import android.content.Context;

import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;
import wificontrol.lichuang.com.wificontrol.service.runnable.RunnableCreator;
import wificontrol.lichuang.com.wificontrol.service.runnable.UdpBroadRunnable;
import wificontrol.lichuang.com.wificontrol.service.runnable.UdpGetMsgRunnable;
import wificontrol.lichuang.com.wificontrol.service.runnable.UdpHandleMsgRunnable;
import wificontrol.lichuang.com.wificontrol.service.runnable.UdpSendMsgRunnable;

/**
 * Created by Administrator on 2014/12/26.
 */
public class UdpCommunicationManager {

    private ChazuoManager chazuoManager;

    private static UdpCommunicationManager udpCommunicationManager;

    private RunnableCreator runnableCreator;
    private Thread broadThread;
    private Thread getMsgThread;
    private Thread sendMsgThread;
    private Thread handleMsgThread;

    private  UdpCommunicationManager(Context context){
        chazuoManager = ChazuoManager.GetInstance(context);

        runnableCreator = new RunnableCreator(context);
        UdpBroadRunnable broadRunnable = (UdpBroadRunnable)runnableCreator.CreateRunnable(RunnableCreator.RunnableType.UDP广播);
        UdpGetMsgRunnable getMsgRunnable = (UdpGetMsgRunnable)runnableCreator.CreateRunnable(RunnableCreator.RunnableType.UDP接收);
        UdpSendMsgRunnable sendMsgRunnable = (UdpSendMsgRunnable)runnableCreator.CreateRunnable(RunnableCreator.RunnableType.UDP发送);
        UdpHandleMsgRunnable handleMsgRunnable = (UdpHandleMsgRunnable)runnableCreator.CreateRunnable(RunnableCreator.RunnableType.UDP数据处理);

        broadThread = new Thread(broadRunnable);
        getMsgThread = new Thread(getMsgRunnable);
        sendMsgThread = new Thread(sendMsgRunnable);
        handleMsgThread = new Thread(handleMsgRunnable);
    }

    public static UdpCommunicationManager GetInstance(Context context ){
        if (udpCommunicationManager == null){
            udpCommunicationManager = new UdpCommunicationManager(context);
        }
        return udpCommunicationManager;
    }



    /**
     * 添加要执行的命令，以及在命令中发送的数据
     * @param mac_addr
     * @param data
     */
    public void AddCmd(String mac_addr,byte[] data){
       chazuoManager.AddCmd(mac_addr,data);
    }


    public void AddCzListListener(ChazuoManager.ActivityChazuoListListener listener){
        chazuoManager.AddChazuoListListener(listener);
    }

    public void DeleteCzListListener(ChazuoManager.ActivityChazuoListListener listener){
        chazuoManager.DeleteChazuoListListener(listener);
    }

    public void AddWcListener(ChazuoManager.ActivityWifiConfigListener listener){
        chazuoManager.AddWifiConfigListener(listener);
    }

    public void DeleteWcListener(ChazuoManager.ActivityWifiConfigListener listener){
        chazuoManager.DeleteWifiConfigListener(listener);
    }

    public void AddCcListener(ChazuoManager.ActivityChazuoConfigListener listener){
        chazuoManager.AddChazuoConfigListener(listener);
    }

    public void DeleteCcListener(ChazuoManager.ActivityChazuoConfigListener listener){
        chazuoManager.DeleteChazuoConfigListener(listener);
    }

    public void StartThread(){
        chazuoManager.StartThread();
        broadThread.start();
        getMsgThread.start();
        sendMsgThread.start();
        handleMsgThread.start();
    }

    public void StopThread(){
        chazuoManager.StopThread();
    }
    public void ReleaseSource(){
        udpCommunicationManager = null;
    }

}
