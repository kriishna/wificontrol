package wificontrol.lichuang.com.wificontrol.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.Util.SysUtil;
import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;
import wificontrol.lichuang.com.wificontrol.device.WifiSocket;

/**
 * Created by Administrator on 2014/12/22.
 */
public class SocketConnectSetting extends BaseActivity implements ChazuoManager.ActivityWifiConfigListener{

    private EditText wifi_name_edt;
    private EditText wifi_pwd_edt;
    private EditText server_ip_edt;
    private EditText server_port_edt;


    private String chazuo_ip;
    private String chazuo_mac;
    private String chazuo_modbus;
    private int loginType;

    private WifiSocket wifiSocket;
    private ChazuoManager chazuomanager;

    private static String STORE_NAME = "setting";
    private SharedPreferences settingPreference;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 100002:
                    Bundle bundle = msg.getData();
                    String tempStr = bundle.getString("data");
                    if(loginType != 3){
                        if("配置成功".equals(tempStr)){
                            Toast.makeText(SocketConnectSetting.this,tempStr,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("result",chazuo_mac);
                            setResult(203, intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(SocketConnectSetting.this,tempStr,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(SocketConnectSetting.this,SocketManageMain.class);
                        intent.putExtra("mac",chazuo_mac);
                        startActivity(intent);
                    }


                    break;
                case 100004:
                    Bundle bundle2 = msg.getData();
                    String ssid =  bundle2.getString(WifiSocket.SSID_NAME);

                    String wifi_pwd = bundle2.getString(WifiSocket.WIFI_PSWD);
                    String server_ip = bundle2.getString(WifiSocket.SERVER_IP);
                    String server_port = bundle2.getString(WifiSocket.SERVER_PORT);
                    wifi_name_edt.setText(ssid);
                    wifi_pwd_edt.setText(wifi_pwd);
                    server_ip_edt.setText(server_ip);
                    server_port_edt.setText(server_port);
                    break;
                default :
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.socket_connect_setting);
        wifiSocket = new WifiSocket();
        chazuomanager = SysApplication.getInstance().getCmInstance();
        chazuomanager.AddWifiConfigListener(this);
        settingPreference = getSharedPreferences(STORE_NAME,MODE_PRIVATE);
        InitView();

        //获取查询信息
        Intent intent =getIntent();
        Bundle bundle = intent.getBundleExtra("value");
        chazuo_ip = bundle.getString("ip");
        chazuo_mac = bundle.getString("mac");
        chazuo_modbus = bundle.getString("modbus");
        loginType = bundle.getInt("logintype");


        switch(loginType){
            case 1: //插座出厂配置
                //显示插座上次配置内容
                SetChazuoFlag(2);  //将插座Led置红
                GetEditText();
                break;
            case 2: //插座wifi配置
                SetChazuoFlag(2);  //将插座Led置红
                CheckChazuoParam();
                break;
            case 3:
//                CheckChazuoParam();
                GetEditText();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            finish();    //返回退出
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.LogInfo("SocketConnectSetting","onDestroy");
        chazuomanager.DeleteWifiConfigListener(this);
        if(loginType != 3){
            SetChazuoFlag(1);
        }

    }

    private void InitView(){
        // 回退按钮
        ImageButton socket_control_back_img = (ImageButton) findViewById(R.id.socket_connectt_setting_back_img);
        socket_control_back_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                finish();
//                System.exit(0);
            }
        });

//        Button saveHotSpotSettings = (Button) findViewById(R.id.sockect_connect_setting_hotspot_save_btn);
//        saveHotSpotSettings.setOnClickListener(listener);

        Button saveServerSettings = (Button) findViewById(R.id.sockect_connect_setting_server_save_btn);
        saveServerSettings.setOnClickListener(listener);

        wifi_name_edt = (EditText)findViewById(R.id.socket_connect_setting_hotspot_name_et);
        wifi_pwd_edt = (EditText)findViewById(R.id.socket_connect_setting_hotspot_psw_et);
        server_ip_edt = (EditText)findViewById(R.id.socket_connect_setting_server_ip_et);
        server_port_edt = (EditText)findViewById(R.id.socket_connect_setting_server_port_et);

    }

