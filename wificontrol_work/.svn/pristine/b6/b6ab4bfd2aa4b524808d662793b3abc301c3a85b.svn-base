package wificontrol.lichuang.com.wificontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.adapter.SocketConnectModelAdapter;

/**
 * Created by Administrator on 2014/12/27.
 */
public class SocketConnectSettingSelect extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.socket_connect_setting_select_layout);

        // 回退按钮
        ImageButton connect_setting_select_back_img = (ImageButton) findViewById(R.id.socket_connect_setting_select_back_img);
        connect_setting_select_back_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                finish();
//                System.exit(0);
//                Intent intent = new Intent();
//                intent.setClass(SocketConnectSettingSelect.this, WIFIConnect.class);
//                startActivity(intent);

            }
        });

        //自定义adapter
        final ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("socket_connect_model_img", R.drawable.first_setting);
        hm.put("socket_connect_model_tv", "插座出厂配置");
        hm.put("type",1);
        data.add(hm);

        hm = new HashMap<String, Object>();
        hm.put("socket_connect_model_img", R.drawable.wifi_setting);
        hm.put("socket_connect_model_tv", "插座WIFI配置");
        hm.put("type",2);
        data.add(hm);

        SocketConnectModelAdapter adapter = new SocketConnectModelAdapter(SocketConnectSettingSelect.this, data);
        ListView socket_connect_select_lv = (ListView) findViewById(R.id.socket_connect_model_lv);
        socket_connect_select_lv.setAdapter(adapter);

        socket_connect_select_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                HashMap<String,Object> map = data.get(position);
                int type = (int)map.get("type");
                bundle.putInt("type",type);
                intent.putExtra("value",bundle);
                switch(type){
                    case 1:
                        intent.setClass(SocketConnectSettingSelect.this, SocketManageMainConnect.class);
                        break;
                    case 2:
                        intent.setClass(SocketConnectSettingSelect.this, WiFiSelectActivity.class);
                        break;
                }
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            finish();   //双击退出
        }
        return false;
    }
}
