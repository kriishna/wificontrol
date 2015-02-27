package wificontrol.lichuang.com.wificontrol.fragment;

import android.app.Activity;
import android.app.Fragment;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.activity.SocketManageMain;
import wificontrol.lichuang.com.wificontrol.wifi.WifiAdmin;


/**
 * Created by Administrator on 2015/1/26.
 */
public class WifiConnectedFragment extends Fragment implements View.OnClickListener{

    private TextView connectedWifi_txt;
    private Button continue_bt;
    private TextView getanother_tv;

    private WifiAdmin wifiAdmin;
    private GetAnotherWifi getAnotherInter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getAnotherInter= (GetAnotherWifi)activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wifi_connected_frag,null);
        connectedWifi_txt = (TextView)view.findViewById(R.id.wifi_connected_frag_name_tv);
        continue_bt = (Button)view.findViewById(R.id.wifi_connected_frag_continue_bt);
        continue_bt.setOnClickListener(this);
        getanother_tv = (TextView)view.findViewById(R.id.wifi_connected_frag_getanotherwifi_tv);
        getanother_tv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wifiAdmin = new WifiAdmin(getActivity());

        boolean is_wifi_enable = wifiAdmin.getWifiState();
        if(is_wifi_enable){
            WifiInfo wifiInfo = wifiAdmin.getWifiInfo();
            String ssid = wifiInfo.getSSID();
            MyLog.LogInfo("WIFIConnect", "fragment 不为空");
            SetText("已连接到热点:"+ssid);

        }
    }

    public void SetText(String str){
        connectedWifi_txt.setText(str);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.wifi_connected_frag_continue_bt:
                Intent intent = new Intent(getActivity(),SocketManageMain.class);
                startActivity(intent);
                break;
            case R.id.wifi_connected_frag_getanotherwifi_tv:
                getAnotherInter.ToWifiSelectPage();
                break;
        }
    }

    public interface GetAnotherWifi{
        public void ToWifiSelectPage();
    }

}
