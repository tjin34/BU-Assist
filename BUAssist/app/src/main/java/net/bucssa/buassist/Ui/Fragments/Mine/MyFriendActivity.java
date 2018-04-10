package net.bucssa.buassist.Ui.Fragments.Mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Friend.Friend;
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

    @BindView(R.id.search)
    LinearLayout search;

    @BindView(R.id.searchShadow)
    View searchShadow;

    @BindView(R.id.rvRefresh)
    RefreshView rvRefresh;

    @BindView(R.id.listView)
    CustomListViewForRefreshView lvFriend;

    private List<Friend> Friends = new ArrayList<>();
    private FriendListAdapter myAdapter;
    private int state = Enum.STATE_NORMAL;

    int pageIndex = 1;//当前页
    int pageSize = 10;//每一页数量
    int totalCount = 0;//总数

    private List<String> toUidList = new ArrayList<>();

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classmate_listview;
    }

    public static void launch(Activity context) {
        ((Activity)context).startActivity(new Intent(context, MyFriendActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        tvTitle.setText("我的好友");

        search.setVisibility(View.GONE);
        searchShadow.setVisibility(View.GONE);

        rvRefresh.setRefreshHelper(new RefreshHelper() {
            //初始化刷新view
            @Override
            public View onInitRefreshHeaderView() {
                return LayoutInflater.from(mContext).inflate(R.layout.widget_lulu_headview, null);
            }

            //初始化尺寸高度
            @Override
            public boolean onInitRefreshHeight(int originRefreshHeight) {
                rvRefresh.setRefreshNormalHeight(0);
                rvRefresh.setRefreshingHeight(rvRefresh.getOriginRefreshHeight());
                rvRefresh.setRefreshArrivedStateHeight(rvRefresh.getOriginRefreshHeight());
                return false;
            }

            //刷新状态的改变
            @Override
            public void onRefreshStateChanged(View refreshView, int refreshState) {
                ImageView ivLulu = (ImageView) refreshView.findViewById(R.id.ivLulu);
                switch (refreshState) {
                    case RefreshView.STATE_REFRESH_NORMAL:
                        Glide.with(mContext)
                                .asGif()
                                .load(R.raw.pull)
                                .into(ivLulu);
                        break;
                    case RefreshView.STATE_REFRESH_NOT_ARRIVED:
                        break;
                    case RefreshView.STATE_REFRESH_ARRIVED:
                        break;
                    case RefreshView.STATE_REFRESHING:
                        Glide.with(mContext)
                                .asGif()
                                .load(R.raw.refreshing)
                                .into(ivLulu);
                        lvFriend.onRefresh();
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    refreshData();
                                                    rvRefresh.onCompleteRefresh();
                                                }
                                            });
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                        ).start();
                        break;
                }
            }
        });

        lvFriend.setOnLoadMoreListener(new CustomListViewForRefreshView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    ((Activity)mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadMore();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                ).start();
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
                myAdapter = new FriendListAdapter(mContext, Friends, 0);
                myAdapter.setOnFriendSelectedListener(new FriendListAdapter.onFriendSelectedListener() {
                    @Override
                    public void onFriendSelected(Friend friend) {
//                        if (toUidList.contains(String.valueOf(friend.getFriendid()))) {
//                            toUidList.remove(String.valueOf(friend.getFriendid()));
//                        } else {
//                            toUidList.add(String.valueOf(friend.getFriendid()));
//                        }
                    }
                });
                lvFriend.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addItems(Friends);
                lvFriend.LoadingComplete();
                break;
            case Enum.STATE_MORE:
                if (Friends.size() == 0) {
                    lvFriend.LoadingComplete();
                    break;
                }
                myAdapter.addItems(Friends);
                lvFriend.LoadingComplete();
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

}

