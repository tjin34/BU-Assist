package net.bucssa.buassist.Ui.Login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Login.UserInfo;
import net.bucssa.buassist.Bean.Request.RegisterReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.MainActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Constant;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.SPBuild;
import net.bucssa.buassist.Util.SPUtils;
import net.bucssa.buassist.Util.StatusBarUtil;
import net.bucssa.buassist.Util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Shin on 17/5/24.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    protected static final String TAG = "LoginActivity";
    private Context context;
    private LinearLayout mLoginLinearLayout; // 登录内容的容器
    private Animation mTranslate; // 位移动画
    private Dialog mLoginingDlg; // 显示正在登录的Dialog
    private Dialog mRegisterDlg; // 显示正在注册的Dialog
    private EditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private Button mLoginButton; // 登录按钮
    private TextView guestLogin;
    private ImageView iv_back;
    private RelativeLayout rootView;
    private String mIdString;
    private String mPwdString;
    private int mUid;
    private String mToken;
    private int keyboardHeight;

    @BindView(R.id.tvRegister)
    TextView mRegisterTextView;

    @BindView(R.id.tvLogin)
    TextView mLoginTextView;

    @BindView(R.id.register_linearLayout)
    LinearLayout mRegisterLineaLayout;

    @BindView(R.id.etUsername)
    EditText mUsernameEditText;

    @BindView(R.id.etEmail)
    EditText mEmailEditText;

    @BindView(R.id.etPassword)
    EditText mPassEditText;

    @BindView(R.id.etRepeatPass)
    EditText mRepeatEditText;

    @BindView(R.id.btnRegister)
    Button mRegisterButton;

    private Intent intent;
    private int preActivity = 0;
    private Animation alphaOut, alphaIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preActivity = getIntent().getIntExtra("preActivity", 0);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        StatusBarUtil.transparencyBar((Activity)context);
        initView();
        setListener();
        mLoginLinearLayout.startAnimation(mTranslate); // Y轴水平移动
        if (UserSingleton.getInstance().isLogin(context)) {
            mUid = UserSingleton.getInstance().getUid(context);
            mToken = UserSingleton.getInstance().getUserToken(context);
            showLoginingDlg();
            getUserInfo(mUid, mToken);
        }
    }

    public static void logout(Context context){
        UserSingleton.getInstance().clear(context);
        UserSingleton.getInstance().setLogin(false);
    }



    private void setListener() {
        mIdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mIdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPwdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        guestLogin.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);
    }

    private void initView() {
        View status_bar = findViewById(R.id.status_bar);
        status_bar.getLayoutParams().height = getStatusBarHeight();
        mIdEditText = (EditText) findViewById(R.id.login_edtId);
        mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
        mLoginButton = (Button) findViewById(R.id.login_btnLogin);
        guestLogin = (TextView) findViewById(R.id.tv_guest_login);
        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.GONE);
        mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // 初始化动画对象
        initLoginingDlg();
    }



    /* 初始化正在登录对话框 */
    private void initLoginingDlg() {

        mLoginingDlg = new Dialog(this, R.style.loginDialog);
        mLoginingDlg.setContentView(R.layout.dialog_login);

        Window window = mLoginingDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cxScreen = dm.widthPixels;
        int cyScreen = dm.heightPixels;

        int height = (int) getResources().getDimension(
                R.dimen.loginingdlg_height);// 高100dp
        int lrMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin); // 左右边沿30dp
        int topMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_top_margin); // 上沿20dp

//        params.y = (-(cyScreen - height) / 2) + topMargin; // -199
		/* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */

        params.width = cxScreen;
        params.height = height;
        // width,height表示mLoginingDlg的实际大小

        mLoginingDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /* 初始化正在登录对话框 */
    private void initRegisterDlg() {

        mRegisterDlg = new Dialog(this, R.style.loginDialog);
        mRegisterDlg.setContentView(R.layout.dialog_login);

        TextView tvMessage = (TextView) mRegisterDlg.findViewById(R.id.tvMessage);
        tvMessage.setText(getResources().getString(R.string.attempt_register));

        Window window = mRegisterDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cxScreen = dm.widthPixels;
        int cyScreen = dm.heightPixels;

        int height = (int) getResources().getDimension(
                R.dimen.loginingdlg_height);// 高100dp
        int lrMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin); // 左右边沿30dp
        int topMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_top_margin); // 上沿20dp

