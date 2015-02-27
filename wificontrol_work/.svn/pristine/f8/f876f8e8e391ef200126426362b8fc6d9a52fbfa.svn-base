package wificontrol.lichuang.com.wificontrol.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wificontrol.lichuang.com.wificontrol.Util.MyLog;

/**
 * 插座协议
 * Created by Administrator on 2014/12/23.
 */
public class WifiSocket {
    public static String SSID_NAME = "SSID名称";
    public static String WIFI_PSWD = "WIFI密码";
    public static String SERVER_IP = "服务器IP";
    public static String SERVER_PORT = "服务器端口号";
    public static String SOCKET_STATE = "插座状态";

    public String MAC_ADDRE = "MAC地址";
    public static String MAC = "MAC地址";

    public String MODBUS_ADDRE = "MODBUS地址";
    public static String MODBUS = "MODBUS地址";
    public static String WIFI_STRENGTH = "WIFI信号强度";
    public static String SERVER_CONNECT_STATE = "服务器连接状态";

    public WifiSocket(){

    }

    //----------01H 搜索插座指令------------begin

    /**
     * 搜索插座的指令
     *
     * @return
     */
    public byte[] searchSocketCommand(byte[] address) {//address:IP+PORT
        int len = address.length;

        byte[] command = new byte[14 + len];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 0;//0x00
        command[3] = (byte) (8 + len);//len
        command[4] = (byte) 255;//0xff
        command[5] = (byte) 255;//0xff
        command[6] = (byte) 255;//0xff
        command[7] = (byte) 255;//0xff
        command[8] = (byte) 255;//0xff
        command[9] = (byte) 255;//0xff
        command[10] = (byte) 255;//0xff
        command[11] = (byte) 1;//0x01

        //ip+port
        for (int i = 0; i < len; i++) {
            command[12 + i] = address[i];
        }
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[12 + len] = (byte) num;

        command[13 + len] = (byte) 13;//0x0d

        return command;
    }

    /**
     * 解析插座对搜索指令的响应
     *
     * @param data
     * @return
     */
    public String analyseSearchSocketResponse(byte[] data) {
        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("ff".equals(tempStr) == false) {
            MyLog.LogInfo("wifisocket","验证type== ff报错");
            return null;
        }
        //判断len
        if ((data.length - 6) != (int) data[3]) {
            MyLog.LogInfo("wifisocket","验证数据长度报错");
            return null;
        }
        //判断cmd
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("01".equals(tempStr) == false) {
            MyLog.LogInfo("wifisocket","验证命令类型报错");
            return null;
        }
        //判断ender
        tempStr = Integer.toHexString(data[data.length - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            return null;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < data.length - 2; i++) {
            sum = sum + (int) (data[i]);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[data.length - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            return null;
        }

        StringBuilder tempSb = new StringBuilder();
        for (int i =4;i<10;i++){
            tempStr = Integer.toHexString(data[i] & 0xff);
            if (tempStr.length() < 2) {
                tempStr = "0" + tempStr;
            }
            tempSb.append(tempStr+"-");

        }
        MAC_ADDRE = tempSb.toString();
        MAC_ADDRE = MAC_ADDRE.substring(0,MAC_ADDRE.length()-1);
        MyLog.LogInfo("WifiSocket",MAC_ADDRE);
        MODBUS_ADDRE = Integer.toHexString(data[10]);

        //解析插座IP
        String IP_address = "";
        for (int i = 12; i < data.length - 2; i++) {
            IP_address = IP_address + (char) data[i];
        }

        return IP_address;
    }

    /**
     * 智能设备收到插座的响应后发送确认帧
     *
     * @return
     */
    public byte[] checkDeviceResponse(String mac_address,String modbus_address) {
        byte[] command = new byte[15];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 255;//0xff
        command[3] = (byte) 9;//len

        String[] strr = mac_address.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            int tempInt = Integer.parseInt(strr[i],16);
            command[4+i] = (byte)tempInt;
        }
        command[10] = Integer.valueOf(modbus_address,16).byteValue();
        command[11] = (byte) 1;//0x01
        command[12] = 0;//0x00
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[13] = (byte) num;
        command[14] = (byte) 13;//0x0d
        return command;
    }
    //----------01H 搜索插座指令------------end
    //----------02H 查询插座参数指令---------begin

    /**
     * 查询插座参数的指令
     *
     * @return
     */
    public byte[] getSocketParametersCommand( String mac_address,String modbus_addr) {
        byte[] command = new byte[15];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 0;//0x00
        command[3] = (byte) 9;//len

        String[] strr = mac_address.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            command[4+i] = Integer.valueOf(strr[i],16).byteValue();
        }
        command[10] = Integer.valueOf(modbus_addr,16).byteValue();
        command[11] = (byte) 2;//0x02
        command[12] = (byte) 0;//0x00
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[13] = (byte) num;
        command[14] = (byte) 13;//0x0d
        return command;
    }

