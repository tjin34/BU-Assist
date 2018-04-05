package net.bucssa.buassist.Ui.Classmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.ClassListAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class ClassmateActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.lv_class)
    ListView lv_class;

    @BindView(R.id.tv_notification)
    TextView tv_notification;

    @BindView(R.id.tv_myGroup)
    TextView tv_myGroup;

    @BindView(R.id.tv_myTopic)
    TextView tv_myTopic;

    @BindView(R.id.ll_findClass)
    LinearLayout ll_findClass;

    @BindView(R.id.ll_myGroup)
    LinearLayout ll_myGroup;

    @BindView(R.id.ll_myCollection)
    LinearLayout ll_myCollection;

    @BindView(R.id.ll_myTopic)
    LinearLayout ll_myTopic;

    private List<Class> classList;
    private int totalCount = 0;

    private ClassListAdapter myAdapter;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classmate_final;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("大腿课友");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FindClassActivity.class);
                startActivity(intent);
            }
        });

        tv_myGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyGroupActivity.class);
                startActivity(intent);
            }
        });

        ll_myTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyTopicActivity.class);
                startActivity(intent);
            }
        });


        getClassCollection(1, 10);


    }


    private void getClassCollection(int pageIndex, int pageSize){
        Observable<BaseEntity<List<Class>>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getClassCollection(UserSingleton.USERINFO.getUid(), pageIndex, pageSize, UserSingleton.USERINFO.getToken());

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
                            classList = baseEntity.getDatas();
                            totalCount = baseEntity.getTotal();
                            if (totalCount > 0 && classList.size() > 0) {
                                tv_notification.setVisibility(View.GONE);
                                lv_class.setVisibility(View.VISIBLE);
                                myAdapter = new ClassListAdapter(mContext, classList);
                                lv_class.setAdapter(myAdapter);
                            }
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