//        params.y = (-(cyScreen - height) / 2) + topMargin; // -199
		/* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */

        params.width = cxScreen;
        params.height = height;
        // width,height表示mLoginingDlg的实际大小

        mRegisterDlg.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog
    }

    /* 显示正在登录对话框 */
    private void showLoginingDlg() {
        if (mLoginingDlg != null)
            mLoginingDlg.show();
    }

    /* 关闭正在登录对话框 */
    private void closeLoginingDlg() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }


    /* 显示正在登录对话框 */
    private void showRegisterDlg() {
        if (mRegisterDlg != null)
            mRegisterDlg.show();
    }

    /* 关闭正在登录对话框 */
    private void closeRegisterDlg() {
        if (mRegisterDlg != null && mRegisterDlg.isShowing())
            mRegisterDlg.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.login_btnLogin:
                // 启动登录
                showLoginingDlg(); // 显示"正在登录"对话框
                if (mIdString == null || mIdString.equals("")) { // 账号为空时
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT)
                            .show();
                    closeLoginingDlg();

                } else if (mPwdString == null || mPwdString.equals("")) {// 密码为空时
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT)
                            .show();
                    closeLoginingDlg();

                } else {
                    login(mIdString, mPwdString);
                }
                break;
            case R.id.btnRegister:
                // 启动注册
                showRegisterDlg(); // 显示"正在注册"对话框
                String username = mUsernameEditText.getText().toString();
                String password = mPassEditText.getText().toString();
                String repeat = mRepeatEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                if ( username.equals("") || password.equals("") || repeat.equals("") || email.equals("")) { // 账号为空时
                    Toast.makeText(LoginActivity.this, "请确认输入所有必要信息~", Toast.LENGTH_SHORT).show();
                    closeRegisterDlg();
                } else if (!password.equals(repeat)) {// 密码为空时
                    Toast.makeText(LoginActivity.this, "两次输入密码不一致哦", Toast.LENGTH_SHORT).show();
                    closeRegisterDlg();
                } else {
                    RegisterReq req = new RegisterReq(username, password, email);
                    Gson gson = new Gson();
                    String json = gson.toJson(req);
                    register(json);
                }
                break;
            case R.id.tvRegister:
                alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);
                alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
                alphaOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mRegisterLineaLayout.setVisibility(View.VISIBLE);
                        mRegisterLineaLayout.startAnimation(alphaIn);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mLoginLinearLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                mLoginLinearLayout.startAnimation(alphaOut);
                break;
            case R.id.tvLogin:
                alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);
                alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
                alphaOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mLoginLinearLayout.setVisibility(View.VISIBLE);
                        mLoginLinearLayout.startAnimation(alphaIn);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRegisterLineaLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                mRegisterLineaLayout.startAnimation(alphaOut);
                break;
            default:
                break;
        }

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

    /**
     * 用户登录获取信息
     */
    private void login(String useracc, String userpw){
        Observable<BaseEntity<UserInfo>> observable = RetrofitClient.createService(UserAPI.class)
                .login(useracc, userpw);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<UserInfo>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        closeLoginingDlg();
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity<UserInfo> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            UserSingleton.USERINFO = baseEntity.getDatas();
                            UserSingleton.getHttpAvatar();
                            UserSingleton.getInstance().setLogin(true);

                            saveUserInfo(UserSingleton.USERINFO.getUsername(),
                                    UserSingleton.USERINFO.getUid(), UserSingleton.USERINFO.getToken());

                            closeLoginingDlg();

                            if (preActivity == 0){
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                setResult(-1);
                                finish();
                            }

                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    /**
     * 用户注册
     */
    private void register(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .register(body);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        closeRegisterDlg();
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        closeRegisterDlg();
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "注册成功！");
                            mLoginTextView.performClick();
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void getUserInfo(int uid, String token){
        Observable<BaseEntity<UserInfo>> observable = RetrofitClient.createService(UserAPI.class)
                .getMyInfos(uid, token);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<UserInfo>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity<UserInfo> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            UserSingleton.USERINFO = baseEntity.getDatas();
                            UserSingleton.getHttpAvatar();
                            UserSingleton.getInstance().setLogin(true);

                            closeLoginingDlg();

                            if (preActivity == 0){
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                setResult(1);
                                finish();
                            }

                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    /**
     * 保存登陆用户信息
     *
     * @param mUseracc
     * @param mToken
     */
    private void saveUserInfo(String mUseracc, int mUid, String mToken) {
        //保存先清空内容
        SPUtils.clear(getApplicationContext());

        new SPBuild(getApplicationContext())
                .addData(Constant.ISLOGIN, Boolean.TRUE)//登陆志位
                .addData(Constant.LOGINTIME, System.currentTimeMillis())//登陆时间
                .addData(Constant.USERACCOUNT, mUseracc)//账号
                .addData(Constant.USERID, mUid) //用户id
                .addData(Constant.TOKEN, mToken)//token
                .build();
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

}
