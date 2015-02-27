package wificontrol.lichuang.com.wificontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.Util.MyLog;

/**
 * Created by Administrator on 2015/1/26.
 */
public class WifiConnectFragment extends Fragment implements View.OnClickListener{

    private Spinner spinner;
    private ArrayAdapter spAdapter;
    private Button wifi_connect_bt;
    private EditText wifi_pwd_edt;
    private CheckBox display_pwd;

    private List<String> apNameList;   //WIFI热点名称集合
    private ConnectInter connectInter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        connectInter=(ConnectInter)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wifi_connect_frag,null);
        InitView(view);
        return view;
    }

    private void InitView(View view){
        apNameList = new ArrayList<String>();

        spinner = (Spinner) view.findViewById(R.id.wifi_connect_frag_wifi_sp);

        //将可选内容与ArrayAdapter连接起来
        spAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, apNameList);

        //设置下拉列表的风格
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(spAdapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置默认值
        spinner.setVisibility(View.VISIBLE);



        // 连接
        wifi_connect_bt = (Button) view.findViewById(R.id.wifi_connect_frag_connect_btn);
        wifi_connect_bt.setOnClickListener(this);

        wifi_pwd_edt = (EditText)view.findViewById(R.id.wifi_connect_frag_password_et);
        display_pwd = (CheckBox)view.findViewById(R.id.wifi_connect_frag_dispwd_cb);
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
    }

    public void SetData(List<String> list){
        apNameList.clear();
        apNameList.addAll(list);
        spAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.wifi_connect_frag_connect_btn:
                String wifi_ap_name = spinner.getSelectedItem().toString();
                MyLog.LogInfo("wifiConnect", "要连接的热定名:" + wifi_ap_name);
                String tempStr = wifi_pwd_edt.getText().toString();
                if("".equals(tempStr)){
                    Toast.makeText(getActivity(), "WIFI密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                connectInter.ConnectWifi(wifi_ap_name,tempStr);
                break;
        }
    }

    public interface ConnectInter{

        public void ConnectWifi(String ap_name,String ap_pwd);
    }

}
