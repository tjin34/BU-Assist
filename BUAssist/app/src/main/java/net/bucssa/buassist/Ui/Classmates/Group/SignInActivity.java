package net.bucssa.buassist.Ui.Classmates.Group;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.qrcode.encoder.QRCode;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.Bean.Classmate.Meeting;
import net.bucssa.buassist.Bean.Classmate.QRcode;
import net.bucssa.buassist.Bean.Request.CheckInReq;
import net.bucssa.buassist.Bean.Request.InitMeetReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.BitmapUtil;
import net.bucssa.buassist.Util.DateUtil;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Util.zxing.android.CaptureActivity;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Shinji on 2018/4/10.
 */

public class SignInActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.llMeetDetail)
    LinearLayout llMeetDetail;

    @BindView(R.id.tvMeetType)
    TextView tvMeetType;

    @BindView(R.id.tvSignCount)
    TextView tvSignCount;

    @BindView(R.id.tvRemainTime)
    TextView tvRemainTime;

    @BindView(R.id.ivQRcode)
    ImageView ivQRcode;

    @BindView(R.id.tvStart)
    TextView tvStart;

    @BindView(R.id.tvScan)
    TextView tvScan;

    private static final int SCANNING_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 2;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

    private Group group;
    private Meeting meeting;


    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        group = (Group) getIntent().getSerializableExtra("Group");
        super.onCreate(savedInstanceState);

        getMeeting();
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

        tv_title.setText("小组签到");


        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = meeting != null ? 1 : 0;
                switch (state) {
                    case 0:
                        InitMeetReq req = new InitMeetReq(UserSingleton.USERINFO.getUid()
                                , group.getGroupId(), UserSingleton.USERINFO.getToken());
                        Gson gson = new Gson();
                        String json = gson.toJson(req);
                        initMeeting(json);
                        break;
                    case 1:
                        makeErweimaMethod(v);
                        tvStart.setVisibility(View.GONE);
                        tvScan.setVisibility(View.GONE);
                        break;
                }
            }
        });


        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                } else {
                    canningMethod(v);
                }
            }
        });
    }

    private void initMeetView(){
        llMeetDetail.setVisibility(View.VISIBLE);
//        tvMeetType.setText();
        String signCount = meeting.getChecked() +" / "+ group.getMembers();
        tvSignCount.setText(signCount);
        Date finishTime = new Date(meeting.getCreated()+86400);
        Date currentTime = new Date();
        final long interval = finishTime.getTime()- currentTime.getTime();
        CountDownTimer timer = new CountDownTimer(interval,60000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hourCount =  60 * 60 * 1000;
                long remain = millisUntilFinished;
                int hours = (int) ( remain / hourCount );
                remain -= hourCount * hours;
                int minutes = (int) (remain / 60);
                String remainDisplay = hours + "小时" + minutes + "分钟";
                tvRemainTime.setText(remainDisplay);
            }

            @Override
            public void onFinish() {

            }
        }.start();

        tvStart.setText("显示签到二维码");
    }

    //点击跳转到扫描界面
    public void canningMethod(View v) {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, SCANNING_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        switch (requestCode) {
            case SCANNING_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String content = data.getStringExtra(DECODED_CONTENT_KEY);
                        Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                        Gson gson = new Gson();
                        QRcode code = gson.fromJson(content, QRcode.class);
                        CheckInReq req = new CheckInReq(UserSingleton.USERINFO.getUid(),
                                code.getMeetingId(), UserSingleton.USERINFO.getToken());
                        String json = gson.toJson(req);
                        meetingCheckIn(json);
                    }
                }
                break;
            case CAMERA_PERMISSION_REQUEST:
                canningMethod(tvScan);
                break;
        }

    }

    //点击生成带图片的二维码
    public void makeErweimaMethod(View v) {
        Date date = new Date();
        QRcode code = new QRcode(meeting.getMeetingId(), date.getTime());
        Gson gson = new Gson();
        String json = gson.toJson(code);
        Create2QR2(json, ivQRcode);
    }

    //生成二维码的方法
    private void Create2QR2(String urls,ImageView imageView) {
        String uri = urls;
        int mScreenWidth = 0;
        Bitmap bitmap;
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;

            bitmap = BitmapUtil.createQRImage(uri, mScreenWidth,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.me));//自己写的方法

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getMeeting(){
        Observable<BaseEntity<Meeting>> observable = RetrofitClient.createService(ClassmateAPI.class)
                .getMeeting(group.getGroupId());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<Meeting>>() {
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
                    public void onNext(BaseEntity<Meeting> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null) {
                                meeting = baseEntity.getDatas();
                                initMeetView();
                            }
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void initMeeting(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .initMeeting(body);

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
                            ToastUtils.showToast(mContext, "开始成功！");
                            getMeeting();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void meetingCheckIn(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .meetingCheckIn(body);

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
                            ToastUtils.showToast(mContext, "签到成功！");
                            getMeeting();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
