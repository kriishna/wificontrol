package wificontrol.lichuang.com.wificontrol.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.wifi.WifiAdminNew;
import wificontrol.lichuang.com.wificontrol.wifi.WifiStateReceiver;

/**
 * Created by Administrator on 2015/1/20.
 */
public class WiFiSelectActivity extends BaseActivity {

    private ImageButton back_btn;
    private Spinner wifi_name_sp;
    private EditText wifi_pwd_edt;
    private Button connect_btn;

    private RelativeLayout connected_wifi_rl;
    private RelativeLayout wifi_control_rl;
    private RelativeLayout wifi_connect_rl;
    private TextView connected_wifi_name_tv;
    private Button get_anotherwifi_bt;
    private Button continue_bt;
    private CheckBox display_pwd;
    private ProgressDialog m_pDialog;


    private List<ScanResult> wifiList;
    private List<String> apNameList;   //WIFI热点名称集合
    private ArrayAdapter spAdapter;

    private WifiAdminNew myWifiAdminNew;
    private WifiStateReceiver wifiStateReceiver;

    private boolean toNextPage = false;

    private Timer mTimer ;
    private ScanListTask slTask;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 100002:
                    if(apNameList.size()>0){
                        MyLog.LogInfo("WifiSelectActivity", "列表Item数目大于0;" + apNameList.size());
//                        spAdapter.clear();

                        spAdapter.notifyDataSetChanged();
                    }

                    break;

                case 600009: //WIFI已经打开
                    if(m_pDialog != null){
                        m_pDialog.cancel();
                    }
                    if(slTask == null){
                        slTask = new ScanListTask();
                        mTimer.schedule(slTask,100,15000);
                    }

                    break;
                case 600005:  //监听到wifi路由已连接上的广播
                    if(m_pDialog != null){
                        m_pDialog.cancel();
                    }
                    if(toNextPage){
                        toNextPage = false;
                        Intent intent = new Intent(WiFiSelectActivity.this,SocketManageMainConnect.class);
                        Bundle bundle = new Bundle();

                        bundle.putInt("type",2);
                        intent.putExtra("value",bundle);
                        startActivityForResult(intent, 502);
                    }else{
                        if(slTask != null){
                            boolean c =slTask.cancel();
                            if(c){
                                slTask = null;
                            }

                        }
                        WifiInfo wifiInfo = myWifiAdminNew.getWifiInfo();
                        String ssid = wifiInfo.getSSID();
                        connected_wifi_name_tv.setText("已连接的WIFI热点:"+ssid);
                        connected_wifi_rl.setVisibility(View.VISIBLE);
                        wifi_control_rl.setVisibility(View.VISIBLE);
                        wifi_connect_rl.setVisibility(View.GONE);
                    }

                    break;
                case 600015:  //Wifi路由连接失败
                    if(m_pDialog != null){
                        m_pDialog.cancel();
                    }
                    toNextPage = false;
                    Toast.makeText(WiFiSelectActivity.this,"WIFI连接失败",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.wifi_select_activity);
        myWifiAdminNew = new WifiAdminNew(WiFiSelectActivity.this) {
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
                mHandler.sendEmptyMessage(600005);
            }

