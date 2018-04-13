package net.bucssa.buassist.Ui.Fragments.Mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.TuiSongAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Thread.Collection;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Mine.Adapter.CollectionAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/8/22.
 */

public class MyCollectionActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    private List<Collection> collections = new ArrayList<>();
    private CollectionAdapter myAdapter;
    private int state = Enum.STATE_NORMAL;

    int pageIndex = 1;//当前页
    int pageSize = 10;//每一页数量
    int totalCount = 0;//总数

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smart_recyclerview;
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MyCollectionActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title.setText("我的收藏");

        initData();
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

        Glide.with(mContext)
                .asGif()
                .load(R.raw.pull)
                .into(ivLulu);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Glide.with(mContext)
                        .asGif()
                        .load(R.raw.refreshing)
                        .into(ivLulu);
                refreshData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Glide.with(mContext)
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
        collections = new ArrayList<>();
        getCollection();
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new CollectionAdapter(mContext, collections);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerView.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addItems(collections, 0);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (collections.size() == 0) {
                    refreshLayout.finishLoadMore(1000);
                    ToastUtils.showToast(mContext, "已加载全部推送！");
                    break;
                }
                myAdapter.addItems(collections, myAdapter.getItemCount());
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }


    private void getCollection() {
        Observable<BaseEntity<List<Collection>>> observable = RetrofitClient.createService(TuiSongAPI.class)
                .getCollection(UserSingleton.USERINFO.getUid(),
                        pageIndex, pageSize , UserSingleton.USERINFO.getToken());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Collection>>>() {
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
                    public void onNext(BaseEntity<List<Collection>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                collections = baseEntity.getDatas();
                            totalCount = baseEntity.getTotal();
                            changeByState();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
