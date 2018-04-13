package net.bucssa.buassist.Ui.Classmates.Post;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    ListView lvComment;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    @BindView(R.id.header)
    LinearLayout headerRootView;

    @BindView(R.id.rootView)
    RelativeLayout rootView;

    
    private Post post;
    private List<Comment> comments;
    private CommentListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

    private int pageIndex = 1;
    private int pageSize = 10;
    private int totalCount = 0;

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
        getComment(pageIndex,pageSize);
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

    private void refreshData() {
        pageIndex = 1;
        state = Enum.STATE_REFRESH;
        initData();
    }

    private void loadMore() {
        pageIndex++;
        state = Enum.STATE_MORE;
        initData();
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
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (comments.size() == 0) {
                    refreshLayout.finishLoadMore(1000);
                    ToastUtils.showToast(mContext, "已加载全部评论！");
                    break;
                }
                myAdapter.addDatas(comments);
                refreshLayout.finishLoadMore(1000);
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
