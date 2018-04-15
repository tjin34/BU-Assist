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

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.Bean.Request.EditGroupInfoReq;
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

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.iv_submit)
    ImageView iv_submit;

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
    private Group group;
    private int wordLimit = 0;

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
        /* 获取前一个Activity中传入了区分修改type的数值 */
        inputType = getIntent().getIntExtra("InputType", 0);
        /* 如果inputType为2以上，则获取前一个Activity传入的Group Object */
        if (inputType > 2) group = (Group) getIntent().getSerializableExtra("Group");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initResAndListener() {

        initView();

        iv_back.setVisibility(View.VISIBLE);
        /* 后退按钮为提交按钮 */
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 小输入框的监听 */
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(s);
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

        /* 大输入框的监听 */
        et_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(s);
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

        /* 提交按钮 */
        iv_submit.setVisibility(View.VISIBLE);
        iv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEditInfo();
            }
        });
    }

    /**
     * 根据输入类型设置页面数值
     */
    private void initView() {
        int wordLeft = 0;
        switch (inputType) {
            case 0:
                tv_title.setText("修改昵称");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(UserSingleton.USERINFO.getNickname());
                wordLimit = 16;
                wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(et_name.getText());
                tv_nameCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.material_red));
                }
                break;
            case 1:
                tv_title.setText("修改真名");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(UserSingleton.USERINFO.getRealname());
                wordLimit = 16;
                wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(et_name.getText());
                tv_nameCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.material_red));
                }
                break;
            case 2:
                tv_title.setText("修改个人介绍");
                intro_inputBox.setVisibility(View.VISIBLE);
                et_intro.setText(UserSingleton.USERINFO.getBio());
                wordLimit = 255;
                wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(et_intro.getText());
                tv_introCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_introCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_introCount.setTextColor(getResources().getColor(R.color.material_red));
                }
                break;
            case 3:
                tv_title.setText("修改小组名");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(group.getGroupName());
                wordLimit = 25;
                wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(et_name.getText());
                tv_nameCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.material_red));
                }
                break;
            case 4:
                tv_title.setText("修改小组介绍");
                intro_inputBox.setVisibility(View.VISIBLE);
                et_intro.setText(group.getGroupIntro());
                wordLimit = 255;
                wordLeft = wordLimit - SuperEditText.SMLengthFilter.getSequenceLength(et_intro.getText());
                tv_introCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_introCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_introCount.setTextColor(getResources().getColor(R.color.material_red));
                }
                break;
        }


    }


    /**
     * 提交修改信息
      */
    private void uploadEditInfo() {
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
            case 3:
                key = "groupName";
                value = et_name.getText().toString();
                break;
            case 4:
                key = "groupIntro";
                value = et_intro.getText().toString();

                break;
        }
        /* 如果输入超过长度，则提醒并不提交 */
        if (value.length() > wordLimit) {
            ToastUtils.showToast(mContext, "字数超过要求，请确认后重试！");
            return;
        }
        /* 根据inputType提交POST */
        if (inputType < 3) {
            EditInfoStr req = new EditInfoStr(UserSingleton.USERINFO.getUid(),
                    key, value, UserSingleton.USERINFO.getToken());
            Gson gson = new Gson();
            String json = gson.toJson(req);
            editUserInfo(json);
        } else {
            EditGroupInfoReq req = new EditGroupInfoReq(UserSingleton.USERINFO.getUid(),
                    group.getGroupId(), key, value, UserSingleton.USERINFO.getToken());
            Gson gson = new Gson();
            String json = gson.toJson(req);
            editGroupInfo(json);
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
                            setResult(-1);
                            finish();
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
