package wificontrol.lichuang.com.wificontrol.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;
import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoDevice;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;
import wificontrol.lichuang.com.wificontrol.view.Switch;

public class SocketControl extends BaseActivity implements ChazuoManager.ActivityChazuoConfigListener,OnClickListener{
    public boolean isQzsd = true;//是否强制锁定
    public boolean isOn = false;//插座开关是否开启

//    private WiperSwitch wiperSwitch;//强制锁定滑动开关
    private Switch socket_switch;//插座开关

    private ImageButton back_imbt;
    private ImageButton setup_imbt;
    private ImageButton name_edit_imbt;
    private TextView now_power_tv;
    private TextView current_tv;
    private TextView voltage_tv;
    private TextView power_tv;
    private TextView state_tv;

    private String chazuo_mac;
    private String chazuo_modbus;

    private ChazuoDevice chazuoDevice;
    private ChazuoManager chazuoManager;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 100005:
                    Bundle bundle = msg.getData();
                    now_power_tv.setText(bundle.getString(ChazuoDevice.TOTAL_POWER,"...")+"Kwh");
                    current_tv.setText(bundle.getString(ChazuoDevice.ELEC_CURRENT,"...")+"A");
                    voltage_tv.setText(bundle.getString(ChazuoDevice.VOLTAGE,"...")+"V");
                    power_tv.setText(bundle.getString(ChazuoDevice.POWER,"...")+"W");
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //发送读取插座锁定状态命令
                    byte[] cmd2 = chazuoDevice.Get_ValveLocked_Cmd(chazuo_modbus);
                    chazuoManager.AddCmd(chazuo_mac,cmd2);
                    break;
                case 100006:
                    Bundle bundle2 = msg.getData();
                    String tempStr = bundle2.getString(ChazuoDevice.VALVE_LOCK_STATE, null);
                    if(tempStr != null){
                        if(("未锁定").equals(tempStr)){
                            socket_switch.setVisibility(View.VISIBLE);
                            state_tv.setText("未锁定");
                            byte[] cmd = chazuoDevice.Get_ValveState_Cmd(chazuo_modbus);
                            chazuoManager.AddCmd(chazuo_mac,cmd);
                        }else{
                            state_tv.setText("锁定");
                        }
                    }

                    break;
                case 100007:
                    Bundle bundle3 = msg.getData();
                    String tempStr2 = bundle3.getString(ChazuoDevice.VALVE_STATE,null);
                    if(tempStr2 != null){
                        if("继电器开".equals(tempStr2)){
                            socket_switch.setIsSwitchOn(true);
                        }else{
                            socket_switch.setIsSwitchOn(false);
                        }
                    }
                    break;
                case 100008:
                    Bundle bundle4 = msg.getData();
                    String tempStr3 = bundle4.getString(ChazuoDevice.VALVE_SET_RESULT,null);
                    if(tempStr3 != null){
                        if("开".equals(tempStr3)){
                            socket_switch.setIsSwitchOn(true);
                        }else{
                            socket_switch.setIsSwitchOn(false);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.socket_control_layout);
        InitView();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("value");
        chazuo_mac = bundle.getString("mac");
        chazuo_modbus = bundle.getString("modbus");
        chazuoDevice = new ChazuoDevice();
        chazuoManager = SysApplication.getInstance().getCmInstance();
        chazuoManager.AddChazuoConfigListener(this);
        GetChazuoInfo();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            finish();    //双击退出
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chazuoManager.DeleteChazuoConfigListener(this);
    }

    private void InitView() {
       /*
        wiperSwitch = (WiperSwitch) findViewById(R.id.socket_control_qzsd_wiper_switch);
        wiperSwitch.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                if (checkState) {//如果强制锁定
                    //隐藏插座开关按钮
                    socket_switch.setVisibility(View.GONE);
                } else {
                    socket_switch.setVisibility(View.VISIBLE);
                }
            }
        });

        wiperSwitch.setChecked(isQzsd);
        */


        // 回退按钮
        back_imbt = (ImageButton) findViewById(R.id.socket_control_back_img);
        back_imbt.setOnClickListener(this);

        setup_imbt = (ImageButton) findViewById(R.id.socket_control_setup_img);
        setup_imbt.setOnClickListener(this);

        //编辑插座名称按钮
        name_edit_imbt = (ImageButton) findViewById(R.id.socket_control_socket_name_edit_ib);
        name_edit_imbt.setOnClickListener(this);

        now_power_tv = (TextView)findViewById(R.id.socket_control_ydl_value_tv);
        current_tv = (TextView)findViewById(R.id.socket_control_dl_value_tv);
        voltage_tv = (TextView)findViewById(R.id.socket_control_dy_value_tv);
        power_tv = (TextView)findViewById(R.id.socket_control_xhzt_value_tv);
        state_tv = (TextView)findViewById(R.id.socket_control_qzsd_tv);


        //--TODO 获取是否强制锁定
        socket_switch = (Switch) findViewById(R.id.socket_control_socket_switch);
        socket_switch.setVisibility(View.INVISIBLE);
        socket_switch.setOnClickListener(this);
        state_tv.setText("...");
    }

