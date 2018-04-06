package net.bucssa.buassist.Ui.Classmates;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.FragmentAdapter;
import net.bucssa.buassist.Ui.Classmates.Fragments.GroupsFragment;
import net.bucssa.buassist.Ui.Classmates.Fragments.PostsFragment;
import net.bucssa.buassist.Util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class ClassDetailActivity extends BaseActivity {

    @BindView(R.id.status_bar)
    View status_bar;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tv_classCode)
    TextView tv_classCode;

    @BindView(R.id.tv_className)
    TextView tv_className;

    @BindView(R.id.tv_classSection)
    TextView tv_classSection;


    @BindView(R.id.tv_add_collection)
    TextView tv_add_collection;

    @BindView(R.id.tv_professor)
    TextView tv_professor;

    @BindView(R.id.tv_schedule)
    TextView tv_schedule;

    @BindView(R.id.rl_group)
    RelativeLayout rl_group;

    @BindView(R.id.rl_topic)
    RelativeLayout rl_topic;

    private GroupsFragment GroupsFragment;
    private PostsFragment PostsFragment;
    private FragmentAdapter myAdapter;
    private List<Fragment> fragments;

    private Class classItem;



    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_detail_final;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        classItem = (Class) getIntent().getSerializableExtra("Class");

        super.onCreate(savedInstanceState);

        StatusBarUtil.transparencyBar((Activity) mContext);

        initFragment();
    }

    @Override
    protected void initResAndListener() {

        status_bar.getLayoutParams().height = getStatusBarHeight();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_classCode.setText(classItem.getClassCode());
        tv_className.setText(classItem.getClassName());
        tv_professor.setText(classItem.getProfessorName());

        /* 确认当前用户是否收藏了这课程 */
        if (classItem.isCollected()) {
            tv_add_collection.setText(getResources().getString(R.string.isCollected));
            // TODO: 2018/4/5  
        } else {
            tv_add_collection.setText(getResources().getString(R.string.notCollected));
            // TODO: 2018/4/5  
        }

    }

    /**
     * 初始化Fragments及相关部件
     */
    private void initFragment() {

        fragments = new ArrayList<>();

        /* 初始PostFragment并传入classId */
        PostsFragment = new PostsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classItem.getClassId());
        PostsFragment.setArguments(bundle);

        /* 初始GroupsFragment并传入classCode */
        GroupsFragment = new GroupsFragment();
        bundle = new Bundle();
        bundle.putString("classCode", classItem.getClassCode());
        GroupsFragment.setArguments(bundle);

        /* 将Fragments传入adapter并跟Viewpager绑定 */
        fragments.add(PostsFragment);
        fragments.add(GroupsFragment);
        myAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myAdapter);

        /* 初始选择Post分区 */
        rl_topic.setSelected(true);

        /* Post分区点击事件 */
        rl_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_topic.setSelected(true);
                rl_group.setSelected(false);
                viewPager.setCurrentItem(0);
            }
        });

        /* Group分区点击事件 */
        rl_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_topic.setSelected(false);
                rl_group.setSelected(true);
                viewPager.setCurrentItem(1);
            }
        });


    }
}
