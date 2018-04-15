package net.bucssa.buassist.Ui.Fragments.Mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Request.EditInfoStr;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Shinji on 2018/4/14.
 */

public class PickMajorActivity extends BaseActivity {

    @BindView(R.id.rootView)
    RelativeLayout rootView;

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

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.listView)
    ListView lv_major;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private String searchKey = "";

    private List<String> majorList = new ArrayList<>();
    private ArrayAdapter<String> myAdapter;

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
        tv_title.setText("专业选择");
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

        iv_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search.setText("");
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchKey = charSequence.toString();
                tv_search.setText(searchKey);
                pageIndex = 1;
                pageSize = 10;
                state = Enum.STATE_NORMAL;
                initData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    fakeSearchBox.setVisibility(View.VISIBLE);
                    realSearchBox.setVisibility(View.GONE);
                }
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

        lv_major.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditInfoStr req = new EditInfoStr(UserSingleton.USERINFO.getUid(), "field2",
                        myAdapter.getItem(position), UserSingleton.USERINFO.getToken());
                Gson gson = new Gson();
                String json = gson.toJson(req);
                editUserInfo(json);
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
        majorList = new ArrayList<>();
        getMajor();
    }


    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, majorList);
                lv_major.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clear();
                myAdapter.addAll(majorList);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (majorList.size() == 0) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                    ToastUtils.showToast(mContext, "已加载全部课程！");
                    break;
                }
                myAdapter.addAll(majorList);
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }

    private void getMajor() {
        List<String> rawMajor = Arrays.asList(getResources().getStringArray(R.array.major));
        List<String> filterMajor = new ArrayList<>();
        int start = pageIndex * pageSize;
        int end = ( pageIndex + 1 ) * pageSize;
        if (searchKey.equals("")) {
            for (int i = start; i < end; i++ )
            majorList = rawMajor;
            changeByState();
        } else {
            for (int i = 0; i < rawMajor.size(); i++) {
                if (rawMajor.get(i).contains(searchKey)) {
                    filterMajor.add(rawMajor.get(i));
                }
            }
            if (filterMajor.size() == 0 || filterMajor.size() < start) {
                return;
            } else if (filterMajor.size() < end) {
                int newEnd = filterMajor.size() - start;
                for (int i = start; i < newEnd; i++) {
                    majorList.add(filterMajor.get(i));
                }
            } else {
                for (int i = start; i < end; i++) {
                    majorList.add(filterMajor.get(i));
                }
            }
            changeByState();
        }
    }

    /**
     * 修改个人信息
     *
     * @param json
     */
    private void editUserInfo(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .editInfoStr(body);

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
                            ToastUtils.showToast(mContext, "修改成功！");
                            setResult(-1);
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