    private void GetChazuoInfo(){
        byte[] cmd = chazuoDevice.Get_QueryPara_Cmd(chazuo_modbus);
        MyLog.LogInfo("SocketControl","添加命令");
        chazuoManager.AddCmd(chazuo_mac,cmd);
//                try {
//                    Thread.currentThread().sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showSocketNameEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SocketControl.this);
        builder.setIcon(R.drawable.edit_selected);
        builder.setTitle("编辑设备名称");
        LayoutInflater li = LayoutInflater.from(SocketControl.this);
        final View socket_name_edit_layout = li.inflate(R.layout.socket_name_edit_layout, null);
        builder.setView(socket_name_edit_layout);


        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText socket_name_et = (EditText) socket_name_edit_layout.findViewById(R.id.socket_name_edit_et);
                TextView socket_name_tv = (TextView) findViewById(R.id.socket_control_socket_name_tv);
                String socket_name = socket_name_et.getText().toString().trim();
                if (socket_name != null && socket_name.trim().isEmpty() == false) {
                    socket_name_tv.setText(socket_name);
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public void SetMapResult(Map<String, String> resultMap) {
        if(resultMap != null){
            MyLog.LogInfo("SocketControl",resultMap.toString());
            Message msg = new Message();
            if(resultMap.size() > 1){
                Bundle bundle = new Bundle();
                bundle.putString(ChazuoDevice.VOLTAGE,resultMap.get(ChazuoDevice.VOLTAGE));
                bundle.putString(ChazuoDevice.ELEC_CURRENT,resultMap.get(ChazuoDevice.ELEC_CURRENT));
                bundle.putString(ChazuoDevice.POWER,resultMap.get(ChazuoDevice.POWER));
                bundle.putString(ChazuoDevice.TOTAL_POWER,resultMap.get(ChazuoDevice.TOTAL_POWER));
                msg.setData(bundle);
                msg.what = 100005;
            }else{
                if(resultMap.containsKey(ChazuoDevice.VALVE_LOCK_STATE)){
                    Bundle bundle = new Bundle();
                    bundle.putString(ChazuoDevice.VALVE_LOCK_STATE,resultMap.get(ChazuoDevice.VALVE_LOCK_STATE));
                    msg.setData(bundle);
                    msg.what = 100006;
                }
                if(resultMap.containsKey(ChazuoDevice.VALVE_STATE)){
                    Bundle bundle = new Bundle();
                    bundle.putString(ChazuoDevice.VALVE_STATE,resultMap.get(ChazuoDevice.VALVE_STATE));
                    msg.setData(bundle);
                    msg.what = 100007;
                }
                if(resultMap.containsKey(ChazuoDevice.VALVE_SET_RESULT)){
                    Bundle bundle = new Bundle();
                    bundle.putString(ChazuoDevice.VALVE_SET_RESULT,resultMap.get(ChazuoDevice.VALVE_SET_RESULT));
                    msg.setData(bundle);
                    msg.what = 100008;
                }
            }


            mHandler.sendMessage(msg);
        }

    }

    @Override
    public void SetStrResult(String resultStr) {
        MyLog.LogInfo("SocketControl",resultStr);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.socket_control_back_img:
                finish();
                break;
            case R.id.socket_control_setup_img:
                Bundle bundle = new Bundle();
                bundle.putString("ip", "null");
                bundle.putString("mac",chazuo_mac);
                bundle.putString("modbus", chazuo_modbus);
                bundle.putInt("logintype",3);
                Intent intent = new Intent();
                intent.putExtra("value", bundle);
                intent.setClass(SocketControl.this,SocketConnectSetting.class);
                startActivity(intent);
                break;
            case R.id.socket_control_socket_name_edit_ib:
                showSocketNameEditDialog();
                break;
            case R.id.socket_control_socket_switch:
                byte[] cmd = null;
                if (socket_switch.getSwitchIsOn()) {
                    Toast.makeText(SocketControl.this,"设置插座关",Toast.LENGTH_SHORT).show();
                    cmd = chazuoDevice.Get_SetValve_Cmd(chazuo_modbus,'F');
                } else {
                    Toast.makeText(SocketControl.this,"设置插座开",Toast.LENGTH_SHORT).show();
                    cmd = chazuoDevice.Get_SetValve_Cmd(chazuo_modbus,'N');
                }
                chazuoManager.AddCmd(chazuo_mac,cmd);
                break;
        }
    }
}
