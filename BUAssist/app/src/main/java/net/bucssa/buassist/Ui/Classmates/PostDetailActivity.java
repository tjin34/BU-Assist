package net.bucssa.buassist.Ui.Classmates;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
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

    
    private Post post;
    private List<Comment> comments;
    private int totalCount;
    private CommentListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

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
        
        initView();
        initData();

    }
    
    private void initView() {
        Glide.with(mContext)
                .load("http://bucssa.net/uc_server/avatar.php?uid="+post.getAuthorId()+"&size=middle")
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
        super.initResAndListener();
    }


    private void refresh() {
        state = Enum.STATE_REFRESH;
        getComment(0,0);
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
