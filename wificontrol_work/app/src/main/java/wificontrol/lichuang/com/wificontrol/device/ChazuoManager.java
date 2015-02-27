package wificontrol.lichuang.com.wificontrol.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.Util.SysUtil;
import wificontrol.lichuang.com.wificontrol.device.model.CmdParcelable;
import wificontrol.lichuang.com.wificontrol.wifi.WifiAdmin;

/**
 *
 * Created by Administrator on 2014/12/20.
 */
public class ChazuoManager {

    private static ChazuoManager chazuoManager;


    private Queue<HashMap<String,byte[]>> comdDataList;
    private Queue<byte[]> resultData;
    private Queue<String>    broadCmdList; //
    private DatagramSocket datagramSocket;


    private List<WifiDevice> wifi_list;
    private List<ActivityChazuoListListener> chazuolist_listener_list;
    private List<ActivityWifiConfigListener> wificonfig_listener_list;
    private List<ActivityChazuoConfigListener>  chazuoconfig_listener_list;

    private boolean broad_run_flag= true;
    private boolean getmsg_run_flag = true;
    private boolean sendmsg_run_flag = true;
    private boolean handlemsg_run_flag = true;

    private WifiSocket wifiSocket;
    private ChazuoDevice chazuoDevice;
    private Context mContext;




    private ChazuoManager(Context context)  {
        mContext = context;
//        comdTargetList = new LinkedList<String>();
        comdDataList = new LinkedList<HashMap<String,byte[]>>();
        resultData = new LinkedList<byte[]>();
        broadCmdList = new LinkedList<String>();

        try {
            datagramSocket = new DatagramSocket(SysUtil.PORT);
//            datagramSocket.setSoTimeout(10000);     //设置接收数据时最长阻塞时间为3秒
        } catch (SocketException e) {
            e.printStackTrace();
        }

//
//        try {
//            ms = new MulticastSocket();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        wifi_list = new ArrayList<WifiDevice>();
        chazuolist_listener_list = new ArrayList<ActivityChazuoListListener>();
        wificonfig_listener_list = new ArrayList<ActivityWifiConfigListener>();
        chazuoconfig_listener_list = new ArrayList<ActivityChazuoConfigListener>();

        wifiSocket = new WifiSocket();
        chazuoDevice = new ChazuoDevice();

    }

    /**
     *  生成Manager 实例
     * @param context
     * @return
     */
    public static ChazuoManager GetInstance(Context context){
        if(chazuoManager == null){
            chazuoManager = new ChazuoManager(context);
        }
        return chazuoManager;
    }


    /**
     * 将命令添加至队列准备发送
     * @param mac_addr    要发送的目的设备mac 地址
     * @param data        要发送的命令本身
     */
    public void AddCmd(String mac_addr,byte[] data){
        synchronized (comdDataList){
            MyLog.LogInfo("ChazuoManager","添加命令。。。");
            HashMap<String,byte[]> tempMap = new HashMap<String,byte[]>();
            byte[] mac_bytes= mac_addr.getBytes();
//            comdTargetList.offer(mac_addr);
            tempMap.put("mac",mac_bytes);
            tempMap.put("cmd",data);
            comdDataList.offer(tempMap);
        }

    }


    public void AddBroad(String flag){
        synchronized (broadCmdList){
            broadCmdList.offer(flag);
        }
    }

    public void AddChazuoListListener(ActivityChazuoListListener listener){
        chazuolist_listener_list.add(listener);
    }

    public void DeleteChazuoListListener(ActivityChazuoListListener listener){
        chazuolist_listener_list.remove(listener);
    }

    public void AddWifiConfigListener(ActivityWifiConfigListener listener ){
        wificonfig_listener_list.add(listener);
    }

    public void DeleteWifiConfigListener(ActivityWifiConfigListener listener){
        wificonfig_listener_list.remove(listener);
    }

    public void AddChazuoConfigListener(ActivityChazuoConfigListener listener){
        chazuoconfig_listener_list.add(listener);
    }

    public void DeleteChazuoConfigListener(ActivityChazuoConfigListener listener){
        chazuoconfig_listener_list.remove(listener);
    }



