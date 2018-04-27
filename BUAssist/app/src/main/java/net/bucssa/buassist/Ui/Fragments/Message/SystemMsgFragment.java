package net.bucssa.buassist.Ui.Fragments.Message;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.bucssa.buassist.Api.ClassmateAPI;
import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Api.SystemMessageAPI;
import net.bucssa.buassist.Api.UserAPI;
import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Bean.Message.SystemNotification;
import net.bucssa.buassist.Bean.Request.AddFriendReq;
import net.bucssa.buassist.Bean.Request.DelSystemMsgReq;
import net.bucssa.buassist.Bean.Request.HandleInviteReq;
import net.bucssa.buassist.Bean.Request.HandleJoinReq;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Message.Adapter.NewChatAdapter;
import net.bucssa.buassist.Ui.Fragments.Message.Adapter.SystemMsgAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;
import net.bucssa.buassist.Widget.CustomListViewForRefreshView;
import net.bucssa.buassist.Widget.RefreshHelper;
import net.bucssa.buassist.Widget.RefreshView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/27.
 */

public class SystemMsgFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.ivLulu)
    ImageView ivLulu;

    @BindView(R.id.lv_message)
    ListView lv_message;

    @BindView(R.id.initView)
    LinearLayout initView;

    @BindView(R.id.iv_light)
    ImageView iv_light;

    @BindView(R.id.iv_lulu)
    ImageView iv_lulu;

    @BindView(R.id.tv_status)
    TextView tv_status;

    private List<SystemNotification> chatList = new ArrayList<>();
    private SystemMsgAdapter myAdapter;
    private int state = Enum.STATE_NORMAL;

    int pageIndex = 1;//当前页
    int pageSize = 20;//每一页数量
    int totalCount = 0;//总数

    private Dialog mSystemDialog;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_system_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        Glide.with(context)
                .asGif()
                .load(R.raw.pull)
                .into(ivLulu);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Glide.with(context)
                        .asGif()
                        .load(R.raw.refreshing)
                        .into(ivLulu);
                refreshData();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Glide.with(context)
                        .asGif()
                        .load(R.raw.pull)
                        .into(ivLulu);
                loadMore();
            }
        });

        initLuluAnim();
    }

    private void initLuluAnim(){
        initView.setVisibility(View.VISIBLE);
        Animation luluShake = AnimationUtils.loadAnimation(context, R.anim.lulu_anim_shake);
        iv_lulu.startAnimation(luluShake);
        tv_status.setText(R.string.lulu_loading);
    }

    private void stopLuluAnim() {
        iv_lulu.clearAnimation();
        iv_light.setVisibility(View.VISIBLE);
        tv_status.setText(R.string.lulu_finishLoading);
        Animation initViewOut = AnimationUtils.loadAnimation(context, R.anim.lulu_anim_out);
        initViewOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        initView.startAnimation(initViewOut);
    }

    /**
     * 下拉刷新
     */
    public void refreshData() {
        pageIndex = 1;
        state = Enum.STATE_REFRESH;
        initData();
    }

    /**
     * 上拉刷新
     */
    private void loadMore() {
        pageIndex++;
        state = Enum.STATE_MORE;
        initData();
    }

    private void initData() {
        chatList = new ArrayList<>();
        getMessages();
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new SystemMsgAdapter(context, chatList);
                myAdapter.setOnSystemRequestClickListener(new SystemMsgAdapter.OnSystemRequestClickListener() {
                    @Override
                    public void OnSystemRequestClick(SystemNotification systemNotification) {
                        initSystemDialog(systemNotification);
                    }
                });
                lv_message.setAdapter(myAdapter);
                stopLuluAnim();
                break;
            case Enum.STATE_REFRESH:
                myAdapter.clearData();
                myAdapter.addData(0, chatList);
                refreshLayout.finishRefresh(1000);
                break;
            case Enum.STATE_MORE:
                if (chatList.size() == 0) {
                    refreshLayout.finishLoadMore(1000);
                    ToastUtils.showToast(context, "已加载全部系统消息！");
                    break;
                }
                myAdapter.addData(myAdapter.getCount(),chatList);
                refreshLayout.finishLoadMore(1000);
                break;
        }
    }

    private void initSystemDialog(final SystemNotification data) {
        mSystemDialog = new Dialog(context, R.style.loginDialog);
        mSystemDialog.setContentView(R.layout.dialog_system_notification);
        ImageView ivAvatar = (ImageView) mSystemDialog.findViewById(R.id.ivAvatar);
        TextView tvName = (TextView) mSystemDialog.findViewById(R.id.tvName);
        TextView tvFromType = (TextView) mSystemDialog.findViewById(R.id.tvFromType);
        TextView tvMessage = (TextView) mSystemDialog.findViewById(R.id.tvMessage);
        ImageView ivClose = (ImageView) mSystemDialog.findViewById(R.id.ivClose);
        TextView tvAgree = (TextView) mSystemDialog.findViewById(R.id.tvAgree);
        TextView tvDisagree = (TextView) mSystemDialog.findViewById(R.id.tvDisagree);
        final EditText etComment = (EditText) mSystemDialog.findViewById(R.id.etComment);

        if (data.getCategory() == 3) etComment.setVisibility(View.VISIBLE);

        Glide.with(context)
                .asBitmap()
                .load(data.getAvatar())
                .into(ivAvatar);

        tvName.setText(data.getAuthor());
        tvFromType.setText(data.getFrom_type());
        tvMessage.setText(data.getContent());
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSystemDialog.dismiss();
            }
        });

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResult(data,1,etComment.getText().toString());
                mSystemDialog.dismiss();
            }
        });

        tvDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCategory() == 2) handleResult(data, 0, "");
                mSystemDialog.dismiss();
            }
        });

        Window window = mSystemDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 获取和mLoginingDlg关联的当前窗口的属性，从而设置它在屏幕中显示的位置

        // 获取屏幕的高宽
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int cxScreen = dm.widthPixels;
        int cyScreen = dm.heightPixels;

        int height = (int) getResources().getDimension(
                R.dimen.loginingdlg_height);// 高100dp
        int lrMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin); // 左右边沿30dp
        int topMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_top_margin); // 上沿20dp

