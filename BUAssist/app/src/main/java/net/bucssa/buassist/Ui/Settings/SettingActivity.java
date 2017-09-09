package net.bucssa.buassist.Ui.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/20.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.ll_account)
    LinearLayout ll_account;

    @BindView(R.id.ll_notification)
    LinearLayout ll_notification;

    @BindView(R.id.ll_advice)
    LinearLayout ll_advice;

    @BindView(R.id.ll_manual)
    LinearLayout ll_manual;

    @BindView(R.id.ll_aboutUs)
    LinearLayout ll_aboutUs;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title.setText("设置");
    }

    @Override
    protected void initResAndListener() {
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccSettingActivity.launch((Activity) mContext);
            }
        });


    }
}
