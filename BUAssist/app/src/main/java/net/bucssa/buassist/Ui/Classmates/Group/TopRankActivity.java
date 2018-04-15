package net.bucssa.buassist.Ui.Classmates.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
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
 * Created by Shinji on 2018/4/15.
 */

public class TopRankActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    @BindView(R.id.tvGroup1)
    TextView tvGroup1;

    @BindView(R.id.tvCredit1)
    TextView tvCredit1;

    @BindView(R.id.tvIntro1)
    TextView tvIntro1;

    @BindView(R.id.llRankOne)
    LinearLayout llRankOne;

    @BindView(R.id.tvGroup2)
    TextView tvGroup2;

    @BindView(R.id.tvCredit2)
    TextView tvCredit2;

    @BindView(R.id.tvIntro2)
    TextView tvIntro2;

    @BindView(R.id.llRankSecond)
    LinearLayout llRankSecond;

    @BindView(R.id.tvGroup3)
    TextView tvGroup3;

    @BindView(R.id.tvCredit3)
    TextView tvCredit3;

    @BindView(R.id.tvIntro3)
    TextView tvIntro3;

    @BindView(R.id.llRankThird)
    LinearLayout llRankThird;

    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    private List<Group> topTenGroups = new ArrayList<>();

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTopRank();
    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("小组风云榜");

        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
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
                getTopRank();
                refreshLayout.finishRefresh(1000);
            }
        });

        refreshLayout.setEnableLoadMore(false);
    }

    private void setRankValue() {
        TextView[] tvGroups = {tvGroup1, tvGroup2, tvGroup3};
        TextView[] tvCredits = {tvCredit1, tvCredit2, tvCredit3};
        TextView[] tvIntros = {tvIntro1, tvIntro2, tvIntro3};
        LinearLayout[] llRanks = {llRankOne, llRankSecond, llRankThird};
        llBottom.removeAllViews();
        for (int i = 0; i < topTenGroups.size(); i++) {
            final int index = i;
            if (index < 3) {
                tvGroups[i].setText(topTenGroups.get(i).getGroupName());
                tvCredits[i].setText(String.valueOf(topTenGroups.get(i).getCredit()));
                tvIntros[i].setText(topTenGroups.get(i).getGroupIntro());
                llRanks[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GroupDetailActivity.class);
                        intent.putExtra("group", topTenGroups.get(index));
                        startActivity(intent);
                    }
                });
            } else {
                LinearLayout placing = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_ranking, null, false);
                TextView tvRank = (TextView) placing.findViewById(R.id.tvRank);
                TextView tvGroup = (TextView) placing.findViewById(R.id.tvGroup);
                TextView tvCredit = (TextView) placing.findViewById(R.id.tvCredit);
                tvRank.setText(String.valueOf(index+1));
                tvGroup.setText(topTenGroups.get(index).getGroupName());
                tvCredit.setText(String.valueOf(topTenGroups.get(index).getCredit()));
                placing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GroupDetailActivity.class);
                        intent.putExtra("group", topTenGroups.get(index));
                        startActivity(intent);
                    }
                });
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                placing.setLayoutParams(lp);
                llBottom.addView(placing);
            }
        }
    }

    private void getTopRank(){
        Observable<BaseEntity<List<Group>>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getTopRank(UserSingleton.USERINFO.getUid());

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
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity<List<Group>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                topTenGroups = baseEntity.getDatas();
                            setRankValue();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
