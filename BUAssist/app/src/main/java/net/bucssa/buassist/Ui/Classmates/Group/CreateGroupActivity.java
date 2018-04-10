package net.bucssa.buassist.Ui.Classmates.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.Bean.Request.CreateGroupReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Class.FindClassActivity;
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
 * Created by Shinji on 2018/4/9.
 */

public class CreateGroupActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tvClassCode)
    TextView tvClassCode;

    @BindView(R.id.ivAddClass)
    ImageView ivAddClass;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etIntro)
    EditText etIntro;

    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private Class selectedClass;

    private final int ADD_CLASS_TAG = 1;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedClass = getIntent().getSerializableExtra("Class") == null ?
                null : (Class) getIntent().getSerializableExtra("Class");
        super.onCreate(savedInstanceState);

        initView();
    }

    /**
     * 初始化界面数值
     */
    private void initView() {
        if (selectedClass != null) {
            String hashTag = "#"+selectedClass.getClassCode();
            tvClassCode.setText(hashTag);
            tvClassCode.setVisibility(View.VISIBLE);
            ivAddClass.setVisibility(View.GONE);
        } else {
            tvClassCode.setVisibility(View.GONE);
            ivAddClass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initResAndListener() {

        tvTitle.setText("创建话题");
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, FindClassActivity.class), ADD_CLASS_TAG);
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedClass != null) {
                    String hashTag = selectedClass.getClassCode();
                    CreateGroupReq req = new CreateGroupReq(UserSingleton.USERINFO.getUid(),
                            etName.getText().toString(), etIntro.getText().toString(),
                            hashTag,UserSingleton.USERINFO.getToken());
                    Gson gson = new Gson();
                    String json = gson.toJson(req);
                    createGroup(json);
                } else {
                    ToastUtils.showToast(mContext, "请选择一个课程！");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_CLASS_TAG:
                selectedClass = (Class) data.getSerializableExtra("Class");
                initView();
                break;
        }
    }

    private void createGroup(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .createGroup(body);

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
