package net.bucssa.buassist.Ui.Fragments.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.FragmentAdapter;
import net.bucssa.buassist.Ui.Classmates.Fragments.GroupsFragment;
import net.bucssa.buassist.Ui.Classmates.Fragments.PostsFragment;
import net.bucssa.buassist.Ui.Fragments.Message.Adapter.NewChatAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Widget.CustomListViewForRefreshView;
import net.bucssa.buassist.Widget.RefreshHelper;
import net.bucssa.buassist.Widget.RefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/27.
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private PersonalMsgFragment personalMsgFragment;
    private SystemMsgFragment systemMsgFragment;
    private FragmentAdapter myAdapter;
    private List<Fragment> fragments;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFragments();
    }

    private void initFragments() {
        fragments = new ArrayList<>();

        personalMsgFragment = new PersonalMsgFragment();
        systemMsgFragment = new SystemMsgFragment();

        /* 将Fragments传入adapter并跟Viewpager绑定 */
        fragments.add(personalMsgFragment);
        fragments.add(systemMsgFragment);
        myAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(myAdapter);

        onSwitchBarListener.switchContent(0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onSwitchBarListener.switchContent(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void switchPage(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

    }


    public interface OnSwitchBarListener{
        void switchContent(int index);
    }

    private OnSwitchBarListener onSwitchBarListener;

    public void setOnSwitchBarListener(OnSwitchBarListener onSwitchBarListener) {
        this.onSwitchBarListener = onSwitchBarListener;
    }
}
