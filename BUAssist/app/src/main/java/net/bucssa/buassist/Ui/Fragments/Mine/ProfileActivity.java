package net.bucssa.buassist.Ui.Fragments.Mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Request.UserInfoReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Shin on 17/6/28.
 */

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.ll_avatar)
    LinearLayout ll_avatar;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.ll_username)
    LinearLayout ll_username;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.ll_realname)
    LinearLayout ll_realname;

    @BindView(R.id.tv_realname)
    TextView tv_realname;

    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;

    @BindView(R.id.tv_sex)
    TextView tv_sex;

    @BindView(R.id.ll_birth)
    LinearLayout ll_birth;

    @BindView(R.id.tv_birth)
    TextView tv_birth;

    @BindView(R.id.ll_relationship)
    LinearLayout ll_relationship;

    @BindView(R.id.tv_relationship)
    TextView tv_relationship;

    @BindView(R.id.ll_self_intro)
    LinearLayout ll_self_intro;

    @BindView(R.id.tv_self_intro)
    TextView tv_self_intro;

    @BindView(R.id.enter1)
    ImageView enter1;

    @BindView(R.id.enter2)
    ImageView enter2;

    @BindView(R.id.enter3)
    ImageView enter3;

    @BindView(R.id.enter4)
    ImageView enter4;

    @BindView(R.id.enter5)
    ImageView enter5;


    private List<String> optionSex = new ArrayList<>();
    private OptionsPickerView sexPickerView;

    private TimePickerView datePickerView;

    private List<String> optionMarriage = new ArrayList<>();
    private OptionsPickerView marriagePickerView;

    private String userName;
    private int sex;
    private int year;
    private int month;
    private int day;
    private String relationshipStatus;
    private String birthday;
    private String self_intro;
    private int editAndSave = 0;
    private Calendar calendar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setValues();
        initSexPickerView();
        initDatePickerView();
        initMarriagePickerView();
    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("修改个人资料");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initUsernameDialog();
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 0);
                startActivity(intent);
            }
        });

        ll_realname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 1);
                startActivity(intent);
            }
        });

        ll_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexPickerView.show();
            }
        });

        ll_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView.show();
            }
        });

        ll_relationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marriagePickerView.show();
            }
        });

        ll_self_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initSelfIntroDialog();
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 2);
                startActivity(intent);
            }
        });
    }

    private void setValues() {
        Picasso.with(mContext).load(UserSingleton.bigAvatar).error(R.drawable.profile_photo).into(iv_avatar);
        tv_username.setText(UserSingleton.USERINFO.getUsername());
        tv_realname.setText(UserSingleton.USERINFO.getRealname());
        switch (UserSingleton.USERINFO.getGender()) {
            case 1:
                tv_sex.setText("男性");
                break;
            case 2:
                tv_sex.setText("女性");
                break;
            case 3:
                tv_sex.setText("未知");
                break;
        }
        tv_birth.setText(UserSingleton.USERINFO.getDateOfBirth());
        tv_self_intro.setText(UserSingleton.USERINFO.getBio());
        tv_relationship.setText(UserSingleton.USERINFO.getAffectivestatus());

    }

    private void initUsernameDialog() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.popup_username, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(layout).show();
        final EditText et_reason = (EditText) layout.findViewById(R.id.et_username);
        et_reason.setText(tv_username.getText());
        userName = "";
        et_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                userName = s.toString();
            }
        });
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.equals("") || userName == null) {
                    ToastUtils.showToast(mContext, "请输入你需要的用户名哦~");
                } else {
                    tv_username.setText(userName);
                    dialog.dismiss();
                }
            }
        });
    }

    private void initSexPickerView() {
        optionSex.add("男性");
        optionSex.add("女性");
        optionSex.add("未知");

        sexPickerView = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = optionSex.get(options1);
                tv_sex.setText(tx);
                if (tx.equals("男性")){
                    sex = 1;
                } else if (tx.equals("女性")) {
                    sex = 2;
                } else {
                    sex = 3;
                }
            }
        })
                .setContentTextSize(20)
                .build();

        sexPickerView.setPicker(optionSex);
    }

    private void initDatePickerView() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1970,1,1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020,1,1);
        boolean[] type = {true, true, true, false, false, false};

        datePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,View v) {//选中事件回调
                tv_birth.setText(getTime(date));
            }
        })
                .setType(type)//默认全部显示
                .setContentSize(18)//滚轮文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("","","","","","")
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        year = date.getYear()+1900;
        month = date.getMonth()+1;
        day = date.getDate();
        birthday = day + " " + DateUtil.getEngMonth(month-1) + " " + year;
        return birthday;
    }

    private void initMarriagePickerView() {
        optionMarriage.add("单身");
        optionMarriage.add("恋爱中");
        optionMarriage.add("已婚");
        optionMarriage.add("未知");

        marriagePickerView = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = optionMarriage.get(options1);
                tv_relationship.setText(tx);
                relationshipStatus = tx;
            }
        })
                .setContentTextSize(20)
                .build();

        marriagePickerView.setPicker(optionMarriage);
    }

    private void initSelfIntroDialog() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.popup_self_intro, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(layout).show();
        final EditText et_self_intro = (EditText) layout.findViewById(R.id.et_self_intro);
        et_self_intro.setText(tv_self_intro.getText());
        self_intro = "";
        et_self_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                userName = s.toString();
            }
        });
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_self_intro.setText("已填写");
                dialog.dismiss();
            }
        });
    }
    /**
     * 修改个人信息
     * @param json
     */
    private void editUserInfo(String json){
//        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
//                .editUserInfo(UserSingleton.USERINFO.getUid(), userName, sex, year, month, day);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .editUserInfos(body);

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
                            ToastUtils.showToast(mContext, "Success");
                        } else {
                            ToastUtils.showToast(mContext, "Error");
                        }
                        Logger.d();
                    }
                });
    }
}
