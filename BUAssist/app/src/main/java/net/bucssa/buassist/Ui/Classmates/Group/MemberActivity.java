package net.bucssa.buassist.Ui.Classmates.Group;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Member;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.MemberListAdapter;
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
 * Created by Shinji on 2018/4/10.
 */

public class MemberActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.search)
    LinearLayout search;

    @BindView(R.id.searchShadow)
    View searchShadow;

    @BindView(R.id.rvRefresh)
    RefreshView rvRefresh;

    @BindView(R.id.listView)
    CustomListViewForRefreshView lv_class;

    private int groupId;
    private List<Member> memberList = new ArrayList<>();
    private MemberListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

    private int pageIndex = 1;
    private int pageSize = 10;
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classmate_listview;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        groupId = getIntent().getIntExtra("groupId", 0);
        super.onCreate(savedInstanceState);

        initData();

    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("成员列表");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        lv_class.setOnLoadMoreListener(new CustomListViewForRefreshView.onLoadMoreListener() {
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
        memberList = new ArrayList<>();
        getMember(pageIndex, pageSize);
    }


    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new MemberListAdapter(mContext, memberList);
                myAdapter.setOnClickEventListener(new MemberListAdapter.OnClickEventListener() {
                    @Override
                    public void OnItemClick(Member member) {
                        // TODO: 2018/4/10  
                    }

                    @Override
                    public void OnItemLongClick() {
                        // TODO: 2018/4/10
                    }
                });
                lv_class.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addData(0, memberList);
                lv_class.LoadingComplete();
                break;
            case Enum.STATE_MORE:
                if (memberList.size() == 0) {
                    lv_class.NoMoreData();
                    break;
                }
                myAdapter.addData(myAdapter.getCount(), memberList);
                lv_class.LoadingComplete();
                break;

        }
    }


    private void getMember(int pageIndex, int pageSize){
        Observable<BaseEntity<List<Member>>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getMember(groupId,pageIndex, pageSize);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Member>>>() {
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
                    public void onNext(BaseEntity<List<Member>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                memberList = baseEntity.getDatas();
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
