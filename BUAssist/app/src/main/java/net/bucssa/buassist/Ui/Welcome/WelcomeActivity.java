package net.bucssa.buassist.Ui.Welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Login.UserInfo;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.MainActivity;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Login.LoginActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/25.
 */
public class WelcomeActivity extends BaseActivity{
    @BindView(R.id.iv_welcome)
    ImageView iv_welcome;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tv_status)
    TextView tv_status;

    private int mUid;
    private String mToken;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    /**
     * 启动欢迎页面
     *
     * @param activity
     */
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UserSingleton.getInstance().isLogin(mContext)) {
            mUid = UserSingleton.getInstance().getUid(mContext);
            mToken = UserSingleton.getInstance().getUserToken(mContext);
            progressBar.setVisibility(View.VISIBLE);
            tv_status.setText(R.string.auto_login);
            getUserInfo(mUid, mToken);
        } else {
            tv_status.setText(R.string.welcome);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);

                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);

                    } catch (Exception ex) {
                        ToastUtils.showToast(mContext, ex.toString());
                    }
                }
            }).start();
        }
    }

    @Override
    protected void initResAndListener() {
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
                        if (!isLogin || UserSingleton.USERINFO == null) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onNext(BaseEntity<UserInfo> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            UserSingleton.USERINFO = baseEntity.getDatas();
                            UserSingleton.getHttpAvatar();
                            UserSingleton.getInstance().setLogin(true);

                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);

                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
