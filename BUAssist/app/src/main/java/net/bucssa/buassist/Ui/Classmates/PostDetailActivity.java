package net.bucssa.buassist.Ui.Classmates;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Comment;
import net.bucssa.buassist.Bean.Classmate.Post;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.CommentListAdapter;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Util.Utils;
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
 * Created by shinji on 2018/4/6.
 */

public class PostDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.rvRefresh)
    RefreshView rvRefresh;

    @BindView(R.id.ivProfile)
    ImageView ivProfile;

    @BindView(R.id.tvCreator)
    TextView tvCreator;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.tvComment)
    TextView tvComment;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.ivComment)
    ImageView ivComment;

    @BindView(R.id.lvComment)
    CustomListViewForRefreshView lvComment;

    @BindView(R.id.header)
    LinearLayout headerRootView;

    @BindView(R.id.rootView)
    RelativeLayout rootView;

    
    private Post post;
    private List<Comment> comments;
    private int totalCount;
    private CommentListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

    private int pageIndex = 1;
    private int pageSize = 10;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        post = (Post) getIntent().getSerializableExtra("Post");
        super.onCreate(savedInstanceState);

        ((Activity) mContext).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        
        initView();
        initData();

    }
    
    private void initView() {
        
        title.setText("话题详情");

        Glide.with(mContext)
                .asBitmap()
                .load(post.getAvatar())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(ivProfile);
        tvCreator.setText(post.getAuthorName());
        tvTitle.setText(post.getSubject());
        tvContent.setText(post.getContent());
        tvComment.setText(String.valueOf(post.getComment()));

        ivComment.setSelected(false);
        if (post.getComment() > 5)
            ivComment.setSelected(false);

        tvTime.setText(DateUtil.dateToOutput(post.getDateline()));
    }

    private void initData() {
        comments = new ArrayList<>();
        getComment(0,0);
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
                        refresh();
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
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

        lvComment.setOnLoadMoreListener(new CustomListViewForRefreshView.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 100) {
                    rvRefresh.getRefreshHeaderView().setPadding(0, -(Utils.px2dp(mContext, rvRefresh.getOriginRefreshHeight())-20),0,0);
                }
            }
        });

    }

    private void refresh() {
        pageIndex = 1;
        state = Enum.STATE_REFRESH;
        getComment(pageIndex,pageSize);
    }

    private void loadMore() {
        pageIndex++;
        state = Enum.STATE_MORE;
        getComment(pageIndex, pageSize);
    }


    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new CommentListAdapter(mContext, comments);
                lvComment.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clear();
                myAdapter.addDatas(comments);
                break;
            case Enum.STATE_MORE:
                if (comments.size() == 0) {
                    lvComment.NoMoreData();
                } else {
                    myAdapter.addDatas(comments);
                    lvComment.LoadingComplete();
                }
                break;
        }
    }

    private void getComment(int pageIndex, int pageSize){
        Observable<BaseEntity<List<Comment>>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getComment(0,post.getPostId(), pageIndex, pageSize,"");

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Comment>>>() {
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
                    public void onNext(BaseEntity<List<Comment>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                comments = baseEntity.getDatas();
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
