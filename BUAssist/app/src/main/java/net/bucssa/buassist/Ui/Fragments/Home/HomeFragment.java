package net.bucssa.buassist.Ui.Fragments.Home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Api.TuiSongAPI;
import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Request.AddCollectionReq;
import net.bucssa.buassist.Bean.Request.DelCollectionReq;
import net.bucssa.buassist.Bean.Thread.TuiSong;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.ClassmateActivity;
import net.bucssa.buassist.Ui.Fragments.Home.Adapter.NewsListAdapter;
import net.bucssa.buassist.Ui.Fragments.Home.Adapter.ViewPagerAdapter;
import net.bucssa.buassist.Ui.Webview.WebViewActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Widget.CustomListViewForRefreshView;
import net.bucssa.buassist.Widget.CustomListViewWithLoadMore;
import net.bucssa.buassist.Widget.CustomScrollView;
import net.bucssa.buassist.Widget.RefreshHelper;
import net.bucssa.buassist.Widget.RefreshView;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/8/2.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    @BindView(R.id.lv_news)
    ListView lv_news;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tvAdvTitle)
    TextView tvAdvTitle;

    @BindView(R.id.RadioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.ll_classmate)
    LinearLayout ll_classmate;

    @BindView(R.id.ll_manual)
    LinearLayout ll_manual;


    private List<TuiSong> tuiSongList = new ArrayList<>();
    private NewsListAdapter myAdapter;
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

    private Handler mHandler = new Handler();

    private static final int[] pics = { R.drawable.bulogo, R.drawable.bulogo,
            R.drawable.bulogo, R.drawable.bulogo, R.drawable.bulogo };

    private static final String[] picPath = { "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2295574849,2578711430&fm=27&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3294968488,2101976562&fm=27&gp=0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523297409&di=cf016aeb727a58838f431b56e0441171&imgtype=jpg&er=1&src=http%3A%2F%2Fs15.sinaimg.cn%2Fmw690%2F006xUpFJzy77vHVwe8mae%26amp%3B690",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=858706739,1622508857&fm=27&gp=0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523297551&di=f9bde8aa6311dd014ee4d685b224423d&imgtype=jpg&er=1&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fe1fe9925bc315c60ebc2eb768ab1cb13485477d0.jpg"};

    private static final String[] advTitle = {"新垣结衣-1","新垣结衣-2","新垣结衣-3","新垣结衣-4","新垣结衣-5"};


@Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                    tvAdvTitle.setText(advTitle[0]);
                    break;
                case R.id.radio2:
                    viewPager.setCurrentItem(1);
                    currentItem = 1;
                    tvAdvTitle.setText(advTitle[1]);
                    break;
                case R.id.radio3:
                    viewPager.setCurrentItem(2);
                    currentItem = 2;
                    tvAdvTitle.setText(advTitle[2]);
                    break;
                case R.id.radio4:
                    viewPager.setCurrentItem(3);
                    currentItem = 3;
                    tvAdvTitle.setText(advTitle[3]);
                    break;
                case R.id.radio5:
                    viewPager.setCurrentItem(4);
                    currentItem = 4;
                    tvAdvTitle.setText(advTitle[4]);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initResAndListener() {

        radioGroup.setOnCheckedChangeListener(listener);
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < picPath.length; i++) {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(mParams);
            Glide.with(context)
                    .asBitmap()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                    .load(picPath[i])
                    .into(iv);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            views.add(iv);
        }

        viewPagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        tvAdvTitle.setText(advTitle[0]);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                radioButton = (RadioButton) radioGroup.getChildAt(position);
                radioButton.setChecked(true);
                currentItem = position;
                tvAdvTitle.setText(advTitle[position]);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        ll_classmate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ClassmateActivity.class));
            }
        });

        ll_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", "https://tianrenli92.github.io/BUCSSA-new-student-manual/index.html");
                startActivity(intent);
            }
        });


        initData();

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        Glide.with(context)
                .asGif()
                .load(R.raw.pull)
                .into(ivLulu);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Glide.with(context)
                        .asGif()
                        .load(R.raw.refreshing)
                        .into(ivLulu);
                refreshData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Glide.with(context)
                        .asGif()
                        .load(R.raw.pull)
                        .into(ivLulu);
                loadMore();
            }
        });

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
                myAdapter = new NewsListAdapter(context, tuiSongList);
                setListener();
                lv_news.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addItems(tuiSongList, 0);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (tuiSongList.size() == 0) {
                    refreshLayout.finishLoadMore(1000);
                    ToastUtils.showToast(context, "已加载全部推送！");
                    break;
                }
                myAdapter.addItems(tuiSongList,myAdapter.getCount());
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }

    private void setListener() {
        myAdapter.setOnRecyclerItemClickListener(new NewsListAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onClick(String url) {

            }

            @Override
            public void onAddCollection(TuiSong tuiSong) {
                AddCollectionReq req=new AddCollectionReq(UserSingleton.USERINFO.getUid(),
                        tuiSong.getTid(), tuiSong.getSubject(),
                        tuiSong.getAuthor(), tuiSong.getDateline() ,UserSingleton.USERINFO.getToken());
                Gson gson=new Gson();
                String json = gson.toJson(req);
                addCollection(json);
            }

            @Override
            public void onDelCollection(TuiSong tuiSong) {
                DelCollectionReq req=new DelCollectionReq(UserSingleton.USERINFO.getUid(),
                        tuiSong.getTid(),UserSingleton.USERINFO.getToken());
                Gson gson=new Gson();
                String json = gson.toJson(req);
                delCollection(json);
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

    private void addCollection(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(TuiSongAPI.class)
                .addCollection(body);

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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "收藏成功！");
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void delCollection(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(TuiSongAPI.class)
                .delCollection(body);

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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "删除收藏成功！");
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }


}
