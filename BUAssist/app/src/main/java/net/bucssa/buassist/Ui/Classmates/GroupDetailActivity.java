package net.bucssa.buassist.Ui.Classmates;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/30.
 */

public class GroupDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_groupName)
    TextView tv_groupName;

    private String groupName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        groupName = getIntent().getStringExtra("groupName");
        super.onCreate(savedInstanceState);

        tv_title.setText("小组详情");

        tv_groupName.setText(groupName);

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
    }
}