    /**
     * 发送广播
     */
    public void SendBroadCast(){

        while(broad_run_flag){
            while(broadCmdList.size() > 0){
                String flag = broadCmdList.poll();
                DatagramPacket dataPacket = GetPacket();
                try {
                    MyLog.LogInfo("chazuomanager","发送广播");
                    datagramSocket.send(dataPacket);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    //得到广播数据报
    private DatagramPacket GetPacket(){
        DatagramPacket dataPacket = null;
        //224.0.0.1为广播地址
        InetAddress address = null;
        String broad_ip = GetBroadIp();
        MyLog.LogInfo("ChazuoManager","广播地址:"+broad_ip);
        try {
            address = InetAddress.getByName(broad_ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] cmd = wifiSocket.searchSocketCommand(GetAddrBytes());
        //这个地方可以输出判断该地址是不是广播类型的地址
        System.out.println(address.isMulticastAddress());
        dataPacket = new DatagramPacket(cmd, cmd.length, address,
                9999);
        return dataPacket;
    }

    /**
     * 发送插座指令
     */
    public void SendCmd() throws InterruptedException{
        while(sendmsg_run_flag){

            while(comdDataList.size() > 0){
                HashMap<String,byte[]> hashMap = comdDataList.poll();
                String mac = new String(hashMap.get("mac"));
                byte[] cmd = hashMap.get("cmd");
//                List<String> tempList = new ArrayList<String>(); //将byte数据都转换成16进制字符串并暂存在list里面虚
//                String tempStr;
//                for (int i =0;i< cmd.length;i++ ){
//                    tempStr = Integer.toHexString(cmd[i]&0xff);
//                    if (tempStr.length() < 2){
//                        tempStr= "0"+tempStr ;
//                    }
//                    tempList.add(tempStr);
//                }
                if(cmd == null){
                    MyLog.LogInfo("ChazuoManager","发送命令为空");
                    continue;
                }else{
                    MyLog.LogInfo("ChazuoManager","发送命令");
                }

                if(wifi_list.size() > 0){
                    for(WifiDevice device:wifi_list){
                        if(mac.equals(device.wifi_mac_address)){
                            InetAddress address = null;

                            try {
                                MyLog.LogInfo("ChazuoManager sendCmd","ip+"+device.ip_address);
                                address = InetAddress.getByName(device.ip_address);
                                DatagramPacket packet = new DatagramPacket(cmd,cmd.length,address,9999);
                                datagramSocket.send(packet);

                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }catch(IOException e){
                                e.printStackTrace();
                            }

                            MyLog.LogInfo("ChazuoManager","已发送命令");
                            break;
                        }
                    }
                }


            }
        }

    }

    /**
     * 接收插座回复
     */
    public void GetReply(){
        byte[] data = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(data,data.length);
        while(getmsg_run_flag){

            try {
                datagramSocket.receive(receivePacket);
                byte[] destbuf = Arrays.copyOfRange(receivePacket.getData(), receivePacket.getOffset(),
                        receivePacket.getOffset() + receivePacket.getLength());
                resultData.offer(destbuf);
                receivePacket.setLength(1024);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * 处理插座回复
     */
    public void HandleReply(){
        while(handlemsg_run_flag){
            while(resultData.size() > 0){
                MyLog.LogInfo("Chazuo manager","resultData.size >0");
                byte[] data = resultData.poll();
                List<String> tempList = new ArrayList<String>(); //将byte数据都转换成16进制字符串并暂存在list里面虚
                String tempStr;
                for (int i =0;i< data.length;i++ ){
                    tempStr = Integer.toHexString(data[i]&0xff);
                    if (tempStr.length() < 2){
                        tempStr= "0"+tempStr ;
                    }
                    tempList.add(tempStr);
                }
                MyLog.LogInfo("chazuo manager","处理数据"+tempList.toString());

                if("fe".equals(tempList.get(0)) && "a5".equals(tempList.get(1))){
                    //wifi部分的命令解析
                    String cmd_flag = tempList.get(11);
                    if("01".equals(cmd_flag)){   //搜索命令回复
                        String ip =wifiSocket.analyseSearchSocketResponse(data);
                        MyLog.LogInfo("ChazuoManager","搜索命令回复");
                        if(ip != null){
                            MyLog.LogInfo("ChazuoManager","搜索命令回复,解析完成ip"+ip);
                            boolean isHas = false;
                            for(WifiDevice device:wifi_list){
                                if(wifiSocket.MAC_ADDRE.equals(device.wifi_mac_address)){
                                    synchronized (wifi_list){
                                        isHas = true;
                                        device.is_register = false;
                                        device.ip_address= ip;
                                        //添加命令，智能设备向插座回复确认
                                        byte[] cmd = wifiSocket.checkDeviceResponse(device.wifi_mac_address,device.modbus_address);
                                        AddCmd(device.wifi_mac_address,cmd);
                                    }

                                }
                            }
                            if(!isHas){
                                WifiDevice wifiDevice = new WifiDevice();
                                wifiDevice.wifi_mac_address = wifiSocket.MAC_ADDRE;
                                wifiDevice.modbus_address = wifiSocket.MODBUS_ADDRE;
                                wifiDevice.ip_address = ip;
                                wifi_list.add(wifiDevice);

                                //添加命令，智能设备向插座回复确认
                                byte[] cmd = wifiSocket.checkDeviceResponse(wifiDevice.wifi_mac_address,wifiDevice.modbus_address);
                                AddCmd(wifiDevice.wifi_mac_address,cmd);
                            }

                        }

                    }
                    if("02".equals(cmd_flag)){   //查询插座参数命令回复
                        Map<String,String> result = wifiSocket.analyseSocketParametersResponse(data);

                        if(result != null){
                            //传回给请求页面
                            if(wificonfig_listener_list.size() > 0 ){
                                for(ActivityWifiConfigListener listener:wificonfig_listener_list){
                                    listener.SetMapResult(result);
                                }
                            }
                        }
                    }
                    if("03".equals(cmd_flag)){   //配置插座参数命令回复
                        boolean result = wifiSocket.checkSocketParaSettingResponse(data);
                         //传回给页面配置结果
                        if(wificonfig_listener_list.size()>0){
                            for(ActivityWifiConfigListener listener:wificonfig_listener_list){
                                if(result){
                                    listener.SetStrResult("配置成功");
                                }else{
                                    listener.SetStrResult("配置失败");
                                }
                            }
                        }
                    }
                    if("04".equals(cmd_flag)){    //插座注册(wifi主动上传)
                        MyLog.LogInfo("ChazuoManager","插座注册");
                        Map<String,String> result = wifiSocket.analyseSocketRegistrationCommand(data);

                        if(result != null){
                            MyLog.LogInfo("ChazuoManager","插座注册,命令解析完成");
                            String tempMac = result.get(WifiSocket.MAC);
                            for(WifiDevice wd : wifi_list){
                                if(tempMac.equals(wd.wifi_mac_address)){
                                    if(!wd.is_register){  //监测插座是否注册
                                        wd.is_register= true;
                                        //结果传回请求页面
                                        if(chazuolist_listener_list.size() > 0){
                                            for(ActivityChazuoListListener listListener:chazuolist_listener_list){
                                                listListener.SetChazuoDevice(wd);
                                            }

                                        }
                                        byte[] cmd = wifiSocket.responseToSocketRegistrateInfo(tempMac,result.get(WifiSocket.MODBUS));
                                        AddCmd(tempMac,cmd);
                                    }

                                    break;
                                }
                            }

                        }


                    }
                    if("05".equals(cmd_flag)){    //插座心跳(wifi主动上传)

                        Map<String,String> result = wifiSocket.analyseSocketHeartBeatCommand(data);
                        MyLog.LogInfo("ChazuoManager","插座心跳"+result.toString());
                        if(result != null){
                            MyLog.LogInfo("ChazuoManager","插座心跳验证成功");
                            byte[] cmd = wifiSocket.responseToSocketHeartBeatCommand(result.get(WifiSocket.MAC),result.get(WifiSocket.MODBUS));
                            AddCmd(result.get(WifiSocket.MAC),cmd);
                        }

                    }
                    if("06".equals(cmd_flag)){    //修改插座标示
                        String result = wifiSocket.analyseSocketMarkEditResponse(data);
                        if(result != null){
                            //结果返回给调用页面   00或ff
                            if("ff".equals(result)){
                                result="红色Led点亮";
                            }else{
                                result="Led正常工作";
                            }
                            if(wificonfig_listener_list.size() > 0){
                                for(ActivityWifiConfigListener listener:wificonfig_listener_list){
                                    listener.SetStrResult(result);
                                }
                            }
                            if(chazuoconfig_listener_list.size() > 0){
                                for(ActivityChazuoConfigListener listener:chazuoconfig_listener_list){
                                    listener.SetStrResult(result);
                                }
                            }

                        }
                    }
                    if("07".equals(cmd_flag)){    //修改插座modbus
                        String modbus_addr= wifiSocket.analyseSocketModbusAddreEditResponse(data);
                        if(modbus_addr != null){
                            //结果返回给调用页面
                            //结果返回给调用页面   00或ff
                            if(wificonfig_listener_list.size() > 0){
                                for(ActivityWifiConfigListener listener:wificonfig_listener_list){
                                    listener.SetStrResult(modbus_addr);
                                }
                            }
                        }
                    }


                }else{
                    //zigbee部分的命令解析
                    Map<String,String> result = chazuoDevice.HandleData(data);
                    if(result != null){
                        if(chazuoconfig_listener_list.size() > 0){
                            for(ActivityChazuoConfigListener listener: chazuoconfig_listener_list){
                                listener.SetMapResult(result);
                            }
                        }
                    }
                }


            }
        }

    }

    public void StartThread(){
        broad_run_flag = true;
        getmsg_run_flag = true;
        sendmsg_run_flag = true;
        handlemsg_run_flag = true;
    }
    public void StopThread(){
        broad_run_flag = false;
        getmsg_run_flag = false;
        sendmsg_run_flag = false;
        handlemsg_run_flag = false;
    }

    //释放内存
    public void ReleaseSource(){
        broad_run_flag = false;
        getmsg_run_flag = false;
        sendmsg_run_flag = false;
        handlemsg_run_flag = false;
        try {
            Thread.currentThread().sleep(500);   //睡眠一段时间，等待相关线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        datagramSocket.close();
    }

    /**
     * 得到广播地址
     * @return
     */
    public String GetBroadIp(){
        System.out.println("ip address:"+SysUtil.MYIPADDRESS);
        int index = SysUtil.MYIPADDRESS.lastIndexOf('.');
        String tempStr = SysUtil.MYIPADDRESS.substring(0,index);
        tempStr = tempStr + ".255";
        return tempStr;
    }


    /**
     * 得到广播地址Byte字节数组
     * @return
     */
    public byte[] GetAddrBytes(){
        char[] cc = (SysUtil.MYIPADDRESS+"+"+SysUtil.PORT).toCharArray();
        byte[] cmd = new byte[cc.length];
        int[] tempInt = new int[cc.length];
        for(int i=0;i<cc.length;i++){
//            String tempStr = Integer.toString((int)cc[i]);
//            tempInt[i] = Integer.parseInt(tempStr,16);
            tempInt[i] = (int)cc[i];
        }

        for(int i = 0;i< cc.length;i++){

            cmd[i] = (byte)tempInt[i];
        }

        return cmd;
    }

    /**
     * 返回插座列表
     */
    public interface ActivityChazuoListListener{
        public void SetChazuoDevice(WifiDevice device);
    }

    /**
     * 插座Wifi部分配置回复
     */
    public interface ActivityWifiConfigListener{
        public void SetMapResult(Map<String,String> resultMap);   //0：失败 1:成功
        public void SetStrResult(String resultStr);
     }

    /**
     * 插座zigbee部分命令回复
     */
    public interface ActivityChazuoConfigListener{
        public void SetMapResult(Map<String,String> resultMap);
        public void SetStrResult(String resultStr);
    }



}
