package net.bucssa.buassist.Ui.Fragments.Mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Friend.Friend;
import net.bucssa.buassist.Bean.Request.GroupInviteReq;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Mine.Adapter.FriendAdapter;
import net.bucssa.buassist.Ui.Fragments.Mine.Adapter.FriendListAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Util.Utils;
import net.bucssa.buassist.Widget.CustomListViewForRefreshView;
import net.bucssa.buassist.Widget.RefreshHelper;
import net.bucssa.buassist.Widget.RefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/8/22.
 */

public class MyFriendActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_submit)
    ImageView iv_submit;

    @BindView(R.id.search)
    LinearLayout search;

    @BindView(R.id.searchShadow)
    View searchShadow;

    @BindView(R.id.listView)
    ListView lvFriend;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    private int mMode;
    private int mGroupId;
    private List<Friend> Friends = new ArrayList<>();
    private FriendListAdapter myAdapter;
    private int state = Enum.STATE_NORMAL;

    int pageIndex = 1;//当前页
    int pageSize = 20;//每一页数量
    int totalCount = 0;//总数

    private List<String> toUidList = new ArrayList<>();

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smart_refresh;
    }

    public static void launch(Activity context) {
        ((Activity)context).startActivity(new Intent(context, MyFriendActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMode = getIntent().getIntExtra("MODE", 0);
        if (mMode == 2) mGroupId = getIntent().getIntExtra("GroupId", 0);
        super.onCreate(savedInstanceState);

        initData();

    }

    @Override
    protected void initResAndListener() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(mMode == 2 ? "邀请好友" : "我的好友");

        iv_submit.setVisibility(mMode == 2 ? View.VISIBLE : View.GONE);
        iv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mMode) {
                    case 2:
                        if (toUidList.size() == 0) {
                            ToastUtils.showToast(mContext, "请选择一个好友进行邀请");
                            break;
                        }
                        GroupInviteReq groupInviteReq = new GroupInviteReq(
                                UserSingleton.USERINFO.getUid(),toUidToString(toUidList),
                                mGroupId,UserSingleton.USERINFO.getToken());
                        Gson gson = new Gson();
                        String json = gson.toJson(groupInviteReq);
                        inviteJoin(json);
                        break;
                    default:
                            break;
                }
            }
        });

        search.setVisibility(View.GONE);
        searchShadow.setVisibility(View.GONE);

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
        getFriend();
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new FriendListAdapter(mContext, Friends, mMode);
                myAdapter.setOnFriendSelectedListener(new FriendListAdapter.onFriendSelectedListener() {
                    @Override
                    public void onFriendSelected(Friend friend) {
                        switch (mMode) {
                            case 0:
                                break;
                            case 2:
                                if (toUidList.contains(String.valueOf(friend.getFriendid()))) {
                                    toUidList.remove(String.valueOf(friend.getFriendid()));
                                } else {
                                    toUidList.add(String.valueOf(friend.getFriendid()));
                                }
                                break;
                        }
                    }
                });
                lvFriend.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addItems(Friends);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (Friends.size() == 0) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    ToastUtils.showToast(mContext, "已加载全部好友！");
                    break;
                }
                myAdapter.addItems(Friends);
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }

    private String toUidToString(List<String> list) {
        StringBuilder toUidBuilder = new StringBuilder();
        toUidBuilder = toUidBuilder.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            String temp = "," + list.get(i);
            toUidBuilder = toUidBuilder.append(",").append(list.get(i));
        }
        return toUidBuilder.toString();
    }


    private void getFriend() {
        Observable<BaseEntity<List<Friend>>> observable = RetrofitClient.createService(UserAPI.class)
                .getFriends(UserSingleton.USERINFO.getUid(),
                        pageIndex, pageSize , UserSingleton.USERINFO.getToken());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Friend>>>() {
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
                    public void onNext(BaseEntity<List<Friend>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                Friends = baseEntity.getDatas();
                            totalCount = baseEntity.getTotal();
                            changeByState();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void inviteJoin(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .groupInvite(body);

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
                            ToastUtils.showToast(mContext, "邀请成功");
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

}

