package net.bucssa.buassist.Ui.Fragments.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Message.ChatRoomActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

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

    @BindView(R.id.tvMajor)
    TextView tvMajor;

    @BindView(R.id.tvDateOrBirth)
    TextView tvDateOfBirth;

    @BindView(R.id.tvClassYear)
    TextView tvClassYear;

    @BindView(R.id.tvRelationship)
    TextView tvRelationship;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvSendMsg)
    TextView tvSendMsg;

    @BindView(R.id.tvAddFriend)
    TextView tvAddFriend;

    private int otherUid;
    private OtherInfo otherInfo;
    private Chat chat;

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
        ivSexDisplay.setSelected(otherInfo.getGender()==1);
        tvClassYear.setText("");
        ivSexDisplay.setSelected(otherInfo.getGender() == 2);
        tvIntro.setText(otherInfo.getBio());
        tvCollege.setText(otherInfo.getCollege());
        tvMajor.setText(otherInfo.getMajor());
        tvDateOfBirth.setText(otherInfo.getDateOfBirth());
        tvRelationship.setText(otherInfo.getAffectivestatus());
        tvEmail.setText("");
        tvAddFriend.setText(otherInfo.isIsFriend()?"删除好友":"加为好友");
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

        tvSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            ToastUtils.showToast(mContext, "已删除该好友！");
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
}
