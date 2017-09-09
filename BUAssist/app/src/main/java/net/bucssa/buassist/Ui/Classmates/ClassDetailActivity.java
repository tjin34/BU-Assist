package net.bucssa.buassist.Ui.Classmates;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.Temp.Class;
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

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tv_classCode)
    TextView tv_classCode;

    @BindView(R.id.tv_className)
    TextView tv_className;

    @BindView(R.id.tv_professor)
    TextView tv_professor;

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
        return R.layout.activity_class_detail;
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
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_classCode.setText(classItem.getClassCode());
        tv_className.setText(classItem.getClassName());
        tv_professor.setText(classItem.getProfessorName());


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset == 0) {
//                    // TODO: 17/7/28
//                    tv_title.setVisibility(View.GONE);
//                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()*2/3){
//                    tv_title.setVisibility(View.VISIBLE);
////                    tv_title.setAlpha((appBarLayout.getTotalScrollRange()*2/3)/(Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange()/3));
//                    tv_title.setAlpha((float) (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange()*2/3)/
//                            (appBarLayout.getTotalScrollRange()/3));
//                }
            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        PostsFragment = new PostsFragment();
        GroupsFragment = new GroupsFragment();
        fragments.add(PostsFragment);
        fragments.add(GroupsFragment);

        myAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