//        params.y = (-(cyScreen - height) / 2) + topMargin; // -199
		/* 对话框默认位置在屏幕中心,所以x,y表示此控件到"屏幕中心"的偏移量 */

        params.width = cxScreen;
//        params.height = height;
        // width,height表示mLoginingDlg的实际大小

        mSystemDialog.setCanceledOnTouchOutside(true); // 设置点击Dialog外部任意区域关闭Dialog

        mSystemDialog.show();
    }

    /**
     * 系统信息的处理结果
     * @param data
     * @param result
     * @param comment
     */
    public void handleResult(SystemNotification data, int result, String comment) {
        Gson gson=new Gson();
        String json;
        switch (data.getCategory()) {
            case 2: /* 好友请求 */
                if (result == 1) { /* 如果处理为同意，则通过请求加对方为好友 */
                    AddFriendReq addFriendReq = new AddFriendReq(UserSingleton.USERINFO.getUid(),
                            data.getAuthorid(), comment, UserSingleton.USERINFO.getToken());
                    json = gson.toJson(addFriendReq);
                    addFriend(json);
                } else { /* 如果处理为不同意，则删除该系统消息 */
                    DelSystemMsgReq addFriendReq = new DelSystemMsgReq(UserSingleton.USERINFO.getUid(),
                            data.getId(), UserSingleton.USERINFO.getToken());
                    json = gson.toJson(addFriendReq);
                    delMessage(json);
                }
                break;
            case 3: /* 处理请求加入 */
                HandleJoinReq handleJoinReq = new HandleJoinReq(data.getId(),UserSingleton.USERINFO.getUid(),
                        data.getAuthorid(),data.getExtra(),result,comment,UserSingleton.USERINFO.getToken());
                json = gson.toJson(handleJoinReq);
                handleJoinRequest(json);
                break;
            case 4: /* 处理邀请加入 */
                HandleInviteReq handleInviteReq = new HandleInviteReq(data.getId(),UserSingleton.USERINFO.getUid(),
                        data.getExtra(),result,UserSingleton.USERINFO.getToken());
                json = gson.toJson(handleInviteReq);
                handleInviteRequest(json);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 101:
                refreshData();
                break;
        }
    }

    private void getMessages() {
        Observable<BaseEntity<List<SystemNotification>>> observable = RetrofitClient.createService(SystemMessageAPI.class)
                .getSystemMessage(UserSingleton.USERINFO.getUid(), UserSingleton.USERINFO.getToken(),
                        pageIndex, pageSize);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<SystemNotification>>>() {
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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity<List<SystemNotification>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            if (baseEntity.getDatas() != null)
                                chatList = baseEntity.getDatas();
                            totalCount = baseEntity.getTotal();
                            changeByState();
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void handleJoinRequest(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .handleJoin(body);

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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "处理成功！");
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void handleInviteRequest(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(ClassmateAPI.class)
                .handleInvite(body);

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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "处理成功！");
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "处理成功！");
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void delMessage(String json){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(SystemMessageAPI.class)
                .deleteSystemMessage(body);

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
                        ToastUtils.showToast(context, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        if (baseEntity.isSuccess()) {
                            ToastUtils.showToast(context, "处理成功！");
                        } else {
                            ToastUtils.showToast(context, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
