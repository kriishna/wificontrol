package wificontrol.lichuang.com.wificontrol.device;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import wificontrol.lichuang.com.wificontrol.Util.MyLog;

/**
 * Created by Administrator on 2014/12/25.
 */
public class ChazuoDevice {
    public static String VOLTAGE = "电压";
    public static String ELEC_CURRENT = "电流";
    public static String POWER = "功率";
    public static String TOTAL_POWER = "电量总用能";
    public static String POWER_FACTOR = "功率因数";
    public static String TOTAL_DURATION = "总用电时长";
    public static String NOW_POWER = "单次电量用能";
    public static String NOW_DURATION = "单次用电时长";
    public static String VALVE_SET_RESULT = "继电器设置结果";
    public static String VALVE_LOCK_STATE = "远程锁定状态";
    public static String VALVE_STATE = "继电器状态";

    private CRC32 crc32;
    private CRC16M crc16M;

    public ChazuoDevice(){
        crc16M = new CRC16M();
        crc32 = new CRC32();
    }


    /**
     * 查询插座参数值
     * @param mod_bus_address   插座的modbus地址
     * @return
     */
    public byte[] Get_QueryPara_Cmd(String mod_bus_address){
        int tempInt = 0;
        byte[] cmd = new byte[8];
        tempInt = Integer.valueOf(mod_bus_address,16);
        cmd[0] =(byte) tempInt;
        cmd[1]= (byte) 3;
        cmd[2]= (byte) 0;
        cmd[3]= (byte) 72;
        cmd[4]= (byte) 0;
        cmd[5]= (byte) 16;
        cmd[6]= (byte) 0;
        cmd[7]= (byte) 0;
        tempInt = crc16M.update(cmd,cmd.length - 2);
        cmd[cmd.length-2]=(byte) (0xff & tempInt);
        cmd[cmd.length-1]=(byte) ((0xff00 & tempInt) >> 8);
        return cmd;

    }

    /**
     *设置插座开关
     * @param mod_bus_address 插座的modbus地址
     * @param c    插座继电器状态 'N'为开 'F' 为关
     * @return
     */
    public byte[] Get_SetValve_Cmd(String mod_bus_address,char c){
        int tempInt = 0;
        byte[] cmd = new byte[8];
        tempInt = Integer.valueOf(mod_bus_address,16);
        cmd[0] =(byte) tempInt;
        cmd[1]= (byte) 5;
        cmd[2] = (byte) 0;
        cmd[3]= (byte) 0;
        if (c== 'N'){   //继电器输出为开
            cmd[4] = (byte) 255;
        }else{
            cmd[4] = (byte) 0;
        }
        cmd[5] = (byte) 0;
        cmd[6]= (byte) 0;
        cmd[7]= (byte) 0;
        tempInt = crc16M.update(cmd,cmd.length - 2);
        cmd[cmd.length-2]=(byte) (0xff & tempInt);
        cmd[cmd.length-1]=(byte) ((0xff00 & tempInt) >> 8);
        return cmd;
    }

    /**
     * 查询插座锁定状态
     * @return
     */
    public byte[] Get_ValveLocked_Cmd(String mod_bus_address){
        int tempInt = 0;
        byte[] cmd = new byte[8];
        tempInt = Integer.valueOf(mod_bus_address,16);
        cmd[0] =(byte) tempInt;
        cmd[1]= (byte) 3;
        cmd[2]= (byte) 0;
        cmd[3]= (byte) 22;
        cmd[4]= (byte) 0;
        cmd[5] = (byte)1;
        cmd[6]= (byte) 0;
        cmd[7]= (byte) 0;
        tempInt = crc16M.update(cmd,cmd.length - 2);

        cmd[cmd.length-2]=(byte) (0xff & tempInt);
        cmd[cmd.length-1]=(byte) ((0xff00 & tempInt) >> 8);
        return cmd;

    }


    /**
     * 查询继电器开关状态
     * @param mod_bus_address
     * @return
     */
    public byte[] Get_ValveState_Cmd(String mod_bus_address){
        int tempInt = 0;
        byte[] cmd = new byte[8];
        tempInt = Integer.valueOf(mod_bus_address,16);
        cmd[0] =(byte) tempInt;
        cmd[1]= (byte) 1;
        cmd[2]= (byte) 0;
        cmd[3]= (byte) 0;
        cmd[4]= (byte) 0;
        cmd[5] = (byte)16;
        cmd[6]= (byte) 0;
        cmd[7]= (byte) 0;
        tempInt = crc16M.update(cmd,cmd.length - 2);
        cmd[cmd.length-2]=(byte) (0xff & tempInt);
        cmd[cmd.length-1]=(byte) ((0xff00 & tempInt) >> 8);
        return cmd;
    }



