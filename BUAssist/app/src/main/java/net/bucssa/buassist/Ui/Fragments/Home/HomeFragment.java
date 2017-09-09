package net.bucssa.buassist.Ui.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Api.TuiSongAPI;
import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Thread.TuiSong;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.ClassmateActivity;
import net.bucssa.buassist.Ui.Fragments.Home.Adapter.NewsRecyclerAdapter;
import net.bucssa.buassist.Ui.Fragments.Home.Adapter.ViewPagerAdapter;
import net.bucssa.buassist.Ui.News.WebPageActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/19.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.ll_housing)
    LinearLayout ll_housing;

    @BindView(R.id.ll_classmate)
    LinearLayout ll_class;

    @BindView(R.id.refreshLayout)
    MaterialRefreshLayout mRefreshLayout;

    @BindView(R.id.rv_news)
    RecyclerView rv_news;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.RadioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.initView)
    LinearLayout initView;

    @BindView(R.id.iv_light)
    ImageView iv_light;

    @BindView(R.id.iv_lulu)
    ImageView iv_lulu;

    @BindView(R.id.tv_status)
    TextView tv_status;


    private List<TuiSong> tuiSongList = new ArrayList<>();
    private NewsRecyclerAdapter myAdapter;
    private int state = Enum.STATE_NORMAL;

    int pageIndex = 1;//当前页
    int pageSize = 10;//每一页数量
    int totalCount = 0;//总数

    private List<View> views;
    private ViewPagerAdapter viewPagerAdapter;
    private RadioButton radioButton;
    private int currentItem; // 当前页面
    private ScheduledExecutorService scheduledExecutorService;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 设置当前页面
            viewPager.setCurrentItem(currentItem);
        };
    };

    private static final int[] pics = { R.drawable.bulogo, R.drawable.bulogo,
            R.drawable.bulogo, R.drawable.bulogo, R.drawable.bulogo };

    private static final String[] picPath = { "http://imgsrc.baidu.com/forum/w%3D580/sign=71713a8b0e7b02080cc93fe952d8f25f/dc75c62a2834349b1c4c7cabcdea15ce36d3be2a.jpg",
            "http://imgsrc.baidu.com/forum/w%3D580/sign=6d5e7578d4160924dc25a213e406359b/cf22c8b44aed2e73507794028301a18b86d6fadf.jpg",
            "http://imgsrc.baidu.com/forum/w%3D580/sign=a50d5a1008f3d7ca0cf63f7ec21fbe3c/29fa1ef431adcbef4b266328a8af2edda3cc9f62.jpg",
            "http://imgsrc.baidu.com/forum/w%3D580/sign=b64b0730bd389b5038ffe05ab534e5f1/04475334970a304ea585bdd4d5c8a786c8175cd5.jpg",
            "http://imgsrc.baidu.com/forum/w%3D580/sign=bad2ae38cbfc1e17fdbf8c397a91f67c/f1a2433d269759eec41679e5b6fb43166d22df37.jpg"};

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initResAndListener() {

        radioGroup.setOnCheckedChangeListener(listener);
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < picPath.length; i++) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(mParams);
            Picasso.with(context).load(picPath[i]).error(pics[i]).into(iv);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            views.add(iv);
        }

        viewPagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                radioButton = (RadioButton) radioGroup.getChildAt(position);
                radioButton.setChecked(true);
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if ((pageIndex * pageSize) <= totalCount) {
                    loadMore();
                } else {
                    ToastUtils.showToast(context, "没有更多了...");
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });


        ll_housing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), HousingActivity.class);
//                startActivity(intent);
            }
        });

        ll_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassmateActivity.class);
                startActivity(intent);
            }
        });

        initLuluAnim();

        initData();

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {


    }

    @Override
    public void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 每隔2秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 10, 10, TimeUnit.SECONDS);
        // scheduleAtFixedRate(command, initialDelay, period, unit);
        // command：执行线程 initialDelay：初始化延时 period：前一次执行结束到下一次执行开始的间隔时间（间隔执行延迟时间）
        // unit：计时单位
    }

    // 切换图片
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            currentItem = (currentItem + 1) % pics.length;
            // 更新界面
            handler.obtainMessage().sendToTarget();
            // message对象sendToTarget()，handler对象sendMessage();
        }
    }

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkId) {
            switch (checkId) {
                case R.id.radio1:
                    viewPager.setCurrentItem(0);
                    currentItem = 0;
                    break;
                case R.id.radio2:
                    viewPager.setCurrentItem(1);
                    currentItem = 1;
                    break;
                case R.id.radio3:
                    viewPager.setCurrentItem(2);
                    currentItem = 2;
                    break;
                case R.id.radio4:
                    viewPager.setCurrentItem(3);
                    currentItem = 3;
                    break;
                case R.id.radio5:
                    viewPager.setCurrentItem(4);
                    currentItem = 4;
                    break;
                default:
                    break;
            }
        }
    };

    private void initLuluAnim(){
        iv_light.setVisibility(View.INVISIBLE);
        initView.setVisibility(View.VISIBLE);
        Animation luluShake = AnimationUtils.loadAnimation(context, R.anim.lulu_anim_shake);
        iv_lulu.startAnimation(luluShake);
        tv_status.setText(R.string.lulu_loading);
    }

    private void stopLuluAnim() {
        iv_lulu.clearAnimation();
        iv_light.setVisibility(View.VISIBLE);
        tv_status.setText(R.string.lulu_finishLoading);
        Animation initViewOut = AnimationUtils.loadAnimation(context, R.anim.lulu_anim_out);
        initViewOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        initView.startAnimation(initViewOut);
    }

    /**
     * 下拉刷新
     */
    public void refreshData() {
        pageIndex = 1;
        state = Enum.STATE_REFRESH;
        initData();
    }

    /**
     * 上拉刷新
     */
    private void loadMore() {
        pageIndex++;
        state = Enum.STATE_MORE;
        initData();
    }

    private void initData() {
        getOfficialThreads(pageIndex, pageSize);
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                rv_news.setLayoutManager(new GridLayoutManager(context, 2));
//                rv_news.setLayoutManager(new LinearLayoutManager(context));
                myAdapter = new NewsRecyclerAdapter(context, tuiSongList);
                setListener();
                rv_news.setAdapter(myAdapter);
                stopLuluAnim();
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addItems(tuiSongList, 0);
                mRefreshLayout.finishRefresh();
                break;
            case Enum.STATE_MORE:
                myAdapter.addItems(tuiSongList, myAdapter.getItemCount());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void setListener() {
        myAdapter.setOnRecyclerItemClickListener(new NewsRecyclerAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onTopbarClick(int tid) {
                Intent intent = new Intent(context, WebPageActivity.class);
                intent.putExtra("tid", tid);
                startActivity(intent);
            }

            @Override
            public void onCommentClick(int tid) {

            }

            @Override
            public void onLikeClick(int tid) {

            }
        });

    }


    private void getOfficialThreads(int pageIndex, int pageSize){
        Observable<BaseEntity<List<TuiSong>>> observable = RetrofitClient.createService(TuiSongAPI.class)
                .getOfficialThreads(UserSingleton.USERINFO.getUid(), pageIndex, pageSize);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<TuiSong>>>() {
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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity<List<TuiSong>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            tuiSongList = baseEntity.getDatas();
                            totalCount = baseEntity.getTotal();
                            changeByState();
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    public void scrollToTopAndrefreshNews(){
        scrollView.smoothScrollTo(0,0);
        mRefreshLayout.autoRefresh();
    }
}