            @Override
            public void onNotifyWifiConnectFailed() {
                mHandler.sendEmptyMessage(600015);
            }
        };
        mTimer = new Timer();
        //WIFI状态接收器
        wifiStateReceiver=new WifiStateReceiver(this,mHandler);

        InitView();
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wifiStateReceiver,filter);
        CheckWifi();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 503){
            MyLog.LogInfo("WifiSelectActiviyt","resultcode="+resultCode);
            CheckWifi();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void InitView(){
        back_btn = (ImageButton)findViewById(R.id.wifi_select_back_imbt);
        wifi_name_sp = (Spinner)findViewById(R.id.wifi_select_wifi_sp);
        wifi_pwd_edt = (EditText)findViewById(R.id.wifi_select_password_et);
        display_pwd = (CheckBox)findViewById(R.id.wifi_select_dispwd_cb);
        display_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    wifi_pwd_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    wifi_pwd_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        connect_btn = (Button)findViewById(R.id.wifi_select_connect_btn);
        MyOnClickListener myListener = new MyOnClickListener();
        back_btn.setOnClickListener(myListener);
        connect_btn.setOnClickListener(myListener);

        //将可选内容与ArrayAdapter连接起来
        apNameList = new ArrayList<String>();
        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, apNameList);

        //设置下拉列表的风格
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        wifi_name_sp.setAdapter(spAdapter);

        connected_wifi_rl = (RelativeLayout)findViewById(R.id.wifi_select_wificontent_rl);
        connected_wifi_name_tv = (TextView)findViewById(R.id.wifi_select_connected_name_tv);
        wifi_control_rl = (RelativeLayout)findViewById(R.id.wifi_select_wificontrol_rl);
        get_anotherwifi_bt = (Button)findViewById(R.id.wifi_select_getanotherwifi_bt);
        get_anotherwifi_bt.setOnClickListener(myListener);
        continue_bt = (Button)findViewById(R.id.wifi_select_continue_bt);
        continue_bt.setOnClickListener(myListener);
        wifi_connect_rl = (RelativeLayout)findViewById(R.id.wifi_select_connect_rl);
    }

    private void CheckWifi(){

        int is_wifi_enable = myWifiAdminNew.checkState(WiFiSelectActivity.this);
        if(is_wifi_enable == WifiManager.WIFI_STATE_ENABLED){
            WifiInfo wifiInfo = myWifiAdminNew.getWifiInfo();
            String ssid = wifiInfo.getSSID();
            if(ssid != null&& !("".equals(ssid))){
                connected_wifi_rl.setVisibility(View.VISIBLE);
                wifi_control_rl.setVisibility(View.VISIBLE);
                wifi_connect_rl.setVisibility(View.GONE);
                connected_wifi_name_tv.setText("已连接的WIFI热点:"+ssid);
            }else{ //wifi已打开，但未连接
                connected_wifi_rl.setVisibility(View.GONE);
                wifi_control_rl.setVisibility(View.GONE);
                wifi_connect_rl.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessage(600009);  //搜索WIFI信号
            }

        }else{
            connected_wifi_rl.setVisibility(View.GONE);
            wifi_control_rl.setVisibility(View.GONE);
            wifi_connect_rl.setVisibility(View.VISIBLE);
            ShowDialog("正在打开WIFI");
            myWifiAdminNew.openWifi();
        }


    }

    private void ShowDialog(String msg){
        m_pDialog = new ProgressDialog(WiFiSelectActivity.this);

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



    private class ScanListTask extends TimerTask {
        @Override
        public void run() {
            myWifiAdminNew.startScan();
            wifiList = myWifiAdminNew.getWifiList();
            if(wifiList != null){
                if(wifiList.size()>0){
                    FilterWifiName();
                    mHandler.sendEmptyMessage(100002);
                }
            }

        }
    }

    /**
     * 将返回的WIFI热点名称列表中重复的过滤掉
     */
    private void FilterWifiName(){
        synchronized (apNameList){
            apNameList.clear();
            for(ScanResult sr:wifiList){

                if(apNameList.size()>0){
                    boolean isExist = false;
                    for(int i=0;i<apNameList.size();i++){
                        if(sr.SSID.equals(apNameList.get(i))){
                            isExist = true;
                        }
                    }
                    if(!isExist){
                        apNameList.add(sr.SSID);
                    }
                }else{
                    apNameList.add(sr.SSID);
                }

            }
        }

    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.wifi_select_back_imbt:
                    finish();
                    break;
                case R.id.wifi_select_connect_btn:
                    String wifi_ap_name = wifi_name_sp.getSelectedItem().toString();

                    String tempStr = wifi_pwd_edt.getText().toString();
                    if("".equals(tempStr)){
                        Toast.makeText(WiFiSelectActivity.this, "WIFI密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    myWifiAdminNew.addNetwork(wifi_ap_name,tempStr,WifiAdminNew.TYPE_WPA);

                    toNextPage = true;
                    ShowDialog("正在连接WIFi");

                    break;
                case R.id.wifi_select_getanotherwifi_bt:
                    if(wifi_connect_rl.getVisibility() == View.GONE){
                        wifi_connect_rl.setVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessage(600009);
                    }else{
                        wifi_connect_rl.setVisibility(View.GONE);
                        if(slTask != null){
                            boolean c = slTask.cancel();
                            if(c){
                                slTask =null;
                            }


                        }
                    }

                    break;
                case R.id.wifi_select_continue_bt:
                    Intent intent = new Intent(WiFiSelectActivity.this,SocketManageMainConnect.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt("type",2);
                    intent.putExtra("value",bundle);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTimer.cancel();

        unregisterReceiver(wifiStateReceiver);
    }


}
