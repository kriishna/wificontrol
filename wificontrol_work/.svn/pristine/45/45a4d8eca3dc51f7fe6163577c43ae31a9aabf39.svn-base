package wificontrol.lichuang.com.wificontrol.activity;

import android.app.FragmentManager;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.adapter.ModelAdapter;
import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.fragment.WifiConnectBackFragment;
import wificontrol.lichuang.com.wificontrol.fragment.WifiConnectFragment;
import wificontrol.lichuang.com.wificontrol.fragment.WifiConnectedFragment;
import wificontrol.lichuang.com.wificontrol.view.SlidingMenu;
import wificontrol.lichuang.com.wificontrol.wifi.WifiAdminNew;
import wificontrol.lichuang.com.wificontrol.wifi.WifiStateReceiver;


public class WIFIConnect extends BaseActivity implements OnClickListener,WifiConnectedFragment.GetAnotherWifi,
        WifiConnectBackFragment.ConnectBackInter,WifiConnectFragment.ConnectInter {

    private Spinner spinner;
    private SlidingMenu slidingMenu;
    private ImageButton model_select_ib;
    private ListView model_select_lv;
    private FragmentManager fragmentManager;

    private WifiAdminNew myWifiAdminNew;
    private WifiStateReceiver wifiStateReceiver;
    private List<ScanResult> wifiList;
    private List<String> apNameList;   //WIFI热点名称集合
    private ArrayAdapter spAdapter;

    private Timer mTimer;
    private ScanListTask slTask;

    private ProgressDialog m_pDialog;

    private boolean isExit= false;
    private boolean isContinue = true; //用于打开wifi时判定
    private boolean toNextPage = false;

    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 100002:
                    if(apNameList.size()>0){
                        MyLog.LogInfo("WifiConnect","列表Item数目大于0;"+apNameList.size());
//                        spAdapter.clear();
                        WifiConnectBackFragment backFragment = (WifiConnectBackFragment)fragmentManager.findFragmentByTag("ConnectBackFragment");
                        if(backFragment != null){
                            MyLog.LogInfo("WIFIConnect","backFragment 返回为空");
                            backFragment.SetData(apNameList);
                        }else{
                            WifiConnectFragment connectFragment = (WifiConnectFragment)fragmentManager.findFragmentByTag("ConnectFragment");
                            if(connectFragment != null){
                                connectFragment.SetData(apNameList);
                            }
                        }
                    }

                    break;
                case 100034:
                     if(slTask != null){
                         boolean result =slTask.cancel();
                         MyLog.LogInfo("WIFIConnect","slTask 销毁ing....");
                         if(result){
                             slTask = null;
                             MyLog.LogInfo("WIFIConnect","slTask 销毁....");
                         }

                     }
                    break;
                case 600009: //WIFI已经打开
                    if(m_pDialog != null){
                        m_pDialog.cancel();
                    }
                    if(slTask == null){
                        slTask = new ScanListTask();
                        MyLog.LogInfo("WIFIConnect","slTask 新建....");
                        mTimer.schedule(new ScanListTask(),900,7000);
                    }

                    break;
                case 600005:  //监听到wifi路由已连接上的广播
                    if(m_pDialog != null){
                        m_pDialog.cancel();
                    }
                    if(toNextPage){
                        toNextPage = false;
                        Intent intent = new Intent(WIFIConnect.this,SocketManageMain.class);
                        startActivity(intent);
                    }else{
                        fragmentManager.beginTransaction().replace(R.id.wifi_content_ll, new WifiConnectedFragment(), "ConnectedFragment").commit();
                        mHandler.sendEmptyMessage(100034);
                    }

                    break;
                case 600015: //正在打开wifi
                    ShowDialog("正在打开WIFI");
                    break;
                case 600007://监听到WIFI已关闭

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.LogInfo("WIFIConnect","onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.wifi_connect_layout);
        apNameList = new ArrayList<String>();
        myWifiAdminNew = new WifiAdminNew(WIFIConnect.this) {
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
        fragmentManager = getFragmentManager();
        //WIFI状态接收器
        wifiStateReceiver=new WifiStateReceiver(this,mHandler);
        InitView();
        CheckWifi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyLog.LogInfo("WIFIConnect","onStart");
        CheckWifi();
        mTimer = new Timer();
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wifiStateReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyLog.LogInfo("WiFiConnect","onStop");
        mTimer.cancel();
        slTask = null;
        unregisterReceiver(wifiStateReceiver);
    }



    private void InitView(){
        InitLeftSlidingView();

    }

    private void CheckWifi(){
                int is_wifi_enable = myWifiAdminNew.checkState(WIFIConnect.this);
                if(is_wifi_enable == WifiManager.WIFI_STATE_ENABLED){
                    WifiInfo wifiInfo = myWifiAdminNew.getWifiInfo();
                    String ssid = wifiInfo.getSSID();
                    if(ssid != null&& !("".equals(ssid))){
                        fragmentManager.beginTransaction().replace(R.id.wifi_content_ll, new WifiConnectedFragment(), "ConnectedFragment").commit();
                    }else{
                        fragmentManager.beginTransaction().replace(R.id.wifi_content_ll, new WifiConnectFragment(), "ConnectFragment").commit();
                        MyLog.LogInfo("WIFIConnect","启动搜索WIFI列表任务");
                        mHandler.sendEmptyMessage(600009);
                    }

                }else{
                    fragmentManager.beginTransaction().replace(R.id.wifi_content_ll, new WifiConnectFragment(), "ConnectFragment").commit();
                    myWifiAdminNew.openWifi();   //打开WIFI
                    mHandler.sendEmptyMessage(600015);

                }

    }

    private void ShowDialog(String msg){
        m_pDialog = new ProgressDialog(WIFIConnect.this);
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

    private void InitLeftSlidingView(){
        slidingMenu = (SlidingMenu) findViewById(R.id.wifi_connect_sliding);
        model_select_ib = (ImageButton) findViewById(R.id.wifi_connect_model_select_img);
        model_select_ib.setOnClickListener(this );
        //数据
        final ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("model_tv", "登陆");
        hm.put("model_img", R.drawable.model_login);
        data.add(hm);

        hm = new HashMap<String, Object>();
        hm.put("model_tv", "插座出厂配置");
        hm.put("model_img", R.drawable.model_phone_hot_spot);
        data.add(hm);

        hm = new HashMap<String, Object>();
        hm.put("model_tv", "室内控制");
        hm.put("model_img", R.drawable.model_phone_connect_wifi);
        data.add(hm);

        hm = new HashMap<String, Object>();
        hm.put("model_tv", "远程控制");
        hm.put("model_img", R.drawable.model_phone_remote_control);
        data.add(hm);

        ModelAdapter adapter = new ModelAdapter(WIFIConnect.this, data);

        model_select_lv = (ListView) findViewById(R.id.model_select_lv);
        model_select_lv.setAdapter(adapter);

        model_select_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       Intent intent = new Intent();
                                                       switch (position) {
                                                           case 0://登陆
                                                               Toast.makeText(WIFIConnect.this, (String) data.get(position).get("model_tv"),
                                                                       Toast.LENGTH_SHORT).show();
                                                               break;
                                                           case 1://选择插座出厂配置


//                                                               intent.setClass(WIFIConnect.this, SocketConnectSettingSelect.class);
                                                               intent.setClass(WIFIConnect.this, SocketManageMainConnect.class);
                                                               Bundle bundle = new Bundle();
                                                               bundle.putInt("type",1);
                                                               intent.putExtra("value",bundle);
                                                               startActivity(intent);

                                                               break;
                                                           case 2://室内控制
                                                               slidingMenu.toggle();
                                                               break;
                                                           case 3://远程控制


                                                               Toast.makeText(WIFIConnect.this, (String) data.get(position).get("model_tv"),
                                                                       Toast.LENGTH_SHORT).show();
                                                               break;
                                                           default:
                                                               break;
                                                       }
                                                   }

                                               }
        );
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            exitBy2Click();    //双击退出
        }
        return false;
    }

    /**
     * 双击退出事件
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; //
            Toast.makeText(this, "请按返回键退出", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; //
                }
            }, 2000); //

        } else {

            SysApplication.getInstance().exit();
        }
    }


    //ConnectedWifiFragment
    @Override
    public void ToWifiSelectPage() {
        //跳转到连接其它WIFI页面
        fragmentManager.beginTransaction().replace(R.id.wifi_content_ll, new WifiConnectBackFragment(), "ConnectBackFragment").commit();
        mHandler.sendEmptyMessage(600009);
    }

    //ConnectBackFragment
    @Override
    public void BackToPrePage() {
        MyLog.LogInfo("WIFIConnect","back to pre page");
        fragmentManager.beginTransaction().replace(R.id.wifi_content_ll, new WifiConnectedFragment(), "ConnectedFragment").commit();
        mHandler.sendEmptyMessage(100034);
    }

    @Override
    public void ConnectWifiBack(String ap_name,String ap_pwd) {
        myWifiAdminNew.addNetwork(ap_name,ap_pwd,WifiAdminNew.TYPE_WPA);
        toNextPage = true;
        ShowDialog("正在连接WIFI热点");
    }


    //ConnectFragment
    @Override
    public void ConnectWifi(String ap_name, String ap_pwd) {
        myWifiAdminNew.addNetwork(ap_name, ap_pwd, WifiAdminNew.TYPE_WPA);
        toNextPage = true;
        ShowDialog("正在连接WIFI热点");
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



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.wifi_connect_model_select_img:
                slidingMenu.toggle();
                break;

            default:
                break;
        }
    }
}