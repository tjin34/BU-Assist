package net.bucssa.buassist.Ui.Fragments.Mine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Login.UserInfo;
import net.bucssa.buassist.Bean.Request.EditDoBReq;
import net.bucssa.buassist.Bean.Request.EditInfoInt;
import net.bucssa.buassist.Bean.Request.EditInfoStr;
import net.bucssa.buassist.Bean.Request.UploadAvatarReq;
import net.bucssa.buassist.Bean.Request.UserInfoReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.BitmapToBase64;
import net.bucssa.buassist.Util.BitmapUtil;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.ImageUtils;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

    @BindView(R.id.ll_classYear)
    LinearLayout ll_classYear;

    @BindView(R.id.tv_classYear)
    TextView tv_classYear;

    @BindView(R.id.ll_school)
    LinearLayout ll_school;

    @BindView(R.id.tv_school)
    TextView tv_school;

    @BindView(R.id.ll_major)
    LinearLayout ll_major;

    @BindView(R.id.tv_major)
    TextView tv_major;

    @BindView(R.id.ll_relationship)
    LinearLayout ll_relationship;

    @BindView(R.id.tv_relationship)
    TextView tv_relationship;

    @BindView(R.id.ll_self_intro)
    LinearLayout ll_self_intro;

    @BindView(R.id.tv_self_intro)
    TextView tv_self_intro;


    private List<String> optionSex = new ArrayList<>();
    private OptionsPickerView sexPickerView;

    private TimePickerView datePickerView;

    private TimePickerView schoolYearPicker;

    private List<String> schools = new ArrayList<>();
    private OptionsPickerView schoolPickerView;

    private List<String> optionMarriage = new ArrayList<>();
    private OptionsPickerView marriagePickerView;

    private int sex;
    private int year;
    private int month;
    private int day;
    private String birthday;

    private ProgressDialog progressDialog;

    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.png";
    private static final int REQUEST_SELECT_PICTURE = 0x01;

    private static final int EDIT_PROFILE = 0x02;


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
        initSchoolYearPicker();
        initSchoolPickerView();
        initMarriagePickerView();
    }


    private void setValues() {
        Picasso.with(mContext).load(UserSingleton.bigAvatar).error(R.drawable.profile_photo).into(iv_avatar);
        tv_username.setText(UserSingleton.USERINFO.getNickname());
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
        tv_classYear.setText(String.valueOf(UserSingleton.USERINFO.getSchoolYear()));
        tv_school.setText(UserSingleton.USERINFO.getCollege());
        tv_major.setText(UserSingleton.USERINFO.getMajor());

    }


    @Override
    protected void initResAndListener() {

        progressDialog = new ProgressDialog(mContext);

        tv_title.setText("修改个人资料");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        ll_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 0);
                startActivityForResult(intent, EDIT_PROFILE);
            }
        });

        ll_realname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 1);
                startActivityForResult(intent, EDIT_PROFILE);
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

        ll_classYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolYearPicker.show();
            }
        });

        ll_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolPickerView.show();
            }
        });

        ll_major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, PickMajorActivity.class), EDIT_PROFILE);
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
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("InputType", 2);
                startActivityForResult(intent, EDIT_PROFILE);
            }
        });
    }

    private void initSexPickerView() {
        optionSex.add("男性");
        optionSex.add("女性");
        optionSex.add("未知");

        sexPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                uploadIntChange("gender", options1);
            }
        })
                .setContentTextSize(20)
                .build();

        sexPickerView.setPicker(optionSex);
    }

    private void initDatePickerView() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1970, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        boolean[] type = {true, true, true, false, false, false};

        datePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                uploadDob(date);
            }
        })
                .setType(type)//默认全部显示
                .setContentSize(18)//滚轮文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("", "", "", "", "", "")
                .build();
    }

    private void uploadDob(Date date) {//可根据需要自行截取数据显示
        year = date.getYear() + 1900;
        month = date.getMonth() + 1;
        day = date.getDate();

        EditDoBReq req = new EditDoBReq(UserSingleton.USERINFO.getUid(),
                year, month, day, UserSingleton.USERINFO.getToken());
        Gson gson = new Gson();
        String json = gson.toJson(req);
        editDob(json);
    }

    private void initSchoolYearPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2025, 1, 1);
        boolean[] type = {true, false, false, false, false, false};

        schoolYearPicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                uploadIntChange("schoolYear", 1900+date.getYear());
            }
        })
                .setType(type)//默认全部显示
                .setContentSize(18)//滚轮文字大小
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("", "", "", "", "", "")
                .build();
    }

    private void initSchoolPickerView() {
        schools = Arrays.asList(getResources().getStringArray(R.array.schools));

        schoolPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                uploadStringChange("field1", schools.get(options1));
            }
        })
                .setContentTextSize(14)
                .build();

        schoolPickerView.setPicker(schools);
    }

    private void initMarriagePickerView() {
        optionMarriage.add("单身");
        optionMarriage.add("恋爱中");
        optionMarriage.add("已婚");
        optionMarriage.add("未知");

        marriagePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                uploadStringChange("affectivestatus", optionMarriage.get(options1));
            }
        })
                .setContentTextSize(20)
                .build();

        marriagePickerView.setPicker(optionMarriage);
    }

    private void uploadStringChange(String key, String value) {
        EditInfoStr req = new EditInfoStr(UserSingleton.USERINFO.getUid(), key, value, UserSingleton.USERINFO.getToken());
        Gson gson = new Gson();
        String json = gson.toJson(req);
        editUserInfo(json);
    }

    private void uploadIntChange(String key, int value) {
        EditInfoInt req = new EditInfoInt(UserSingleton.USERINFO.getUid(), key, value, UserSingleton.USERINFO.getToken());
        Gson gson = new Gson();
        String json = gson.toJson(req);
        editUserInfo(json);
    }


    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            iv_avatar.setImageURI(resultUri);
            Bitmap bitmap = ImageUtils.getBitmapFromUri(mContext, resultUri);
            progressDialog.setMessage("上传头像中");
            progressDialog.show();
            UploadAvatarReq req = new UploadAvatarReq(UserSingleton.USERINFO.getUid(),
                    BitmapToBase64.bitmapToBase64(bitmap), UserSingleton.USERINFO.getToken());
            Gson gson = new Gson();
            String json = gson.toJson(req);
            uploadAvatar(json);
        } else {
            ToastUtils.showToast(mContext, "读取图片错误");
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(getTAG(), "handleCropError: ", cropError);
            Toast.makeText(mContext, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "不明错误~呜呜呜", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        Date time = new Date();
        String destinationFileName = String.valueOf(time.getTime()) + ".png";

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(((Activity) mContext).getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1)
                .withMaxResultSize(800, 800);

        uCrop.withOptions(options);
        uCrop.start((Activity) mContext, UCrop.REQUEST_CROP);
    }


    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    private android.support.v7.app.AlertDialog mAlertDialog;


    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
        }
    }

    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_SELECT_PICTURE:
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startCropActivity(data.getData());
                    } else {
                        ToastUtils.showToast(mContext, "照片读取出错！");
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
                case EDIT_PROFILE:
                    getUserInfo();
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void getUserInfo() {
        Observable<BaseEntity<UserInfo>> observable = RetrofitClient.createService(UserAPI.class)
                .getMyInfos(UserSingleton.USERINFO.getUid(), UserSingleton.USERINFO.getToken());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<UserInfo>>() {
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
                    public void onNext(BaseEntity<UserInfo> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            UserSingleton.USERINFO = baseEntity.getDatas();
                            UserSingleton.getHttpAvatar();
                            setValues();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    /**
     * 修改个人信息
     *
     * @param json
     */
    private void editUserInfo(String json) {
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
                            ToastUtils.showToast(mContext, "修改成功！");
                            getUserInfo();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void editDob(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .editDoB(body);

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
                            getUserInfo();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void uploadAvatar(String json) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .uploadAvatar(body);

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
                        progressDialog.dismiss();
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        progressDialog.dismiss();
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(mContext, "头像上传成功！");
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
