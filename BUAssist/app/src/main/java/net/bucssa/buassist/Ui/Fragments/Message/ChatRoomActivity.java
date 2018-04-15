package net.bucssa.buassist.Ui.Fragments.Message;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import net.bucssa.buassist.Api.PersonalMessageAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Bean.Message.Message;
import net.bucssa.buassist.Bean.Request.ReplyReq;
import net.bucssa.buassist.Bean.Message.SocketMessage;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Socket.Constants;
import net.bucssa.buassist.Ui.Fragments.Message.Adapter.MessageRecyclerAdapter;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Shin on 17/7/24.
 */

public class ChatRoomActivity extends BaseActivity{

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.lv_message)
    RecyclerView rv_message;

    @BindView(R.id.et_message)
    EditText et_message;

    @BindView(R.id.iv_send)
    ImageView tv_send;

    @BindView(R.id.mRefreshLayout)
    MaterialRefreshLayout mRefreshLayout;

    @BindView(R.id.rootView)
    RelativeLayout rootView;

    @BindView(R.id.header)
    LinearLayout headerRootView;

    @BindView(R.id.status_bar)
    View statusBar;

    private List<Message> messages = new ArrayList<>();
    private MessageRecyclerAdapter myAdapter;
    private Chat chat;
    private int state = Enum.STATE_NORMAL;
    private int type = 0;
    private String msg;

    /**
     * socket参数
     */
    private Socket mSocket;
    private Boolean isConnected = true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_room;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        chat = (Chat) getIntent().getSerializableExtra("Chat");
        type = chat.getPmtype();
        super.onCreate(savedInstanceState);

        ((Activity) mContext).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        switch (type) {
            case 1:
                tv_title.setText(chat.getTousername());
                break;
            default:
                tv_title.setText(chat.getSubject());
                break;
        }

        this.setResult(101);

        initData();

        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        initSocket();

    }

    @Override
    protected void initResAndListener() {
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                loadMore();
            }
        });

        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().equals("")) {
                    tv_send.setEnabled(false);
                } else {
                    tv_send.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    tv_send.setEnabled(false);
                } else {
                    tv_send.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_message.getText() != null && !et_message.getText().toString().equals("")) {
                    msg = et_message.getText().toString();
                    et_message.setText("");
                    hideKeyboard(tv_send);
                    sendMessage();
                }
            }
        });

        rv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 100) {
                    rv_message.scrollToPosition(myAdapter == null ? 0 : myAdapter.getItemCount()-1);
                }
            }
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        mSocket.emit("room", "room-"+chat.getPlid());
                        Toast.makeText(((Activity) mContext).getApplicationContext(),
                                R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    public Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(((Activity) mContext).getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    public Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String roomId;
//                    String message;
//                    try {
//                        roomId = data.getString("roomId");
//                        message = data.getString("text");
//                    } catch (JSONException e) {
//                        Log.e(TAG, e.getMessage());
//                        return;
//                    }
                    int lastpmid = myAdapter.getmDatas().get(myAdapter.getItemCount()-1).getPmid();
                    checkNewMessages(lastpmid);
                }
            });
        }
    };

    public Emitter.Listener onAnnouncement = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String announcement = (String) args[0];
                    ToastUtils.showToast(mContext, announcement);
                }
            });
        }
    };


    private void sendMessage() {
        ReplyReq req=new ReplyReq(UserSingleton.USERINFO.getUid(),
                UserSingleton.USERINFO.getUsername(), chat.getPlid(),
                msg ,UserSingleton.USERINFO.getToken());
        Gson gson=new Gson();
        String json = gson.toJson(req);

        /**
         * 传输消息
         */
        replyMessage(json);
    }


    private void scrollToBottom() {
        rv_message.scrollToPosition(myAdapter == null ? 0 : myAdapter.getItemCount()-1);
    }



    private void initData() {
        getMessagesByplid(0, 10);
    }

    private void initSocket() {
        mSocket.on("room", onConnect);
        mSocket.on("leave room", onDisconnect);
        mSocket.on("message", onMessage);
        mSocket.on("announcement", onAnnouncement);
        mSocket.connect();

        connectSocket();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.emit("close");

        mSocket.disconnect();

        mSocket.off("room", onConnect);
        mSocket.off("leave room", onDisconnect);
        mSocket.off("message", onMessage);
        mSocket.off("announcement", onAnnouncement);
    }

    private void connectSocket() {
        mSocket.emit("room", "room-"+chat.getPlid());
    }

    private void loadMore() {
        state = Enum.STATE_MORE;
        int lastpmid = myAdapter.getmDatas().get(0).getPmid();
        getMessagesByplid(lastpmid, 10);
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                rv_message.setLayoutManager(new LinearLayoutManager(mContext));
                myAdapter = new MessageRecyclerAdapter(mContext, messages);
                setListeners();
                rv_message.setAdapter(myAdapter);
                scrollToBottom();
                break;
            case Enum.STATE_SEND:
                myAdapter.addItems(messages, myAdapter.getItemCount());
                scrollToBottom();
                break;
            case Enum.STATE_RECEIVE:
                myAdapter.addItems(messages, myAdapter.getItemCount());
                scrollToBottom();
                break;
            case Enum.STATE_MORE:
                if (messages.size() == 0) {
                    ToastUtils.showToast(mContext, "已经加载全部消息了");
                    mRefreshLayout.finishRefresh();
                } else {
                    int lastIndex = myAdapter.getItemCount();
                    myAdapter.addItems(messages, 0);
                    mRefreshLayout.finishRefresh();
                    rv_message.scrollToPosition(myAdapter.getItemCount() - lastIndex - 1);
                }
                break;
        }
    }

    private void setListeners() {
        myAdapter.setOnClickListeners(new MessageRecyclerAdapter.OnClickListeners() {
            @Override
            public void OnItemClick(View view) {
                hideKeyboard(view);
            }
        });
    }

    private void getMessagesByplid( int pmid, int offset) {
        Observable<BaseEntity<List<Message>>> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .getMsgByOffset(UserSingleton.USERINFO.getUid(), chat.getPlid(),chat.getPmtype(), pmid, offset, UserSingleton.USERINFO.getToken());

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<List<Message>>>() {
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
                    public void onNext(BaseEntity<List<Message>> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            messages = new ArrayList<>();
                            if (baseEntity.getDatas() != null)
                                messages = baseEntity.getDatas();
                            changeByState();
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void checkNewMessages(final int pmid) {
        Observable<BaseEntity> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .checkNewMsgBypid(UserSingleton.USERINFO.getUid(), chat.getPlid(), pmid, UserSingleton.USERINFO.getToken());

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
                            int offset = baseEntity.getNewNum();
                            state = Enum.STATE_RECEIVE;
                            getMessagesByplid(0, offset);
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }

    private void replyMessage(String json){

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Observable<BaseEntity> observable = RetrofitClient.createService(PersonalMessageAPI.class)
                .replyMessage(body);

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

                            SocketMessage socketMsg = new SocketMessage(msg, UserSingleton.USERINFO.getUid(),
                                    UserSingleton.midAvatar, "", "", "room-"+chat.getPlid());
                            Gson socketGson = new Gson();
                            String jsonMsg = socketGson.toJson(socketMsg);

                            /**
                             * socket回调
                             */
                            mSocket.emit("message", jsonMsg);

                            state = Enum.STATE_SEND;

                            getMessagesByplid(0, 1);

                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }
}