    /**
     * 解析Wifi插座接收到查询参数指令后，应答插座现在的参数信息的应答帧
     *
     * @param data
     * @return
     */
    public Map<String, String> analyseSocketParametersResponse(byte[] data) {
        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("ff".equals(tempStr) == false) {
            return null;
        }
        //判断len
        if ((data.length - 6) != (int) data[3]) {
            return null;
        }
        //判断cmd
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("02".equals(tempStr) == false) {
            return null;
        }
        //判断ender
        tempStr = Integer.toHexString(data[data.length - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            return null;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < data.length - 2; i++) {
            sum = sum + (int) (data[i]);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[data.length - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            return null;
        }

        //解析data
        Map<String, String> result = new HashMap<String, String>();
        int len = data.length;

        String paraStr = "";

        for (int i = 12; i < len - 2; i++) {
            if (data[i] == 34) {//去掉"
                continue;
            }
            if(i== len -3){
                paraStr = paraStr+Integer.toHexString(data[i] &0xff);
            }else{
                paraStr = paraStr + (char) data[i];
            }

        }
        System.out.println("插座参数：" + paraStr);

        String para[] = paraStr.split(",");

//        len = para.length;
//        if (len != 5) {
//            return null;
//        }

        result.put(SSID_NAME, para[0]);
        result.put(WIFI_PSWD, para[1]);
        result.put(SERVER_IP, para[2]);
        result.put(SERVER_PORT, para[3]);
        result.put(SOCKET_STATE, para[4]);
        return result;
    }

    //----------02H 查询插座参数指令---------end
    //----------03H 配置插座参数指令---------begin

    /**
     * 由APP或服务器端向wifi插座发送的参数配置指令
     *
     * @param mac_addr
     * @param modbus_addr
     * @param para
     * @return
     */
    public byte[] setSocketParametersCommand(String mac_addr,String modbus_addr, byte[] para) {//address包含mac addr和modbus addr

        int para_len = para.length;

        byte[] command = new byte[14 + para_len];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 0;//0x00
        command[3] = (byte) (8 + para_len);//len

        String[] strr = mac_addr.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            command[4+i] = Integer.valueOf(strr[i],16).byteValue();
        }
        command[10] = Integer.valueOf(modbus_addr,16).byteValue();
        command[11] = (byte) 3;//0x03
        //para
        for (int i = 0; i < para_len; i++) {
            command[12 + i] = para[i];
        }
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[12 + para_len] = (byte) num;
        command[13 + para_len] = (byte) 13;//0x0d
        return command;
    }

    /**
     * 检查插座收到参数配置指令的确认帧
     *
     * @return
     */
    public boolean checkSocketParaSettingResponse(byte[] data) {
//        List<String> tempList = new ArrayList<String>(); //将byte数据都转换成16进制字符串并暂存在list里面
//        for (int i =0;i< data.length;i++ ){
//            String tempStr = Integer.toHexString(data[i]&0xff);
//            if (tempStr.length() < 2){
//                tempStr= "0"+tempStr ;
//            }
//            tempList.add(tempStr);
//        }
//        MyLog.LogInfo("Socket connect setting","配置插座收回命令"+tempList.toString());
        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("ff".equals(tempStr) == false) {
            MyLog.LogInfo("WifiSocket","首字符ff验证失败");
            return false;
        }
        //判断len
        int tempInt = (int)(data[3]&0xff);
        if ((data.length - 6) != tempInt) {
            MyLog.LogInfo("WifiSocket","数据长度验证失败");
            return false;
        }
        //判断cmd
        int datalen = data.length;
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("03".equals(tempStr) == false) {
            MyLog.LogInfo("WifiSocket","命令符验证失败");
            return false;
        }
        //判断ender
        tempStr = Integer.toHexString(data[datalen - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            MyLog.LogInfo("WifiSocket","结束符验证失败");
            return false;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < datalen - 2; i++) {
            sum = sum + (int) (data[i]&0xff);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[datalen - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            MyLog.LogInfo("WifiSocket","校验和验证失败");
            return false;
        }
        return true;
    }
    //----------03H 配置插座参数指令---------end
    //----------04H 插座注册指令------------begin

    /**
     * 解析插座发送的注册指令
     *
     * @param data
     * @return
     */
    public Map<String, String> analyseSocketRegistrationCommand(byte[] data) {

        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("00".equals(tempStr) == false) {
            return null;
        }
        //判断len
        if ((data.length - 6) != (int) data[3]) {
            return null;
        }
        //判断cmd
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("04".equals(tempStr) == false) {
            return null;
        }
        //判断ender
        tempStr = Integer.toHexString(data[data.length - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            return null;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < data.length - 2; i++) {
            sum = sum + (int) (data[i]);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[data.length - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            return null;
        }

        //解析data
        Map<String, String> result = new HashMap<String, String>();
        String str = "";
        //mac地址
        StringBuilder tempSb = new StringBuilder();
        for (int i =4;i<10;i++){
            tempStr = Integer.toHexString(data[i] & 0xff);
            if (tempStr.length() < 2) {
                tempStr = "0" + tempStr;
            }
            tempSb.append(tempStr+"-");

        }
        String tempMac = tempSb.toString();
        tempMac = tempMac.substring(0,tempMac.length()-1);
        result.put(MAC,tempMac);
        //modbus地址
        result.put(MODBUS, Integer.toHexString(data[10]));


        //WIFI强度
        result.put(WIFI_STRENGTH, String.valueOf((char) data[12]));
        //服务器连接状态
        result.put(SERVER_CONNECT_STATE, Integer.toHexString( data[13]& 0xff));
        //插座状态
        result.put(SOCKET_STATE, Integer.toHexString( data[14] & 0xff));

        return result;
    }

    /**
     * 服务器或者智能设备收到插座的注册信息后的响应
     *
     * @param
     * @return
     */
    public byte[] responseToSocketRegistrateInfo(String mac_addr,String modbus_addr) {
        byte[] command = new byte[15];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 255;//0xff
        command[3] = (byte) 9;//len

        String[] strr = mac_addr.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            command[4+i] = Integer.valueOf(strr[i],16).byteValue();
        }
        command[10] = Integer.valueOf(modbus_addr,16).byteValue();
        command[11] = (byte) 4;//0x04
        command[12] = (byte) 0;//0x00
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[13] = (byte) num;
        command[14] = (byte) 13;//0x0d
        return command;
    }
    //----------04H 插座注册指令------------end
    //----------05H 插座心跳指令------------begin

    /**
     * 插座向服务器或APP发送的心跳包
     *
     * @param data
     * @return
     */
    public Map<String, String> analyseSocketHeartBeatCommand(byte[] data) {
        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("00".equals(tempStr) == false) {
            return null;
        }
        //判断len
        if ((data.length - 6) != (int) data[3]) {
            return null;
        }
        //判断cmd
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("05".equals(tempStr) == false) {
            return null;
        }
        //判断ender
        tempStr = Integer.toHexString(data[data.length - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            return null;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < data.length - 2; i++) {
            sum = sum + (int) (data[i]);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[data.length - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            return null;
        }



        //解析data
        Map<String, String> result = new HashMap<String, String>();
        //mac地址

        StringBuilder tempSb = new StringBuilder();
        for (int i =4;i<10;i++){
            tempStr = Integer.toHexString(data[i] & 0xff);
            if (tempStr.length() < 2) {
                tempStr = "0" + tempStr;
            }
            tempSb.append(tempStr+"-");

        }
        String tempMac = tempSb.toString();
        tempMac = tempMac.substring(0,tempMac.length()-1);
        result.put(MAC,tempMac);
        //modbus地址
        result.put(MODBUS, Integer.toHexString(data[10] & 0xff));
        //WIFI强度
        result.put(WIFI_STRENGTH, String.valueOf((char) data[12]));
        //服务器连接状态
        result.put(SERVER_CONNECT_STATE, Integer.toHexString( data[13] & 0xff));
        //插座状态
        result.put(SOCKET_STATE, Integer.toHexString(data[14] & 0xff));

        return result;
    }

    /**
     * 服务器或智能设备对插座心跳指令的响应
     *
     *
     * @return
     */
    public byte[] responseToSocketHeartBeatCommand(String mac_addr,String modbus_addr) {
        byte[] command = new byte[15];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 255;//0xff
        command[3] = (byte) 9;//len
        MyLog.LogInfo("wifisocket 心跳回复","mac="+mac_addr);
        String[] strr = mac_addr.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            command[4+i] = Integer.valueOf(strr[i],16).byteValue();
        }
        command[10] = Integer.valueOf(modbus_addr,16).byteValue();
        command[11] = (byte) 5;//0x05
        command[12] = (byte) 0;//0x00
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[13] = (byte) num;
        command[14] = (byte) 13;//0x0d
        return command;
    }
    //----------05H 插座心跳指令------------end
    //----------06H 插座标示修改指令---------begin

    /**
     * 服务器或APP向插座发送的标示修改指令
     *
     * @return
     */
    public byte[] socketMarkEditCommand( String mac_addr,String modbus_addr,byte mark) {
        byte[] command = new byte[15];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 0;//0x00
        command[3] = (byte) 9;//len

        String[] strr = mac_addr.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            command[4+i] = Integer.valueOf(strr[i],16).byteValue();
        }
        command[10] = Integer.valueOf(modbus_addr,16).byteValue();
        command[11] = (byte) 6;//0x06
        command[12] = mark;
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[13] = (byte) num;
        command[14] = (byte) 13;//0x0d
        return command;
    }

    /**
     * 解析插座标示修改的响应
     *
     * @param data
     * @return
     */
    public String analyseSocketMarkEditResponse(byte[] data) {
        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("ff".equals(tempStr) == false) {
            return null;
        }
        //判断len
        if ((data.length - 6) != (int) data[3]) {
            return null;
        }
        //判断cmd
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("06".equals(tempStr) == false) {
            return null;
        }
        //判断ender
        tempStr = Integer.toHexString(data[data.length - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            return null;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < data.length - 2; i++) {
            sum = sum + (int) (data[i]);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[data.length - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            return null;
        }
        String result = Integer.toHexString(data[12] & 0xff);

        return result;
    }

    //----------06H 插座标示修改指令---------end
    //----------07H 修改插座modbus地址指令--------------begin

    /**
     * app或服务器向插座发送的修改modbus地址的指令
     *
     * @param
     * @param newModbusAddress   要设定的新的modbus地址
     * @return
     */
    public byte[] socketModbusAddrEditCommand(String mac_addr,String modbus_addr, byte newModbusAddress) {
        byte[] command = new byte[15];
        command[0] = (byte) 254;// 0xfe
        command[1] = (byte) 165;//0xa5
        command[2] = (byte) 0;//0x00
        command[3] = (byte) 9;//len

        String[] strr = mac_addr.split("-");
        for (int i = 0; i < strr.length; i++) {
//            int tempInt = Integer.valueOf(mac_list.get(i),16);
//            command[4 + i] = (byte)tempInt;
            command[4+i] = Integer.valueOf(strr[i],16).byteValue();
        }
        command[10] = Integer.valueOf(modbus_addr,16).byteValue();
        command[11] = (byte) 7;//0x07
        command[12] = newModbusAddress;
        //校验和
        int num = 0;
        for (int i = 2; i < command.length - 2; i++) {
            num = num + (int) command[i];
        }
        num = num % 256;
        command[13] = (byte) num;
        command[14] = (byte) 13;//0x0d
        return command;
    }

    /**
     * 解析插座modbus地址修改的响应
     *
     * @param data
     * @return
     */
    public String analyseSocketModbusAddreEditResponse(byte[] data) {
        String tempStr = "";
        //判断type
        tempStr = Integer.toHexString(data[2] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("ff".equals(tempStr) == false) {
            return null;
        }
        //判断len
        if ((data.length - 6) != (int) data[3]) {
            return null;
        }
        //判断cmd
        tempStr = Integer.toHexString(data[11] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("07".equals(tempStr) == false) {
            return null;
        }
        //判断ender
        tempStr = Integer.toHexString(data[data.length - 1] & 0xff);
        if (tempStr.length() < 2) {
            tempStr = "0" + tempStr;
        }
        if ("0d".equals(tempStr) == false) {
            return null;
        }

        //判断校验和 cs
        int sum = 0;
        for (int i = 2; i < data.length - 2; i++) {
            sum = sum + (int) (data[i]);
        }
        String csStr = Integer.toHexString((sum % 256) & 0xff);

        tempStr = Integer.toHexString(data[data.length - 2] & 0xff);
        if (csStr.equals(tempStr) == false) {
            return null;
        }

        String modbus_address = Integer.toHexString( data[12]);
        return modbus_address;
    }
    //----------07H 修改插座modbus地址指令--------------end
}
