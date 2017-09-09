package net.bucssa.buassist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.NewMsg;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.Ui.Fragments.Explore.ExploreFragment;
import net.bucssa.buassist.Ui.Fragments.Home.HomeFragment;
import net.bucssa.buassist.Ui.Fragments.Message.MessageFragment;
import net.bucssa.buassist.Ui.Fragments.Mine.MineFragment;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Shin on 2017-6-22
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 底部导航栏的按键
     **/
    @BindView(R.id.ll_home)
    LinearLayout ll_shouye;

    @BindView(R.id.ll_message)
    LinearLayout ll_xiaoxi;

    @BindView(R.id.ll_explore)
    LinearLayout ll_faxian;

    @BindView(R.id.ll_profile)
    LinearLayout ll_wode;

    /**
     * 底部导航栏的ImageView
     **/
    @BindView(R.id.iv_home)
    ImageView iv_shouye;

    @BindView(R.id.iv_message)
    ImageView iv_xiaoxi;

    @BindView(R.id.iv_explore)
    ImageView iv_faxian;

    @BindView(R.id.iv_profile)
    ImageView iv_wode;


    /**
     * 底部导航栏的TextView
     **/
    @BindView(R.id.tv_home)
    TextView tv_shouye;

    @BindView(R.id.tv_message)
    TextView tv_xiaoxi;

    @BindView(R.id.tv_explore)
    TextView tv_faxian;

    @BindView(R.id.tv_profile)
    TextView tv_wode;

    @BindView(R.id.tip_message)
    ImageView tip_message;

    /**
     * 底部导航栏
     **/
    @BindView(R.id.tap_bar)
    LinearLayout tap_bar;

    private Context context = MainActivity.this;
    private List<Fragment> list_fragment = new ArrayList<>();
    private HomeFragment mFragment_home;
    private MessageFragment mFragment_message;
    private ExploreFragment mFragment_explore;
    private MineFragment mFragment_profile;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment mContent = new Fragment();

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, String action) {
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra("action", action);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String getTAG() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFragment();
        initBottomBar();

        checkNewMsg();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentManager = null;
        transaction = null;
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        mFragment_home = new HomeFragment();
        mFragment_message = new MessageFragment();
        mFragment_explore = new ExploreFragment();
        mFragment_profile = new MineFragment();
        list_fragment.add(mFragment_home);
        list_fragment.add(mFragment_message);
        list_fragment.add(mFragment_explore);
        list_fragment.add(mFragment_profile);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_content, list_fragment.get(0));
        tv_title.setText("推送主页 Home");
        mContent = list_fragment.get(0);
        transaction.commit();
    }

    /**
     * 初始化下部按键
     */
    private void initBottomBar() {
        /** 底部导航栏的ImageView集合 **/
        final ImageView[] arrImageView = {iv_shouye, iv_xiaoxi, iv_faxian, iv_wode};
        /** 底部导航栏的TextView集合 **/
        final TextView[] arrTextView = {tv_shouye, tv_xiaoxi, tv_faxian, tv_wode};

        List<View> tapbar = new ArrayList<>();
        tapbar.add(ll_shouye);
        tapbar.add(ll_xiaoxi);
        tapbar.add(ll_faxian);
        tapbar.add(ll_wode);

        for (int i = 0; i < 4; i++) {
            tapbar.get(i).setTag(i);
            tapbar.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    switch (position) {
                        case 0: //首页
                            switchContent(mContent, list_fragment.get(position));
                            tv_title.setText("推送主页 Home");
                            break;
                        case 1: //消息
                            switchContent(mContent, list_fragment.get(position));
                            tv_title.setText("消息 Message");
                            tip_message.setVisibility(View.GONE);
                            break;
                        case 2: //发现
                            switchContent(mContent, list_fragment.get(position));
                            tv_title.setText("发现 Explore");
                            break;
                        case 3: //我的
                            switchContent(mContent, list_fragment.get(position));
                            tv_title.setText("我的 Profile");
                            break;
                    }
                    for (int j = 0; j < 4; j++) {
                        arrImageView[j].setEnabled(true);
                        arrTextView[j].setTextColor(context.getResources().getColor(R.color.grey_600));
                    }
                    arrImageView[position].setEnabled(false);
                    arrTextView[position].setTextColor(context.getResources().getColor(R.color.themeColor));
                }
            });
        }
        arrImageView[0].setEnabled(false);
        arrTextView[0].setTextColor(context.getResources().getColor(R.color.themeColor));
    }

    /**
     * 切换Fragment
     *
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.frame_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
        mContent = to;
    }

    /**
     * 检测是否有新消息
     */
    private void checkNewMsg(){
        Observable<BaseEntity<NewMsg>> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .checkNewMsgByUid(UserSingleton.USERINFO.getUid());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<NewMsg>>() {
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
                    public void onNext(BaseEntity<NewMsg> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            tip_message.setVisibility(View.VISIBLE);
                        } else {
                            tip_message.setVisibility(View.GONE);
                        }
                        Logger.d();
                    }
                });
    }
}
