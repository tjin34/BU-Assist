package net.bucssa.buassist.Ui.Classmates.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.GroupsListAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Widget.CustomListViewForRefreshView;
import net.bucssa.buassist.Widget.RefreshHelper;
import net.bucssa.buassist.Widget.RefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class GroupsFragment extends BaseFragment{

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;
    
    private String classCode = "";

    private List<Group> groupList = new ArrayList<>();
    private GroupsListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

    private int pageIndex = 1;
    private int pageSize = 10;
    private int totalCount = 0;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_listview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            classCode = bundle.getString("classCode");
        }
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
        groupList = new ArrayList<>();
        getGroup(pageIndex, pageSize);
    }


    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new GroupsListAdapter(context, groupList);
                listView.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clear();
                myAdapter.addDatas(groupList);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (groupList.size() == 0) {
                    refreshLayout.finishLoadMore(1000);
                    ToastUtils.showToast(context, "已加载全部小组！");
                    break;
                }
                myAdapter.addDatas(groupList);
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshData();
    }

    private void getGroup(int pageIndex, int pageSize){
        Observable<BaseEntity<List<Group>>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getGroup(UserSingleton.USERINFO.getUid(),classCode, pageIndex, pageSize,"");

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Group>>>() {
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
                    public void onNext(BaseEntity<List<Group>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                groupList = baseEntity.getDatas();
                            totalCount = baseEntity.getTotal();
                            changeByState();
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }






}
