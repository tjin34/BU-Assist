package net.bucssa.buassist.Ui.Classmates.Class;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.Bean.Request.EditGroupInfoReq;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.ClassListAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class FindClassActivity extends BaseActivity {

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
    ListView lv_class;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private String searchKey = "";

    private List<Class> classList = new ArrayList<>();
    private ClassListAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;

    private int pageIndex = 1;
    private int pageSize = 10;
    private int totalCount = 0;

    private int preActivity;
    private Group group;


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
        preActivity = getIntent().getIntExtra("preActivity", 0);
        if (preActivity == 2) group = (Group) getIntent().getSerializableExtra("Group");
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
                myAdapter.setOnClassItemClickListener(new ClassListAdapter.onClassItemClickListener() {
                    @Override
                    public void onClassItemClick(Class classItem) {
                        switch (preActivity) {
                            case 1:
                                Intent intent = new Intent(mContext, ClassDetailActivity.class);
                                intent.putExtra("Class", classItem);
                                startActivity(intent);
                                break;
                            case 2:
                                String finalTag;
                                if (group.getGroupTag().equals("")) {
                                    finalTag = classItem.getClassCode();
                                } else if (group.getGroupTag().toLowerCase().contains(classItem.getClassCode().toLowerCase())) {
                                    ToastUtils.showToast(mContext, "这课已经在涉及科目中了！");
                                    return;
                                }else {
                                    finalTag = group.getGroupTag() + "," + classItem.getClassCode();
                                }
                                EditGroupInfoReq req = new EditGroupInfoReq(UserSingleton.USERINFO.getUid(),
                                        group.getGroupId(), "groupTag", finalTag, UserSingleton.USERINFO.getToken());
                                Gson gson = new Gson();
                                String json = gson.toJson(req);
                                editGroupInfo(json);
                                break;
                        }
                    }
                });
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

    private void editGroupInfo(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .editGroupInfo(body);

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
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
