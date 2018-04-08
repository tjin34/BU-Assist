package net.bucssa.buassist.Ui.Classmates;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.Bean.Request.AddClassCollectionReq;
import net.bucssa.buassist.Bean.Request.DelClassCollectionReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.FragmentAdapter;
import net.bucssa.buassist.Ui.Classmates.Fragments.GroupsFragment;
import net.bucssa.buassist.Ui.Classmates.Fragments.PostsFragment;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.StatusBarUtil;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    @BindView(R.id.llGroupLeft)
    LinearLayout llGroupLeft;

    @BindView(R.id.llGroupRight)
    LinearLayout llGroupRight;

    @BindView(R.id.ivGroupAdd)
    ImageView ivGroupAdd;

    @BindView(R.id.rl_topic)
    RelativeLayout rl_topic;

    @BindView(R.id.llTopicLeft)
    LinearLayout llTopicLeft;

    @BindView(R.id.llTopicRight)
    LinearLayout llTopicRight;

    @BindView(R.id.ivTopicAdd)
    ImageView ivTopicAdd;

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
            setDeleteCollection();
        } else {
            setAddCollection();
        }

    }

    private void setDeleteCollection() {
        tv_add_collection.setText(getResources().getString(R.string.isCollected));
        tv_add_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelClassCollectionReq req = new DelClassCollectionReq(UserSingleton.USERINFO.getUid(),
                        classItem.getClassId(),UserSingleton.USERINFO.getToken());
                Gson gson=new Gson();
                String json = gson.toJson(req);
                delCollection(json);
            }
        });
    }

    private void setAddCollection() {
        tv_add_collection.setText(getResources().getString(R.string.notCollected));
        tv_add_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClassCollectionReq req = new AddClassCollectionReq(UserSingleton.USERINFO.getUid(),
                        classItem.getClassId(),UserSingleton.USERINFO.getToken());
                Gson gson=new Gson();
                String json = gson.toJson(req);
                addCollection(json);
            }
        });
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

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rl_topic.setSelected(true);
                        rl_group.setSelected(false);
                        llTopicRight.setSelected(true);
                        ivTopicAdd.setSelected(true);
                        break;
                    case 1:
                        rl_topic.setSelected(false);
                        rl_group.setSelected(true);
                        llGroupRight.setSelected(true);
                        ivGroupAdd.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /* Post分区点击事件 */
        llTopicLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_topic.setSelected(true);
                rl_group.setSelected(false);
                llTopicRight.setSelected(true);
                ivTopicAdd.setSelected(true);
                viewPager.setCurrentItem(0);
            }
        });

        /* Group分区点击事件 */
        llGroupLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_topic.setSelected(false);
                rl_group.setSelected(true);
                llGroupRight.setSelected(true);
                ivGroupAdd.setSelected(true);
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void addCollection(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .addClassCollection(body);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(mContext, "收藏成功！");
                            setDeleteCollection();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void delCollection(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .deleteClassCollection(body);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(mContext, "删除收藏成功！");
                            setAddCollection();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }


    private void createGroup(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .createGroup(body);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(mContext, "创建成功！");
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void createPost(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .createPost(body);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(mContext, "创建成功！");
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

}
