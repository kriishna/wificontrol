package wificontrol.lichuang.com.wificontrol.activity;

import android.app.Activity;
import android.os.Bundle;

import wificontrol.lichuang.com.wificontrol.application.SysApplication;

/**
 * Created by Administrator on 2014/12/19.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
    }
}
