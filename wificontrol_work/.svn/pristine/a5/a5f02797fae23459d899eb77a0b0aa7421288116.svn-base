package wificontrol.lichuang.com.wificontrol.service.runnable;

import android.content.Context;

/**
 * Created by Administrator on 2014/12/20.
 */
public class RunnableCreator {
    private Context mContext;

    public RunnableCreator(Context context){
        mContext = context;
    }

    public BaseRunnable CreateRunnable(RunnableType rt){
        switch(rt){
            case UDP广播:
                return new UdpBroadRunnable(mContext);

            case UDP发送:
                return new UdpSendMsgRunnable(mContext);


            case UDP接收:
                return new UdpGetMsgRunnable(mContext);

            case UDP数据处理:
                return new UdpHandleMsgRunnable(mContext);

            default :
                return null;
        }

    }

    public enum RunnableType{
        UDP广播,
        UDP发送,
        UDP接收,
        UDP数据处理
    }
}
