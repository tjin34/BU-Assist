package net.bucssa.buassist.Ui.Settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Request.EditPwdReq;
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

public class EditPasswordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_submit)
    ImageView ivSubmit;

    @BindView(R.id.etOriginalPass)
    EditText etOriginalPass;

    @BindView(R.id.etNewPass)
    EditText etNewPass;

    @BindView(R.id.etRepeatPass)
    EditText etRepeatPass;

    private String originalPassword;
    private String newPassword;
    private String repeatPassword;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        tvTitle.setText("修改密码");

        ivSubmit.setVisibility(View.VISIBLE);
        ivSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 获取三个EditText的值 */
                originalPassword = etOriginalPass.getText().toString();
                newPassword = etNewPass.getText().toString();
                repeatPassword = etRepeatPass.getText().toString();

                /* 进行密码格式判定 */
                if (originalPassword.equals("") || newPassword.equals("") || repeatPassword.equals("")) {
                    ToastUtils.showToast(mContext, "请填写所有要求的哦~");
                } else if (newPassword.equals(repeatPassword)) {
                    ToastUtils.showToast(mContext, "两次输入的不一样哦~");
                } else if (newPassword.length() < 8 || newPassword.length() > 16) {
                    ToastUtils.showToast(mContext, "请输入长度为8-16的密码~");
                } else {
                    EditPwdReq req = new EditPwdReq(UserSingleton.USERINFO.getUid(),
                            originalPassword,newPassword,UserSingleton.USERINFO.getToken());
                    Gson gson = new Gson();
                    String json = gson.toJson(req);
                    editPassword(json);
                }
            }
        });
    }


    private void editPassword(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .editPwd(body);

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
