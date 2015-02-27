package wificontrol.lichuang.com.wificontrol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import wificontrol.lichuang.com.wificontrol.R;


public class SlidingMenu extends HorizontalScrollView {
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * dp
     */
    private int mMenuRightPadding = 50;
    /**
     * 菜单的宽度
     */
    private int mMenuWidth;
    private int mHalfMenuWidth;

    private boolean once;


    private boolean isOpen = false;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = getScreenWidth();

        mMenuRightPadding = getmMenuRightPadding(context, attrs);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScreenWidth = getScreenWidth();

        /*--不知道在干什么
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            Log.i("ATTR", "个数：" + i);
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    // 默认50
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50f,

                    getResources().getDisplayMetrics()));// 默认为10DP
                    break;

            }
        }
        a.recycle();
        */
        mMenuRightPadding = getmMenuRightPadding(context, attrs);
    }

    public int getmMenuRightPadding(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        int rightPadding = ta.getDimensionPixelSize(R.styleable.SlidingMenu_rightPadding, 50);
        ta.recycle();
        Log.i("mRightPadding", "" + rightPadding);
        return rightPadding;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 显示的设置一个宽度
         */
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
            ViewGroup content = (ViewGroup) wrapper.getChildAt(1);
            // dp to px
            //不知道在干什么
//            mMenuRightPadding = (int) TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, content
//                            .getResources().getDisplayMetrics());

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 2;
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth)
                    this.smoothScrollTo(mMenuWidth, 0);
                else
                    this.smoothScrollTo(0, 0);
                return true;

//            case MotionEvent.ACTION_DOWN:
//                Log.i("EV", "ACTION_DOWN");
//                Log.i("EV", "isopen:" + isOpen);
//                if (isOpen) {
//                    float pointX = ev.getX();
//                    Log.i("EV", "X:" + pointX + " mMenuWidth：" + mMenuWidth);
//                    if (pointX > mMenuWidth) {
//                        this.smoothScrollTo(mMenuWidth, 0);
//                        Log.i("EV", "close");
//                        isOpen = false;
//                    }
//                }
//                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

}
