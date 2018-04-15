package net.bucssa.buassist.Ui.Fragments.Mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Login.LoginActivity;
import net.bucssa.buassist.Ui.Settings.SettingActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.ToastUtils;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/20.
 */

public class MineFragment extends BaseFragment {


    @BindView(R.id.rl_editProfile)
    RelativeLayout rl_editProfile;

    @BindView(R.id.rl_logout)
    RelativeLayout rl_logout;

    @BindView(R.id.rl_myFriend)
    RelativeLayout rl_myFriend;

    @BindView(R.id.rl_myCollection)
    RelativeLayout rl_myCollection;

    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    @BindView(R.id.profileBar)
    LinearLayout profileBar;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.tv_college)
    TextView tv_college;

    @BindView(R.id.iv_sexDisplay)
    ImageView iv_sexDisplay;

    @BindView(R.id.tv_dateOfBirth)
    TextView tv_dateOfBirth;

    @BindView(R.id.tv_self_intro)
    TextView tv_selfIntro;

    @BindView(R.id.tv_loveStatus)
    TextView tv_loveStatus;

    @BindView(R.id.tv_email)
    TextView tv_email;

    private final int LOGIN = 111;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserSingleton.getInstance().isLogin(context)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("preActivity", 1);
                    startActivityForResult(intent, LOGIN);
                }
            }
        });

        rl_logout.setVisibility(View.GONE);
        profileBar.setVisibility(View.GONE);
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.logout(context);
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("preActivity", 1);
                startActivityForResult(intent, LOGIN);
            }
        });

        rl_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserSingleton.getInstance().isLogin(context)) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(context, "请先登录后再查看/修改个人信息哦~");
                }
            }
        });

        if (UserSingleton.getInstance().isLogin(context)) {
            rl_logout.setVisibility(View.VISIBLE);
            setProfileValues();
        }

        rl_myFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFriendActivity.launch((Activity) context);
            }
        });

        rl_myCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCollectionActivity.launch((Activity) context);
            }
        });

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.launch((Activity) context);
            }
        });

    }

    private void setProfileValues() {

        Glide.with(context)
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .load(UserSingleton.bigAvatar)
                .into(iv_profile);

        tv_username.setText(UserSingleton.USERINFO.getUsername());
        tv_college.setText(UserSingleton.USERINFO.getCollege());
        tv_dateOfBirth.setText(UserSingleton.USERINFO.getDateOfBirth());
        tv_loveStatus.setText(UserSingleton.USERINFO.getAffectivestatus());
        if (!UserSingleton.USERINFO.getBio().equals("")) tv_selfIntro.setText(UserSingleton.USERINFO.getBio());

        switch (UserSingleton.USERINFO.getGender()) {
            case 0:
                iv_sexDisplay.setVisibility(View.GONE);
            case 1:
                iv_sexDisplay.setSelected(true);
                break;
            case 2:
                iv_sexDisplay.setSelected(false);
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case LOGIN:
                    rl_logout.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(UserSingleton.bigAvatar).error(R.drawable.profile_photo).into(iv_profile);
                    break;
            }
        }
    }

}
