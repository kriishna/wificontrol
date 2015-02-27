package wificontrol.lichuang.com.wificontrol.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.Util.SysUtil;
import wificontrol.lichuang.com.wificontrol.adapter.SocketAdapter;
import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;
import wificontrol.lichuang.com.wificontrol.device.WifiDevice;
import wificontrol.lichuang.com.wificontrol.device.WifiSocket;
import wificontrol.lichuang.com.wificontrol.service.UdpCommunicationManager;

public class SocketManageMain extends BaseActivity implements ChazuoManager.ActivityChazuoListListener,OnClickListener{

    private ListView chazuo_list;
    private SocketAdapter socketAdapter;
    private ImageButton back_imbt;
    private ImageButton list_refresh_imbt;

    private List<WifiDevice> deviceList;

    private UdpCommunicationManager udpManager;

    private ProgressDialog m_pDialog;

    private Timer mTimer;
    private int broad_num =0;
    private ChazuoManager chazuoManager;
    private WifiSocket wifiSocket;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 100001:
                    m_pDialog.cancel();
                    socketAdapter.notifyDataSetChanged();
                    break;
                case 100003:
                    mTimer.schedule(new TimerTask(){
                        @Override
                        public void run() {
//                            chazuoManager.AddBroad("发送");  //添加广播发送命令
                            mTimer.schedule(new TimerTask(){
                                @Override
                                public void run() {
                                    DealBroadcast();
                                    if(broad_num == 2){
                                        broad_num =0;
                                        cancel();

                                    }
                                }
                            },1000,30000);

                        }
                    },1000,600000);
                    ShowDialog("正在搜索插座");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.socket_manage_main);
        deviceList = new ArrayList<WifiDevice>();
        mTimer = new Timer();
        chazuoManager = SysApplication.getInstance().getCmInstance();
        wifiSocket = new WifiSocket();
        InitView();
        WifiConfig();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra("mac")){
            String mac =intent.getStringExtra("mac");
            for(WifiDevice wd:deviceList){
                if(wd.wifi_mac_address.equals(mac)){

                    deviceList.remove(wd);
                    break;
                }
            }
            List<Map<String, Object>> testData = GetData(deviceList);
            socketAdapter.SetData(testData);
            mHandler.sendEmptyMessage(100001);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.LogInfo("Socket Manage Main", "onDestroy");
        mTimer.cancel();
        udpManager.DeleteCzListListener(this);
        udpManager.StopThread();
        udpManager.ReleaseSource();
//        myWifiManager.UnLockWifi();
    }




    private void WifiConfig(){
        deviceList = new ArrayList<WifiDevice>();
        udpManager = UdpCommunicationManager.GetInstance(getApplicationContext());
        udpManager.AddCzListListener(this);
        udpManager.StartThread();
        formatIpAddress();
//        myWifiManager.LockWifi();
        mHandler.sendEmptyMessage(100003);//等待wifi路由连接上之后开始搜索插座
    }

    private void ShowDialog(String msg){
        m_pDialog = new ProgressDialog(SocketManageMain.this);

        // 设置进度条风格，风格为圆形，旋转的
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // 设置ProgressDialog 标题
        m_pDialog.setTitle("提示");

        // 设置ProgressDialog 提示信息
        m_pDialog.setMessage(msg);

        // 设置ProgressDialog 标题图标
        m_pDialog.setIcon(R.drawable.waiting);

        // 设置ProgressDialog 的进度条是否不明确
        m_pDialog.setIndeterminate(false);

        // 设置ProgressDialog 是否可以按退回按键取消
        m_pDialog.setCancelable(false);   //不可取消

        //设置ProgressDialog 的一个Button
        m_pDialog.setButton(DialogInterface.BUTTON_POSITIVE,"取消",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i)
            {
                //点击“确定按钮”取消对话框
                dialog.cancel();
            }
        });


        // 让ProgressDialog显示
        m_pDialog.show();
    }

    private void DealBroadcast(){
        broad_num++;
        if(broad_num< 2){
            chazuoManager.AddBroad("发送");  //添加广播发送命令

        }

        if(broad_num==2){
            //开始踢掉未上线插座
            List<WifiDevice> dl = new ArrayList<WifiDevice>();
            for(WifiDevice wd:deviceList){
                if(wd.renew_flag == 2){
                    dl.add(wd);
                }
            }
            deviceList.clear();
            deviceList.addAll(dl);
            for(WifiDevice wd:deviceList){
                wd.renew_flag =1;//将标志位置回1
            }

            List<Map<String, Object>> testData = GetData(deviceList);
            socketAdapter.SetData(testData);
            mHandler.sendEmptyMessage(100001);


        }
    }


    private void formatIpAddress() {
        WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo =  mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = (ipAddress & 0xFF ) + "." +
                   ((ipAddress >> 8 ) & 0xFF) + "." +
                    ((ipAddress >> 16 ) & 0xFF) + "." +
                    ( ipAddress >> 24 & 0xFF) ;
        SysUtil.MYIPADDRESS = ip;
         MyLog.LogInfo("WifiAdminNew","ip="+ip);

    }

    private void InitView(){
        chazuo_list = (ListView) findViewById(R.id.socket_manage_main_socket_list);
        // 加载插座的listview
        List<Map<String, Object>> testData = GetData(deviceList);
        socketAdapter = new SocketAdapter(SocketManageMain.this, testData);
        chazuo_list.setAdapter(socketAdapter);
        chazuo_list.setOnItemClickListener(new SocketItemClickListener());
        chazuo_list.setOnItemLongClickListener(new SocketItemLongClickListener());

        back_imbt=(ImageButton) findViewById(R.id.socket_manage_main_back_img);
        back_imbt.setOnClickListener(this);
        list_refresh_imbt = (ImageButton) findViewById(R.id.socket_manage_main_refresh_img);
        list_refresh_imbt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socket_manage_main_back_img:
                finish();
                break;
            case R.id.socket_manage_main_refresh_img:
                mTimer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        DealBroadcast();
                        if(broad_num == 2){
                            broad_num =0;
                            cancel();

                        }
                    }
                },200,5000);
                break;
        }
    }

    private class SocketItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WifiDevice wd = deviceList.get(position);
            if(wd.is_selected == false){
                byte[] cmd2 = wifiSocket.socketMarkEditCommand(wd.wifi_mac_address,wd.modbus_address,(byte)255);
                chazuoManager.AddCmd(wd.wifi_mac_address, cmd2);
                view.setBackgroundColor(getResources().getColor(R.color.list_item_select));
                wd.is_selected = true;
            }else{
                byte[] cmd2 = wifiSocket.socketMarkEditCommand(wd.wifi_mac_address,wd.modbus_address,(byte)0);
                chazuoManager.AddCmd(wd.wifi_mac_address, cmd2);
                view.setBackgroundColor(getResources().getColor(R.color.list_item_noselect));
                wd.is_selected = false;
            }

        }
    }

    private class SocketItemLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();

            WifiDevice wd = deviceList.get(position);

            if(wd.is_selected){
                view.setBackgroundColor(getResources().getColor(R.color.list_item_noselect));
            }
            bundle.putString("ip", wd.ip_address);
            bundle.putString("mac", wd.wifi_mac_address);
            bundle.putString("modbus", wd.modbus_address);
            Intent intent = new Intent();
            intent.putExtra("value", bundle);
            intent.setClass(SocketManageMain.this, SocketControl.class);
            startActivity(intent);
            return true;
        }
    }

    private List<Map<String, Object>> GetData(List<WifiDevice> dl) {
        List<Map<String, Object>> testData = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < dl.size(); i++) {
            WifiDevice wd = dl.get(i);
            MyLog.LogInfo("Socket Manage Main ", "ip=" + wd.ip_address);
            String[] strr = wd.ip_address.split("\\.");
            String name = strr[strr.length - 1];
            Map<String, Object> socket_item = new HashMap<String, Object>();
            socket_item.put("socket_img", R.drawable.socket);
            socket_item.put("socket_state", "在线");
            socket_item.put("socket_modbus",wd.modbus_address);
            socket_item.put("socket_name", "插座" + name);
            testData.add(socket_item);
        }
//        if (dl.size() == 0) {
//            Map<String, Object> socket_item = new HashMap<String, Object>();
//            socket_item.put("socket_img", R.drawable.socket);
//            socket_item.put("socket_state", "在线");
//            socket_item.put("socket_modbus","01");
//            socket_item.put("socket_name", "测试插座");
//            testData.add(socket_item);
//        }

        return testData;
    }

    @Override
    public void SetChazuoDevice(WifiDevice device) {
        boolean isHave = false;
        if(deviceList.size() > 0){
            for(WifiDevice wd:deviceList){
                if(wd.wifi_mac_address.equals(device.wifi_mac_address)){
                    isHave=true;
                    wd.renew_flag =2;
                    if(!wd.ip_address.equals(device.ip_address)){
                        wd.ip_address = device.ip_address;
//                        List<Map<String, Object>> testData = GetData(deviceList);
//                        socketAdapter.SetData(testData);
//                        mHandler.sendEmptyMessage(100001);
                    }
                    break;
                }
            }
        }
        if(!isHave){
            device.renew_flag = 2;
            deviceList.add(device);
            List<Map<String, Object>> testData = GetData(deviceList);
            socketAdapter.SetData(testData);
            mHandler.sendEmptyMessage(100001);
        }
    }
}