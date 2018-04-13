package net.bucssa.buassist.Ui.Fragments.Mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Request.EditInfoStr;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Widget.SuperEditText;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/8/20.
 */

public class InputActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_save)
    TextView tv_save;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.name_inputBox)
    RelativeLayout name_inputBox;

    @BindView(R.id.tv_nameCount)
    TextView tv_nameCount;

    @BindView(R.id.et_name)
    SuperEditText et_name;

    @BindView(R.id.intro_inputBox)
    RelativeLayout intro_inputBox;

    @BindView(R.id.et_intro)
    SuperEditText et_intro;

    @BindView(R.id.tv_introCount)
    TextView tv_introCount;

    private int inputType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_input;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inputType = getIntent().getIntExtra("InputType", 0);
        super.onCreate(savedInstanceState);
        initView(inputType);
    }

    @Override
    protected void initResAndListener() {
        tv_save.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = "";
                String value = "";
                switch (inputType) {
                    case 0:
                        key = "nickname";
                        value = et_name.getText().toString();
                        break;
                    case 1:
                        key = "realname";
                        value = et_name.getText().toString();
                        break;
                    case 2:
                        key = "bio";
                        value = et_intro.getText().toString();
                        break;
                }
                EditInfoStr req = new EditInfoStr(UserSingleton.USERINFO.getUid(),
                        key, value, UserSingleton.USERINFO.getToken());
                Gson gson = new Gson();
                String json = gson.toJson(req);
                editUserInfo(json);
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordLeft = 16 - SuperEditText.SMLengthFilter.getSequenceLength(s);
                tv_nameCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.material_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordLeft = 100 - SuperEditText.SMLengthFilter.getSequenceLength(s);
                tv_introCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_introCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_introCount.setTextColor(getResources().getColor(R.color.material_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView(int type) {
        switch (type) {
            case 0:
                tv_title.setText("用户名");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(UserSingleton.USERINFO.getNickname());
                break;
            case 1:
                tv_title.setText("真名");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(UserSingleton.USERINFO.getRealname());
                break;
            case 2:
                tv_title.setText("个人介绍");
                intro_inputBox.setVisibility(View.VISIBLE);
                et_intro.setText(UserSingleton.USERINFO.getBio());
                break;
        }
    }

    private void editUserInfo(String json){
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
                            finish();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

}
