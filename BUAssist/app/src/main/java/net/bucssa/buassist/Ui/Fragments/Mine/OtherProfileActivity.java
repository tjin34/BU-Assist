package net.bucssa.buassist.Ui.Fragments.Mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Login.OtherInfo;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Bean.Request.AddFriendReq;
import net.bucssa.buassist.Bean.Request.DelFriendReq;
import net.bucssa.buassist.Bean.Request.SendReq;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Message.ChatRoomActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Util.Utils;

import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Shinji on 2018/4/9.
 */

public class OtherProfileActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_profile)
    ImageView ivProfile;

    @BindView(R.id.tv_username)
    TextView tvNickname;

    @BindView(R.id.ivSexDisplay)
    ImageView ivSexDisplay;

    @BindView(R.id.tvIntro)
    TextView tvIntro;

    @BindView(R.id.tvCollege)
    TextView tvCollege;

    @BindView(R.id.llMajor)
    LinearLayout llMajor;

    @BindView(R.id.tvMajor)
    TextView tvMajor;

    @BindView(R.id.tvDateOrBirth)
    TextView tvDateOfBirth;

    @BindView(R.id.llDob)
    LinearLayout llDateOfBirth;

    @BindView(R.id.tvClassYear)
    TextView tvClassYear;

    @BindView(R.id.llRelationship)
    LinearLayout llRelationship;

    @BindView(R.id.tvRelationship)
    TextView tvRelationship;

    @BindView(R.id.llEmail)
    LinearLayout llEmail;


    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvSendMsg)
    TextView tvSendMsg;

    @BindView(R.id.tvAddFriend)
    TextView tvAddFriend;

    private int otherUid;
    private OtherInfo otherInfo;
    private Chat chat;
    private Dialog mCreateChatDialog;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        otherUid = getIntent().getIntExtra("OtherId",0);
        super.onCreate(savedInstanceState);

        getOtherInfo();
    }

    private void initView() {
        String title = otherInfo.getNickname() + " - 个人资料";
        tvTitle.setText(title);

        Glide.with(mContext)
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .load(otherInfo.getAvatar())
                .into(ivProfile);

        tvNickname.setText(otherInfo.getNickname());
        /* 性别标识 */
        ivSexDisplay.setSelected(otherInfo.getGender()==1);
        ivSexDisplay.setVisibility(otherInfo.getGender() == 0 ? View.GONE : View.VISIBLE);
        /* 个人介绍 */
        tvIntro.setText(otherInfo.getBio());
        /* 学院 */
        tvCollege.setText(otherInfo.getCollege());
        /* 专业 */
        tvMajor.setText(otherInfo.getMajor()!= null ? otherInfo.getMajor() : "");
        llMajor.setVisibility(tvMajor.getText().toString().equals("") ? View.GONE : View.VISIBLE);
        /* 生日 */
        tvDateOfBirth.setText(otherInfo.getDateOfBirth() != null ? otherInfo.getDateOfBirth() : "");
        llDateOfBirth.setVisibility(tvDateOfBirth.getText().toString().equals("") ? View.GONE : View.VISIBLE);
        /* 感情状况 */
        tvRelationship.setText(otherInfo.getAffectivestatus() != null ? otherInfo.getAffectivestatus() : "");
        llRelationship.setVisibility(tvRelationship.getText().toString().equals("") ? View.GONE : View.VISIBLE);
        /* 邮件 */
        tvEmail.setText("");
        llEmail.setVisibility(tvEmail.getText().equals("") ? View.GONE : View.VISIBLE);

        tvAddFriend.setText(otherInfo.isIsFriend() ? "删除好友" : "加为好友");
        tvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherInfo.isIsFriend()) {
                    DelFriendReq req = new DelFriendReq(UserSingleton.USERINFO.getUid(),otherUid, UserSingleton.USERINFO.getToken());
                    Gson gson = new Gson();
                    String json = gson.toJson(req);
                    delFriend(json);
                } else {
                    AddFriendReq req = new AddFriendReq(UserSingleton.USERINFO.getUid(),otherUid,"", UserSingleton.USERINFO.getToken());
                    Gson gson = new Gson();
                    String json = gson.toJson(req);
                    addFriend(json);
                }
            }
        });

        tvSendMsg.setText(otherInfo.getPlid() == 0 ? "打个招呼" : "发送消息");
        tvSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherInfo.getPlid() == 0) initCreateChatDialog();
                else getChatByPlid();
            }
        });
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

        tvSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChatByPlid();
            }
        });

    }

    private void initCreateChatDialog() {
        mCreateChatDialog = new Dialog(mContext, R.style.loginDialog);
        mCreateChatDialog.setContentView(R.layout.dialog_create_chat);
        TextView tvAgree = (TextView) mCreateChatDialog.findViewById(R.id.tvAgree);
        TextView tvDisagree = (TextView) mCreateChatDialog.findViewById(R.id.tvDisagree);
        final EditText etComment = (EditText) mCreateChatDialog.findViewById(R.id.etComment);

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(mContext, v);
                SendReq req=new SendReq(UserSingleton.USERINFO.getUid(),
                        UserSingleton.USERINFO.getUsername(), String.valueOf(otherInfo.getUid()),
                        etComment.getText().toString(), etComment.getText().toString() ,UserSingleton.USERINFO.getToken());
                Gson gson=new Gson();
                String json = gson.toJson(req);
                createChat(json);
                mCreateChatDialog.dismiss();
            }
        });

        tvDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreateChatDialog.dismiss();
            }
        });

        Window window = mCreateChatDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int cxScreen = dm.widthPixels;

        params.width = cxScreen;

        mCreateChatDialog.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog

        mCreateChatDialog.show();
    }

    private void getOtherInfo() {
        Observable<BaseEntity<OtherInfo>> observable = RetrofitClient.createService(UserAPI.class)
                .getOthersInfo(otherUid, UserSingleton.USERINFO.getUid(),UserSingleton.USERINFO.getToken());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<OtherInfo>>() {
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
                    public void onNext(BaseEntity<OtherInfo> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            otherInfo = new OtherInfo();
                            if (baseEntity.getDatas() != null)
                                otherInfo = baseEntity.getDatas();

                            initView();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void getChatByPlid() {
        Observable<BaseEntity<List<Chat>>> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .getChatByPlid(UserSingleton.USERINFO.getUid(), otherInfo.getPlid(), UserSingleton.USERINFO.getToken());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Chat>>>() {
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
                    public void onNext(BaseEntity<List<Chat>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            chat = new Chat();
                            if (baseEntity.getDatas() != null) {
                                chat = baseEntity.getDatas().get(0);
                                Intent intent = new Intent(mContext, ChatRoomActivity.class);
                                intent.putExtra("chat", chat);
                                startActivity(intent);
                            }
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void delFriend(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .deleteFriend(body);

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
                            ToastUtils.showToast(mContext, "已发起好友请求！");
                            getOtherInfo();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void addFriend(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(UserAPI.class)
                .addFriend(body);

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
                            ToastUtils.showToast(mContext, "已删除该好友！");
                            getOtherInfo();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void createChat(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .createChat(body);

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
                            ToastUtils.showToast(mContext, "发送成功！");
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
