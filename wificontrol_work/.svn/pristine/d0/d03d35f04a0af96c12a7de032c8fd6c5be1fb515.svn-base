package wificontrol.lichuang.com.wificontrol.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
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
import android.widget.Toast;

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
import wificontrol.lichuang.com.wificontrol.wifi.WifiAdminNew;
import wificontrol.lichuang.com.wificontrol.wifi.WifiApAdmin;

public class SocketManageMainConnect extends BaseActivity implements ChazuoManager.ActivityChazuoListListener {

    private int  loginType=-1;
    private List<WifiDevice> deviceList;
    private UdpCommunicationManager udpManager;
    private ListView socket_lv;
    private ImageButton back_imbt;
    private ImageButton refresh_imbt;
    private SocketAdapter socketAdapter;

    private WifiApAdmin myWifiApAdmin;
    private WifiAdminNew myWifiAdminNew;
    private WifiConfiguration wc;

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
                    socketAdapter.notifyDataSetChanged();
                    m_pDialog.cancel();
                    break;
                case 100002:  //热点建立失败
                    Toast.makeText(SocketManageMainConnect.this,"热点建立失败",Toast.LENGTH_SHORT).show();
                    m_pDialog.cancel();
                    break;
                case 100012:    //热点建立成功后发送广播搜索插座
                    mTimer.schedule(new TimerTask(){
                        @Override
                        public void run() {
                            chazuoManager.AddBroad("发送");  //添加广播发送命令
                        }
                    },1000,30000);
                    m_pDialog.setMessage("正在搜索插座...");
                    break;

                case 100004:
                    m_pDialog.setMessage("连接路由失败");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.socket_manage_main);
        MyLog.LogInfo("Socket Manage Main","onCreate");
        mTimer = new Timer();
        chazuoManager = SysApplication.getInstance().getCmInstance();
        wifiSocket = new WifiSocket();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("value");
        loginType = bundle.getInt("type");
        MyLog.LogInfo("SocketManagemain","type="+loginType);
        switch(loginType){
            case 1: //插座出厂配置
                FirstConfig();
                break;

            default:
                break;
        }
        myWifiAdminNew = new WifiAdminNew(SocketManageMainConnect.this) {
            @Override
            public Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                registerReceiver(receiver,filter);
                return null;
            }

            @Override
            public void myUnregisterReceiver(BroadcastReceiver receiver) {
                unregisterReceiver(receiver);
            }

            @Override
            public void onNotifyWifiConnected() {

            }

            @Override
            public void onNotifyWifiConnectFailed() {

            }
        };
        InitView();   //初始化控件
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            finish();    //双击退出
        }
        return false;
    }

    private  void FirstConfig(){
        SysUtil.MYIPADDRESS="192.168.43.1";   //设置默认Android热点地址
        //        //创建wifi热点
        myWifiApAdmin = new WifiApAdmin(SocketManageMainConnect.this,mHandler);
        ShowDialog("正在创建热点...");
        myWifiApAdmin.startWifiAp("IOT_Config","jichuyanfa");

        deviceList = new ArrayList<WifiDevice>();
        udpManager = UdpCommunicationManager.GetInstance(SocketManageMainConnect.this);
        udpManager.AddCzListListener(this);
        udpManager.StartThread();

    }





    private void ShowDialog(String msg){
        m_pDialog = new ProgressDialog(SocketManageMainConnect.this);

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.LogInfo("Socket Manage Main","onDestroy");
        if(loginType == 1){
           if(myWifiApAdmin != null){
               myWifiApAdmin.closeWifiAp(SocketManageMainConnect.this);
               myWifiAdminNew.openWifi();
           }
        }else{
//            myWifiManager.UnLockWifi();
        }
        mTimer.cancel();
        udpManager.DeleteCzListListener(SocketManageMainConnect.this);
        udpManager.StopThread();   //结束后台线程
        udpManager.ReleaseSource();  //释放资源

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200001){
            if(resultCode == 203){
                String temp_mac = data.getStringExtra("result");
                for(WifiDevice device:deviceList){
                    if(temp_mac.equals(device.wifi_mac_address)){
                        deviceList.remove(device);
                        break;
                    }
                }
                List<Map<String, Object>> testData = GetData(deviceList);
                socketAdapter.SetData(testData);
                mHandler.sendEmptyMessage(100001);
            }
        }
    }

    private void InitView() {
        socket_lv = (ListView) findViewById(R.id.socket_manage_main_socket_list);
        // 加载插座的listview
        List<Map<String, Object>> testData = GetData(deviceList);
        socketAdapter = new SocketAdapter(SocketManageMainConnect.this, testData);
        socket_lv.setAdapter(socketAdapter);
        socket_lv.setOnItemClickListener(new SocketItemClickListener());
        socket_lv.setOnItemLongClickListener(new SocketItemLongClickListener());
        // 回退
        back_imbt = (ImageButton) findViewById(R.id.socket_manage_main_back_img);
        back_imbt.setOnClickListener(new ImbtClickListener());
        // 刷新当前界面
        refresh_imbt = (ImageButton) findViewById(R.id.socket_manage_main_refresh_img);
        refresh_imbt.setOnClickListener(new ImbtClickListener());
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
//            if (deviceList.size() == 0) {
//                bundle.putString("ip", "127.0.0.1");
//                bundle.putString("mac", "aaaa");
//                bundle.putString("modbus", "bbbb");
//            } else {
//                WifiDevice wd = deviceList.get(position);
//                bundle.putString("ip", wd.ip_address);
//                bundle.putString("mac", wd.wifi_mac_address);
//                bundle.putString("modbus", wd.modbus_address);
//            }
            WifiDevice wd = deviceList.get(position);
            if(wd.is_selected){
                view.setBackgroundColor(getResources().getColor(R.color.list_item_noselect));
            }
            bundle.putString("ip", wd.ip_address);
            bundle.putString("mac", wd.wifi_mac_address);
            bundle.putString("modbus", wd.modbus_address);
            bundle.putInt("logintype",loginType);
            Intent intent = new Intent();
            intent.putExtra("value", bundle);
            intent.setClass(SocketManageMainConnect.this, SocketConnectSetting.class);
            startActivityForResult(intent, 200001);
            return true;
        }
    }

    private class ImbtClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.socket_manage_main_back_img:
                    finish();
//                    Intent intent = new Intent(SocketManageMainConnect.this, SocketConnectSettingSelect.class);
//                    startActivity(intent);
                    break;
                case R.id.socket_manage_main_refresh_img:
                    //刷新列表
                    chazuoManager.AddBroad("发送");  //添加广播发送命令
                    break;
            }
        }
    }


    private List<Map<String, Object>> GetData(List<WifiDevice> dl) {
        List<Map<String, Object>> testData = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < dl.size(); i++) {
            WifiDevice wd = dl.get(i);
            MyLog.LogInfo("Socket Manage Main ","ip="+wd.ip_address);
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
                    if(!wd.ip_address.equals(device.ip_address)){
                        wd.ip_address = device.ip_address;
                        List<Map<String, Object>> testData = GetData(deviceList);
                        socketAdapter.SetData(testData);
                        mHandler.sendEmptyMessage(100001);
                    }

                    break;
                }
            }
        }
        if(!isHave){
            deviceList.add(device);
            List<Map<String, Object>> testData = GetData(deviceList);
            socketAdapter.SetData(testData);
            mHandler.sendEmptyMessage(100001);
        }

    }
}