    private void GetEditText(){
        if(settingPreference.contains("wifi_name")){
            wifi_name_edt.setText(settingPreference.getString("wifi_name",""));
            wifi_pwd_edt.setText(settingPreference.getString("wifi_pwd",""));
            server_ip_edt.setText(settingPreference.getString("server_ip",""));
            server_port_edt.setText(settingPreference.getString("server_port",""));
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.sockect_connect_setting_server_save_btn:
                    MyLog.LogInfo("Socket Connect setting","点击保存"+chazuo_mac);
                    byte[] para = GetConfigMsg();
                    if(para != null){
                        byte[] cmd = wifiSocket.setSocketParametersCommand(chazuo_mac,chazuo_modbus,para);

                        //添加命令 2.继承接口，方便收回数据
                        chazuomanager.AddCmd(chazuo_mac,cmd);
                        MyLog.LogInfo("Socket Connect setting","点击保存"+chazuo_mac);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private byte[] GetConfigMsg(){
        String wifi_name = wifi_name_edt.getText().toString();
        MyLog.LogInfo("SocketConnectSetting","wifi_name:"+wifi_name+"...");
        if("".equals(wifi_name)){
            Toast.makeText(SocketConnectSetting.this,"wifi热点名称不能为空",Toast.LENGTH_SHORT).show();
            return null;
        }
        String wifi_pwd = wifi_pwd_edt.getText().toString();
        if("".equals(wifi_pwd)){
            Toast.makeText(SocketConnectSetting.this,"wifi热点密码不能为空",Toast.LENGTH_SHORT).show();
            return null;
        }
        String server_ip = server_ip_edt.getText().toString();
        if("".equals(server_ip)){
            Toast.makeText(SocketConnectSetting.this,"服务器地址不能为空",Toast.LENGTH_SHORT).show();
            return null;
        }
        String server_port = server_port_edt.getText().toString();
        if("".equals(server_port)){
            Toast.makeText(SocketConnectSetting.this,"服务器端口号不能为空",Toast.LENGTH_SHORT).show();
            return null;
        }
        SharedPreferences.Editor editor = settingPreference.edit();
        editor.putString("wifi_name",wifi_name);
        editor.putString("wifi_pwd",wifi_pwd);
        editor.putString("server_ip",server_ip);
        editor.putString("server_port",server_port);
        editor.commit();
        String tempStr = "\""+wifi_name+"\",\""+wifi_pwd+"\","+server_ip+","+server_port;
        byte[] result = GetBytes(tempStr);
        return result;
    }

    private  byte[] GetBytes(String str){
        char[] cc = str.toCharArray();
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
     * 修改插座标识
     * @param flag  1：Led正常工作 2：Led常亮
     */
    private void SetChazuoFlag(int flag){
        MyLog.LogInfo("SocketConnect setting","设置LED");
        switch(flag){
            case 1:
                byte[] cmd = wifiSocket.socketMarkEditCommand(chazuo_mac,chazuo_modbus,(byte)0);
                chazuomanager.AddCmd(chazuo_mac,cmd);
                break;
            case 2:
                byte[] cmd2 = wifiSocket.socketMarkEditCommand(chazuo_mac,chazuo_modbus,(byte)255);
                chazuomanager.AddCmd(chazuo_mac,cmd2);
                break;

        }
    }

    private void CheckChazuoParam(){
        MyLog.LogInfo("SocketConnect setting","采集插座参数");
        byte[] cmd = wifiSocket.getSocketParametersCommand(chazuo_mac,chazuo_modbus);
        chazuomanager.AddCmd(chazuo_mac,cmd);
    }

    @Override
    public void SetMapResult(Map<String, String> resultMap) {
        String ssid =  resultMap.get(WifiSocket.SSID_NAME);
        String wifi_pwd = resultMap.get(WifiSocket.WIFI_PSWD);
        String server_ip = resultMap.get(WifiSocket.SERVER_IP);
        String server_port = resultMap.get(WifiSocket.SERVER_PORT);
        Bundle bundle = new Bundle();
        bundle.putString(WifiSocket.SSID_NAME,ssid);
        bundle.putString(WifiSocket.WIFI_PSWD,wifi_pwd);
        bundle.putString(WifiSocket.SERVER_IP, server_ip);
        bundle.putString(WifiSocket.SERVER_PORT,server_port);
        Message msg = new Message();
        msg.setData(bundle);
        msg.what = 100004;
        mHandler.sendMessage(msg);
    }

    @Override
    public void SetStrResult(String resultStr) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data",resultStr);
        msg.setData(bundle);
        msg.what = 100002;
        mHandler.sendMessage(msg);
    }
}




