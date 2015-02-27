package wificontrol.lichuang.com.wificontrol.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import wificontrol.lichuang.com.wificontrol.R;
import wificontrol.lichuang.com.wificontrol.application.SysApplication;
import wificontrol.lichuang.com.wificontrol.device.ChazuoDevice;
import wificontrol.lichuang.com.wificontrol.device.ChazuoManager;

/**
 * Created by Administrator on 2014/12/27.
 */
public class Switch extends View implements View.OnClickListener {
    private Bitmap switch_on, switch_off;

    private boolean isSwitchOn = false;//开关是否开启

    private ChazuoDevice chazuoDevice;
    private ChazuoManager chazuoManager;

    /**
     * 监听接口
     */
    private OnChangedListener listener;

    public Switch(Context context) {
        super(context);
        init();

    }

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //载入图片资源
        switch_on = BitmapFactory.decodeResource(getResources(), R.drawable.socket_on);
        switch_off = BitmapFactory.decodeResource(getResources(), R.drawable.socket_off);

        setOnClickListener(this);
        chazuoDevice = new ChazuoDevice();
        chazuoManager = SysApplication.getInstance().getCmInstance();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        Paint paint = new Paint();

        //根据isSwitchOn设置背景，开或者关状态
        if (isSwitchOn) {
            canvas.drawBitmap(switch_on, matrix, paint);
        } else {
            canvas.drawBitmap(switch_off, matrix, paint);
        }

    }


    /**
     * 设置开关状态
     * @param isSwitchOn  打开为true  关闭为false
     */
    public void setIsSwitchOn(boolean isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
        invalidate();
    }

    public boolean getSwitchIsOn(){
        return isSwitchOn;
    }

    /**
     * 为WiperSwitch设置一个监听，供外部调用的方法
     */
    public void setOnChangedListener(OnChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 回调接口
     *
     * @author len
     */
    public interface OnChangedListener {
        public void OnChanged(Switch sh, boolean isSwitchOn);
    }
}
