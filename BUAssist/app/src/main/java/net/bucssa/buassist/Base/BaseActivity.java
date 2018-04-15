package net.bucssa.buassist.Base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import net.bucssa.buassist.MainActivity;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Base64;
import net.bucssa.buassist.Util.Constant;
import net.bucssa.buassist.Util.SPUtils;
import net.bucssa.buassist.Util.StatusBarUtil;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Shin on 2017-6-22
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = getTAG();
    private LinearLayout statusBar;

    protected abstract int getLayoutId();

    protected abstract String getTAG();

    protected Context mContext;

    //关键的是否登录 由父类提供
    public boolean isLogin = false;
    //关键的https联网字段 由父类提供
    public String mAuthorization;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    private CompositeSubscription mCompositeSubscription;

    public CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    public void addSubscription(Subscription s) {
        if (s == null) {
            return;
        }

        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            StatusBarUtil.transparencyBar((Activity) mContext);
            StatusBarUtil.setStatusBarColor((Activity) mContext, R.color.white);
            StatusBarUtil.StatusBarLightMode((Activity) mContext);
        } else {
            StatusBarUtil.transparencyBar((Activity) mContext);
        }

        Window window = getWindow();
        //调整软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //友盟统计
        registerMobclickAgent();

                /* 蒲公英CrashLog */
        PgyCrashManager.register(this);

        getNecessaryData();

        initResAndListener();
    }

    //空方法 规定子类 初始化监听器 和定义显示资源 的步骤
    protected void initResAndListener() {

    }

    protected void getNecessaryData() {
        UserSingleton.getInstance().isLogin(getApplication());
        isLogin = (boolean) SPUtils.get(mContext, Constant.ISLOGIN, false);
        mAuthorization = getAuthorizations(isLogin);
    }

    //是否statusBar 状态栏为透明 的方法 默认为真
    protected boolean isTranslucentStatusBar() {
        return true;
    }

    protected boolean isLogin() {
        return true;
    }

    protected String getAuthorizations(boolean isLogin) {

        String temp = " ";
        if (isLogin) {
            return SPUtils.get(mContext, Constant.TOKENTYPE, temp)
                    + temp
                    + SPUtils.get(mContext, Constant.TOKENACCESS, temp);
        }
        return Base64.mClientInto;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        MobclickAgent.onPageStart(getTAG());
//        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PgyCrashManager.unregister();

//        MobclickAgent.onPageEnd(getTAG());
//        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 注册友盟统计
     */
    private void registerMobclickAgent() {
        //MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
//        MobclickAgent.openActivityDurationTrack(false);
//        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    /**
     * 友盟统计接口
     */
    private void umengCount(String d_data, String estateName) {
//        HashMap<String, String> keyPath = new HashMap<>();
//        MobclickAgent.onEvent(mContext, "hv_event_open_act", keyPath);
    }


    /**
     * 获取statusbar高度
     * @return result
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
