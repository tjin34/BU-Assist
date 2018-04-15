package net.bucssa.buassist.Ui.Classmates.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Mine.InputActivity;
import net.bucssa.buassist.Util.Utils;
import net.bucssa.buassist.Widget.FlowLayout;

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
    ImageView iv_avatar;

    @BindView(R.id.ll_groupName)
    LinearLayout ll_groupName;

    @BindView(R.id.tv_groupName)
    TextView tv_groupName;

    @BindView(R.id.ll_self_intro)
    LinearLayout ll_groupIntro;

    @BindView(R.id.tv_self_intro)
    TextView tv_groupIntro;

    @BindView(R.id.ivAddClass)
    ImageView ivAddClass;

    @BindView(R.id.flTags)
    FlowLayout flTags;

    private Group group;

    private static final int EDIT_GROUP_PROFILE = 0x03;


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
        group = (Group) getIntent().getSerializableExtra("Group");
        super.onCreate(savedInstanceState);

        setValue();

    }

    /**
     * 设置所有文字信息
     */
    private void setValue() {
        String title = group.getGroupName() + " - 小组资料";
        tv_title.setText(title);

        /* 文字信息 */
        tv_groupName.setText(group.getGroupName());
        tv_groupIntro.setText(group.getGroupIntro());

        /* 小组TAG */
        String[] tags = group.getGroupTag().split(",");
        for (int i = 0; i < tags.length; i++) {
            TextView tvTag = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_class_tags, null, false);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, Utils.dp2px(mContext, 35));
            params.setMargins(10, 5, 0, 5);
            tvTag.setLayoutParams(params);
            String tagDisplay = "#" + tags[i];
            tvTag.setText(tagDisplay);
            flTags.addView(tvTag);
        }
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

        ll_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 3);
                intent.putExtra("Group", group);
                startActivityForResult(intent, EDIT_GROUP_PROFILE);
            }
        });

        ll_groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 4);
                intent.putExtra("Group", group);
                startActivityForResult(intent, EDIT_GROUP_PROFILE);
            }
        });
    }


}
