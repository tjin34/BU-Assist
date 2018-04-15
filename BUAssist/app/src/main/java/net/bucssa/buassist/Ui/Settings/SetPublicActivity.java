package net.bucssa.buassist.Ui.Settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Request.EditInfoInt;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Shinji on 2018/4/14.
 */

public class SetPublicActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.ll_public)
    LinearLayout llPublic;

    @BindView(R.id.ivPublic)
    ImageView ivPublic;

    @BindView(R.id.ll_private)
    LinearLayout llPrivate;

    @BindView(R.id.ivPrivate)
    ImageView ivPrivate;

    public boolean isPublic;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_public;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isPublic = getIntent().getBooleanExtra("isPublic", false);
        super.onCreate(savedInstanceState);
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

        tvTitle.setText("修改公开个人信息");

        llPublic.setEnabled(!isPublic);
        ivPublic.setVisibility(isPublic ? View.VISIBLE : View.GONE);

        llPrivate.setEnabled(isPublic);
        ivPrivate.setVisibility(isPublic ? View.GONE : View.VISIBLE);

        llPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPublic.setVisibility(View.VISIBLE);
                ivPrivate.setVisibility(View.INVISIBLE);
                llPublic.setEnabled(false);
                llPrivate.setEnabled(true);
                uploadEdit(1);
            }
        });

        llPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPrivate.setVisibility(View.VISIBLE);
                ivPublic.setVisibility(View.INVISIBLE);
                llPrivate.setEnabled(false);
                llPublic.setEnabled(true);
                uploadEdit(0);
            }
        });
    }

    private void uploadEdit(int value) {
        EditInfoInt req = new EditInfoInt(UserSingleton.USERINFO.getUid(),
                "show", value, UserSingleton.USERINFO.getToken());
        Gson gson = new Gson();
        String json = gson.toJson(req);
        editPublic(json);
    }

    private void editPublic(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .editInfoInt(body);

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
                            ToastUtils.showToast(mContext, "创建成功！");
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }




}
