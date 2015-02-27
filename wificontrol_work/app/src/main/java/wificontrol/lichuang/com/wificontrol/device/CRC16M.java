package wificontrol.lichuang.com.wificontrol.device;

/**
 * Created by Administrator on 2014/12/25.
 */
public class CRC16M {


    public int update(byte[] puchMsg, int usDataLen) {

        int TempVal = 0xffff;
        int LSB;
        long CRCValue;
        String CrcStr;
        for(int i=0;i<usDataLen;i++){
            int tempInt=0;
            if(puchMsg[i] < 0){
                tempInt = 256 + puchMsg[i];
            }else{
                tempInt = puchMsg[i];
            }
            TempVal = TempVal ^ tempInt;

            for(int j =0;j<8;j++){

                LSB = TempVal % 2;
                TempVal = TempVal / 2;
                if (LSB == 1){
                    TempVal = TempVal ^ 0xa001;
                }
            }
        }
        int high =  TempVal %256;
        int low = TempVal /256;
        String temp1 = Integer.toHexString(high);
        String temp2 = Integer.toHexString(low);

        CrcStr = temp1+"+"+temp2;
        return TempVal;
    }


}
