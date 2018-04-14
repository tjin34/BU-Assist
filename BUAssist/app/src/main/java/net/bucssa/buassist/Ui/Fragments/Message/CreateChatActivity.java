package net.bucssa.buassist.Ui.Fragments.Message;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Friend.Friend;
import net.bucssa.buassist.Bean.Request.SendReq;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
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
 * Created by Shinji on 2018/4/9.
 */

public class CreateChatActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rootView)
    RelativeLayout rootView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    @BindView(R.id.lvFriend)
    ListView lvFriend;

    @BindView(R.id.et_message)
    EditText etMessage;

    @BindView(R.id.iv_send)
    ImageView ivSend;

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
        return R.layout.activity_create_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((Activity) mContext).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
        tvTitle.setText("创建对话");

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

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(mContext, v);
            }
        });


        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(mContext, v);
                SendReq req=new SendReq(UserSingleton.USERINFO.getUid(),
                        UserSingleton.USERINFO.getUsername(), toUidToString(toUidList),
                        etMessage.getText().toString(), etMessage.getText().toString() ,UserSingleton.USERINFO.getToken());
                Gson gson=new Gson();
                String json = gson.toJson(req);
                createChat(json);
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
                myAdapter = new FriendListAdapter(mContext, Friends, 1);
                myAdapter.setOnFriendSelectedListener(new FriendListAdapter.onFriendSelectedListener() {
                    @Override
                    public void onFriendSelected(Friend friend) {
                        Utils.hideKeyboard(mContext, rootView);
                        if (toUidList.contains(String.valueOf(friend.getFriendid()))) {
                            toUidList.remove(String.valueOf(friend.getFriendid()));
                        } else {
                            toUidList.add(String.valueOf(friend.getFriendid()));
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
                    refreshLayout.finishLoadMore(1000);
                    ToastUtils.showToast(mContext, "已加载全部私信！");
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


    private void createChat(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .createChat(body);

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
                            ToastUtils.showToast(mContext, "发送成功！");
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

}
