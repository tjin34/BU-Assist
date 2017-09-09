package net.bucssa.buassist.Ui.Login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Login.UserInfo;
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
    private EditText mIdEditText; // 登录ID编辑框
    private EditText mPwdEditText; // 登录密码编辑框
    private Button mLoginButton; // 登录按钮
//    private Button mRegisterButton; //注册按钮
    private TextView guestLogin;
    private ImageView iv_back;
    private RelativeLayout rootView;
    private String mIdString;
    private String mPwdString;
    private int mUid;
    private String mToken;
    private int keyboardHeight;

    private Intent intent;

    private int preActivity = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preActivity = getIntent().getIntExtra("preActivity", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        iv_back.setOnClickListener(this);
        guestLogin.setOnClickListener(this);
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
        if (preActivity == 0)
            iv_back.setVisibility(View.GONE);
        else
            iv_back.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.login_btnLogin:
                // 启动登录
                showLoginingDlg(); // 显示"正在登录"对话框
                Log.i(TAG, mIdString + "  " + mPwdString);
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
            case R.id.tv_guest_login:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
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
