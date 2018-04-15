package net.bucssa.buassist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.moduth.blockcanary.BlockCanary;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Ui.Classmates.ClassmateActivity;
import net.bucssa.buassist.Ui.Fragments.Home.HomeFragment;
import net.bucssa.buassist.Ui.Fragments.Message.CreateChatActivity;
import net.bucssa.buassist.Ui.Fragments.Message.MessageFragment;
import net.bucssa.buassist.Ui.Fragments.Mine.MineFragment;
import net.bucssa.buassist.Ui.Webview.WebViewActivity;
import net.bucssa.buassist.Util.AppBlockCanaryContext;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/2.
 */

public class MainActivity  extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.rlSwitchBar)
    RelativeLayout rlSwitchBar;

    @BindView(R.id.tvPrivate)
    TextView tvPrivate;

    @BindView(R.id.tvSystem)
    TextView tvSystem;

    @BindView(R.id.ll_classmate)
    LinearLayout ll_classmate;

    @BindView(R.id.ll_manual)
    LinearLayout ll_manual;

    @BindView(R.id.bottomBar)
    LinearLayout ll_bottomBar;

    /**
     * 底部导航栏的按键
     **/
    @BindView(R.id.ll_home)
    LinearLayout ll_shouye;

    @BindView(R.id.ll_message)
    LinearLayout ll_xiaoxi;

    @BindView(R.id.ll_mine)
    LinearLayout ll_wode;

    /**
     * 底部导航栏的ImageView
     **/
    @BindView(R.id.iv_home)
    ImageView iv_shouye;

    @BindView(R.id.iv_message)
    ImageView iv_xiaoxi;

    @BindView(R.id.iv_mine)
    ImageView iv_wode;

    /**
     * 底部导航栏的TextView
     **/
    @BindView(R.id.tv_home)
    TextView tv_shouye;

    @BindView(R.id.tv_message)
    TextView tv_xiaoxi;

    @BindView(R.id.tv_mine)
    TextView tv_wode;


    @BindView(R.id.iv_lulu)
    ImageView iv_lulu;


    @BindView(R.id.fl_bucssa)
    FrameLayout fl_bucssa;


    private List<Fragment> list_fragment = new ArrayList<>();
    private HomeFragment mFragment_home;
    private MessageFragment mFragment_message;
    private MineFragment mFragment_profile;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment mContent = new Fragment();

    private final int CREATE_CHAT_REQUEST = 1;

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

        PgyUpdateManager.setIsForced(true); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
            }

            @Override
            public void onUpdateAvailable(String s) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(s);
                new AlertDialog.Builder(mContext)
                        .setTitle("更新")
                        .setMessage("发现新的版本，是否下载更新")
                        .setNegativeButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                startDownloadTask(
                                        (Activity) mContext,
                                        appBean.getDownloadURL());
                            }
                        }).show();
            }

        });

        initFragment();
        initBottomBar();
        initView();
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
        PgyUpdateManager.unregister();
        fragmentManager = null;
        transaction = null;
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        mFragment_home = new HomeFragment();
        mFragment_message = new MessageFragment();
        mFragment_message.setOnSwitchBarListener(new MessageFragment.OnSwitchBarListener() {
            @Override
            public void switchContent(int index) {
                switch (index) {
                    case 0:
                        tvPrivate.setSelected(true);
                        tvPrivate.setTextColor(getResources().getColor(R.color.mainTheme));
                        tvSystem.setSelected(false);
                        tvSystem.setTextColor(getResources().getColor(R.color.tv_grey));
                        iv_add.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        tvPrivate.setSelected(false);
                        tvPrivate.setTextColor(getResources().getColor(R.color.tv_grey));
                        tvSystem.setSelected(true);
                        tvSystem.setTextColor(getResources().getColor(R.color.mainTheme));
                        iv_add.setVisibility(View.GONE);
                        break;
                }
            }
        });
        mFragment_profile = new MineFragment();
        list_fragment.add(mFragment_home);
        list_fragment.add(mFragment_message);
        list_fragment.add(mFragment_profile);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_content, list_fragment.get(0));
        mContent = list_fragment.get(0);
        switchPage(0);
        transaction.commit();
    }

    /**
     * 初始化下部按键
     */
    private void initBottomBar() {
        /** 底部导航栏的ImageView集合 **/
        final ImageView[] arrImageView = {iv_shouye, iv_xiaoxi, iv_wode};
        /** 底部导航栏的TextView集合 **/
        final TextView[] arrTextView = {tv_shouye, tv_xiaoxi, tv_wode};

        List<View> tapbar = new ArrayList<>();
        tapbar.add(ll_shouye);
        tapbar.add(ll_xiaoxi);
        tapbar.add(ll_wode);

        for (int i = 0; i < 3; i++) {
            tapbar.get(i).setTag(i);
            tapbar.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    switch (position) {
                        case 0: //首页
                            switchContent(mContent, list_fragment.get(position));
                            switchPage(0);
                            break;
                        case 1: //消息
                            switchContent(mContent, list_fragment.get(position));
                            switchPage(1);
                            break;
                        case 2: //我的
                            switchContent(mContent, list_fragment.get(position));
                            switchPage(2);
                            break;
                    }
                    for (int j = 0; j < 3; j++) {
                        arrImageView[j].setEnabled(true);
                        arrTextView[j].setTextColor(mContext.getResources().getColor(R.color.white));
                    }
                    arrImageView[position].setEnabled(false);
                    arrTextView[position].setTextColor(mContext.getResources().getColor(R.color.tapbarPressed));
                }
            });
        }
        arrImageView[0].setEnabled(false);
        arrTextView[0].setTextColor(mContext.getResources().getColor(R.color.tapbarPressed));
    }

    private void initView() {
        tvPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment_message.switchPage(0);
                tvPrivate.setSelected(true);
                tvSystem.setSelected(false);
            }
        });

        tvSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment_message.switchPage(1);
                tvPrivate.setSelected(false);
                tvSystem.setSelected(true);
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, CreateChatActivity.class), CREATE_CHAT_REQUEST);
            }
        });

        ll_classmate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ClassmateActivity.class));
            }
        });

        ll_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", "https://tianrenli92.github.io/BUCSSA-new-student-manual/index.html");
                startActivity(intent);
            }
        });
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

    private void switchPage(int index) {
        switch (index) {
            case 0:
                tv_title.setText("BUCSSA");
                tv_title.setVisibility(View.VISIBLE);
                iv_add.setVisibility(View.GONE);
                iv_lulu.setVisibility(View.VISIBLE);
                fl_bucssa.setVisibility(View.VISIBLE);
                rlSwitchBar.setVisibility(View.GONE);
                ll_bottomBar.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_title.setVisibility(View.GONE);
                if (mFragment_message.viewPager != null) {
                    iv_add.setVisibility(mFragment_message.viewPager.getCurrentItem() == 0 ? View.VISIBLE : View.GONE);
                } else {
                    iv_add.setVisibility(View.VISIBLE);
                }
                iv_lulu.setVisibility(View.GONE);
                fl_bucssa.setVisibility(View.GONE);
                rlSwitchBar.setVisibility(View.VISIBLE);
                ll_bottomBar.setVisibility(View.GONE);
                break;
            case 2:
                tv_title.setText("我的");
                tv_title.setVisibility(View.VISIBLE);
                iv_add.setVisibility(View.GONE);
                iv_lulu.setVisibility(View.GONE);
                fl_bucssa.setVisibility(View.GONE);
                rlSwitchBar.setVisibility(View.GONE);
                ll_bottomBar.setVisibility(View.GONE);
                break;
        }
    }

}
