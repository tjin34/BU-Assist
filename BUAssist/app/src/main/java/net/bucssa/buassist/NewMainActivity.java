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

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Ui.Fragments.Home.HomeFragment;
import net.bucssa.buassist.Ui.Fragments.Home.NewHomeFragment;
import net.bucssa.buassist.Ui.Fragments.Message.MessageFragment;
import net.bucssa.buassist.Ui.Fragments.Mine.MineFragment;
import net.bucssa.buassist.Ui.Fragments.Mine.NewMineFragment;
import net.bucssa.buassist.Util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/2.
 */

public class NewMainActivity  extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_add)
    ImageView iv_add;

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


    @BindView(R.id.bucssa1)
    ImageView bucssaLeft;

    @BindView(R.id.bucssa2)
    ImageView bucssaRight;


    private List<Fragment> list_fragment = new ArrayList<>();
    private NewHomeFragment mFragment_home;
    private MessageFragment mFragment_message;
    private NewMineFragment mFragment_profile;
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
        return R.layout.activity_main_final;
    }

    @Override
    protected String getTAG() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        status_bar.getLayoutParams().height = getStatusBarHeight();


        initFragment();
        initBottomBar();
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
        mFragment_home = new NewHomeFragment();
        mFragment_message = new MessageFragment();
        mFragment_profile = new NewMineFragment();
        list_fragment.add(mFragment_home);
        list_fragment.add(mFragment_message);
        list_fragment.add(mFragment_profile);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frame_content, list_fragment.get(0));
        mContent = list_fragment.get(0);
        tv_title.setText("BUCSSA");
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
                    arrTextView[position].setTextColor(mContext.getResources().getColor(R.color.mainBgTheme));
                }
            });
        }
        arrImageView[0].setEnabled(false);
        arrTextView[0].setTextColor(mContext.getResources().getColor(R.color.mainBgTheme));
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
                iv_add.setVisibility(View.GONE);
                bucssaLeft.setVisibility(View.VISIBLE);
                bucssaRight.setVisibility(View.GONE);
                break;
            case 1:
                tv_title.setText("消息");
                iv_add.setVisibility(View.VISIBLE);
                bucssaLeft.setVisibility(View.VISIBLE);
                bucssaRight.setVisibility(View.GONE);
                break;
            case 2:
                tv_title.setText("我的");
                iv_add.setVisibility(View.GONE);
                bucssaLeft.setVisibility(View.GONE);
                bucssaRight.setVisibility(View.VISIBLE);
                break;
        }
    }

}
