package net.bucssa.buassist.Ui.Classmates.Class;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.ClassListAdapter;
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
 * Created by KimuraShin on 17/7/29.
 */

public class FindClassActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.search_textView)
    LinearLayout fakeSearchBox;

    @BindView(R.id.search_editText)
    LinearLayout realSearchBox;

    @BindView(R.id.iv_search_clear)
    ImageView iv_search_clear;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.listView)
    ListView lv_class;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    private String searchKey = "";

    private List<Class> classList = new ArrayList<>();
    private ClassListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

    private int pageIndex = 1;
    private int pageSize = 10;
    private int totalCount = 0;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_smart_refresh;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("寻找课程");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fakeSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeSearchBox.setVisibility(View.GONE);
                realSearchBox.setVisibility(View.VISIBLE);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeSearchBox.setVisibility(View.VISIBLE);
                realSearchBox.setVisibility(View.GONE);
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchKey = charSequence.toString();
                pageIndex = 1;
                pageSize = 10;
                state = Enum.STATE_NORMAL;
                initData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        classList = new ArrayList<>();
        getClass(pageIndex, pageSize);
    }


    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new ClassListAdapter(mContext, classList);
                lv_class.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addData(0, classList);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (classList.size() == 0) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    ToastUtils.showToast(mContext, "已加载全部课程！");
                    break;
                }
                myAdapter.addData(myAdapter.getCount(), classList);
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }


    private void getClass(int pageIndex, int pageSize){
        Observable<BaseEntity<List<Class>>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getClassList(UserSingleton.USERINFO.getUid(), pageIndex, pageSize, searchKey);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Class>>>() {
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
                    public void onNext(BaseEntity<List<Class>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            /* 每次获取都将列表清楚 */
                            classList = new ArrayList<>();
                            /* 如果列表不为空，更新列表 */
                            if (baseEntity.getDatas() !=  null) {
                                classList = baseEntity.getDatas();
                            }
                            /* 更新总数并更新adapter和listView */
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
