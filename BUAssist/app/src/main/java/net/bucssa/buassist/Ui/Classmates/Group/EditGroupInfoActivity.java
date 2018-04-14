package net.bucssa.buassist.Ui.Classmates.Group;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;

import butterknife.BindView;

/**
 * Created by shinji on 2018/4/13.
 */

public class EditGroupInfoActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_avatar)
    LinearLayout ll_avatar;

    @BindView(R.id.iv_avatar)
    TextView iv_avatar;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initResAndListener() {
        super.initResAndListener();
    }


}