    public Map<String,String> HandleData(byte[] data){
        Map<String,String> result= new HashMap<String,String>();
        if(data.length == 8){   //设置单路开关量输出
            if(data[1] != 5){   //功能码等于5
                return null;
            }
            if(data[4] ==1){
                result.put(VALVE_SET_RESULT,"开");
            }else{
                result.put(VALVE_SET_RESULT,"关");
            }

            return result;
        }
        if(data.length == 7){   //读取插座锁定状态

            if(data[1]== 3 || data[1] == 1 ){
                if(data[1] == 3){   //查询插座锁定状态
                    int tempInt = data[3] & 0x01;
                    if(tempInt == 1){
                        result.put(VALVE_LOCK_STATE,"锁定");
                    }else{
                        result.put(VALVE_LOCK_STATE,"未锁定");
                    }
                    return result;
                }else{
                    int tempInt = data[3] & 0x01;
                    if(tempInt == 1){
                        result.put(VALVE_STATE,"继电器开");
                    }else{
                        result.put(VALVE_STATE,"继电器关");
                    }
                    return result;
                }
            }else{
                return null;
            }



        }
        if(data.length == 37){  //读取插座多个参数值
            if(data[1] != 3){   //功能码是否等于03
                return null;
            }
            DecimalFormat df = new DecimalFormat("#.0");
            DecimalFormat df_z = new DecimalFormat("0.0");
            DecimalFormat df2 = new DecimalFormat("#.00");
            DecimalFormat df2_z = new DecimalFormat("0.00");
            DecimalFormat df3 = new DecimalFormat("#.000");
            DecimalFormat df3_z = new DecimalFormat("0.000");

            int sign = 0;
            sign = data[22];  //为1表示反向有功功率,0表示正向有功功率
            String tempStr;
            int tempInt = 1;
            double tempDouble= 0;

            tempDouble= (tempInt*data[3]*256 + tempInt* data[4])/100.0;  //电压

            tempStr = Double.toString(tempDouble);
            result.put(VOLTAGE,tempStr);
            tempDouble= (tempInt*data[5]*256 + tempInt* data[6])/1000.0; //电流
            MyLog.LogInfo("ChazuoDevice","dianliu:"+tempDouble);
            tempStr = Double.toString(tempDouble);
            MyLog.LogInfo("ChazuoDevice","dianliu:"+tempStr);
            result.put(ELEC_CURRENT,tempStr);
            tempDouble= (tempInt*data[7]*256 + tempInt* data[8]);        //功率
            tempStr = Double.toString(tempDouble);

            result.put(POWER,tempStr);

            tempDouble= (tempInt*data[9]*256^3 + tempInt* data[10]*256^2+tempInt*data[11]*256 +tempInt*data[12])/3200.0; //总用电量
            tempStr = String.format("%.3f", tempDouble);
            result.put(TOTAL_POWER,tempStr);
            tempDouble= (tempInt*data[13]*256 + tempInt* data[14])/1000.0;  //功率因数
            tempStr = Double.toString(tempDouble);
            result.put(POWER_FACTOR,tempStr);
            tempDouble= (tempInt*data[23]*256^3 + tempInt* data[24]*256^2+tempInt*data[25]*256 +tempInt*data[26])/10.0;  //总用电时间
            tempStr = Double.toString(tempDouble);
            result.put(TOTAL_DURATION,tempStr);
            tempDouble= (tempInt*data[27]*256^3 + tempInt* data[28]*256^2+tempInt*data[29]*256 +tempInt*data[30])/3200.0; //当前用电量
            tempStr = String.format("%.3f", tempDouble);
            result.put(NOW_POWER,tempStr);
            tempDouble= (tempInt*data[31]*256^3 + tempInt* data[32]*256^2+tempInt*data[33]*256 +tempInt*data[34])/10.0; //当前用电时间
            tempStr = Double.toString(tempDouble);
            result.put(NOW_DURATION,tempStr);

            return result;
        }
        return null;
    }

}
