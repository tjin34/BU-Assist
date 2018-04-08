package net.bucssa.buassist.Ui.Fragments.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Api.SystemMessageAPI;
import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Bean.Message.SystemNotification;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Message.Adapter.NewChatAdapter;
import net.bucssa.buassist.Ui.Fragments.Message.Adapter.SystemMsgAdapter;
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
 * Created by KimuraShin on 17/7/27.
 */

public class SystemMsgFragment extends BaseFragment {

    @BindView(R.id.rvRefresh)
    RefreshView rvRefresh;

    @BindView(R.id.lv_message)
    CustomListViewForRefreshView lv_message;

    @BindView(R.id.initView)
    LinearLayout initView;

    @BindView(R.id.iv_light)
    ImageView iv_light;

    @BindView(R.id.iv_lulu)
    ImageView iv_lulu;

    @BindView(R.id.tv_status)
    TextView tv_status;

    private List<SystemNotification> chatList = new ArrayList<>();
    private SystemMsgAdapter myAdapter;
    private int state = Enum.STATE_NORMAL;

    int pageIndex = 1;//当前页
    int pageSize = 10;//每一页数量
    int totalCount = 0;//总数

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_personal_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        rvRefresh.setRefreshHelper(new RefreshHelper() {
            //初始化刷新view
            @Override
            public View onInitRefreshHeaderView() {
                return LayoutInflater.from(context).inflate(R.layout.widget_lulu_headview, null);
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
                        Glide.with(context)
                                .asGif()
                                .load(R.raw.pull)
                                .into(ivLulu);
                        break;
                    case RefreshView.STATE_REFRESH_NOT_ARRIVED:
                        break;
                    case RefreshView.STATE_REFRESH_ARRIVED:
                        break;
                    case RefreshView.STATE_REFRESHING:
                        Glide.with(context)
                                .asGif()
                                .load(R.raw.refreshing)
                                .into(ivLulu);
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            ((Activity)context).runOnUiThread(new Runnable() {
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

        lv_message.setOnLoadMoreListener(new CustomListViewForRefreshView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    ((Activity)context).runOnUiThread(new Runnable() {
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

        initLuluAnim();
    }

    private void initLuluAnim(){
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
        chatList = new ArrayList<>();
        getMessages();
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new SystemMsgAdapter(context, chatList);
                lv_message.setAdapter(myAdapter);
                stopLuluAnim();
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addData(0, chatList);
                lv_message.LoadingComplete();
                break;
            case Enum.STATE_MORE:
                if (chatList.size() == 0) {
                    lv_message.NoMoreData();
                    break;
                }
                myAdapter.addData(myAdapter.getCount(),chatList);
                lv_message.LoadingComplete();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                refreshData();
                break;
        }
    }

    private void getMessages() {
        Observable<BaseEntity<List<SystemNotification>>> observable = RetrofitClient.createService(SystemMessageAPI.class)
                .getSystemMessage(UserSingleton.USERINFO.getUid(), UserSingleton.USERINFO.getToken(),
                        pageIndex, pageSize);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<SystemNotification>>>() {
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
                    public void onNext(BaseEntity<List<SystemNotification>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                chatList = baseEntity.getDatas();
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
