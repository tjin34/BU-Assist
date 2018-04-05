package net.bucssa.buassist.Ui.Classmates;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Util.CreditUtils;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Widget.StripProgressBar;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/30.
 */

public class GroupDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_groupName)
    TextView tv_groupName;

    @BindView(R.id.iv_groupCover)
    ImageView iv_groupCover;

    @BindView(R.id.credit_bar)
    StripProgressBar credit_bar;

    @BindView(R.id.tv_ranking)
    TextView tv_ranking;

    @BindView(R.id.tv_groupId)
    TextView tv_groupId;

    @BindView(R.id.tv_groupTag)
    TextView tv_groupTag;

    @BindView(R.id.tv_groupIntro)
    TextView tv_groupIntro;

    @BindView(R.id.tv_requestJoin)
    TextView tv_requestJoin;

    @BindView(R.id.tv_groupChat)
    TextView tv_groupChat;

    @BindView(R.id.rl_members)
    RelativeLayout rl_members;

    @BindView(R.id.tv_historyEvent)
    TextView tv_historyEvent;

    private Group group;
    private CreditUtils.Level level;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* 从上一Activity中获取bundle中的group Object */
        group = (Group) getIntent().getSerializableExtra("group");

        super.onCreate(savedInstanceState);

        /* 填充页面资料 */
        initView();

    }

    /**
     * 初始化并将数据填充
     */
    private void initView() {
        /* Activity 标题 */
        tv_title.setText("小组详情");

        /* 小组名字 */
        tv_groupName.setText(group.getGroupName());

        /* 用CreditUtils生成Level object */
        level = CreditUtils.calculateLevel(group.getCredit());
        if (level.getCurrentLV() == 5) {    /* 如果小组已达到满级 */

            /* 经验条文字显示 / --- 代表无下一等级目标 */
            credit_bar.setText(level.getTotalCredit() + " / ---");
            /* 经验条添加至100 */
            credit_bar.setProgress(100);

        } else {    /* 如果不是5级，则正常显示 */

            /* 经验值文字显示 当前credit加上还需要的credit */
            credit_bar.setText(level.getTotalCredit() + " / " + level.getCreditForNextLV());
            /* 得到当前的百分比，float得出小数，乘以100后取整数 */
            credit_bar.setProgress((int) (100 * ((float)level.getTotalCredit() / (float)level.getCreditForNextLV())));

        }

        /* 小组学霸值排行 */
        tv_ranking.setText("1");

        /* 小组ID，因为ID为int，所以进行String转化再setText */
        tv_groupId.setText(String.valueOf(group.getGroupId()));

        /* 小组简介 */
        tv_groupIntro.setText(group.getGroupIntro());

//        AddCollectionReq req=new AddCollectionReq(UserSingleton.USERINFO.getUid(),
//                tuiSong.getTid(), tuiSong.getSubject(),
//                tuiSong.getAuthor(), tuiSong.getDateline() ,UserSingleton.USERINFO.getToken());
//        Gson gson=new Gson();
//        String json = gson.toJson(req);
//        addCollection(json);

    }

    @Override
    protected void initResAndListener() {
        /* 返回按钮设置可见， 并附加点击事件 */
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 如果当前用户不在小组中的话，可选择申请加入 */
        tv_requestJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void requestJoin(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .requestJoin(body);

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
                            ToastUtils.showToast(mContext, "申请成功");
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }



}
